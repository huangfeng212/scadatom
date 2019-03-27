package io.scadatom.electron.domain;



import io.scadatom.neutron.OpState;
import io.scadatom.neutron.SmmPollStatus;
import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A SmmBondOp.
 */
@Entity
@Table(name = "smm_bond_op")
public class SmmBondOp implements Serializable {

    private static final long serialVersionUID = 1L;
    
  @Id private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private OpState state;

    @Column(name = "dt")
    private Instant dt;

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
    private Instant writeRequestDt;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OpState getState() {
        return state;
    }

    public SmmBondOp state(OpState state) {
        this.state = state;
        return this;
    }

    public void setState(OpState state) {
        this.state = state;
    }

    public Instant getDt() {
        return dt;
    }

    public SmmBondOp dt(Instant dt) {
        this.dt = dt;
        return this;
    }

    public void setDt(Instant dt) {
        this.dt = dt;
    }

    public SmmPollStatus getPollStatus() {
        return pollStatus;
    }

    public SmmBondOp pollStatus(SmmPollStatus pollStatus) {
        this.pollStatus = pollStatus;
        return this;
    }

    public void setPollStatus(SmmPollStatus pollStatus) {
        this.pollStatus = pollStatus;
    }

    public String getReadRequest() {
        return readRequest;
    }

    public SmmBondOp readRequest(String readRequest) {
        this.readRequest = readRequest;
        return this;
    }

    public void setReadRequest(String readRequest) {
        this.readRequest = readRequest;
    }

    public String getReadResponse() {
        return readResponse;
    }

    public SmmBondOp readResponse(String readResponse) {
        this.readResponse = readResponse;
        return this;
    }

    public void setReadResponse(String readResponse) {
        this.readResponse = readResponse;
    }

    public String getWriteRequest() {
        return writeRequest;
    }

    public SmmBondOp writeRequest(String writeRequest) {
        this.writeRequest = writeRequest;
        return this;
    }

    public void setWriteRequest(String writeRequest) {
        this.writeRequest = writeRequest;
    }

    public String getWriteResponse() {
        return writeResponse;
    }

    public SmmBondOp writeResponse(String writeResponse) {
        this.writeResponse = writeResponse;
        return this;
    }

    public void setWriteResponse(String writeResponse) {
        this.writeResponse = writeResponse;
    }

    public Instant getWriteRequestDt() {
        return writeRequestDt;
    }

    public SmmBondOp writeRequestDt(Instant writeRequestDt) {
        this.writeRequestDt = writeRequestDt;
        return this;
    }

    public void setWriteRequestDt(Instant writeRequestDt) {
        this.writeRequestDt = writeRequestDt;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmmBondOp{" +
            "id=" + getId() +
            ", state='" + getState() + "'" +
            ", dt='" + getDt() + "'" +
            ", pollStatus='" + getPollStatus() + "'" +
            ", readRequest='" + getReadRequest() + "'" +
            ", readResponse='" + getReadResponse() + "'" +
            ", writeRequest='" + getWriteRequest() + "'" +
            ", writeResponse='" + getWriteResponse() + "'" +
            ", writeRequestDt='" + getWriteRequestDt() + "'" +
            "}";
    }
    public SmmBondOp id(long id) {
        setId(id);
        return this;
    }
}
