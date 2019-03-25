package io.scadatom.neutron;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the SmsDevice entity.
 */
public class SmsDeviceDTO implements Serializable {

  private Long id;

  @NotNull
  private Boolean enabled;

  @NotNull
  @Size(min = 1, max = 32)
  private String name;

  @NotNull
  @Size(min = 1, max = 32)
  private String address;

  private EntityRef smsCharger;

  private Set<EntityRef> smsBonds = new HashSet<>();

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

  public EntityRef getSmsCharger() {
    return smsCharger;
  }

  public void setSmsCharger(EntityRef smsCharger) {
    this.smsCharger = smsCharger;
  }

  public Set<EntityRef> getSmsBonds() {
    return smsBonds;
  }

  public void setSmsBonds(Set<EntityRef> smsBonds) {
    this.smsBonds = smsBonds;
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
    SmsDeviceDTO that = (SmsDeviceDTO) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("SmsDeviceDTO{");
    sb.append("id=").append(id);
    sb.append(", enabled=").append(enabled);
    sb.append(", name='").append(name).append('\'');
    sb.append(", address='").append(address).append('\'');
    sb.append(", smsCharger=").append(smsCharger);
    sb.append(", smsBonds=").append(smsBonds);
    sb.append('}');
    return sb.toString();
  }
}
