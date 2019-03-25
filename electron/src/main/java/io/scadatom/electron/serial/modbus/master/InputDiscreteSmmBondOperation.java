package io.scadatom.electron.serial.modbus.master;

import com.ghgande.j2mod.modbus.msg.ModbusRequest;
import com.ghgande.j2mod.modbus.msg.ModbusResponse;
import com.ghgande.j2mod.modbus.msg.ReadInputDiscretesRequest;
import com.ghgande.j2mod.modbus.msg.ReadInputDiscretesResponse;
import com.ghgande.j2mod.modbus.util.BitVector;
import io.scadatom.neutron.SmmBondDTO;
import io.scadatom.neutron.SmmDeviceDTO;

public class InputDiscreteSmmBondOperation extends SmmBondOperation {

  private final ReadInputDiscretesRequest readRequest;

  InputDiscreteSmmBondOperation(SmmDeviceDTO smmDeviceDTO, SmmBondDTO smmBondDTO) {
    super(smmDeviceDTO, smmBondDTO);
    readRequest = new ReadInputDiscretesRequest();
    readRequest.setReference(refStart);
    readRequest.setBitCount(1);
    readRequest.setUnitID(addr);
    readRequest.setHeadless();
  }

  @Override
  public double parseReadResponse(ModbusResponse modbusResponse) {
    BitVector bv = ((ReadInputDiscretesResponse) modbusResponse).getDiscretes();
    bv.forceSize(1);
    return bv.getBit(0) ? 1 : 0;
  }

  @Override
  public boolean isWritable() {
    return false;
  }

  @Override
  public ModbusRequest getReadRequest() {
    return readRequest;
  }

  @Override
  public ModbusRequest getWriteRequest() {
    throw new IllegalStateException(
        "should not call getWriteRequest() on InputDiscrete, check isWritable() before calling");
  }
}
