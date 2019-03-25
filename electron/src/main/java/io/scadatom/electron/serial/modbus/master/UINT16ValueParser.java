package io.scadatom.electron.serial.modbus.master;

import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;
import com.ghgande.j2mod.modbus.util.ModbusUtil;

/**
 * @author fhuang
 */
public class UINT16ValueParser implements ValueParser<Integer> {

    @Override
    public Integer toNumber(InputRegister[] inputRegisters) {
        return toNumber(inputRegisters, 0);
    }

    @Override
    public double toDouble(InputRegister[] inputRegisters) {
        return toDouble(inputRegisters, 0);
    }

    @Override
    public Integer toNumber(InputRegister[] inputRegisters, int startIndex) {
        return inputRegisters[startIndex].toUnsignedShort();
    }

    @Override
    public double toDouble(InputRegister[] inputRegisters, int startIndex) {
        return inputRegisters[startIndex].toUnsignedShort();
    }

    @Override
    public Register[] fromNumber(Integer value) {
        byte[] bytes = ModbusUtil.unsignedShortToRegister(value);
        return new Register[]{new SimpleRegister(bytes[0], bytes[1])};
    }

    @Override
    public Register[] fromDouble(Double value) {
        return fromNumber(value.intValue());
    }

    @Override
    public int getWordCount() {
        return 1;
  }
}
