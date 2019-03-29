package io.scadatom.electron.service.operation.serialmodbus.slave;

import com.ghgande.j2mod.modbus.procimg.SimpleDigitalIn;
import com.ghgande.j2mod.modbus.procimg.SimpleProcessImage;
import io.scadatom.electron.service.operation.OpEventService;
import io.scadatom.electron.service.util.DoubleUtil;
import io.scadatom.neutron.SmsBondDTO;

public class InputDiscreteSmsBondOperation extends SmsBondOperation {

  private SimpleDigitalIn storage;

  InputDiscreteSmsBondOperation(
      SmsBondDTO smsBondDTO, SimpleProcessImage spi, OpEventService opEventService) {
    super(smsBondDTO, opEventService);
    SimpleDigitalIn si = new SimpleDigitalIn();
    storage = si;
    spi.addDigitalIn(regStart, si);
  }

  @Override
  public void onValueChange(String newValue) {
    if (smsBondDTO.getEnabled()) {
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("InputDiscreteSmsBondOperation{");
        sb.append("storage=").append(storage);
        sb.append(", regStart=").append(regStart);
        sb.append('}');
        return sb.toString();
    }
}
