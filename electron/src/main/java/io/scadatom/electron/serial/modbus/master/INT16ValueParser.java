package io.scadatom.electron.serial.modbus.master;

import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;
import com.ghgande.j2mod.modbus.util.ModbusUtil;

/**
 * @author fhuang
 */
public class INT16ValueParser implements ValueParser<Short> {

    @Override
    public Short toNumber(InputRegister[] inputRegisters) {
        return toNumber(inputRegisters, 0);
    }

    @Override
    public double toDouble(InputRegister[] inputRegisters) {
        return toDouble(inputRegisters, 0);
    }

    @Override
    public Short toNumber(InputRegister[] inputRegisters, int startIndex) {
        return inputRegisters[startIndex].toShort();
    }

    @Override
    public double toDouble(InputRegister[] inputRegisters, int startIndex) {
        return inputRegisters[startIndex].toShort();
    }

    @Override
    public Register[] fromNumber(Short value) {
        byte[] bytes = ModbusUtil.shortToRegister(value);
        return new Register[]{new SimpleRegister(bytes[0], bytes[1])};
    }

    @Override
    public Register[] fromDouble(Double value) {
        return fromNumber(value.shortValue());
    }

    @Override
    public int getWordCount() {
        return 1;
  }
}
