package io.scadatom.neutron;

import com.ghgande.j2mod.modbus.ModbusSlaveException;

/** The SmmPollStatus enumeration. */
public enum SmmPollStatus {
  NA,
  Normal,
  IOError,
  IllegalFunction,
  IllegalDataAddress,
  IllegalDataValue,
  SlaveDeviceFailure,
  Acknowledge,
  SlaveDeviceBusy,
  MemoryParityError,
  UnknownError;

  public static SmmPollStatus fromModbusSlaveException(ModbusSlaveException modbusSlaveException) {
    switch (modbusSlaveException.getType()) {
      case 1:
        return IllegalFunction;
      case 2:
        return IllegalDataAddress;
      case 3:
        return IllegalDataValue;
      case 4:
        return SlaveDeviceFailure;
      case 5:
        return Acknowledge;
      case 6:
        return SlaveDeviceBusy;
      case 8:
        return MemoryParityError;
      default:
        return UnknownError;
    }
  }
}
