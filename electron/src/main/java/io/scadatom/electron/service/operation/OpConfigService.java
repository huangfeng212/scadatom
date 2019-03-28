package io.scadatom.electron.service.operation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.scadatom.neutron.ElectronInitReq;
import java.io.File;
import java.io.IOException;
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

  private Optional<ElectronInitReq> optionalElectronInitReq = Optional.empty();

  public OpConfigService(ObjectMapper objectMapper, @Value("${electron.id}") Long electronId) {
    this.objectMapper = objectMapper;
    this.electronId = electronId;
  }

  public Optional<ElectronInitReq> loadConfig() {
    try {
      ElectronInitReq config =
          objectMapper.readValue(
              new File(
                  System.getProperty("user.home")
                      + File.separator
                      + String.format(CONFIG_FILE_PATTERN, electronId)),
              ElectronInitReq.class);
      if (config.getElectronDTO().getId().equals(electronId)) {
        return optionalElectronInitReq = Optional.of(config);
      } else {
        log.error("config file has invalid electron id");
      }
    } catch (IOException e) {
      log.error("error loading config from disk");
    }
    return optionalElectronInitReq = Optional.empty();
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
      optionalElectronInitReq = Optional.of(config);
    } catch (IOException e) {
      log.error("failed to write config file to disk");
    }
  }

  public Optional<ElectronInitReq> getOptionalElectronInitReq() {
    return optionalElectronInitReq;
  }
}
