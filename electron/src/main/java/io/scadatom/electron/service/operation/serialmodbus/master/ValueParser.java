package io.scadatom.electron.service.operation.serialmodbus.master;

import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.procimg.Register;

/**
 * @author fhuang
 */
public interface ValueParser<T extends Number> {

    T toNumber(InputRegister[] inputRegisters);

    double toDouble(InputRegister[] inputRegisters);

    T toNumber(InputRegister[] inputRegisters, int startIndex);

    double toDouble(InputRegister[] inputRegisters, int startIndex);

    Register[] fromNumber(T value);

    Register[] fromDouble(Double value);

    int getWordCount();
}
