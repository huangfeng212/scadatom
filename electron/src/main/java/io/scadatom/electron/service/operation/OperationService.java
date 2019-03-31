package io.scadatom.electron.service.operation;

import static io.scadatom.neutron.OpIntents.CTRL_ELECTRON;
import static io.scadatom.neutron.OpIntents.CTRL_PARTICLE;
import static io.scadatom.neutron.OpIntents.VIEW_ELECTRON;
import static io.scadatom.neutron.OpIntents.VIEW_PARTICLE;
import static io.scadatom.neutron.OpResult.FAILURE;
import static io.scadatom.neutron.OpResult.SUCCESS;

import io.scadatom.electron.repository.ElectronOpRepository;
import io.scadatom.electron.repository.ParticleOpRepository;
import io.scadatom.electron.service.mapper.ElectronOpMapper;
import io.scadatom.electron.service.mapper.ParticleOpMapper;
import io.scadatom.electron.service.operation.serialmodbus.master.SmmChargerService;
import io.scadatom.electron.service.operation.serialmodbus.slave.SmsChargerService;
import io.scadatom.neutron.ElectronOpDTO;
import io.scadatom.neutron.FlattenedMessage;
import io.scadatom.neutron.FlattenedMessageHandler;
import io.scadatom.neutron.OpCtrlCmds;
import io.scadatom.neutron.OpCtrlReq;
import io.scadatom.neutron.OpState;
import io.scadatom.neutron.OpViewReq;
import io.scadatom.neutron.ParticleOpDTO;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OperationService {

  public static final String INIT_VALUE = "InitValue";
  private final Logger log = LoggerFactory.getLogger(OperationService.class);
  private final OpConfigService opConfigService;
  private final SmmChargerService smmChargerService;
  private final SmsChargerService smsChargerService;
  private final OpEventService opEventService;
  private final OpRepoService opRepoService;
  private final ElectronOpRepository electronOpRepository;
  private final ParticleOpRepository particleOpRepository;
  private final ParticleOpMapper particleOpMapper;
  private final ElectronOpMapper electronOpMapper;
  private final Map<String, Function<FlattenedMessage, FlattenedMessage>> inboundRequestHandlers =
      new HashMap<>();
  private final FlattenedMessageHandler flattenedMessageHandler;

  private final Long electronId;

  public OperationService(
      OpConfigService opConfigService,
      SmmChargerService smmChargerService,
      SmsChargerService smsChargerService,
      OpEventService opEventService,
      OpRepoService opRepoService,
      ElectronOpRepository electronOpRepository,
      ParticleOpRepository particleOpRepository,
      ParticleOpMapper particleOpMapper,
      ElectronOpMapper electronOpMapper,
      @Value("${electron.id}") Long electronId) {
    this.opConfigService = opConfigService;
    this.smmChargerService = smmChargerService;
    this.smsChargerService = smsChargerService;
    this.opEventService = opEventService;
    this.opRepoService = opRepoService;
    this.electronOpRepository = electronOpRepository;
    this.particleOpRepository = particleOpRepository;
    this.particleOpMapper = particleOpMapper;
    this.electronOpMapper = electronOpMapper;
    this.electronId = electronId;
    inboundRequestHandlers.put(VIEW_ELECTRON, this::handleViewElectron);
    inboundRequestHandlers.put(CTRL_ELECTRON, this::handleCtrlElectron);
    inboundRequestHandlers.put(VIEW_PARTICLE, this::handleViewParticle);
    inboundRequestHandlers.put(CTRL_PARTICLE, this::handleCtrlParticle);
    flattenedMessageHandler = new FlattenedMessageHandler(inboundRequestHandlers);
  }

  private FlattenedMessage handleViewElectron(FlattenedMessage flattenedMessage) {
    try {
      OpViewReq opViewReq = flattenedMessage.peel(OpViewReq.class);
      return new FlattenedMessage(SUCCESS, viewElectron(opViewReq.getId()));
    } catch (IOException e) {
      return new FlattenedMessage(FAILURE + ":can not parse payload");
    }
  }

  private FlattenedMessage handleCtrlElectron(FlattenedMessage flattenedMessage) {
    try {
      ctrlElectron(flattenedMessage.peel(OpCtrlReq.class));
      return new FlattenedMessage(SUCCESS);
    } catch (IOException e) {
      return new FlattenedMessage(FAILURE + ":can not parse payload");
    }
  }

  private FlattenedMessage handleViewParticle(FlattenedMessage flattenedMessage) {
    try {
      OpViewReq opViewReq = flattenedMessage.peel(OpViewReq.class);
      return new FlattenedMessage(SUCCESS, viewParticle(opViewReq.getId()));
    } catch (IOException e) {
      return new FlattenedMessage(FAILURE + ":can not parse or form message");
    }
  }

  private FlattenedMessage handleCtrlParticle(FlattenedMessage flattenedMessage) {
    try {
      ctrlParticle(flattenedMessage.peel(OpCtrlReq.class), true);
      return new FlattenedMessage(SUCCESS);
    } catch (IOException e) {
      return new FlattenedMessage(FAILURE + ":can not parse");
    }
  }

  public ElectronOpDTO viewElectron(Long id) {
    return electronOpMapper.toDto(opRepoService.getElectronOp(id));
  }

  public void ctrlElectron(OpCtrlReq opCtrlReq) {
    switch (opCtrlReq.getCommand()) {
      case OpCtrlCmds.RESTART:
        stop();
        initialize();
        start();
        break;
      case OpCtrlCmds.RELOAD:
        stop();
        opConfigService.registerElectron();
        initialize();
        start();
        break;
      case OpCtrlCmds.RESET:
        System.exit(0);
        break;
    }
  }

  public ParticleOpDTO viewParticle(Long id) {
    return particleOpMapper.toDto(opRepoService.getParticleOp(id));
  }

  public void ctrlParticle(OpCtrlReq opCtrlReq, boolean isRemoteUser) {
    opEventService.onCommandWritten(
        opCtrlReq.getId(),
        opCtrlReq.getCommand(),
        (isRemoteUser ? "RemoteUser_" : "LocalUser_") + opCtrlReq.getUser());
  }

  public void stop() {
    opEventService.dismissAll();
    switch (smmChargerService.getState()) {
      case Initialized:
      case Started:
        try {
          smmChargerService.stop();
        } catch (Exception e) {
          opRepoService.updateSmmChargerOp(
              smmChargerService.getChargerId().get(),
              smmChargerOp -> smmChargerOp.setState(OpState.Aborted));
        }
    }
    switch (smsChargerService.getState()) {
      case Initialized:
      case Started:
        try {
          smsChargerService.stop();
        } catch (Exception e) {
          opRepoService.updateSmsChargerOp(
              smsChargerService.getChargerId().get(),
              smsChargerOp -> smsChargerOp.setState(OpState.Aborted));
        }
    }
    opRepoService.updateElectronOp(electronId, electronOp -> electronOp.setState(OpState.Stopped));
  }

  private void initialize() {
    // drop transient data
    electronOpRepository.deleteAll();
    particleOpRepository.deleteAll();
    // load particles
    opConfigService
        .getConfig()
        .ifPresent(
            config -> {
              if (config.getElectronDTO().isEnabled()) {
                config.getParticleDTOS().stream()
                    .forEach(
                        particleDTO -> {
                          opRepoService.updateParticleOp(
                              particleDTO.getId(),
                              particleOp -> {
                                particleOp.setState(OpState.Uninitialized);
                              });
                        });
                // load charger service
                try {
                  smmChargerService.initialize(config);
                } catch (Exception e) {
                  if (config.getSmmChargerDTO() != null) {
                    opRepoService.updateSmmChargerOp(
                        config.getSmmChargerDTO().getId(),
                        smmChargerOp -> smmChargerOp.setState(OpState.Aborted));
                  }
                }
                try {
                  smsChargerService.initialize(config);
                } catch (Exception e) {
                  if (config.getSmsChargerDTO() != null) {
                    opRepoService.updateSmsChargerOp(
                        config.getSmsChargerDTO().getId(),
                        smsChargerOp -> smsChargerOp.setState(OpState.Aborted));
                  }
                }
                // apply init values
                config.getParticleDTOS().stream()
                    .forEach(
                        particle -> {
                          if (!StringUtils.isBlank(particle.getInitValue())) {
                            opEventService.onCommandWritten(
                                particle.getId(), particle.getInitValue(), INIT_VALUE);
                            opEventService.onValueRead(
                                particle.getId(), particle.getInitValue(), INIT_VALUE);
                          }
                          opRepoService.updateParticleOp(
                              particle.getId(),
                              particleOp -> {
                                particleOp.setState(OpState.Initialized);
                              });
                        });
                opRepoService.updateElectronOp(
                    electronId, electronOp -> electronOp.setState(OpState.Initialized));
              } else {
                opRepoService.updateElectronOp(
                    electronId, electronOp -> electronOp.setState(OpState.Disabled));
              }
            });
  }

  public void start() {
    switch (opRepoService.getElectronOp(electronId).getState()) {
      case Initialized:
      case Stopped:
      case Aborted:
        // onStart bond service
        smmChargerService
            .getChargerId()
            .ifPresent(
                chargerId -> {
                  switch (smmChargerService.getState()) {
                    case Initialized:
                    case Aborted:
                    case Stopped:
                      try {
                        smmChargerService.start();
                      } catch (Exception e) {
                        opRepoService.updateSmmChargerOp(
                            chargerId, smmChargerOp -> smmChargerOp.setState(OpState.Aborted));
                      }
                  }
                });
        smsChargerService
            .getChargerId()
            .ifPresent(
                chargerId -> {
                  switch (smsChargerService.getState()) {
                    case Initialized:
                    case Aborted:
                    case Stopped:
                      try {
                        smsChargerService.start();
                      } catch (Exception e) {
                        opRepoService.updateSmsChargerOp(
                            chargerId, smsChargerOp -> smsChargerOp.setState(OpState.Aborted));
                      }
                  }
                });
        opRepoService.updateElectronOp(
            electronId, electronOp -> electronOp.setState(OpState.Started));
        break;
    }
  }

  @PostConstruct
  public void onStart() {
    opRepoService.updateElectronOp(
        electronId, electronOp -> electronOp.setState(OpState.Uninitialized));
    initialize();
    start();
  }

  @RabbitListener(queues = "#{queueInboundRequest.name}")
  public String onInboundRequest(String message) {
    try {
      return flattenedMessageHandler.handle(message);
    } catch (IOException e) {
      return FAILURE + ":can not parse payload";
    }
  }
}
