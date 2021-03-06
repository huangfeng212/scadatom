package io.scadatom.electron.service.operation;

import static io.scadatom.electron.config.RabbitmqConfig.ROUTING_TO_NUCLEUS;
import static io.scadatom.electron.config.RabbitmqConfig.TOPIC_SCADATOM;
import static io.scadatom.neutron.OpIntents.REGISTER_ELECTRON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.scadatom.neutron.ElectronInitReq;
import io.scadatom.neutron.FlattenedMessage;
import io.scadatom.neutron.OpCtrlReq;
import io.scadatom.neutron.OpException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpConfigService {
  public static final String CONFIG_FILE_PATTERN = "electron%d.json";
  private final Logger log = LoggerFactory.getLogger(OpConfigService.class);
  private final ObjectMapper objectMapper;
  private final Long electronId;
  private final RabbitTemplate rabbitTemplate;
  private Optional<ElectronInitReq> config = Optional.empty();
  private Map<Long, ParticleOperation> particleOperationMap = new HashMap<>();

  public OpConfigService(
      ObjectMapper objectMapper,
      @Value("${electron.id}") Long electronId,
      RabbitTemplate rabbitTemplate) {
    this.objectMapper = objectMapper;
    this.electronId = electronId;
    this.rabbitTemplate = rabbitTemplate;
    loadConfig();
  }

  private void loadConfig() {
    try {
      config =
          Optional.of(
              objectMapper.readValue(
                  new File(
                      System.getProperty("user.home")
                          + File.separator
                          + String.format(CONFIG_FILE_PATTERN, electronId)),
                  ElectronInitReq.class));
      processConfig();
    } catch (IOException e) {
      log.error("error loading config from disk, try registering with nucleus");
      registerElectron();
    }
  }

  private void processConfig() {
    particleOperationMap.clear();
    config.ifPresent(
        electronInitReq ->
            electronInitReq
                .getParticleDTOS()
                .forEach(
                    particleDTO -> {
                      particleOperationMap.put(
                          particleDTO.getId(), new ParticleOperation(particleDTO));
                    }));
  }

  public void registerElectron() {
    try {
      OpCtrlReq opCtrlReq = new OpCtrlReq().id(electronId);
      Object resp =
          rabbitTemplate.convertSendAndReceive(
              TOPIC_SCADATOM,
              ROUTING_TO_NUCLEUS,
              new FlattenedMessage(REGISTER_ELECTRON, opCtrlReq).flat());
      config = Optional.of(FlattenedMessage.parseResp(resp, ElectronInitReq.class));
      saveConfig();
      processConfig();
    } catch (JsonProcessingException | OpException e) {
      log.error("error loading config from nucleus, no config");
    }
  }

  private void saveConfig() {
    try {
      objectMapper
          .writerWithDefaultPrettyPrinter()
          .writeValue(
              new File(
                  System.getProperty("user.home")
                      + File.separator
                      + String.format(CONFIG_FILE_PATTERN, electronId)),
              config.get());
    } catch (IOException e) {
      log.error("failed to write config file to disk");
    }
  }

  public Optional<ElectronInitReq> getConfig() {
    return config;
  }

  public Optional<ParticleOperation> getParticleOperation(long particleId) {
    return Optional.ofNullable(particleOperationMap.get(particleId));
  }
}
