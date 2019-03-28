package io.scadatom.electron.service.operation.serialmodbus.slave;

import com.ghgande.j2mod.modbus.procimg.SimpleInputRegister;
import com.ghgande.j2mod.modbus.procimg.SimpleProcessImage;
import com.ghgande.j2mod.modbus.util.ModbusUtil;
import io.scadatom.electron.service.operation.OpEventService;
import io.scadatom.neutron.SmsBondDTO;
import io.scadatom.neutron.ValueType;
import java.util.Arrays;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class InputRegSmsBondOperation extends SmsBondOperation {

  private Expression exprIn;
  private SimpleInputRegister[] storage;

  InputRegSmsBondOperation(
      SmsBondDTO smsBondDTO, SimpleProcessImage spi, OpEventService opEventService) {
    super(smsBondDTO, opEventService);
    exprIn = new ExpressionBuilder(this.smsBondDTO.getExprIn()).variables("x").build();
    if (this.smsBondDTO.getValueType() == ValueType.Fp32) {
      SimpleInputRegister regHi = new SimpleInputRegister(0);
      SimpleInputRegister regLo = new SimpleInputRegister(0);
      storage = new SimpleInputRegister[] {regHi, regLo};
      spi.addInputRegister(regStart, regHi);
      spi.addInputRegister(regStart + 1, regLo);
    } else {
      SimpleInputRegister sir = new SimpleInputRegister(0);
      storage = new SimpleInputRegister[] {sir};
      spi.addInputRegister(regStart, sir);
    }
  }

  @Override
  public void onValueChange(String newValue) {
    try {
      if (smsBondDTO.getValueType() == ValueType.Fp32) {
        float newVal = (float) exprIn.setVariable("x", Double.parseDouble(newValue)).evaluate();
        byte[] bytes = ModbusUtil.floatToRegisters(newVal);
        storage[0].setValue(new byte[] {bytes[0], bytes[1]});
        storage[1].setValue(new byte[] {bytes[2], bytes[3]});
      } else {
        int newVal = (int) exprIn.setVariable("x", Double.parseDouble(newValue)).evaluate();
        storage[0].setValue(newVal);
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("InputRegSmsBondOperation{");
    sb.append("storage=").append(Arrays.toString(storage));
    sb.append(", regStart=").append(regStart);
    sb.append('}');
    return sb.toString();
  }
}
