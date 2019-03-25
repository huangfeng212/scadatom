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

/** A SmmChargerOp. */
@Entity
@Table(name = "smm_charger_op")
public class SmmChargerOp implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "state")
  private OpState state;

  @Column(name = "dt")
  private ZonedDateTime dt;

  public SmmChargerOp state(OpState state) {
    this.state = state;
    return this;
  }

  public SmmChargerOp dt(ZonedDateTime dt) {
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
    SmmChargerOp smmChargerOp = (SmmChargerOp) o;
    if (smmChargerOp.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), smmChargerOp.getId());
  }

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "SmmChargerOp{"
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
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  public void setState(OpState state) {
    this.state = state;
  }

  public ZonedDateTime getDt() {
    return dt;
  }

  public void setDt(ZonedDateTime dt) {
    this.dt = dt;
  }
    public SmmChargerOp id(long id) {
        setId(id);
        return this;
    }
}
