package io.scadatom.electron.service.operation.serialmodbus.master;

import com.ghgande.j2mod.modbus.msg.ModbusRequest;
import com.ghgande.j2mod.modbus.msg.ModbusResponse;
import com.ghgande.j2mod.modbus.msg.ReadCoilsRequest;
import com.ghgande.j2mod.modbus.msg.ReadCoilsResponse;
import com.ghgande.j2mod.modbus.msg.WriteCoilRequest;
import com.ghgande.j2mod.modbus.util.BitVector;
import io.scadatom.electron.service.operation.CommandWatcher;
import io.scadatom.neutron.SmmBondDTO;
import io.scadatom.neutron.SmmDeviceDTO;

public class CoilSmmBondOperation extends SmmBondOperation implements CommandWatcher {

  private final ReadCoilsRequest readRequest;
  private final WriteCoilRequest writeRequest;
  private String cmd;

  CoilSmmBondOperation(SmmDeviceDTO smmDeviceDTO, SmmBondDTO smmBondDTO) {
    super(smmDeviceDTO, smmBondDTO);
    readRequest = new ReadCoilsRequest();
    readRequest.setReference(refStart);
    readRequest.setBitCount(1);
    readRequest.setUnitID(addr);
    readRequest.setHeadless();
    writeRequest = new WriteCoilRequest();
    writeRequest.setReference(refStart);
    writeRequest.setUnitID(addr);
    writeRequest.setHeadless();
  }

  @Override
  public double parseReadResponse(ModbusResponse modbusResponse) {
    BitVector bv = ((ReadCoilsResponse) modbusResponse).getCoils();
    bv.forceSize(1);
    return bv.getBit(0) ? 1 : 0;
  }

  @Override
  public ModbusRequest getReadRequest() {
    return readRequest;
  }

  @Override
  public ModbusRequest getWriteRequest() {
    return writeRequest;
  }

  @Override
  public void onCommandWritten(String newCmd) {
    cmd = newCmd;
    boolean cmdB = Math.abs(Double.valueOf(newCmd) - 0.0) > 0.001;
    writeRequest.setCoil(cmdB);
  }

  @Override
  public String getCommand() {
    return cmd;
  }

  @Override
  public void clearCommand() {
    cmd = null;
  }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CoilSmmBondOperation{");
        sb.append("readRequest=").append(readRequest);
        sb.append(", writeRequest=").append(writeRequest);
        sb.append(", cmd='").append(cmd).append('\'');
        sb.append(", refStart=").append(refStart);
        sb.append(", addr=").append(addr);
        sb.append('}');
        return sb.toString();
    }
}
