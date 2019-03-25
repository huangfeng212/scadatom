package io.scadatom.neutron;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the SmsCharger entity.
 */
public class SmsChargerDTO implements Serializable {

  private Long id;

  @NotNull
  private Boolean enabled;

  @NotNull
  @Size(min = 1, max = 32)
  private String port;

  @NotNull
  @Min(value = 0)
  private Integer baud;

  @NotNull
  @Min(value = 0)
  private Integer databit;

  @NotNull
  private Parity parity;

  @NotNull
  private Stopbit stopbit;

  @NotNull
  @Min(value = 0)
  private Integer respDelay;

  private EntityRef electron;

  private Set<EntityRef> smsDevices = new HashSet<>();

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

  public Integer getRespDelay() {
    return respDelay;
  }

  public void setRespDelay(Integer respDelay) {
    this.respDelay = respDelay;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public EntityRef getElectron() {
    return electron;
  }

  public void setElectron(EntityRef electron) {
    this.electron = electron;
  }

  public Set<EntityRef> getSmsDevices() {
    return smsDevices;
  }

  public void setSmsDevices(Set<EntityRef> smsDevices) {
    this.smsDevices = smsDevices;
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
    SmsChargerDTO that = (SmsChargerDTO) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("SmsChargerDTO{");
    sb.append("id=").append(id);
    sb.append(", enabled=").append(enabled);
    sb.append(", port='").append(port).append('\'');
    sb.append(", baud=").append(baud);
    sb.append(", databit=").append(databit);
    sb.append(", parity=").append(parity);
    sb.append(", stopbit=").append(stopbit);
    sb.append(", respDelay=").append(respDelay);
    sb.append(", electron=").append(electron);
    sb.append(", smsDevices=").append(smsDevices);
    sb.append('}');
    return sb.toString();
  }
}
