package io.scadatom.electron.domain;

import io.scadatom.neutron.OpState;
import io.scadatom.neutron.SmmPollStatus;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/** A SmmBondOp. */
@Entity
@Table(name = "smm_bond_op")
public class SmmBondOp implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "state")
  private OpState state;

  @Column(name = "dt")
  private ZonedDateTime dt;

  @Enumerated(EnumType.STRING)
  @Column(name = "poll_status")
  private SmmPollStatus pollStatus;

  @Column(name = "read_request")
  private String readRequest;

  @Column(name = "read_response")
  private String readResponse;

  @Column(name = "write_request")
  private String writeRequest;

  @Column(name = "write_response")
  private String writeResponse;

  @Column(name = "write_request_dt")
  private ZonedDateTime writeRequestDt;

  public SmmBondOp state(OpState state) {
    this.state = state;
    return this;
  }

  public SmmBondOp dt(ZonedDateTime dt) {
    this.dt = dt;
    return this;
  }

  public SmmBondOp pollStatus(SmmPollStatus pollStatus) {
    this.pollStatus = pollStatus;
    return this;
  }

  public SmmBondOp readRequest(String readRequest) {
    this.readRequest = readRequest;
    return this;
  }

  public SmmBondOp readResponse(String readResponse) {
    this.readResponse = readResponse;
    return this;
  }

  public SmmBondOp writeRequest(String writeRequest) {
    this.writeRequest = writeRequest;
    return this;
  }

  public SmmBondOp writeResponse(String writeResponse) {
    this.writeResponse = writeResponse;
    return this;
  }

  public SmmBondOp writeRequestDt(ZonedDateTime writeRequestDt) {
    this.writeRequestDt = writeRequestDt;
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
    SmmBondOp smmBondOp = (SmmBondOp) o;
    if (smmBondOp.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), smmBondOp.getId());
  }

  @Override
  public String toString() {
    return "SmmBondOp{"
        + "id="
        + getId()
        + ", state='"
        + getState()
        + "'"
        + ", dt='"
        + getDt()
        + "'"
        + ", pollStatus='"
        + getPollStatus()
        + "'"
        + ", readRequest='"
        + getReadRequest()
        + "'"
        + ", readResponse='"
        + getReadResponse()
        + "'"
        + ", writeRequest='"
        + getWriteRequest()
        + "'"
        + ", writeResponse='"
        + getWriteResponse()
        + "'"
        + ", writeRequestDt='"
        + getWriteRequestDt()
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

  public SmmPollStatus getPollStatus() {
    return pollStatus;
  }

  public void setPollStatus(SmmPollStatus pollStatus) {
    this.pollStatus = pollStatus;
  }

  public String getReadRequest() {
    return readRequest;
  }

  public void setReadRequest(String readRequest) {
    this.readRequest = readRequest;
  }

  public String getReadResponse() {
    return readResponse;
  }

  public void setReadResponse(String readResponse) {
    this.readResponse = readResponse;
  }

  public String getWriteRequest() {
    return writeRequest;
  }

  public void setWriteRequest(String writeRequest) {
    this.writeRequest = writeRequest;
  }

  public String getWriteResponse() {
    return writeResponse;
  }

  public void setWriteResponse(String writeResponse) {
    this.writeResponse = writeResponse;
  }

  public ZonedDateTime getWriteRequestDt() {
    return writeRequestDt;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  public void setWriteRequestDt(ZonedDateTime writeRequestDt) {
    this.writeRequestDt = writeRequestDt;
  }

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public SmmBondOp id(long id) {
    setId(id);
    return this;
  }
}
