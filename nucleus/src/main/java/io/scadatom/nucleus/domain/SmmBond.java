package io.scadatom.nucleus.domain;

import io.scadatom.neutron.RegType;
import io.scadatom.neutron.ValueType;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/** A SmmBond. */
@Entity
@Table(name = "smm_bond")
public class SmmBond implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  private Long id;

  @NotNull
  @Column(name = "enabled", nullable = false)
  private Boolean enabled;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "reg_type", nullable = false)
  private RegType regType;

  @NotBlank
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
  private SmmDevice smmDevice;

  public Boolean isEnabled() {
    return enabled;
  }

  public SmmBond enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public RegType getRegType() {
    return regType;
  }

  public void setRegType(RegType regType) {
    this.regType = regType;
  }

  public SmmBond regType(RegType regType) {
    this.regType = regType;
    return this;
  }

  public String getReg() {
    return reg;
  }

  public void setReg(String reg) {
    this.reg = reg;
  }

  public SmmBond reg(String reg) {
    this.reg = reg;
    return this;
  }

  public ValueType getValueType() {
    return valueType;
  }

  public void setValueType(ValueType valueType) {
    this.valueType = valueType;
  }

  public SmmBond valueType(ValueType valueType) {
    this.valueType = valueType;
    return this;
  }

  public String getExprIn() {
    return exprIn;
  }

  public void setExprIn(String exprIn) {
    this.exprIn = exprIn;
  }

  public SmmBond exprIn(String exprIn) {
    this.exprIn = exprIn;
    return this;
  }

  public String getExprOut() {
    return exprOut;
  }

  public void setExprOut(String exprOut) {
    this.exprOut = exprOut;
  }

  public SmmBond exprOut(String exprOut) {
    this.exprOut = exprOut;
    return this;
  }

  public Particle getParticle() {
    return particle;
  }

  public void setParticle(Particle particle) {
    this.particle = particle;
  }

  public SmmBond particle(Particle particle) {
    this.particle = particle;
    return this;
  }

  public SmmDevice getSmmDevice() {
    return smmDevice;
  }

  public void setSmmDevice(SmmDevice smmDevice) {
    this.smmDevice = smmDevice;
  }

  public SmmBond smmDevice(SmmDevice smmDevice) {
    this.smmDevice = smmDevice;
    return this;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SmmBond smmBond = (SmmBond) o;
    if (smmBond.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), smmBond.getId());
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("SmmBond{");
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
}
