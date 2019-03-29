package io.scadatom.electron.service.operation.serialmodbus.slave;

import com.ghgande.j2mod.modbus.net.ModbusSerialListener;
import com.ghgande.j2mod.modbus.procimg.ProcessImage;
import com.ghgande.j2mod.modbus.procimg.SimpleProcessImage;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import io.scadatom.electron.service.AbstractChargerService;
import io.scadatom.electron.service.operation.OpEventService;
import io.scadatom.electron.service.operation.OpRepoService;
import io.scadatom.electron.service.util.SerialPortUtil;
import io.scadatom.neutron.ElectronInitReq;
import io.scadatom.neutron.OpState;
import io.scadatom.neutron.SmsBondDTO;
import io.scadatom.neutron.SmsChargerDTO;
import io.scadatom.neutron.SmsDeviceDTO;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SmsChargerService extends AbstractChargerService {

  private final Logger log = LoggerFactory.getLogger(SmsChargerService.class);

  private final OpEventService opEventService;
  private final OpRepoService opRepoService;
  private final Map<Integer, ProcessImage> processImageMap = new HashMap<>();
  private SerialParameters serialParameters;
  private ModbusSerialListener listener;
  private Thread listenerThread;
  private SmsChargerDTO smsChargerDTO;
  private Map<Long, SmsDeviceDTO> smsDeviceDTOMap = new HashMap<>();
  private Map<Long, SmsBondDTO> smsBondDTOMap = new HashMap<>();
  private Map<Long, SmsBondOperation> smsBondOperationMap = new HashMap<>();

  public SmsChargerService(OpEventService opEventService, OpRepoService opRepoService) {
    this.opEventService = opEventService;
    this.opRepoService = opRepoService;
  }

  @Override
  public void start() {
    log.info("opening port {} and starting listener ...", serialParameters.getPortName());
    listenerThread.start();
    opRepoService.updateSmsChargerOp(
        smsChargerDTO.getId(), smsChargerOp -> smsChargerOp.setState(OpState.Started));
  }

  @Override
  public void stop() {
    try {
      log.info("stopping listener thread on port {} ...", serialParameters.getPortName());
      listener.stop();
      listenerThread.join();
    } catch (InterruptedException ex) {
      log.error(
          "joining listener thread failed. {}, {}", ex.getClass().getSimpleName(), ex.getMessage());
    }
    processImageMap.clear();
    opRepoService.updateSmsChargerOp(
        smsChargerDTO.getId(), smsChargerOp -> smsChargerOp.setState(OpState.Stopped));
  }

  @Override
  public void initialize(ElectronInitReq config) {
    smsChargerDTO = config.getSmsChargerDTO();
    if (smsChargerDTO == null) {
      return;
    }
    if (!smsChargerDTO.getEnabled()) {
      opRepoService.updateSmsChargerOp(
          smsChargerDTO.getId(), smsChargerOp -> smsChargerOp.setState(OpState.Disabled));
      return;
    }
    // load into data structure
    config
        .getSmsDeviceDTOS()
        .forEach(smsDeviceDTO -> smsDeviceDTOMap.put(smsDeviceDTO.getId(), smsDeviceDTO));
    config
        .getSmsBondDTOS()
        .forEach(smsBondDTO -> smsBondDTOMap.put(smsBondDTO.getId(), smsBondDTO));
    // weave the relations
    smsChargerDTO
        .getSmsDevices()
        .forEach(
            deviceRef -> {
              SmsDeviceDTO smsDeviceDTO = smsDeviceDTOMap.get(deviceRef.getId());
              if (smsDeviceDTO.getEnabled()) {
                int unitId = Integer.decode(smsDeviceDTO.getAddress());
                SimpleProcessImage spi = new SimpleProcessImage(unitId);
                processImageMap.put(unitId, spi);
                smsDeviceDTO
                    .getSmsBonds()
                    .forEach(
                        bondRef -> {
                          SmsBondDTO smsBondDTO = smsBondDTOMap.get(bondRef.getId());
                          if (smsBondDTO.getEnabled()) {
                            switch (smsBondDTO.getRegType()) {
                              case Coil:
                                smsBondOperationMap.put(
                                    smsBondDTO.getId(),
                                    new CoilSmsBondOperation(smsBondDTO, spi, opEventService));
                                break;
                              case InputDiscrete:
                                smsBondOperationMap.put(
                                    smsBondDTO.getId(),
                                    new InputDiscreteSmsBondOperation(
                                        smsBondDTO, spi, opEventService));
                                break;
                              case InputReg:
                                smsBondOperationMap.put(
                                    smsBondDTO.getId(),
                                    new InputRegSmsBondOperation(smsBondDTO, spi, opEventService));
                                break;
                              case HoldingReg:
                                smsBondOperationMap.put(
                                    smsBondDTO.getId(),
                                    new HoldingRegSmsBondOperation(
                                        smsBondDTO, spi, opEventService));
                                break;
                            }
                            opRepoService.updateSmsBondOp(
                                smsBondDTO.getId(),
                                smsBondOp -> smsBondOp.setState(OpState.Initialized));
                          } else {
                            opRepoService.updateSmsBondOp(
                                smsBondDTO.getId(),
                                smsBondOp -> smsBondOp.setState(OpState.Disabled));
                          }
                        });
                opRepoService.updateSmsDeviceOp(
                    smsDeviceDTO.getId(), smsDeviceOp -> smsDeviceOp.setState(OpState.Initialized));
              } else {
                opRepoService.updateSmsDeviceOp(
                    smsDeviceDTO.getId(), smsDeviceOp -> smsDeviceOp.setState(OpState.Disabled));
              }
            });
    // connection and listener
    serialParameters =
        SerialPortUtil.acquirePort(
            smsChargerDTO.getPort(),
            smsChargerDTO.getBaud(),
            smsChargerDTO.getDatabit(),
            smsChargerDTO.getParity(),
            smsChargerDTO.getStopbit());
    listener =
        new MultiDeviceModbusSerialListener(
            serialParameters, smsChargerDTO.getRespDelay(), processImageMap);
    listenerThread = new Thread(listener, "SmsCharger Service Listener");
    opRepoService.updateSmsChargerOp(
        smsChargerDTO.getId(), smsChargerOp -> smsChargerOp.setState(OpState.Initialized));
  }

  @Override
  public OpState getState() {
    return opRepoService.getSmsChargerOp(smsChargerDTO.getId()).getState();
  }

  @Override
  public Long getChargerId() {
    return smsChargerDTO == null ? null : smsChargerDTO.getId();
  }
}
