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

/** A ParticleOp. */
@Entity
@Table(name = "particle_op")
public class ParticleOp implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "state")
  private OpState state;

  @Column(name = "dt")
  private ZonedDateTime dt;

  @Column(name = "jhi_value")
  private String value;

  @Column(name = "written_by")
  private String writtenBy;

  @Column(name = "written_dt")
  private ZonedDateTime writtenDt;

  public ParticleOp state(OpState state) {
    this.state = state;
    return this;
  }

  public ParticleOp dt(ZonedDateTime dt) {
    this.dt = dt;
    return this;
  }

  public ParticleOp value(String value) {
    this.value = value;
    return this;
  }

  public ParticleOp writtenBy(String writtenBy) {
    this.writtenBy = writtenBy;
    return this;
  }

  public ParticleOp writtenDt(ZonedDateTime writtenDt) {
    this.writtenDt = writtenDt;
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
    ParticleOp particleOp = (ParticleOp) o;
    if (particleOp.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), particleOp.getId());
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
    return "ParticleOp{"
        + "id="
        + getId()
        + ", state='"
        + getState()
        + "'"
        + ", dt='"
        + getDt()
        + "'"
        + ", value='"
        + getValue()
        + "'"
        + ", writtenBy='"
        + getWrittenBy()
        + "'"
        + ", writtenDt='"
        + getWrittenDt()
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

  public void setDt(ZonedDateTime dt) {
    this.dt = dt;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getWrittenBy() {
    return writtenBy;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  public void setWrittenBy(String writtenBy) {
    this.writtenBy = writtenBy;
  }

  public ZonedDateTime getWrittenDt() {
    return writtenDt;
  }

  public void setWrittenDt(ZonedDateTime writtenDt) {
    this.writtenDt = writtenDt;
  }

  public ParticleOp id(long id) {
    setId(id);
    return this;
  }
}
