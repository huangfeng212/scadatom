package io.scadatom.electron.service.operation.serialmodbus.slave;

import com.ghgande.j2mod.modbus.procimg.SimpleProcessImage;
import com.ghgande.j2mod.modbus.util.ModbusUtil;
import com.ghgande.j2mod.modbus.util.Observable;
import com.ghgande.j2mod.modbus.util.Observer;
import io.scadatom.electron.service.operation.OpEventService;
import io.scadatom.neutron.SmsBondDTO;
import io.scadatom.neutron.ValueType;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.commons.lang3.ArrayUtils;

public class HoldingRegSmsBondOperation extends SmsBondOperation implements Observer {

  private Expression exprIn;
  private Expression exprOut;
  private SelectiveObservableRegister[] storage;

  HoldingRegSmsBondOperation(
      SmsBondDTO smsBondDTO, SimpleProcessImage spi, OpEventService opEventService) {
    super(smsBondDTO, opEventService);
    exprIn = new ExpressionBuilder(this.smsBondDTO.getExprIn()).variables("x").build();
    exprOut = new ExpressionBuilder(this.smsBondDTO.getExprOut()).variables("x").build();
    if (this.smsBondDTO.getValueType() == ValueType.Fp32) {
      SelectiveObservableRegister regHi = new SelectiveObservableRegister();
      SelectiveObservableRegister regLo = new SelectiveObservableRegister();
      regLo.addObserver(
          this); // assume writing hi first then low, so if low is written,then hi is updated
      // already
      storage = new SelectiveObservableRegister[] {regHi, regLo};
      spi.addRegister(regStart, regHi);
      spi.addRegister(regStart + 1, regLo);
    } else {
      SelectiveObservableRegister sr = new SelectiveObservableRegister();
      sr.addObserver(this);
      storage = new SelectiveObservableRegister[] {sr};
      spi.addRegister(regStart, sr);
    }
  }

  @Override
  public void onValueChange(String newValue) {
    try {
      if (smsBondDTO.getValueType() == ValueType.Fp32) {
        float newVal = (float) exprIn.setVariable("x", Double.parseDouble(newValue)).evaluate();
        byte[] bytes = ModbusUtil.floatToRegisters(newVal);
        storage[0].setValueOnly(new byte[] {bytes[0], bytes[1]});
        storage[1].setValueOnly(new byte[] {bytes[2], bytes[3]});
      } else {
        int newVal = (int) exprIn.setVariable("x", Double.parseDouble(newValue)).evaluate();
        storage[0].setValueOnly(newVal);
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
  }

  /**
   * Callback when coil or holdReg are written from external. The underlying storage is already
   * mutated at this point. This method will update bondRt's sts as well as sending notifyCmdChange
   * to upper level
   */
  @Override
  public void update(Observable o, Object arg) {
    double cmdRaw, cmd;
    switch (smsBondDTO.getValueType()) {
      case Fp32:
        cmdRaw =
            ModbusUtil.registersToFloat(
                ArrayUtils.addAll(storage[0].toBytes(), storage[1].toBytes()));
        cmd = exprOut.setVariable("x", cmdRaw).evaluate();
        break;
      case Uint16:
        cmdRaw = storage[0].toUnsignedShort();
        cmd = exprOut.setVariable("x", cmdRaw).evaluate();
        break;
      case Int16:
        cmdRaw = storage[0].toShort();
        cmd = exprOut.setVariable("x", cmdRaw).evaluate();
        break;
      default:
        throw new IllegalArgumentException("regType invalid");
    }
    opEventService.onCommandWritten(
        smsBondDTO.getParticle().getId(), String.valueOf(cmd), "SmsBond_" + smsBondDTO.getId());
  }
}
