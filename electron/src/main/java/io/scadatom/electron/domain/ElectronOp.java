package io.scadatom.electron.domain;

import io.scadatom.neutron.OpState;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/** A ElectronOp. */
@Entity
@Table(name = "electron_op")
public class ElectronOp implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "state")
  private OpState state;

  @Column(name = "dt")
  private ZonedDateTime dt;

  public ElectronOp id(Long id) {
    setId(id);
    return this;
  }

  public ElectronOp state(OpState state) {
    this.state = state;
    return this;
  }

  public ElectronOp dt(ZonedDateTime dt) {
    this.dt = dt;
    return this;
  }

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
    ElectronOp electronOp = (ElectronOp) o;
    if (electronOp.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), electronOp.getId());
  }

  @Override
  public String toString() {
    return "ElectronOp{"
        + "id="
        + getId()
        + ", state='"
        + getState()
        + "'"
        + ", dt='"
        + getDt()
        + "'"
        + "}";
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
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  public void setDt(ZonedDateTime dt) {
    this.dt = dt;
  }

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
