package io.scadatom.neutron;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the SmmDevice entity.
 */
public class SmmDeviceDTO implements Serializable {

  private Long id;

  @NotNull
  private Boolean enabled;

  @NotNull
  @Size(min = 1, max = 32)
  private String name;

  @NotNull
  @Size(min = 1, max = 32)
  private String address;

  private EntityRef smmCharger;

  private Set<EntityRef> smmBonds = new HashSet<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean isEnabled() {
    return enabled;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public EntityRef getSmmCharger() {
    return smmCharger;
  }

  public void setSmmCharger(EntityRef smmCharger) {
    this.smmCharger = smmCharger;
  }

  public Set<EntityRef> getSmmBonds() {
    return smmBonds;
  }

  public void setSmmBonds(Set<EntityRef> smmBonds) {
    this.smmBonds = smmBonds;
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
    SmmDeviceDTO that = (SmmDeviceDTO) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("SmmDeviceDTO{");
    sb.append("id=").append(id);
    sb.append(", enabled=").append(enabled);
    sb.append(", name='").append(name).append('\'');
    sb.append(", address='").append(address).append('\'');
    sb.append(", smmCharger=").append(smmCharger);
    sb.append(", smmBonds=").append(smmBonds);
    sb.append('}');
    return sb.toString();
  }
}
