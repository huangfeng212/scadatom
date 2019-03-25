package io.scadatom.electron.serial.modbus.master;

import com.ghgande.j2mod.modbus.msg.ModbusRequest;
import com.ghgande.j2mod.modbus.msg.ModbusResponse;
import io.scadatom.neutron.SmmBondDTO;
import io.scadatom.neutron.SmmDeviceDTO;

public abstract class SmmBondOperation {

  // static attributes
  protected final SmmBondDTO smmBondDTO;
  protected final int refStart;
  protected final int addr;
  // dynamic attributes
  protected double sts = Double.NaN;

  SmmBondOperation(SmmDeviceDTO smmDeviceDTO, SmmBondDTO smmBondDTO) {
    this.smmBondDTO = smmBondDTO;
    refStart = Integer.decode(this.smmBondDTO.getReg());
    addr = Integer.decode(smmDeviceDTO.getAddress());
  }

  public abstract double parseReadResponse(ModbusResponse modbusResponse);

  public abstract boolean isWritable();

  public double getSts() {
    return sts;
  }

  public SmmBondOperation setSts(double sts) {
    this.sts = sts;
    return this;
  }

  SmmBondOperation setStsNaN() {
    sts = Double.NaN;
    return this;
  }

  public abstract ModbusRequest getReadRequest();

  public abstract ModbusRequest getWriteRequest();

  public SmmBondDTO getSmmBondDTO() {
    return smmBondDTO;
  }
}
