package io.scadatom.electron.service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.scadatom.neutron.ElectronInitReq;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUtil {
  public static final String CONFIG_FILE_PATTERN = "electron%d.json";
  private static final Logger log = LoggerFactory.getLogger(ConfigUtil.class);

  public static Optional<ElectronInitReq> loadConfig(long electronId) {
    try {
      ElectronInitReq config =
          new ObjectMapper()
              .readValue(
                  new File(
                      System.getProperty("user.home")
                          + File.separator
                          + String.format(CONFIG_FILE_PATTERN, electronId)),
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

  public static void saveConfig(ElectronInitReq config) {
    try {
      new ObjectMapper()
          .writerWithDefaultPrettyPrinter()
          .writeValue(
              new File(
                  System.getProperty("user.home")
                      + File.separator
                      + String.format(CONFIG_FILE_PATTERN, config.getElectronDTO().getId())),
              config);
    } catch (IOException e) {
      log.error("failed to write config file to disk");
      e.printStackTrace();
    }
  }
}
