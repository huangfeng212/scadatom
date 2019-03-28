package io.scadatom.electron.service.operation.serialmodbus.slave;

import com.ghgande.j2mod.modbus.procimg.ObservableRegister;

/** The class provides setValue that does not notify observer */
public class SelectiveObservableRegister extends ObservableRegister {
  public final synchronized void setValueOnly(short s) {
    register = s;
  }

  public final synchronized void setValueOnly(byte[] bytes) {
    if (bytes.length < 2) {
      throw new IllegalArgumentException();
    } else {
      register = (short) (((short) ((bytes[0] << 8))) | (((short) (bytes[1])) & 0xFF));
    }
  }

  public final synchronized void setValueOnly(int v) {
    register = (short) v;
  }
}
