package io.scadatom.electron.service;

import io.scadatom.neutron.ElectronInitReq;
import io.scadatom.neutron.OpState;
import java.util.Optional;

public abstract class AbstractChargerService {

  public abstract void start() throws Exception;

  public abstract void stop();

  public abstract void initialize(ElectronInitReq config);

  public abstract OpState getState();

  public abstract Optional<Long> getChargerId();
}
