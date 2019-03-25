package io.scadatom.electron.serial.modbus.slave;

import io.scadatom.electron.service.OpChangeService;
import io.scadatom.electron.service.ValueWatcher;
import io.scadatom.neutron.SmsBondDTO;

public abstract class SmsBondOperation implements ValueWatcher {

  protected final SmsBondDTO smsBondDTO;
  protected final OpChangeService opChangeService;
  final int regStart;

  SmsBondOperation(SmsBondDTO smsBondDTO, OpChangeService opChangeService) {
    this.smsBondDTO = smsBondDTO;
    this.opChangeService = opChangeService;
    regStart = Integer.decode(this.smsBondDTO.getReg());
    opChangeService.addValueWatcher(smsBondDTO.getParticle().getId(), this);
  }
}
