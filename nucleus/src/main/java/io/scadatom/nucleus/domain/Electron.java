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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/** A Electron. */
@Entity
@Table(name = "electron")
public class Electron implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  private Long id;

  @NotBlank
  @Size(min = 1, max = 32)
  @Column(name = "name", length = 32, nullable = false)
  private String name;

  @OneToMany(mappedBy = "electron", orphanRemoval = true)
  private Set<Particle> particles = new HashSet<>();

  @OneToOne(mappedBy = "electron", orphanRemoval = true)
  private SmmCharger smmCharger;

  @OneToOne(mappedBy = "electron", orphanRemoval = true)
  private SmsCharger smsCharger;

  public Electron name(String name) {
    this.name = name;
    return this;
  }

  public Set<Particle> getParticles() {
    return particles;
  }

  public void setParticles(Set<Particle> particles) {
    this.particles = particles;
    if (this.particles != null) {
      this.particles.forEach(particle -> particle.setElectron(this));
    }
  }

  public Electron particles(Set<Particle> particles) {
    setParticles(particles);
    return this;
  }

  public SmmCharger getSmmCharger() {
    return smmCharger;
  }

  public void setSmmCharger(SmmCharger smmCharger) {
    this.smmCharger = smmCharger;
    if (this.smmCharger != null) {
      this.smmCharger.setElectron(this);
    }
  }

  public SmsCharger getSmsCharger() {
    return smsCharger;
  }

  public void setSmsCharger(SmsCharger smsCharger) {
    this.smsCharger = smsCharger;
    if (this.smmCharger != null) {
      this.smsCharger.setElectron(this);
    }
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
    Electron electron = (Electron) o;
    if (electron.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), electron.getId());
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Electron{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append('}');
    return sb.toString();
  }

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

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
