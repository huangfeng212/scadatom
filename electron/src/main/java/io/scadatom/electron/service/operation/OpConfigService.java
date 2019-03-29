package io.scadatom.electron.service.operation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.scadatom.neutron.ElectronInitReq;
import io.scadatom.neutron.ParticleDTO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpConfigService {
  public static final String CONFIG_FILE_PATTERN = "electron%d.json";
  private final Logger log = LoggerFactory.getLogger(OpConfigService.class);
  private final ObjectMapper objectMapper;
  private final Long electronId;

  private Optional<ElectronInitReq> config;
  private Map<Long, ParticleDTO> particleDTOMap = new HashMap<>();

  public OpConfigService(ObjectMapper objectMapper, @Value("${electron.id}") Long electronId) {
    this.objectMapper = objectMapper;
    this.electronId = electronId;
    loadConfig();
  }

  private void loadConfig() {
    try {
      ElectronInitReq electronInitReq =
          objectMapper.readValue(
              new File(
                  System.getProperty("user.home")
                      + File.separator
                      + String.format(CONFIG_FILE_PATTERN, electronId)),
              ElectronInitReq.class);
      if (electronInitReq.getElectronDTO().getId().equals(electronId)) {
        config = Optional.of(electronInitReq);
        processConfig();
      } else {
        log.error("config file has invalid electron id");
      }
    } catch (IOException e) {
      log.error("error loading config from disk");
    }
  }

  private void processConfig() {
    particleDTOMap.clear();
    config.ifPresent(
        electronInitReq ->
            electronInitReq
                .getParticleDTOS()
                .forEach(particleDTO -> particleDTOMap.put(particleDTO.getId(), particleDTO)));
  }

  public void saveConfig(ElectronInitReq config) {
    try {
      objectMapper
          .writerWithDefaultPrettyPrinter()
          .writeValue(
              new File(
                  System.getProperty("user.home")
                      + File.separator
                      + String.format(CONFIG_FILE_PATTERN, electronId)),
              config);
      this.config = Optional.of(config);
      processConfig();
    } catch (IOException e) {
      log.error("failed to write config file to disk");
    }
  }

  public Optional<ElectronInitReq> getConfig() {
    return config;
  }

  public Optional<ParticleDTO> getParticleDTO(long particleId) {
    return Optional.ofNullable(particleDTOMap.get(particleId));
  }
}
