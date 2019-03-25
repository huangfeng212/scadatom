package io.scadatom.neutron;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the SmmBond entity.
 */
public class SmmBondDTO implements Serializable {

  private Long id;

  @NotNull
  private Boolean enabled;

  @NotNull
  private RegType regType;

  @NotNull
  @Size(min = 1, max = 32)
  private String reg;

  private ValueType valueType;

  private String exprIn;

  private String exprOut;

  private EntityRef particle;

  private EntityRef smmDevice;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean isEnabled() {
    return enabled;
  }

  public RegType getRegType() {
    return regType;
  }

  public void setRegType(RegType regType) {
    this.regType = regType;
  }

  public String getReg() {
    return reg;
  }

  public void setReg(String reg) {
    this.reg = reg;
  }

  public ValueType getValueType() {
    return valueType;
  }

  public void setValueType(ValueType valueType) {
    this.valueType = valueType;
  }

  public String getExprIn() {
    return exprIn;
  }

  public void setExprIn(String exprIn) {
    this.exprIn = exprIn;
  }

  public String getExprOut() {
    return exprOut;
  }

  public void setExprOut(String exprOut) {
    this.exprOut = exprOut;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public EntityRef getParticle() {
    return particle;
  }

  public void setParticle(EntityRef particle) {
    this.particle = particle;
  }

  public EntityRef getSmmDevice() {
    return smmDevice;
  }

  public void setSmmDevice(EntityRef smmDevice) {
    this.smmDevice = smmDevice;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SmmBondDTO that = (SmmBondDTO) o;
    return Objects.equals(id, that.id) && Objects.equals(smmDevice, that.smmDevice);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("SmmBondDTO{");
    sb.append("id=").append(id);
    sb.append(", enabled=").append(enabled);
    sb.append(", regType=").append(regType);
    sb.append(", reg='").append(reg).append('\'');
    sb.append(", valueType=").append(valueType);
    sb.append(", exprIn='").append(exprIn).append('\'');
    sb.append(", exprOut='").append(exprOut).append('\'');
    sb.append(", particle=").append(particle);
    sb.append(", smmDevice=").append(smmDevice);
    sb.append('}');
    return sb.toString();
  }
}
