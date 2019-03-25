package io.scadatom.neutron;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the Particle entity.
 */
public class ParticleDTO implements Serializable {

  private Long id;

  @NotNull
  @Size(min = 1, max = 32)
  private String name;

  private String decimalFormat;

  private String initValue;

  private EntityRef electron;
  private EntityRef smmBond;
  private EntityRef smsBond;

  public EntityRef getElectron() {
    return electron;
  }

  public void setElectron(EntityRef electron) {
    this.electron = electron;
  }

  public EntityRef getSmmBond() {
    return smmBond;
  }

  public void setSmmBond(EntityRef smmBond) {
    this.smmBond = smmBond;
  }

  public EntityRef getSmsBond() {
    return smsBond;
  }

  public void setSmsBond(EntityRef smsBond) {
    this.smsBond = smsBond;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDecimalFormat() {
    return decimalFormat;
  }

  public void setDecimalFormat(String decimalFormat) {
    this.decimalFormat = decimalFormat;
  }

  public String getInitValue() {
    return initValue;
  }

  public void setInitValue(String initValue) {
    this.initValue = initValue;
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
    ParticleDTO that = (ParticleDTO) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ParticleDTO{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", decimalFormat='").append(decimalFormat).append('\'');
    sb.append(", initValue='").append(initValue).append('\'');
    sb.append(", electron=").append(electron);
    sb.append(", smmBond=").append(smmBond);
    sb.append(", smsBond=").append(smsBond);
    sb.append('}');
    return sb.toString();
  }
}
