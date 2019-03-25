package io.scadatom.electron.domain;



import io.scadatom.neutron.OpState;
import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A SmsChargerOp.
 */
@Entity
@Table(name = "sms_charger_op")
public class SmsChargerOp implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private OpState state;

    @Column(name = "dt")
    private ZonedDateTime dt;

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

    public SmsChargerOp state(OpState state) {
        this.state = state;
        return this;
    }

    public void setState(OpState state) {
        this.state = state;
    }

    public ZonedDateTime getDt() {
        return dt;
    }

    public SmsChargerOp dt(ZonedDateTime dt) {
        this.dt = dt;
        return this;
    }

    public void setDt(ZonedDateTime dt) {
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
        SmsChargerOp smsChargerOp = (SmsChargerOp) o;
        if (smsChargerOp.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smsChargerOp.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SmsChargerOp{" +
            "id=" + getId() +
            ", state='" + getState() + "'" +
            ", dt='" + getDt() + "'" +
            "}";
    }

    public SmsChargerOp id(long id) {
        setId(id);
        return this;
    }
}
