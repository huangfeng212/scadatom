package io.scadatom.neutron;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the SmmBondOp entity.
 */
public class SmmBondOpDTO implements Serializable {

    private Long id;

    private OpState state;

    private Instant dt;

    private SmmPollStatus pollStatus;

    private String readRequest;

    private String readResponse;

    private String writeRequest;

    private String writeResponse;

    private Instant writeRequestDt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OpState getState() {
        return state;
    }

    public void setState(OpState state) {
        this.state = state;
    }

    public Instant getDt() {
        return dt;
    }

    public void setDt(Instant dt) {
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

    public Instant getWriteRequestDt() {
        return writeRequestDt;
    }

    public void setWriteRequestDt(Instant writeRequestDt) {
        this.writeRequestDt = writeRequestDt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SmmBondOpDTO smmBondOpDTO = (SmmBondOpDTO) o;
        if (smmBondOpDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smmBondOpDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmmBondOpDTO{" +
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
}
