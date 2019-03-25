package io.scadatom.electron.service;

import io.scadatom.neutron.ElectronInitReq;
import io.scadatom.neutron.OpState;

public abstract class AbstractChargerService {

  public abstract void start();

  public abstract void stop();

  public abstract void initialize(ElectronInitReq config);

  public abstract OpState getState();
}
