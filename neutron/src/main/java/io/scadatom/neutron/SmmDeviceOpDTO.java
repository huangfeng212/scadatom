package io.scadatom.neutron;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the SmmDeviceOp entity.
 */
public class SmmDeviceOpDTO implements Serializable {

    private Long id;

    private OpState state;

    private Instant dt;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SmmDeviceOpDTO smmDeviceOpDTO = (SmmDeviceOpDTO) o;
        if (smmDeviceOpDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smmDeviceOpDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmmDeviceOpDTO{" +
            "id=" + getId() +
            ", state='" + getState() + "'" +
            ", dt='" + getDt() + "'" +
            "}";
    }
}
