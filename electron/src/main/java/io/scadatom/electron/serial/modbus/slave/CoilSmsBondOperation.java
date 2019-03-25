package io.scadatom.electron.serial.modbus.slave;

import com.ghgande.j2mod.modbus.procimg.SimpleProcessImage;
import com.ghgande.j2mod.modbus.util.Observable;
import com.ghgande.j2mod.modbus.util.Observer;
import io.scadatom.electron.service.OpChangeService;
import io.scadatom.electron.service.util.DoubleUtil;
import io.scadatom.neutron.SmsBondDTO;

public class CoilSmsBondOperation extends SmsBondOperation implements Observer {

  private SelectiveObservableDigitalOut storage;

  CoilSmsBondOperation(
      SmsBondDTO smsBondDTO, SimpleProcessImage spi, OpChangeService opChangeService) {
    super(smsBondDTO, opChangeService);
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
    opChangeService.onCommandWritten(
        smsBondDTO.getParticle().getId(), String.valueOf(cmd), "SmmBond_" + smsBondDTO.getId());
  }
}
