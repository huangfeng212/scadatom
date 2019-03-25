package io.scadatom.nucleus.domain;


import io.scadatom.neutron.RegType;
import io.scadatom.neutron.ValueType;
import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A SmsBond.
 */
@Entity
@Table(name = "sms_bond")
public class SmsBond implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SmsBond{");
        sb.append("id=").append(id);
        sb.append(", enabled=").append(enabled);
        sb.append(", regType=").append(regType);
        sb.append(", reg='").append(reg).append('\'');
        sb.append(", valueType=").append(valueType);
        sb.append(", exprIn='").append(exprIn).append('\'');
        sb.append(", exprOut='").append(exprOut).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "reg_type", nullable = false)
    private RegType regType;

    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "reg", length = 32, nullable = false)
    private String reg;

    @Enumerated(EnumType.STRING)
    @Column(name = "value_type")
    private ValueType valueType;

    @Column(name = "expr_in")
    private String exprIn;

    @Column(name = "expr_out")
    private String exprOut;

    @OneToOne
    @JoinColumn(unique = true)
    private Particle particle;

    @ManyToOne(optional = false)
    @NotNull
    private SmsDevice smsDevice;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public SmsBond enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public RegType getRegType() {
        return regType;
    }

    public SmsBond regType(RegType regType) {
        this.regType = regType;
        return this;
    }

    public void setRegType(RegType regType) {
        this.regType = regType;
    }

    public String getReg() {
        return reg;
    }

    public SmsBond reg(String reg) {
        this.reg = reg;
        return this;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public SmsBond valueType(ValueType valueType) {
        this.valueType = valueType;
        return this;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public String getExprIn() {
        return exprIn;
    }

    public SmsBond exprIn(String exprIn) {
        this.exprIn = exprIn;
        return this;
    }

    public void setExprIn(String exprIn) {
        this.exprIn = exprIn;
    }

    public String getExprOut() {
        return exprOut;
    }

    public SmsBond exprOut(String exprOut) {
        this.exprOut = exprOut;
        return this;
    }

    public void setExprOut(String exprOut) {
        this.exprOut = exprOut;
    }

    public Particle getParticle() {
        return particle;
    }

    public SmsBond particle(Particle particle) {
        this.particle = particle;
        return this;
    }

    public void setParticle(Particle particle) {
        this.particle = particle;
    }

    public SmsDevice getSmsDevice() {
        return smsDevice;
    }

    public SmsBond smsDevice(SmsDevice smsDevice) {
        this.smsDevice = smsDevice;
        return this;
    }

    public void setSmsDevice(SmsDevice smsDevice) {
        this.smsDevice = smsDevice;
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
        SmsBond smsBond = (SmsBond) o;
        if (smsBond.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), smsBond.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
