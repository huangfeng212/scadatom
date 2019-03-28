package io.scadatom.electron.service.operation.serialmodbus.slave;

import com.ghgande.j2mod.modbus.procimg.SimpleProcessImage;
import com.ghgande.j2mod.modbus.util.Observable;
import com.ghgande.j2mod.modbus.util.Observer;
import io.scadatom.electron.service.operation.OpEventService;
import io.scadatom.electron.service.util.DoubleUtil;
import io.scadatom.neutron.SmsBondDTO;

public class CoilSmsBondOperation extends SmsBondOperation implements Observer {

  private SelectiveObservableDigitalOut storage;

  CoilSmsBondOperation(
      SmsBondDTO smsBondDTO, SimpleProcessImage spi, OpEventService opEventService) {
    super(smsBondDTO, opEventService);
    SelectiveObservableDigitalOut so = new SelectiveObservableDigitalOut();
    so.addObserver(this);
    storage = so;
    spi.addDigitalOut(regStart, so);
  }

  @Override
  public void onValueChange(String newValue) {
    switch (DoubleUtil.toInt(Double.parseDouble(newValue))) {
      case 1:
        storage.setOnly(true);
        break;
      case 0:
        storage.setOnly(false);
        break;
      default:
        throw new IllegalArgumentException("only 0 or 1 allowed");
    }
  }

  /**
   * Callback when coil or holdReg are written from external. The underlying storage is already
   * mutated at this point. This method will update bondRt's sts as well as sending notifyCmdChange
   * to upper level
   */
  @Override
  public void update(Observable o, Object arg) {
    double cmd = storage.isSet() ? 1 : 0;
    opEventService.onCommandWritten(
        smsBondDTO.getParticle().getId(), String.valueOf(cmd), "SmmBond_" + smsBondDTO.getId());
  }
}
