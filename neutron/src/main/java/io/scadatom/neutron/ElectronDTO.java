package io.scadatom.neutron;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/** A DTO for the Electron entity. */
public class ElectronDTO implements Serializable {

  private Long id;

  @NotNull
  @Size(min = 1, max = 32)
  private String name;

  @NotNull private Boolean enabled;

  private Set<EntityRef> particles = new HashSet<>();
  private EntityRef smmCharger;
  private EntityRef smsCharger;

  public Set<EntityRef> getParticles() {
    return particles;
  }

  public void setParticles(Set<EntityRef> particles) {
    this.particles = particles;
  }

  public EntityRef getSmmCharger() {
    return smmCharger;
  }

  public void setSmmCharger(EntityRef smmCharger) {
    this.smmCharger = smmCharger;
  }

  public EntityRef getSmsCharger() {
    return smsCharger;
  }

  public void setSmsCharger(EntityRef smsCharger) {
    this.smsCharger = smsCharger;
  }

  public Boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
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
    ElectronDTO that = (ElectronDTO) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ElectronDTO{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", particles=").append(particles);
    sb.append(", smmCharger=").append(smmCharger);
    sb.append(", smsCharger=").append(smsCharger);
    sb.append('}');
    return sb.toString();
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
}
