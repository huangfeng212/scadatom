package io.scadatom.electron.serial.modbus.master;

import com.ghgande.j2mod.modbus.msg.ModbusRequest;
import com.ghgande.j2mod.modbus.msg.ModbusResponse;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;
import com.ghgande.j2mod.modbus.msg.WriteMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import io.scadatom.electron.service.CommandWatcher;
import io.scadatom.neutron.SmmBondDTO;
import io.scadatom.neutron.SmmDeviceDTO;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class HoldingRegSmmBondOperation extends SmmBondOperation implements CommandWatcher {

  private final Expression exprOut;
  private final Expression exprIn;
  private final ReadMultipleRegistersRequest readRequest;
  private final WriteMultipleRegistersRequest writeRequest;
  private final ValueParser valueParser;
  private String cmd;

  HoldingRegSmmBondOperation(SmmDeviceDTO smmDeviceDTO, SmmBondDTO smmBondDTO) {
    super(smmDeviceDTO, smmBondDTO);
    writeRequest = new WriteMultipleRegistersRequest();
    writeRequest.setReference(refStart);
    writeRequest.setUnitID(addr);
    writeRequest.setHeadless();
    exprOut = new ExpressionBuilder(smmBondDTO.getExprOut()).variables("x").build();
    switch (smmBondDTO.getValueType()) {
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
    readRequest = new ReadMultipleRegistersRequest();
    readRequest.setReference(refStart);
    readRequest.setWordCount(valueParser.getWordCount());
    readRequest.setUnitID(addr);
    readRequest.setHeadless();
    exprIn = new ExpressionBuilder(smmBondDTO.getExprIn()).variables("x").build();
  }

  @Override
  public double parseReadResponse(ModbusResponse modbusResponse) {
    InputRegister[] regs = ((ReadMultipleRegistersResponse) modbusResponse).getRegisters();
    double stsRaw = valueParser.toDouble(regs);
    return exprIn.setVariable("x", stsRaw).evaluate();
  }

  @Override
  public boolean isWritable() {
    return true;
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
    double cmdD = exprOut.setVariable("x", Double.valueOf(cmd)).evaluate();
    writeRequest.setRegisters(valueParser.fromDouble(cmdD));
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
    final StringBuilder sb = new StringBuilder("HoldingRegSmmBondOperation{");
    sb.append("cmd=").append(cmd);
    sb.append(", refStart=").append(refStart);
    sb.append(", addr=").append(addr);
    sb.append(", sts=").append(sts);
    sb.append('}');
    return sb.toString();
  }
}
