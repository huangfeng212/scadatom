package io.scadatom.electron.domain;



import io.scadatom.neutron.OpState;
import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A ElectronOp.
 */
@Entity
@Table(name = "electron_op")
public class ElectronOp implements Serializable {

    private static final long serialVersionUID = 1L;
    
  @Id private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private OpState state;

    @Column(name = "dt")
    private Instant dt;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }
    public ElectronOp id(Long id) {
        setId(id);
        return this;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public OpState getState() {
        return state;
    }

    public ElectronOp state(OpState state) {
        this.state = state;
        return this;
    }

    public void setState(OpState state) {
        this.state = state;
    }

    public Instant getDt() {
        return dt;
    }

    public ElectronOp dt(Instant dt) {
        this.dt = dt;
        return this;
    }

    public void setDt(Instant dt) {
        this.dt = dt;
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
        ElectronOp electronOp = (ElectronOp) o;
        if (electronOp.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), electronOp.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ElectronOp{" +
            "id=" + getId() +
            ", state='" + getState() + "'" +
            ", dt='" + getDt() + "'" +
            "}";
    }
}
