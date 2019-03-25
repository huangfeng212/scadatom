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

/** A SmmDevice. */
@Entity
@Table(name = "smm_device")
public class SmmDevice implements Serializable {

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

  @OneToMany(mappedBy = "smmDevice", orphanRemoval = true)
  private Set<SmmBond> smmBonds = new HashSet<>();

  @ManyToOne(optional = false)
  @NotNull
  private SmmCharger smmCharger;

  public SmmDevice id(Long id) {
    this.id = id;
    return this;
  }

  public SmmDevice enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public SmmDevice name(String name) {
    this.name = name;
    return this;
  }

  public SmmDevice address(String address) {
    this.address = address;
    return this;
  }

  public Set<SmmBond> getSmmBonds() {
    return smmBonds;
  }

  public void setSmmBonds(Set<SmmBond> smmBonds) {
    this.smmBonds = smmBonds;
    if (this.smmBonds != null) {
      this.smmBonds.forEach(smmBond -> smmBond.setSmmDevice(this));
    }
  }

  public SmmDevice smmBonds(Set<SmmBond> smmBonds) {
    setSmmBonds(smmBonds);
    return this;
  }

  public SmmCharger getSmmCharger() {
    return smmCharger;
  }

  public void setSmmCharger(SmmCharger smmCharger) {
    this.smmCharger = smmCharger;
  }

  public SmmDevice smmCharger(SmmCharger smmCharger) {
    this.smmCharger = smmCharger;
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
    SmmDevice smmDevice = (SmmDevice) o;
    if (smmDevice.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), smmDevice.getId());
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("SmmDevice{");
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
