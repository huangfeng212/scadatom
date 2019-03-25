package io.scadatom.nucleus.domain;

import io.scadatom.neutron.Parity;
import io.scadatom.neutron.Stopbit;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/** A SmmCharger. */
@Entity
@Table(name = "smm_charger")
public class SmmCharger implements Serializable {

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
  @Column(name = "port", length = 32, nullable = false)
  private String port;

  @NotNull
  @Min(value = 0)
  @Column(name = "baud", nullable = false)
  private Integer baud;

  @NotNull
  @Min(value = 0)
  @Column(name = "databit", nullable = false)
  private Integer databit;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "parity", nullable = false)
  private Parity parity;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "stopbit", nullable = false)
  private Stopbit stopbit;

  @NotNull
  @Min(value = 0)
  @Column(name = "timeout", nullable = false)
  private Integer timeout;

  @NotNull
  @Min(value = 0)
  @Column(name = "retry", nullable = false)
  private Integer retry;

  @NotNull
  @Min(value = 0)
  @Column(name = "trans_delay", nullable = false)
  private Integer transDelay;

  @NotNull
  @Min(value = 0)
  @Column(name = "batch_delay", nullable = false)
  private Integer batchDelay;

  @OneToOne
  @JoinColumn(unique = true)
  private Electron electron;
  @OneToMany(mappedBy = "smmCharger", orphanRemoval = true)
  private Set<SmmDevice> smmDevices = new HashSet<>();

  public SmmCharger enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public SmmCharger port(String port) {
    this.port = port;
    return this;
  }

  public SmmCharger baud(Integer baud) {
    this.baud = baud;
    return this;
  }

  public SmmCharger databit(Integer databit) {
    this.databit = databit;
    return this;
  }

  public SmmCharger parity(Parity parity) {
    this.parity = parity;
    return this;
  }

  public SmmCharger stopbit(Stopbit stopbit) {
    this.stopbit = stopbit;
    return this;
  }

  public SmmCharger timeout(Integer timeout) {
    this.timeout = timeout;
    return this;
  }

  public SmmCharger retry(Integer retry) {
    this.retry = retry;
    return this;
  }

  public SmmCharger transDelay(Integer transDelay) {
    this.transDelay = transDelay;
    return this;
  }

  public SmmCharger batchDelay(Integer batchDelay) {
    this.batchDelay = batchDelay;
    return this;
  }

  public Electron getElectron() {
    return electron;
  }

  public void setElectron(Electron electron) {
    this.electron = electron;
  }

  public SmmCharger electron(Electron electron) {
    this.electron = electron;
    return this;
  }

  public Set<SmmDevice> getSmmDevices() {
    return smmDevices;
  }

  public void setSmmDevices(Set<SmmDevice> smmDevices) {
    this.smmDevices = smmDevices;
    if (this.smmDevices != null) {
      this.smmDevices.forEach(smmDevice -> smmDevice.setSmmCharger(this));
    }
  }

  public SmmCharger smmDevices(Set<SmmDevice> smmDevices) {
    setSmmDevices(smmDevices);
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
    SmmCharger smmCharger = (SmmCharger) o;
    if (smmCharger.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), smmCharger.getId());
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("SmmCharger{");
    sb.append("id=").append(id);
    sb.append(", enabled=").append(enabled);
    sb.append(", port='").append(port).append('\'');
    sb.append(", baud=").append(baud);
    sb.append(", databit=").append(databit);
    sb.append(", parity=").append(parity);
    sb.append(", stopbit=").append(stopbit);
    sb.append(", timeout=").append(timeout);
    sb.append(", retry=").append(retry);
    sb.append(", transDelay=").append(transDelay);
    sb.append(", batchDelay=").append(batchDelay);
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

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public Integer getBaud() {
    return baud;
  }

  public void setBaud(Integer baud) {
    this.baud = baud;
  }

  public Integer getDatabit() {
    return databit;
  }

  public void setDatabit(Integer databit) {
    this.databit = databit;
  }

  public Parity getParity() {
    return parity;
  }

  public void setParity(Parity parity) {
    this.parity = parity;
  }

  public Stopbit getStopbit() {
    return stopbit;
  }

  public void setStopbit(Stopbit stopbit) {
    this.stopbit = stopbit;
  }

  public Integer getTimeout() {
    return timeout;
  }

  public void setTimeout(Integer timeout) {
    this.timeout = timeout;
  }

  public Integer getRetry() {
    return retry;
  }

  public void setRetry(Integer retry) {
    this.retry = retry;
  }

  public Integer getTransDelay() {
    return transDelay;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  public void setTransDelay(Integer transDelay) {
    this.transDelay = transDelay;
  }

  public Integer getBatchDelay() {
    return batchDelay;
  }

  public void setBatchDelay(Integer batchDelay) {
    this.batchDelay = batchDelay;
  }
}
