package io.scadatom.electron.service;

import static io.scadatom.electron.config.RabbitmqConfig.ROUTING_TO_NUCLEUS;
import static io.scadatom.electron.config.RabbitmqConfig.TOPIC_SCADATOM;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import io.scadatom.neutron.OpCtrlReq;
import io.scadatom.neutron.OpState;
import io.scadatom.neutron.OpViewReq;
import io.scadatom.neutron.SmsChargerDTO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

  public static final String FAILURE = "failure";
  public static final String SUCCESS = "success";
  public static final String UNSUPPORTED = "unsupported";
  public static final String INIT_VALUE = "InitValue";
  public static final String ELECTRON_CONFIG_JSON = "electronConfig.json";
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
    inboundRequestHandlers.put("initElectron", this::handleInitElectron);
    inboundRequestHandlers.put("viewElectron", this::handleViewElectron);
    inboundRequestHandlers.put("ctrlElectron", this::handleCtrlElectron);
    inboundRequestHandlers.put("viewParticle", this::handleViewParticle);
    inboundRequestHandlers.put("ctrlParticle", this::handleCtrlParticle);
  }

  private FlattenedMessage handleInitElectron(FlattenedMessage flattenedMessage) {
    try {
      ElectronInitReq electronInitReq = flattenedMessage.peel(ElectronInitReq.class);
      if (!Objects.equals(electronInitReq.getElectronDTO().getId(), electronId)) {
        return new FlattenedMessage(FAILURE, "non matching electron id from payload");
      }
      saveConfig(electronInitReq);
      stop();
      onStart();
      return new FlattenedMessage(SUCCESS);
    } catch (IOException e) {
      e.printStackTrace();
      return new FlattenedMessage(FAILURE, "can not parse payload");
    } catch (Exception e) {
      e.printStackTrace();
      return new FlattenedMessage(FAILURE, e.getMessage());
    }
  }

  private FlattenedMessage handleViewElectron(FlattenedMessage flattenedMessage) {
    try {
      OpViewReq opViewReq = flattenedMessage.peel(OpViewReq.class);
      return new FlattenedMessage(
          SUCCESS, electronOpMapper.toDto(opDataService.getElectronOp(opViewReq.getId())));
    } catch (IOException e) {
      e.printStackTrace();
      return new FlattenedMessage(FAILURE, "can not parse payload");
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
      return new FlattenedMessage(SUCCESS);
    } catch (IOException e) {
      e.printStackTrace();
      return new FlattenedMessage(FAILURE, "can not parse payload");
    }
  }

  private FlattenedMessage handleViewParticle(FlattenedMessage flattenedMessage) {
    try {
      OpViewReq opViewReq = flattenedMessage.peel(OpViewReq.class);
      return new FlattenedMessage(
          SUCCESS, particleOpMapper.toDto(opDataService.getParticleOp(opViewReq.getId())));
    } catch (IOException e) {
      e.printStackTrace();
      return new FlattenedMessage(FAILURE, "can not parse or form message");
    }
  }

  private FlattenedMessage handleCtrlParticle(FlattenedMessage flattenedMessage) {
    try {
      OpCtrlReq opCtrlReq = flattenedMessage.peel(OpCtrlReq.class);
      opChangeService.onCommandWritten(
          opCtrlReq.getId(), opCtrlReq.getCommand(), "RemoteUser_" + opCtrlReq.getUser());
      return new FlattenedMessage(SUCCESS);
    } catch (IOException e) {
      e.printStackTrace();
      return new FlattenedMessage(FAILURE, "can not parse");
    }
  }

  private void saveConfig(ElectronInitReq config) {
    try {
      objectMapper
          .writerWithDefaultPrettyPrinter()
          .writeValue(
              new File(System.getProperty("user.home") + File.separator + ELECTRON_CONFIG_JSON),
              config);
    } catch (IOException e) {
      log.error("failed to write config file to disk");
      e.printStackTrace();
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
        loadConfig()
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
    if (smmChargerService.getState() == OpState.Initialized) {
      try {
        smmChargerService.start();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (smsChargerService.getState() == OpState.Initialized) {
      try {
        smsChargerService.start();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    opDataService.updateElectronOp(electronId, electronOp -> electronOp.setState(OpState.Started));
  }

  private Optional<ElectronInitReq> loadConfig() {
    try {
      ElectronInitReq config =
          objectMapper.readValue(
              new File(System.getProperty("user.home") + File.separator + ELECTRON_CONFIG_JSON),
              ElectronInitReq.class);
      if (config.getElectronDTO().getId().equals(electronId)) {
        return Optional.of(config);
      } else {
        log.error("config file has invalid electron id");
      }
    } catch (IOException e) {
      log.error("error loading config from disk");
    }
    return Optional.empty();
  }

  private ElectronInitReq retrieveElectronInitReq() throws IOException {
    ElectronRequestInitReq electronRequestInitReq = new ElectronRequestInitReq(electronId);
    Object response =
        rabbitTemplate.convertSendAndReceive(
            TOPIC_SCADATOM,
            ROUTING_TO_NUCLEUS,
            flattenedRequestOf("requestInitElectron", electronRequestInitReq));
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
    if (config.getSmmChargerDTO() != null) {
      smmChargerService.initialize(config);
    }
    if (config.getSmsChargerDTO() != null) {
      SmsChargerDTO smsChargerDTO = config.getSmsChargerDTO();
      smsChargerService.initialize(config);
    }
    // apply init values
    config.getParticleDTOS().stream()
        .filter(particle -> !StringUtils.isBlank(particle.getInitValue()))
        .forEach(
            particle -> {
              opChangeService.onCommandWritten(
                  particle.getId(), particle.getInitValue(), INIT_VALUE);
              opChangeService.onValueRead(particle.getId(), particle.getInitValue(), INIT_VALUE);
            });
    opDataService.updateElectronOp(
        electronId, electronOp -> electronOp.setState(OpState.Initialized));
  }

  private String flattenedRequestOf(String intent, Object payload) throws JsonProcessingException {
    FlattenedMessage flattenedMessage = new FlattenedMessage();
    flattenedMessage.setTitle(intent);
    flattenedMessage.setPayload(objectMapper.writeValueAsString(payload));
    return objectMapper.writeValueAsString(flattenedMessage);
  }

  private String replyOf(Object object) throws JsonProcessingException {
    return objectMapper.writeValueAsString(object);
  }

  private String responseOf(String result, String... explain) {
    return result + ":" + String.join("|", explain);
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
      response = new FlattenedMessage(FAILURE, "unknown intent " + request.getTitle());
    }
    return response.flat();
  }
}
