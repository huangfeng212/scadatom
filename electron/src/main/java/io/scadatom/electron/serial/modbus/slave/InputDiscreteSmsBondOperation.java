package io.scadatom.electron.serial.modbus.slave;

import com.ghgande.j2mod.modbus.procimg.SimpleDigitalIn;
import com.ghgande.j2mod.modbus.procimg.SimpleProcessImage;
import io.scadatom.electron.service.OpChangeService;
import io.scadatom.electron.service.util.DoubleUtil;
import io.scadatom.neutron.SmsBondDTO;

public class InputDiscreteSmsBondOperation extends SmsBondOperation {

  private SimpleDigitalIn storage;

  InputDiscreteSmsBondOperation(
      SmsBondDTO smsBondDTO, SimpleProcessImage spi, OpChangeService opChangeService) {
    super(smsBondDTO, opChangeService);
    SimpleDigitalIn si = new SimpleDigitalIn();
    storage = si;
    spi.addDigitalIn(regStart, si);
  }

  @Override
  public void onValueChange(String newValue) {
    switch (DoubleUtil.toInt(Double.parseDouble(newValue))) {
      case 1:
        storage.set(true);
        break;
      case 0:
        storage.set(false);
        break;
      default:
        throw new IllegalArgumentException("only 0 or 1 allowed");
    }
  }
}
