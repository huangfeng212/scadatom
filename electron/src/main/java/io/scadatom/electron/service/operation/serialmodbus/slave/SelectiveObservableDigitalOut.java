package io.scadatom.electron.service.operation.serialmodbus.slave;

import com.ghgande.j2mod.modbus.procimg.ObservableDigitalOut;

public class SelectiveObservableDigitalOut extends ObservableDigitalOut {
  public void setOnly(boolean b) {
    set = b;
  }
}
