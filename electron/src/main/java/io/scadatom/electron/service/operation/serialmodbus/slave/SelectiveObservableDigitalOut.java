package io.scadatom.electron.service.operation.serialmodbus.slave;

import com.ghgande.j2mod.modbus.procimg.ObservableDigitalOut;

public class SelectiveObservableDigitalOut extends ObservableDigitalOut {
  public void setOnly(boolean b) {
    set = b;
  }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SelectiveObservableDigitalOut{");
        sb.append("set=").append(set);
        sb.append('}');
        return sb.toString();
    }
}
