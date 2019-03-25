package io.scadatom.neutron;

import java.util.Set;

public class ElectronInitReq {

  private ElectronDTO electronDTO;
  private Set<ParticleDTO> particleDTOS;
  private SmmChargerDTO smmChargerDTO;
  private Set<SmmDeviceDTO> smmDeviceDTOS;
  private Set<SmmBondDTO> smmBondDTOS;
  private SmsChargerDTO smsChargerDTO;
  private Set<SmsDeviceDTO> smsDeviceDTOS;
  private Set<SmsBondDTO> smsBondDTOS;

  public ElectronDTO getElectronDTO() {
    return electronDTO;
  }

  public void setElectronDTO(ElectronDTO electronDTO) {
    this.electronDTO = electronDTO;
  }

  public Set<ParticleDTO> getParticleDTOS() {
    return particleDTOS;
  }

  public void setParticleDTOS(Set<ParticleDTO> particleDTOS) {
    this.particleDTOS = particleDTOS;
  }

  public SmmChargerDTO getSmmChargerDTO() {
    return smmChargerDTO;
  }

  public void setSmmChargerDTO(SmmChargerDTO smmChargerDTO) {
    this.smmChargerDTO = smmChargerDTO;
  }

  public Set<SmmDeviceDTO> getSmmDeviceDTOS() {
    return smmDeviceDTOS;
  }

  public void setSmmDeviceDTOS(Set<SmmDeviceDTO> smmDeviceDTOS) {
    this.smmDeviceDTOS = smmDeviceDTOS;
  }

  public Set<SmmBondDTO> getSmmBondDTOS() {
    return smmBondDTOS;
  }

  public void setSmmBondDTOS(Set<SmmBondDTO> smmBondDTOS) {
    this.smmBondDTOS = smmBondDTOS;
  }

  public SmsChargerDTO getSmsChargerDTO() {
    return smsChargerDTO;
  }

  public void setSmsChargerDTO(SmsChargerDTO smsChargerDTO) {
    this.smsChargerDTO = smsChargerDTO;
  }

  public Set<SmsDeviceDTO> getSmsDeviceDTOS() {
    return smsDeviceDTOS;
  }

  public void setSmsDeviceDTOS(Set<SmsDeviceDTO> smsDeviceDTOS) {
    this.smsDeviceDTOS = smsDeviceDTOS;
  }

  public Set<SmsBondDTO> getSmsBondDTOS() {
    return smsBondDTOS;
  }

  public void setSmsBondDTOS(Set<SmsBondDTO> smsBondDTOS) {
    this.smsBondDTOS = smsBondDTOS;
  }
}
