package io.scadatom.neutron;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the SmsBondOp entity.
 */
public class SmsBondOpDTO implements Serializable {

    private Long id;

    private OpState state;

    private Instant dt;

    private String bytes;

    private Instant writtenDt;

    private String writtenBytes;


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

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }

    public Instant getWrittenDt() {
        return writtenDt;
    }

    public void setWrittenDt(Instant writtenDt) {
        this.writtenDt = writtenDt;
    }

    public String getWrittenBytes() {
        return writtenBytes;
    }

    public void setWrittenBytes(String writtenBytes) {
        this.writtenBytes = writtenBytes;
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

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
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
}
