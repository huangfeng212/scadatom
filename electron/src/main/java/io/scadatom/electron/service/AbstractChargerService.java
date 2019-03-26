package io.scadatom.electron.service;

import io.scadatom.neutron.ElectronInitReq;
import io.scadatom.neutron.OpState;
import java.io.IOException;

public abstract class AbstractChargerService {

  public abstract void start() throws Exception;

  public abstract void stop();

  public abstract void initialize(ElectronInitReq config);

  public abstract OpState getState();

  public abstract long getChargerId();
}
