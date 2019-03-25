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
import io.scadatom.neutron.FlattenedRequest;
import io.scadatom.neutron.OpState;
import io.scadatom.neutron.OpViewReq;
import io.scadatom.neutron.ParticleCtrlReq;
import io.scadatom.neutron.SmsChargerDTO;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
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
  }

  @RabbitListener(queues = "#{queueInboundRequest.name}")
  public String onInboundRequest(String message) throws IOException {
    FlattenedRequest flattenedRequest = objectMapper.readValue(message, FlattenedRequest.class);
    String response = FAILURE;
    OpViewReq opViewReq;
    switch (flattenedRequest.getIntent()) {
      case "initElectron":
        ElectronInitReq electronInitReq =
            objectMapper.readValue(flattenedRequest.getPayload(), ElectronInitReq.class);
        if (!Objects.equals(electronInitReq.getElectronDTO().getId(), electronId)) {
          throw new IllegalArgumentException("non matching electron id from payload");
        }
        saveConfig(electronInitReq);
        stop();
        onStart();
        response = SUCCESS;
        break;
      case "viewElectron":
        opViewReq = objectMapper.readValue(flattenedRequest.getPayload(), OpViewReq.class);
        response = replyOf(electronOpMapper.toDto(opDataService.getElectronOp(opViewReq.getId())));
        break;
      case "ctrlElectron":
        response = "unsupported";
        break;
      case "viewParticle":
        opViewReq = objectMapper.readValue(flattenedRequest.getPayload(), OpViewReq.class);
        response = replyOf(particleOpMapper.toDto(opDataService.getParticleOp(opViewReq.getId())));
        break;
      case "ctrlParticle":
        ParticleCtrlReq particleCtrlReq =
            objectMapper.readValue(flattenedRequest.getPayload(), ParticleCtrlReq.class);
        opChangeService.onCommandWritten(
            particleCtrlReq.getId(),
            particleCtrlReq.getCommand(),
            "RemoteUser_" + particleCtrlReq.getUser());
        response = SUCCESS;
        break;
    }
    return response;
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

  private String replyOf(Object object) throws JsonProcessingException {
    return objectMapper.writeValueAsString(object);
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

  private String flattenedRequestOf(String intent, Object payload) throws JsonProcessingException {
    FlattenedRequest flattenedRequest = new FlattenedRequest();
    flattenedRequest.setIntent(intent);
    flattenedRequest.setPayload(objectMapper.writeValueAsString(payload));
    return objectMapper.writeValueAsString(flattenedRequest);
  }
}
