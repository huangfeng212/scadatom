package io.scadatom.electron.service.operation.serialmodbus.master;

import com.ghgande.j2mod.modbus.msg.ModbusRequest;
import com.ghgande.j2mod.modbus.msg.ModbusResponse;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadInputRegistersResponse;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import io.scadatom.neutron.SmmBondDTO;
import io.scadatom.neutron.SmmDeviceDTO;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class InputRegSmmBondOperation extends SmmBondOperation {

  private final Expression exprIn;
  private final ReadInputRegistersRequest readRequest;
  private final ValueParser valueParser;

  InputRegSmmBondOperation(SmmDeviceDTO smmDeviceDTO, SmmBondDTO smmBondDTO) {
    super(smmDeviceDTO, smmBondDTO);
    switch (this.smmBondDTO.getValueType()) {
      case Uint16:
        valueParser = new UINT16ValueParser();
        break;
      default:
      case Int16:
        valueParser = new INT16ValueParser();
        break;
      case Fp32:
        valueParser = new FP32ValueParser();
        break;
    }
    readRequest = new ReadInputRegistersRequest();
    readRequest.setReference(refStart);
    readRequest.setWordCount(valueParser.getWordCount());
    readRequest.setUnitID(addr);
    readRequest.setHeadless();
    exprIn = new ExpressionBuilder(this.smmBondDTO.getExprIn()).variables("x").build();
  }

  @Override
  public double parseReadResponse(ModbusResponse modbusResponse) {
    InputRegister[] regs = ((ReadInputRegistersResponse) modbusResponse).getRegisters();
    double stsRaw = valueParser.toDouble(regs);
    return exprIn.setVariable("x", stsRaw).evaluate();
  }

  @Override
  public ModbusRequest getReadRequest() {
    return readRequest;
  }

  @Override
  public ModbusRequest getWriteRequest() {
    throw new IllegalStateException(
        "should not call getWriteRequest() on InputReg, check isWritable() before calling");
  }
}
