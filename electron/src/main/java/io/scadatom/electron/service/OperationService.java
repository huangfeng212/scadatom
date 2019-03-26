package io.scadatom.electron.service;

import static io.scadatom.electron.config.RabbitmqConfig.ROUTING_TO_NUCLEUS;
import static io.scadatom.electron.config.RabbitmqConfig.TOPIC_SCADATOM;
import static io.scadatom.electron.service.util.ConfigUtil.loadConfig;
import static io.scadatom.electron.service.util.ConfigUtil.saveConfig;
import static io.scadatom.neutron.Intents.ctrlElectron;
import static io.scadatom.neutron.Intents.ctrlParticle;
import static io.scadatom.neutron.Intents.initElectron;
import static io.scadatom.neutron.Intents.viewElectron;
import static io.scadatom.neutron.Intents.viewParticle;
import static io.scadatom.neutron.OpResult.Failure;
import static io.scadatom.neutron.OpResult.Success;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.scadatom.electron.repository.ElectronOpRepository;
import io.scadatom.electron.repository.ParticleOpRepository;
import io.scadatom.electron.serial.modbus.master.SmmChargerService;
import io.scadatom.electron.serial.modbus.slave.SmsChargerService;
import io.scadatom.electron.service.mapper.ElectronOpMapper;
import io.scadatom.electron.service.mapper.ParticleOpMapper;
import io.scadatom.neutron.ElectronInitReq;
import io.scadatom.neutron.ElectronRequestInitReq;
import io.scadatom.neutron.FlattenedMessage;
import io.scadatom.neutron.Intents;
import io.scadatom.neutron.OpCtrlReq;
import io.scadatom.neutron.OpState;
import io.scadatom.neutron.OpViewReq;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OperationService {

  public static final String INIT_VALUE = "InitValue";
  private final Logger log = LoggerFactory.getLogger(OperationService.class);
  private final ObjectMapper objectMapper;
  private final RabbitTemplate rabbitTemplate;

  private final SmmChargerService smmChargerService;
  private final SmsChargerService smsChargerService;
  private final OpChangeService opChangeService;
  private final OpDataService opDataService;
  private final ElectronOpRepository electronOpRepository;
  private final ParticleOpRepository particleOpRepository;
  private final ParticleOpMapper particleOpMapper;
  private final ElectronOpMapper electronOpMapper;
  private final Map<String, Function<FlattenedMessage, FlattenedMessage>> inboundRequestHandlers =
      new HashMap<>();

  private final Long electronId;

  public OperationService(
      ObjectMapper objectMapper,
      RabbitTemplate rabbitTemplate,
      SmmChargerService smmChargerService,
      SmsChargerService smsChargerService,
      OpChangeService opChangeService,
      OpDataService opDataService,
      ElectronOpRepository electronOpRepository,
      ParticleOpRepository particleOpRepository,
      ParticleOpMapper particleOpMapper,
      ElectronOpMapper electronOpMapper,
      @Value("${electron.id}") Long electronId) {
    this.objectMapper = objectMapper;
    this.rabbitTemplate = rabbitTemplate;
    this.smmChargerService = smmChargerService;
    this.smsChargerService = smsChargerService;
    this.opChangeService = opChangeService;
    this.opDataService = opDataService;
    this.electronOpRepository = electronOpRepository;
    this.particleOpRepository = particleOpRepository;
    this.particleOpMapper = particleOpMapper;
    this.electronOpMapper = electronOpMapper;
    this.electronId = electronId;
    inboundRequestHandlers.put(initElectron, this::handleInitElectron);
    inboundRequestHandlers.put(viewElectron, this::handleViewElectron);
    inboundRequestHandlers.put(ctrlElectron, this::handleCtrlElectron);
    inboundRequestHandlers.put(viewParticle, this::handleViewParticle);
    inboundRequestHandlers.put(ctrlParticle, this::handleCtrlParticle);
  }

  private FlattenedMessage handleInitElectron(FlattenedMessage flattenedMessage) {
    try {
      ElectronInitReq electronInitReq = flattenedMessage.peel(ElectronInitReq.class);
      if (!Objects.equals(electronInitReq.getElectronDTO().getId(), electronId)) {
        return new FlattenedMessage(Failure.name() + ":non matching electron id from payload");
      }
      saveConfig(electronInitReq);
      stop();
      onStart();
      return new FlattenedMessage(Success.name());
    } catch (IOException e) {
      e.printStackTrace();
      return new FlattenedMessage(Failure.name() + ":can not parse payload");
    } catch (Exception e) {
      e.printStackTrace();
      return new FlattenedMessage(Failure.name() + ":" + e.getMessage());
    }
  }

  private FlattenedMessage handleViewElectron(FlattenedMessage flattenedMessage) {
    try {
      OpViewReq opViewReq = flattenedMessage.peel(OpViewReq.class);
      return new FlattenedMessage(
          Success.name(), electronOpMapper.toDto(opDataService.getElectronOp(opViewReq.getId())));
    } catch (IOException e) {
      e.printStackTrace();
      return new FlattenedMessage(Failure.name() + ":can not parse payload");
    }
  }

  private FlattenedMessage handleCtrlElectron(FlattenedMessage flattenedMessage) {
    try {
      OpCtrlReq opCtrlReq = flattenedMessage.peel(OpCtrlReq.class);
      switch (opCtrlReq.getCommand()) {
        case "stop":
          stop();
          break;
        case "start":
          start();
          break;
      }
      return new FlattenedMessage(Success.name());
    } catch (IOException e) {
      e.printStackTrace();
      return new FlattenedMessage(Failure.name() + ":can not parse payload");
    }
  }

  private FlattenedMessage handleViewParticle(FlattenedMessage flattenedMessage) {
    try {
      OpViewReq opViewReq = flattenedMessage.peel(OpViewReq.class);
      return new FlattenedMessage(
          Success.name(), particleOpMapper.toDto(opDataService.getParticleOp(opViewReq.getId())));
    } catch (IOException e) {
      e.printStackTrace();
      return new FlattenedMessage(Failure.name() + ":can not parse or form message");
    }
  }

  private FlattenedMessage handleCtrlParticle(FlattenedMessage flattenedMessage) {
    try {
      OpCtrlReq opCtrlReq = flattenedMessage.peel(OpCtrlReq.class);
      opChangeService.onCommandWritten(
          opCtrlReq.getId(), opCtrlReq.getCommand(), "RemoteUser_" + opCtrlReq.getUser());
      return new FlattenedMessage(Success.name());
    } catch (IOException e) {
      e.printStackTrace();
      return new FlattenedMessage(Failure.name() + ":can not parse");
    }
  }

  public void stop() {
    opChangeService.dismissAll();
    smsChargerService.stop();
    smmChargerService.stop();
  }

  @PostConstruct
  public void onStart() {
    opDataService.updateElectronOp(
        electronId, electronOp -> electronOp.setState(OpState.Uninitialized));
    ElectronInitReq config =
        loadConfig(electronId)
            .orElseGet(
                () -> {
                  try {
                    return retrieveElectronInitReq();
                  } catch (IOException e) {
                    return null;
                  }
                });
    if (config != null) {
      initialize(config);
      start();
    } else {
      log.error("initialization failed, no config available");
    }
  }

  public void start() {
    // onStart bond service
    switch (smmChargerService.getState()) {
      case Initialized:
      case Aborted:
      case Stopped:
        try {
          smmChargerService.start();
        } catch (Exception e) {
          e.printStackTrace();
          opDataService.updateSmmChargerOp(
              smmChargerService.getChargerId(),
              smmChargerOp -> smmChargerOp.setState(OpState.Aborted));
        }
    }
    switch (smsChargerService.getState()) {
      case Initialized:
      case Aborted:
      case Stopped:
        try {
          smsChargerService.start();
        } catch (Exception e) {
          e.printStackTrace();
          opDataService.updateSmsChargerOp(
              smsChargerService.getChargerId(),
              smsChargerOp -> smsChargerOp.setState(OpState.Aborted));
        }
    }
    opDataService.updateElectronOp(electronId, electronOp -> electronOp.setState(OpState.Started));
  }

  private ElectronInitReq retrieveElectronInitReq() throws IOException {
    ElectronRequestInitReq electronRequestInitReq = new ElectronRequestInitReq(electronId);
    Object response =
        rabbitTemplate.convertSendAndReceive(
            TOPIC_SCADATOM,
            ROUTING_TO_NUCLEUS,
            new FlattenedMessage("requestInitElectron", electronRequestInitReq).flat());
    return objectMapper.readValue(response.toString(), ElectronInitReq.class);
  }

  private void initialize(ElectronInitReq config) {
    // drop transient data
    electronOpRepository.deleteAll();
    particleOpRepository.deleteAll();
    // load particles
    config.getParticleDTOS().stream()
        .forEach(
            particleDTO -> {
              opDataService.updateParticleOp(
                  particleDTO.getId(),
                  particleOp -> {
                    particleOp.setState(OpState.Uninitialized);
                  });
            });
    // load charger service
    try {
      smmChargerService.initialize(config);
    } catch (Exception e) {
      opDataService.updateSmmChargerOp(
          config.getSmmChargerDTO().getId(),
          smmChargerOp -> smmChargerOp.setState(OpState.Aborted));
    }
    try {
      smsChargerService.initialize(config);
    } catch (Exception e) {
      opDataService.updateSmsChargerOp(
          config.getSmsChargerDTO().getId(),
          smsChargerOp -> smsChargerOp.setState(OpState.Aborted));
    }
    // apply init values
    config.getParticleDTOS().stream()
        .filter(particle -> !StringUtils.isBlank(particle.getInitValue()))
        .forEach(
            particle -> {
              opChangeService.onCommandWritten(
                  particle.getId(), particle.getInitValue(), INIT_VALUE);
              opChangeService.onValueRead(particle.getId(), particle.getInitValue(), INIT_VALUE);
              opDataService.updateParticleOp(
                  particle.getId(),
                  particleOp -> {
                    particleOp.setState(OpState.Initialized);
                  });
            });
    opDataService.updateElectronOp(
        electronId, electronOp -> electronOp.setState(OpState.Initialized));
  }

  @RabbitListener(queues = "#{queueInboundRequest.name}")
  public String onInboundRequest(String message) throws IOException {
    FlattenedMessage request = FlattenedMessage.inflate(message);
    FlattenedMessage response;
    Function<FlattenedMessage, FlattenedMessage> handler =
        inboundRequestHandlers.get(request.getTitle());
    if (handler != null) {
      response = handler.apply(request);
    } else {
      response = new FlattenedMessage(Failure.name() + ":unknown intent " + request.getTitle());
    }
    return response.flat();
  }
}
