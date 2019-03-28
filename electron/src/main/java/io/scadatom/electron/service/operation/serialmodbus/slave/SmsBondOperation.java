package io.scadatom.electron.service.operation.serialmodbus.slave;

import io.scadatom.electron.service.operation.OpEventService;
import io.scadatom.electron.service.operation.ValueWatcher;
import io.scadatom.neutron.SmsBondDTO;

public abstract class SmsBondOperation implements ValueWatcher {

  protected final SmsBondDTO smsBondDTO;
  protected final OpEventService opEventService;
  final int regStart;

  SmsBondOperation(SmsBondDTO smsBondDTO, OpEventService opEventService) {
    this.smsBondDTO = smsBondDTO;
    this.opEventService = opEventService;
    regStart = Integer.decode(this.smsBondDTO.getReg());
    opEventService.addValueWatcher(smsBondDTO.getParticle().getId(), this);
  }
}
