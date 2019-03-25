package io.scadatom.neutron;

public class OpViewReq {

  private Long id;

  public OpViewReq id(Long id) {
    setId(id);
    return this;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
