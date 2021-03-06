package io.scadatom.electron.service.operation.serialmodbus.master;

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
import io.scadatom.electron.service.operation.CommandWatcher;
import io.scadatom.electron.service.operation.OpEventService;
import io.scadatom.electron.service.operation.OpRepoService;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SmmChargerService extends AbstractChargerService implements Runnable {

  private final Logger log = LoggerFactory.getLogger(SmmChargerService.class);

  private final OpEventService opEventService;
  private final OpRepoService opRepoService;
  private SerialParameters serialParameters;
  private AbstractSerialConnection abstractSerialConnection;
  private ModbusSerialTransaction modbusSerialTransaction;
  private Thread updateThread;
  private volatile boolean updateFlag;
  private Optional<SmmChargerDTO> smmChargerDTOOptional = Optional.empty();
  private Map<Long, SmmDeviceDTO> smmDeviceDTOMap = new HashMap<>();
  private Map<Long, SmmBondDTO> smmBondDTOMap = new HashMap<>();
  private Map<Long, SmmBondOperation> smmBondOperationMap =
      new HashMap<>(); // only enabled bonds enters here

  public SmmChargerService(OpEventService opEventService, OpRepoService opRepoService) {
    this.opEventService = opEventService;
    this.opRepoService = opRepoService;
  }

  @Override
  public void start() throws IOException {
    log.info("opening port {} ...", serialParameters.getPortName());
    abstractSerialConnection.open();
    smmChargerDTOOptional.ifPresent(
        smmChargerDTO -> {
          modbusSerialTransaction = new ModbusSerialTransaction(abstractSerialConnection);
          modbusSerialTransaction.setRetries(smmChargerDTO.getRetry());
          modbusSerialTransaction.setTransDelayMS(smmChargerDTO.getTransDelay());
          log.info("starting polling thread ...");
          updateFlag = true;
          updateThread.start();
          opRepoService.updateSmmChargerOp(
              smmChargerDTO.getId(), smmChargerOp -> smmChargerOp.setState(OpState.Started));
        });
  }

  @Override
  public void stop() {
    try {
      updateFlag = false;
      log.info("stopping polling thread ...");
      updateThread.join();
    } catch (InterruptedException ex) {
      log.error(
          "joining polling thread failed. {}, {}", ex.getClass().getSimpleName(), ex.getMessage());
    }
    log.info("stopping port {} ...", serialParameters.getPortName());
    abstractSerialConnection.close(); // todo can this be closed if already closed?
    smmChargerDTOOptional.ifPresent(
        smmChargerDTO ->
            opRepoService.updateSmmChargerOp(
                smmChargerDTO.getId(), smmChargerOp -> smmChargerOp.setState(OpState.Stopped)));
  }

  @Override
  public void initialize(@NotNull ElectronInitReq config) {
    SmmChargerDTO smmChargerDTO = config.getSmmChargerDTO();
    if (smmChargerDTO == null) {
      return;
    }
    smmChargerDTOOptional = Optional.of(smmChargerDTO);
    if (!smmChargerDTO.getEnabled()) {
      opRepoService.updateSmmChargerOp(
          smmChargerDTO.getId(), smmChargerOp -> smmChargerOp.setState(OpState.Disabled));
      return;
    }
    opRepoService.updateSmmChargerOp(
        smmChargerDTO.getId(), smmChargerOp -> smmChargerOp.setState(OpState.Uninitialized));
    // load into data structure
    config
        .getSmmDeviceDTOS()
        .forEach(smmDeviceDTO -> smmDeviceDTOMap.put(smmDeviceDTO.getId(), smmDeviceDTO));
    config
        .getSmmBondDTOS()
        .forEach(smmBondDTO -> smmBondDTOMap.put(smmBondDTO.getId(), smmBondDTO));
    // weave the relations
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
                                opRepoService.updateSmmBondOp(
                                    smmBondDTO.getId(),
                                    smmBondOp -> {
                                      smmBondOp.setReadRequest(
                                          coilSmmBondOperation.getReadRequest().getHexMessage());
                                    });
                                opEventService.addCommandWatcher(
                                    smmBondDTO.getParticle().getId(), coilSmmBondOperation);
                                break;
                              case InputDiscrete:
                                InputDiscreteSmmBondOperation inputDiscreteSmmBondOperation =
                                    new InputDiscreteSmmBondOperation(smmDeviceDTO, smmBondDTO);
                                smmBondOperationMap.put(
                                    smmBondDTO.getId(), inputDiscreteSmmBondOperation);
                                opRepoService.updateSmmBondOp(
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
                                opRepoService.updateSmmBondOp(
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
                                opEventService.addCommandWatcher(
                                    smmBondDTO.getParticle().getId(), holdingRegSmmBondOperation);
                                opRepoService.updateSmmBondOp(
                                    smmBondDTO.getId(),
                                    smmBondOp -> {
                                      smmBondOp.setReadRequest(
                                          holdingRegSmmBondOperation
                                              .getReadRequest()
                                              .getHexMessage());
                                    });
                                break;
                            }
                            opRepoService.updateSmmBondOp(
                                smmBondDTO.getId(),
                                smmBondOp -> smmBondOp.setState(OpState.Initialized));
                          } else {
                            opRepoService.updateSmmBondOp(
                                smmBondDTO.getId(),
                                smmBondOp -> smmBondOp.setState(OpState.Disabled));
                          }
                        });
                opRepoService.updateSmmDeviceOp(
                    smmDeviceDTO.getId(), smmDeviceOp -> smmDeviceOp.setState(OpState.Initialized));
              } else {
                opRepoService.updateSmmDeviceOp(
                    smmDeviceDTO.getId(), smmDeviceOp -> smmDeviceOp.setState(OpState.Disabled));
              }
            });
    // connection and thread
    serialParameters =
        SerialPortUtil.acquirePort(
            smmChargerDTO.getPort(),
            smmChargerDTO.getBaud(),
            smmChargerDTO.getDatabit(),
            smmChargerDTO.getParity(),
            smmChargerDTO.getStopbit());
    abstractSerialConnection = new SerialConnection(serialParameters);
    abstractSerialConnection.setTimeout(smmChargerDTO.getTimeout());
    updateThread = new Thread(this, "SmmCharger Service Poller");
    opRepoService.updateSmmChargerOp(
        smmChargerDTO.getId(), smmChargerOp -> smmChargerOp.setState(OpState.Initialized));
  }

  @Override
  public OpState getState() {
    return smmChargerDTOOptional
        .map(smmChargerDTO -> opRepoService.getSmmChargerOp(smmChargerDTO.getId()).getState())
        .orElse(OpState.Undefined);
  }

  @Override
  public Optional<Long> getChargerId() {
    return smmChargerDTOOptional.map(smmChargerDTO -> smmChargerDTO.getId());
  }

  @Override
  public void run() {
    outerloop:
    while (updateFlag) {
      try {
        Instant untilInstant =
            Instant.now().plus(smmChargerDTOOptional.get().getBatchDelay(), MILLIS);
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
      } catch (Exception ex) { // wont quit thread no matter what happens
        log.error("{}, {}", ex.getClass().getSimpleName(), ex.getMessage());
      }
    }
  }

  private void readStsAndWriteCmd(@NotNull SmmBondOperation smmBondOperation) {
    try {
      ModbusResponse modbusResponse;
      synchronized (this) {
        modbusSerialTransaction.setRequest(smmBondOperation.getReadRequest());
        modbusSerialTransaction.execute();
        modbusResponse = modbusSerialTransaction.getResponse();
        double readValue = smmBondOperation.parseReadResponse(modbusResponse);
        ModbusResponse finalModbusResponse1 = modbusResponse;
        opRepoService.updateSmmBondOp(
            smmBondOperation.getSmmBondDTO().getId(),
            smmBondOp -> {
              smmBondOp.setPollStatus(SmmPollStatus.Normal);
              smmBondOp.setReadResponse(finalModbusResponse1.getHexMessage());
            });
        opEventService.onValueRead(
            smmBondOperation.getSmmBondDTO().getParticle().getId(),
            readValue,
            "SmmBond_" + smmBondOperation.getSmmBondDTO().getId());
        if (smmBondOperation instanceof CommandWatcher) {
          CommandWatcher commandWatcher = (CommandWatcher) smmBondOperation;
          if (commandWatcher.hasPendingCommand()) {
            if (!DoubleUtil.doubleEqual(
                readValue,
                Double.valueOf(commandWatcher.getCommand()))) { // command is not applied yet
              log.debug(
                  "SmmBond {} writing cmd {} to particle {}, when readValue is {}",
                  smmBondOperation.getSmmBondDTO().getId(),
                  commandWatcher.getCommand(),
                  smmBondOperation.getSmmBondDTO().getParticle().getId(),
                  readValue);
              try {
                synchronized (this) {
                  modbusSerialTransaction.setRequest(smmBondOperation.getWriteRequest());
                  modbusSerialTransaction.execute();
                  modbusResponse = modbusSerialTransaction.getResponse();
                }
                ModbusResponse finalModbusResponse = modbusResponse;
                opRepoService.updateSmmBondOp(
                    smmBondOperation.getSmmBondDTO().getId(),
                    smmBondOp -> {
                      smmBondOp.setWriteRequest(smmBondOperation.getWriteRequest().getHexMessage());
                      smmBondOp.setWriteResponse(finalModbusResponse.getHexMessage());
                      smmBondOp.setWriteRequestDt(Instant.now());
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
      opRepoService.updateSmmBondOp(
          smmBondOperation.getSmmBondDTO().getId(),
          smmBondOp -> smmBondOp.setPollStatus(SmmPollStatus.IOError));
    } catch (ModbusSlaveException e) {
      opRepoService.updateSmmBondOp(
          smmBondOperation.getSmmBondDTO().getId(),
          smmBondOp -> smmBondOp.setPollStatus(SmmPollStatus.fromModbusSlaveException(e)));
    } catch (ModbusException e) {
      opRepoService.updateSmmBondOp(
          smmBondOperation.getSmmBondDTO().getId(),
          smmBondOp -> smmBondOp.setPollStatus(SmmPollStatus.UnknownError));
      e.printStackTrace();
    }
  }
}
