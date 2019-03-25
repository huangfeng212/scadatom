package io.scadatom.electron.serial.modbus.master;

import static java.time.temporal.ChronoUnit.MILLIS;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.ModbusIOException;
import com.ghgande.j2mod.modbus.ModbusSlaveException;
import com.ghgande.j2mod.modbus.io.ModbusSerialTransaction;
import com.ghgande.j2mod.modbus.msg.ModbusResponse;
import com.ghgande.j2mod.modbus.net.AbstractSerialConnection;
import com.ghgande.j2mod.modbus.net.SerialConnection;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import io.scadatom.electron.service.AbstractChargerService;
import io.scadatom.electron.service.CommandWatcher;
import io.scadatom.electron.service.OpChangeService;
import io.scadatom.electron.service.OpDataService;
import io.scadatom.electron.service.util.DoubleUtil;
import io.scadatom.electron.service.util.SerialPortUtil;
import io.scadatom.neutron.ElectronInitReq;
import io.scadatom.neutron.OpState;
import io.scadatom.neutron.SmmBondDTO;
import io.scadatom.neutron.SmmChargerDTO;
import io.scadatom.neutron.SmmDeviceDTO;
import io.scadatom.neutron.SmmPollStatus;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SmmChargerService extends AbstractChargerService implements Runnable {

  private final Logger log = LoggerFactory.getLogger(SmmChargerService.class);

  private final OpChangeService opChangeService;
  private final OpDataService opDataService;
  private SerialParameters serialParameters;
  private AbstractSerialConnection abstractSerialConnection;
  private ModbusSerialTransaction modbusSerialTransaction;
  private Thread updateThread;
  private volatile boolean updateFlag;
  private SmmChargerDTO smmChargerDTO;
  private Map<Long, SmmDeviceDTO> smmDeviceDTOMap = new HashMap<>();
  private Map<Long, SmmBondDTO> smmBondDTOMap = new HashMap<>();
  private Map<Long, SmmBondOperation> smmBondOperationMap =
      new HashMap<>(); // only enabled bonds enters here

  public SmmChargerService(OpChangeService opChangeService, OpDataService opDataService) {
    this.opChangeService = opChangeService;
    this.opDataService = opDataService;
  }

  @Override
  public void start() {
    try {
      log.info("starting with port {}...", serialParameters.getPortName());
      abstractSerialConnection.open();
      modbusSerialTransaction = new ModbusSerialTransaction(abstractSerialConnection);
      modbusSerialTransaction.setRetries(smmChargerDTO.getRetry());
      modbusSerialTransaction.setTransDelayMS(smmChargerDTO.getTransDelay());
      log.info("starting polling thread...");
      updateFlag = true;
      updateThread.start();
      opDataService.updateSmmChargerOp(
          smmChargerDTO.getId(), smmChargerOp -> smmChargerOp.setState(OpState.Started));
    } catch (IOException ex) {
      log.error(
          "port {} failed to open, {} {}",
          serialParameters.getPortName(),
          ex.getClass().getSimpleName(),
          ex.getMessage());
      throw new RuntimeException(
          "non-existing port "
              + smmChargerDTO.getPort()
              + " assigned to SerialModbusMasterCharger");
    }
  }

  @Override
  public void stop() {
    try {
      updateFlag = false;
      log.info("stoping polling thread...");
      updateThread.join();
      log.info("success!");
    } catch (InterruptedException ex) {
      log.error(
          "joining polling thread failed. {}, {}", ex.getClass().getSimpleName(), ex.getMessage());
    }
    log.info("stopping port {}...", serialParameters.getPortName());
    abstractSerialConnection.close(); // todo can this be closed if already closed?
    log.info("success!");
    opDataService.updateSmmChargerOp(
        smmChargerDTO.getId(), smmChargerOp -> smmChargerOp.setState(OpState.Stopped));
  }

  @Override
  public void initialize(@NotNull ElectronInitReq config) {
    smmChargerDTO = config.getSmmChargerDTO();
    if (!smmChargerDTO.getEnabled()) {
      opDataService.updateSmmChargerOp(
          smmChargerDTO.getId(), smmChargerOp -> smmChargerOp.setState(OpState.Disabled));
      return;
    }
    config
        .getSmmDeviceDTOS()
        .forEach(smmDeviceDTO -> smmDeviceDTOMap.put(smmDeviceDTO.getId(), smmDeviceDTO));
    config
        .getSmmBondDTOS()
        .forEach(smmBondDTO -> smmBondDTOMap.put(smmBondDTO.getId(), smmBondDTO));
    serialParameters =
        SerialPortUtil.acquirePort(
            smmChargerDTO.getPort(),
            smmChargerDTO.getBaud(),
            smmChargerDTO.getDatabit(),
            smmChargerDTO.getParity(),
            smmChargerDTO.getStopbit());
    smmChargerDTO
        .getSmmDevices()
        .forEach(
            deviceRef -> {
              SmmDeviceDTO smmDeviceDTO = smmDeviceDTOMap.get(deviceRef.getId());
              if (smmDeviceDTO.getEnabled()) {
                smmDeviceDTO
                    .getSmmBonds()
                    .forEach(
                        bondRef -> {
                          SmmBondDTO smmBondDTO = smmBondDTOMap.get(bondRef.getId());
                          if (smmBondDTO.getEnabled()) {
                            switch (smmBondDTO.getRegType()) {
                              case Coil:
                                CoilSmmBondOperation coilSmmBondOperation =
                                    new CoilSmmBondOperation(smmDeviceDTO, smmBondDTO);
                                smmBondOperationMap.put(smmBondDTO.getId(), coilSmmBondOperation);
                                opDataService.updateSmmBondOp(
                                    smmBondDTO.getId(),
                                    smmBondOp -> {
                                      smmBondOp.setReadRequest(
                                          coilSmmBondOperation.getReadRequest().getHexMessage());
                                    });
                                opChangeService.addCommandWatcher(
                                    smmBondDTO.getParticle().getId(), coilSmmBondOperation);
                                break;
                              case InputDiscrete:
                                InputDiscreteSmmBondOperation inputDiscreteSmmBondOperation =
                                    new InputDiscreteSmmBondOperation(smmDeviceDTO, smmBondDTO);
                                smmBondOperationMap.put(
                                    smmBondDTO.getId(), inputDiscreteSmmBondOperation);
                                opDataService.updateSmmBondOp(
                                    smmBondDTO.getId(),
                                    smmBondOp -> {
                                      smmBondOp.setReadRequest(
                                          inputDiscreteSmmBondOperation
                                              .getReadRequest()
                                              .getHexMessage());
                                    });
                                break;
                              case InputReg:
                                InputRegSmmBondOperation inputRegSmmBondOperation =
                                    new InputRegSmmBondOperation(smmDeviceDTO, smmBondDTO);
                                smmBondOperationMap.put(
                                    smmBondDTO.getId(), inputRegSmmBondOperation);
                                opDataService.updateSmmBondOp(
                                    smmBondDTO.getId(),
                                    smmBondOp -> {
                                      smmBondOp.setReadRequest(
                                          inputRegSmmBondOperation
                                              .getReadRequest()
                                              .getHexMessage());
                                    });
                                break;
                              case HoldingReg:
                                HoldingRegSmmBondOperation holdingRegSmmBondOperation =
                                    new HoldingRegSmmBondOperation(smmDeviceDTO, smmBondDTO);
                                smmBondOperationMap.put(
                                    smmBondDTO.getId(), holdingRegSmmBondOperation);
                                opChangeService.addCommandWatcher(
                                    smmBondDTO.getParticle().getId(), holdingRegSmmBondOperation);
                                opDataService.updateSmmBondOp(
                                    smmBondDTO.getId(),
                                    smmBondOp -> {
                                      smmBondOp.setReadRequest(
                                          holdingRegSmmBondOperation
                                              .getReadRequest()
                                              .getHexMessage());
                                    });
                                break;
                            }
                            opDataService.updateSmmBondOp(
                                smmBondDTO.getId(),
                                smmBondOp -> smmBondOp.setState(OpState.Initialized));
                          } else {
                            opDataService.updateSmmBondOp(
                                smmBondDTO.getId(),
                                smmBondOp -> smmBondOp.setState(OpState.Disabled));
                          }
                        });
                opDataService.updateSmmDeviceOp(
                    smmDeviceDTO.getId(), smmDeviceOp -> smmDeviceOp.setState(OpState.Initialized));
              } else {
                opDataService.updateSmmDeviceOp(
                    smmDeviceDTO.getId(), smmDeviceOp -> smmDeviceOp.setState(OpState.Disabled));
              }
            });

    // conn and thread
    abstractSerialConnection = new SerialConnection(serialParameters);
    abstractSerialConnection.setTimeout(this.smmChargerDTO.getTimeout());
    updateThread = new Thread(this, "SerialModbusMasterCharger updater");
    opDataService.updateSmmChargerOp(
        smmChargerDTO.getId(), smmChargerOp -> smmChargerOp.setState(OpState.Initialized));
  }

  @Override
  public OpState getState() {
    return opDataService.getSmmChargerOp(smmChargerDTO.getId()).getState();
  }

  @Override
  public void run() {
    smmBondOperationMap
        .values()
        .forEach(
            smmBondOperation ->
                opDataService.updateSmmBondOp(
                    smmBondOperation.getSmmBondDTO().getId(),
                    smmBondOp -> smmBondOp.setState(OpState.Started)));
    outerloop:
    while (updateFlag) {
      try {
        Instant untilInstant = Instant.now().plus(smmChargerDTO.getBatchDelay(), MILLIS);
        for (SmmBondOperation smmBondOperation : smmBondOperationMap.values()) {
          if (updateFlag) {
            readStsAndWriteCmd(smmBondOperation); // sts and comm will be set inside
          } else {
            break outerloop;
          }
        }
        Instant endInstant = Instant.now();
        long millistoRest = Duration.between(endInstant, untilInstant).toMillis();
        if (millistoRest > 0) {
          Thread.sleep(millistoRest);
        }
      } catch (Exception ex) {
        log.error("{}, {}", ex.getClass().getSimpleName(), ex.getMessage());
      }
    }
    smmBondOperationMap
        .values()
        .forEach(
            smmBondOperation ->
                opDataService.updateSmmBondOp(
                    smmBondOperation.getSmmBondDTO().getId(),
                    smmBondOp -> {
                      smmBondOp.setState(OpState.Stopped);
                      smmBondOp.setPollStatus(SmmPollStatus.NA);
                    }));
  }

  private void readStsAndWriteCmd(@NotNull SmmBondOperation smmBondOperation) {
    try {
      ModbusResponse modbusResponse;
      synchronized (this) {
        modbusSerialTransaction.setRequest(smmBondOperation.getReadRequest());
        modbusSerialTransaction.execute();
        modbusResponse = modbusSerialTransaction.getResponse();
        double readSts = smmBondOperation.parseReadResponse(modbusResponse);
        ModbusResponse finalModbusResponse1 = modbusResponse;
        opDataService.updateSmmBondOp(
            smmBondOperation.getSmmBondDTO().getId(),
            smmBondOp -> {
              smmBondOp.setPollStatus(SmmPollStatus.Normal);
              smmBondOp.setReadResponse(finalModbusResponse1.getHexMessage());
            });
        if (!DoubleUtil.doubleEqual(
            readSts, smmBondOperation.getSts())) { // observed difference, notify watcher
          smmBondOperation.setSts(readSts);
          opChangeService.onValueRead(
              smmBondOperation.getSmmBondDTO().getParticle().getId(),
              String.valueOf(smmBondOperation.getSts()),
              "SmmBond_" + smmBondOperation.getSmmBondDTO().getId());
        }
        if (smmBondOperation.isWritable()) {
          CommandWatcher commandWatcher = (CommandWatcher) smmBondOperation;
          if (commandWatcher.getCommand() != null) { // there is a command pending
            if (!DoubleUtil.doubleEqual(
                smmBondOperation.getSts(),
                Double.valueOf(commandWatcher.getCommand()))) { // command is not applied yet
              log.debug(
                  "SerialModbusMasterBond {} writing cmd {} to particle {}, when sts is {}",
                  smmBondOperation.getSmmBondDTO().getId(),
                  commandWatcher.getCommand(),
                  smmBondOperation.getSmmBondDTO().getParticle().getId(),
                  smmBondOperation.getSts());
              try {
                synchronized (this) {
                  modbusSerialTransaction.setRequest(smmBondOperation.getWriteRequest());
                  modbusSerialTransaction.execute();
                  modbusResponse = modbusSerialTransaction.getResponse();
                }
                ModbusResponse finalModbusResponse = modbusResponse;
                opDataService.updateSmmBondOp(
                    smmBondOperation.getSmmBondDTO().getId(),
                    smmBondOp -> {
                      smmBondOp.setWriteRequest(smmBondOperation.getWriteRequest().getHexMessage());
                      smmBondOp.setWriteResponse(finalModbusResponse.getHexMessage());
                      smmBondOp.setWriteRequestDt(ZonedDateTime.now());
                    });
              } catch (ModbusException e) {
                // ignore, don't care
              }
            } else {
              commandWatcher.clearCommand();
            }
          }
        }
      }
    } catch (ModbusIOException e) {
      opDataService.updateSmmBondOp(
          smmBondOperation.getSmmBondDTO().getId(),
          smmBondOp -> smmBondOp.setPollStatus(SmmPollStatus.IOError));
    } catch (ModbusSlaveException e) {
      opDataService.updateSmmBondOp(
          smmBondOperation.getSmmBondDTO().getId(),
          smmBondOp -> smmBondOp.setPollStatus(SmmPollStatus.fromModbusSlaveException(e)));
    } catch (ModbusException e) {
      opDataService.updateSmmBondOp(
          smmBondOperation.getSmmBondDTO().getId(),
          smmBondOp -> smmBondOp.setPollStatus(SmmPollStatus.UnknownError));
      e.printStackTrace();
    }
  }
}
