package io.scadatom.nucleus.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/** A SmsDevice. */
@Entity
@Table(name = "sms_device")
public class SmsDevice implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  private Long id;

  @NotNull
  @Column(name = "enabled", nullable = false)
  private Boolean enabled;

  @NotBlank
  @Size(min = 1, max = 32)
  @Column(name = "name", length = 32, nullable = false)
  private String name;

  @NotNull
  @Size(min = 1, max = 32)
  @Column(name = "address", length = 32, nullable = false)
  private String address;

  @OneToMany(mappedBy = "smsDevice", orphanRemoval = true)
  private Set<SmsBond> smsBonds = new HashSet<>();

  @ManyToOne(optional = false)
  @NotNull
  private SmsCharger smsCharger;

  public SmsDevice id(Long id) {
    this.id = id;
    return this;
  }

  public SmsDevice enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public SmsDevice name(String name) {
    this.name = name;
    return this;
  }

  public SmsDevice address(String address) {
    this.address = address;
    return this;
  }

  public Set<SmsBond> getSmsBonds() {
    return smsBonds;
  }

  public void setSmsBonds(Set<SmsBond> smsBonds) {
    this.smsBonds = smsBonds;
    if (this.smsBonds != null) {
      this.smsBonds.forEach(smsBond -> smsBond.setSmsDevice(this));
    }
  }

  public SmsDevice smsBonds(Set<SmsBond> smsBonds) {
    setSmsBonds(smsBonds);
    return this;
  }

  public SmsCharger getSmsCharger() {
    return smsCharger;
  }

  public void setSmsCharger(SmsCharger smsCharger) {
    this.smsCharger = smsCharger;
  }

  public SmsDevice smsCharger(SmsCharger smsCharger) {
    this.smsCharger = smsCharger;
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
    SmsDevice smsDevice = (SmsDevice) o;
    if (smsDevice.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), smsDevice.getId());
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("SmsDevice{");
    sb.append("id=").append(id);
    sb.append(", enabled=").append(enabled);
    sb.append(", name='").append(name).append('\'');
    sb.append(", address='").append(address).append('\'');
    sb.append('}');
    return sb.toString();
  }

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

  public String getName() {
    return name;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
