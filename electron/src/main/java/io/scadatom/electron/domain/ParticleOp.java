package io.scadatom.electron.domain;



import io.scadatom.neutron.OpState;
import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A ParticleOp.
 */
@Entity
@Table(name = "particle_op")
public class ParticleOp implements Serializable {

    private static final long serialVersionUID = 1L;
    
  @Id private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private OpState state;

    @Column(name = "dt")
    private Instant dt;

    @Column(name = "jhi_value")
    private String value;

    @Column(name = "written_by")
    private String writtenBy;

    @Column(name = "written_dt")
    private Instant writtenDt;

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

    public ParticleOp state(OpState state) {
        this.state = state;
        return this;
    }

    public void setState(OpState state) {
        this.state = state;
    }

    public Instant getDt() {
        return dt;
    }

    public ParticleOp dt(Instant dt) {
        this.dt = dt;
        return this;
    }

    public void setDt(Instant dt) {
        this.dt = dt;
    }

    public String getValue() {
        return value;
    }

    public ParticleOp value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getWrittenBy() {
        return writtenBy;
    }

    public ParticleOp writtenBy(String writtenBy) {
        this.writtenBy = writtenBy;
        return this;
    }

    public void setWrittenBy(String writtenBy) {
        this.writtenBy = writtenBy;
    }

    public Instant getWrittenDt() {
        return writtenDt;
    }

    public ParticleOp writtenDt(Instant writtenDt) {
        this.writtenDt = writtenDt;
        return this;
    }

    public void setWrittenDt(Instant writtenDt) {
        this.writtenDt = writtenDt;
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
        ParticleOp particleOp = (ParticleOp) o;
        if (particleOp.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), particleOp.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ParticleOp{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            ", state='" + getState() + "'" +
            ", dt='" + getDt() + "'" +
            ", writtenBy='" + getWrittenBy() + "'" +
            ", writtenDt='" + getWrittenDt() + "'" +
            "}";
    }
    public ParticleOp id(long id) {
        setId(id);
        return this;
    }
}
