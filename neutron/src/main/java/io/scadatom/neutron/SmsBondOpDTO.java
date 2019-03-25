package io.scadatom.neutron;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the SmsBondOp entity.
 */
public class SmsBondOpDTO implements Serializable {

  private Long id;

  private OpState state;

  private ZonedDateTime dt;

  private String bytes;

  private ZonedDateTime writtenDt;

  private String writtenBytes;

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

    SmsBondOpDTO smsBondOpDTO = (SmsBondOpDTO) o;
    if (smsBondOpDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), smsBondOpDTO.getId());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "SmsBondOpDTO{" +
        "id=" + getId() +
        ", state='" + getState() + "'" +
        ", dt='" + getDt() + "'" +
        ", bytes='" + getBytes() + "'" +
        ", writtenDt='" + getWrittenDt() + "'" +
        ", writtenBytes='" + getWrittenBytes() + "'" +
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

  public String getBytes() {
    return bytes;
  }

  public void setBytes(String bytes) {
    this.bytes = bytes;
  }

  public ZonedDateTime getWrittenDt() {
    return writtenDt;
  }

  public void setWrittenDt(ZonedDateTime writtenDt) {
    this.writtenDt = writtenDt;
  }

  public String getWrittenBytes() {
    return writtenBytes;
  }

  public void setWrittenBytes(String writtenBytes) {
    this.writtenBytes = writtenBytes;
  }
}
