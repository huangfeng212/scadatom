package io.scadatom.electron.service.operation.serialmodbus.master;

import com.ghgande.j2mod.modbus.msg.ModbusRequest;
import com.ghgande.j2mod.modbus.msg.ModbusResponse;
import io.scadatom.neutron.SmmBondDTO;
import io.scadatom.neutron.SmmDeviceDTO;

public abstract class SmmBondOperation {

  // static attributes
  protected final SmmBondDTO smmBondDTO;
  protected final int refStart;
  protected final int addr;

  SmmBondOperation(SmmDeviceDTO smmDeviceDTO, SmmBondDTO smmBondDTO) {
    this.smmBondDTO = smmBondDTO;
    refStart = Integer.decode(this.smmBondDTO.getReg());
    addr = Integer.decode(smmDeviceDTO.getAddress());
  }

  public abstract double parseReadResponse(ModbusResponse modbusResponse);

  public abstract ModbusRequest getReadRequest();

  public abstract ModbusRequest getWriteRequest();

  public SmmBondDTO getSmmBondDTO() {
    return smmBondDTO;
  }
}
