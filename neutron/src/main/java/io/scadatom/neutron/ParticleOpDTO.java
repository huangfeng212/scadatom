package io.scadatom.neutron;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the ParticleOp entity.
 */
public class ParticleOpDTO implements Serializable {

  private Long id;

  private OpState state;

  private ZonedDateTime dt;

  private String value;

  private String writtenBy;

  private ZonedDateTime writtenDt;

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

    ParticleOpDTO particleOpDTO = (ParticleOpDTO) o;
    if (particleOpDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), particleOpDTO.getId());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "ParticleOpDTO{" +
        "id=" + getId() +
        ", state='" + getState() + "'" +
        ", dt='" + getDt() + "'" +
        ", value='" + getValue() + "'" +
        ", writtenBy='" + getWrittenBy() + "'" +
        ", writtenDt='" + getWrittenDt() + "'" +
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

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getWrittenBy() {
    return writtenBy;
  }

  public void setWrittenBy(String writtenBy) {
    this.writtenBy = writtenBy;
  }

  public ZonedDateTime getWrittenDt() {
    return writtenDt;
  }

  public void setWrittenDt(ZonedDateTime writtenDt) {
    this.writtenDt = writtenDt;
  }
}
