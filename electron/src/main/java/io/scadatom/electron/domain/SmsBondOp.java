package io.scadatom.electron.domain;



import io.scadatom.neutron.OpState;
import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A SmsBondOp.
 */
@Entity
@Table(name = "sms_bond_op")
public class SmsBondOp implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private OpState state;

    @Column(name = "dt")
    private Instant dt;

    @Column(name = "bytes")
    private String bytes;

    @Column(name = "written_dt")
    private Instant writtenDt;

    @Column(name = "written_bytes")
    private String writtenBytes;

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

    public SmsBondOp state(OpState state) {
        this.state = state;
        return this;
    }

    public void setState(OpState state) {
        this.state = state;
    }

    public Instant getDt() {
        return dt;
    }

    public SmsBondOp dt(Instant dt) {
        this.dt = dt;
        return this;
    }

    public void setDt(Instant dt) {
        this.dt = dt;
    }

    public String getBytes() {
        return bytes;
    }

    public SmsBondOp bytes(String bytes) {
        this.bytes = bytes;
        return this;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }

    public Instant getWrittenDt() {
        return writtenDt;
    }

    public SmsBondOp writtenDt(Instant writtenDt) {
        this.writtenDt = writtenDt;
        return this;
    }

    public void setWrittenDt(Instant writtenDt) {
        this.writtenDt = writtenDt;
    }

    public String getWrittenBytes() {
        return writtenBytes;
    }

    public SmsBondOp writtenBytes(String writtenBytes) {
        this.writtenBytes = writtenBytes;
        return this;
    }

    public void setWrittenBytes(String writtenBytes) {
        this.writtenBytes = writtenBytes;
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
        SmsBondOp smsBondOp = (SmsBondOp) o;
        if (smsBondOp.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smsBondOp.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmsBondOp{" +
            "id=" + getId() +
            ", state='" + getState() + "'" +
            ", dt='" + getDt() + "'" +
            ", bytes='" + getBytes() + "'" +
            ", writtenDt='" + getWrittenDt() + "'" +
            ", writtenBytes='" + getWrittenBytes() + "'" +
            "}";
    }

    public SmsBondOp id(long id) {
        setId(id);
        return this;
    }
}
