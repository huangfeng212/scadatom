package io.scadatom.neutron;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the ParticleOp entity.
 */
public class ParticleOpDTO implements Serializable {

    private Long id;

    private OpState state;

    private Instant dt;

    private String value;

    private String writtenBy;

    private Instant writtenDt;


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

    public Instant getWrittenDt() {
        return writtenDt;
    }

    public void setWrittenDt(Instant writtenDt) {
        this.writtenDt = writtenDt;
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

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
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
}
