package io.scadatom.neutron;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the ElectronOp entity.
 */
public class ElectronOpDTO implements Serializable {

  private Long id;

  private OpState state;

  private ZonedDateTime dt;

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ElectronOpDTO electronOpDTO = (ElectronOpDTO) o;
    if (electronOpDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), electronOpDTO.getId());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "ElectronOpDTO{" +
        "id=" + getId() +
        ", state='" + getState() + "'" +
        ", dt='" + getDt() + "'" +
        "}";
  }

  public OpState getState() {
    return state;
  }

  public void setState(OpState state) {
    this.state = state;
  }

  public ZonedDateTime getDt() {
    return dt;
  }

  public void setDt(ZonedDateTime dt) {
    this.dt = dt;
  }
}
