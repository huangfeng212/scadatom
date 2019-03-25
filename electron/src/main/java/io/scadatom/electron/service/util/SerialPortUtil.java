package io.scadatom.electron.service.util;

import static com.ghgande.j2mod.modbus.Modbus.SERIAL_ENCODING_RTU;

import com.fazecast.jSerialComm.SerialPort;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import io.scadatom.neutron.Parity;
import io.scadatom.neutron.Stopbit;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerialPortUtil {

  private static final Logger log = LoggerFactory.getLogger(SerialPortUtil.class);

  /**
   * Provide port translation and verification and return constructed {@link SerialParameters}.
   *
   * @param designatedPortName exact name or indexed value starting with #
   * @return translated port name
   * @throws IllegalArgumentException if port not found or indexed value is invalid or port found
   *     but not available
   */
  public static SerialParameters acquirePort(
      @NotBlank String designatedPortName,
      int baud,
      int databit,
      @NotNull Parity parity,
      @NotNull Stopbit stopbit) {
    String portName = verifyPortName(designatedPortName);
    SerialParameters serialParameters = new SerialParameters();
    serialParameters.setPortName(portName);
    serialParameters.setBaudRate(baud);
    serialParameters.setDatabits(databit);
    serialParameters.setParity(parity.ordinal());
    serialParameters.setStopbits(stopbit.ordinal());
    serialParameters.setEncoding(SERIAL_ENCODING_RTU);
    serialParameters.setEcho(false);
    return serialParameters;
  }

  private static String verifyPortName(@NotBlank String designatedName) {
    SerialPort[] serialPorts = SerialPort.getCommPorts();
    log.info(
        "ports:[{}]",
        Arrays.asList(serialPorts).stream()
            .map(SerialPort::getSystemPortName)
            .sorted()
            .collect(Collectors.joining(",")));
    SerialPort serialPort = null;
    if (designatedName.startsWith("#")) { // if assigned by port index(general)
      int index = Integer.valueOf(designatedName.substring(1));
      if (index < serialPorts.length) {
        serialPort = serialPorts[index];
      } else {
        throw new IllegalArgumentException("invalid port index " + designatedName);
      }
    } else { // if assigned by exact name(advanced)
      serialPort =
          Arrays.asList(serialPorts).stream()
              .filter(sp -> sp.getSystemPortName().equals(designatedName))
              .findFirst()
              .orElseThrow(
                  () -> new IllegalArgumentException("Non-exist port name " + designatedName));
    }
    if (!serialPort.closePort() || !serialPort.openPort()) { // flip the port test availability
      throw new IllegalArgumentException(
          "port " + serialPort.getSystemPortName() + " in use or problem");
    }
    serialPort.closePort();
    return serialPort.getSystemPortName();
  }
}
