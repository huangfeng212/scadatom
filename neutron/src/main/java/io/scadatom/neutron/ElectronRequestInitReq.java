package io.scadatom.neutron;

public class ElectronRequestInitReq {

  private long id;

  public ElectronRequestInitReq() {}

  public ElectronRequestInitReq(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}
