package io.scadatom.electron.serial.modbus.master;

import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;
import com.ghgande.j2mod.modbus.util.ModbusUtil;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author fhuang
 */
public class FP32ValueParser implements ValueParser<Float> {

    @Override
    public Float toNumber(InputRegister[] inputRegisters) {
        return toNumber(inputRegisters, 0);
    }

    @Override
    public double toDouble(InputRegister[] inputRegisters) {
        return toDouble(inputRegisters, 0);
    }

    @Override
    public Float toNumber(InputRegister[] inputRegisters, int startIndex) {
        return ModbusUtil.registersToFloat(
            ArrayUtils.addAll(
                inputRegisters[startIndex].toBytes(), inputRegisters[startIndex + 1].toBytes()));
    }

    @Override
    public double toDouble(InputRegister[] inputRegisters, int startIndex) {
        return ModbusUtil.registersToFloat(
            ArrayUtils.addAll(
                inputRegisters[startIndex].toBytes(), inputRegisters[startIndex + 1].toBytes()));
    }

    @Override
    public Register[] fromNumber(Float value) {
        byte[] bytes = ModbusUtil.floatToRegisters(value);
        Register regHi = new SimpleRegister(bytes[0], bytes[1]);
        Register regLo = new SimpleRegister(bytes[2], bytes[3]);
        return new Register[]{regHi, regLo};
    }

    @Override
    public Register[] fromDouble(Double value) {
        return fromNumber(value.floatValue());
    }

    @Override
    public int getWordCount() {
        return 2;
  }
}
