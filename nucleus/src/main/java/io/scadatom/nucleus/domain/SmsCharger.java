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

/** A SmsCharger. */
@Entity
@Table(name = "sms_charger")
public class SmsCharger implements Serializable {

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
  @Column(name = "resp_delay", nullable = false)
  private Integer respDelay;
  @OneToOne
  @JoinColumn(unique = true)
  private Electron electron;
  @OneToMany(mappedBy = "smsCharger", orphanRemoval = true)
  private Set<SmsDevice> smsDevices = new HashSet<>();

  public SmsCharger enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public SmsCharger port(String port) {
    this.port = port;
    return this;
  }

  public SmsCharger baud(Integer baud) {
    this.baud = baud;
    return this;
  }

  public SmsCharger databit(Integer databit) {
    this.databit = databit;
    return this;
  }

  public SmsCharger parity(Parity parity) {
    this.parity = parity;
    return this;
  }

  public SmsCharger stopbit(Stopbit stopbit) {
    this.stopbit = stopbit;
    return this;
  }

  public SmsCharger respDelay(Integer respDelay) {
    this.respDelay = respDelay;
    return this;
  }

  public Electron getElectron() {
    return electron;
  }

  public void setElectron(Electron electron) {
    this.electron = electron;
  }

  public SmsCharger electron(Electron electron) {
    this.electron = electron;
    return this;
  }

  public Set<SmsDevice> getSmsDevices() {
    return smsDevices;
  }

  public void setSmsDevices(Set<SmsDevice> smsDevices) {
    this.smsDevices = smsDevices;
    if (this.smsDevices != null) {
      this.smsDevices.forEach(smsDevice -> smsDevice.setSmsCharger(this));
    }
  }

  public SmsCharger smsDevices(Set<SmsDevice> smsDevices) {
    setSmsDevices(smsDevices);
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
    SmsCharger smsCharger = (SmsCharger) o;
    if (smsCharger.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), smsCharger.getId());
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("SmsCharger{");
    sb.append("id=").append(id);
    sb.append(", enabled=").append(enabled);
    sb.append(", port='").append(port).append('\'');
    sb.append(", baud=").append(baud);
    sb.append(", databit=").append(databit);
    sb.append(", parity=").append(parity);
    sb.append(", stopbit=").append(stopbit);
    sb.append(", respDelay=").append(respDelay);
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
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  public void setStopbit(Stopbit stopbit) {
    this.stopbit = stopbit;
  }

  public Integer getRespDelay() {
    return respDelay;
  }

  public void setRespDelay(Integer respDelay) {
    this.respDelay = respDelay;
  }
}
