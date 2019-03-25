package io.scadatom.nucleus.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/** A Particle. */
@Entity
@Table(name = "particle")
public class Particle implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  private Long id;

  @NotBlank
  @Size(min = 1, max = 32)
  @Column(name = "name", length = 32, nullable = false)
  private String name;

  @Column(name = "decimal_format")
  private String decimalFormat;

  @Column(name = "init_value")
  private String initValue;

  @ManyToOne(optional = false)
  @NotNull
  private Electron electron;

  @OneToOne(mappedBy = "particle", orphanRemoval = true)
  private SmmBond smmBond;

  @OneToOne(mappedBy = "particle", orphanRemoval = true)
  private SmsBond smsBond;

  public Particle name(String name) {
    this.name = name;
    return this;
  }

  public Particle decimalFormat(String decimalFormat) {
    this.decimalFormat = decimalFormat;
    return this;
  }

  public Particle initValue(String initValue) {
    this.initValue = initValue;
    return this;
  }

  public Electron getElectron() {
    return electron;
  }

  public void setElectron(Electron electron) {
    this.electron = electron;
  }

  public Particle electron(Electron electron) {
    this.electron = electron;
    return this;
  }

  public SmmBond getSmmBond() {
    return smmBond;
  }

  public void setSmmBond(SmmBond smmBond) {
    this.smmBond = smmBond;
    if (this.smmBond != null) {
      this.smmBond.setParticle(this);
    }
  }

  public SmsBond getSmsBond() {
    return smsBond;
  }

  public void setSmsBond(SmsBond smsBond) {
    this.smsBond = smsBond;
    if (this.smsBond != null) {
      this.smsBond.setParticle(this);
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
    Particle particle = (Particle) o;
    if (particle.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), particle.getId());
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Particle{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", decimalFormat='").append(decimalFormat).append('\'');
    sb.append(", initValue='").append(initValue).append('\'');
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

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
}
