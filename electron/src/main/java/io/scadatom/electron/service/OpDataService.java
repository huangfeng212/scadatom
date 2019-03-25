package io.scadatom.electron.service;

import io.scadatom.electron.domain.ElectronOp;
import io.scadatom.electron.domain.ParticleOp;
import io.scadatom.electron.domain.SmmBondOp;
import io.scadatom.electron.domain.SmmChargerOp;
import io.scadatom.electron.domain.SmmDeviceOp;
import io.scadatom.electron.domain.SmsBondOp;
import io.scadatom.electron.domain.SmsChargerOp;
import io.scadatom.electron.domain.SmsDeviceOp;
import io.scadatom.electron.repository.ElectronOpRepository;
import io.scadatom.electron.repository.ParticleOpRepository;
import io.scadatom.electron.repository.SmmBondOpRepository;
import io.scadatom.electron.repository.SmmChargerOpRepository;
import io.scadatom.electron.repository.SmmDeviceOpRepository;
import io.scadatom.electron.repository.SmsBondOpRepository;
import io.scadatom.electron.repository.SmsChargerOpRepository;
import io.scadatom.electron.repository.SmsDeviceOpRepository;
import io.scadatom.neutron.OpState;
import java.time.ZonedDateTime;
import java.util.function.Consumer;
import org.springframework.stereotype.Service;

@Service
public class OpDataService {
  private final ElectronOpRepository electronOpRepository;
  private final ParticleOpRepository particleOpRepository;
  private final SmmChargerOpRepository smmChargerOpRepository;
  private final SmmDeviceOpRepository smmDeviceOpRepository;
  private final SmmBondOpRepository smmBondOpRepository;
  private final SmsChargerOpRepository smsChargerOpRepository;
  private final SmsDeviceOpRepository smsDeviceOpRepository;
  private final SmsBondOpRepository smsBondOpRepository;

  public OpDataService(
      ElectronOpRepository electronOpRepository,
      ParticleOpRepository particleOpRepository,
      SmmChargerOpRepository smmChargerOpRepository,
      SmmDeviceOpRepository smmDeviceOpRepository,
      SmmBondOpRepository smmBondOpRepository,
      SmsChargerOpRepository smsChargerOpRepository,
      SmsDeviceOpRepository smsDeviceOpRepository,
      SmsBondOpRepository smsBondOpRepository) {
    this.electronOpRepository = electronOpRepository;
    this.particleOpRepository = particleOpRepository;
    this.smmChargerOpRepository = smmChargerOpRepository;
    this.smmDeviceOpRepository = smmDeviceOpRepository;
    this.smmBondOpRepository = smmBondOpRepository;
    this.smsChargerOpRepository = smsChargerOpRepository;
    this.smsDeviceOpRepository = smsDeviceOpRepository;
    this.smsBondOpRepository = smsBondOpRepository;
  }

  public void updateElectronOp(long id, Consumer<ElectronOp> opConsumer) {
    ElectronOp electronOp = electronOpRepository.findById(id).orElse(new ElectronOp().id(id));
    electronOp.setDt(ZonedDateTime.now());
    opConsumer.accept(electronOp);
    electronOpRepository.save(electronOp);
  }

  public ElectronOp getElectronOp(long id) {
    return electronOpRepository
        .findById(id)
        .orElse(new ElectronOp().id(id).state(OpState.Undefined));
  }

  public void updateParticleOp(long id, Consumer<ParticleOp> opConsumer) {
    ParticleOp particleOp = particleOpRepository.findById(id).orElse(new ParticleOp().id(id));
    particleOp.setDt(ZonedDateTime.now());
    opConsumer.accept(particleOp);
    particleOpRepository.save(particleOp);
  }

  public ParticleOp getParticleOp(long id) {
    return particleOpRepository
        .findById(id)
        .orElse(new ParticleOp().id(id).state(OpState.Undefined));
  }

  public void updateSmmChargerOp(long id, Consumer<SmmChargerOp> opConsumer) {
    SmmChargerOp smmChargerOp =
        smmChargerOpRepository.findById(id).orElse(new SmmChargerOp().id(id));
    smmChargerOp.setDt(ZonedDateTime.now());
    opConsumer.accept(smmChargerOp);
    smmChargerOpRepository.save(smmChargerOp);
  }

  public SmmChargerOp getSmmChargerOp(long id) {
    return smmChargerOpRepository
        .findById(id)
        .orElse(new SmmChargerOp().id(id).state(OpState.Undefined));
  }

  public void updateSmmDeviceOp(long id, Consumer<SmmDeviceOp> opConsumer) {
    SmmDeviceOp smmDeviceOp = smmDeviceOpRepository.findById(id).orElse(new SmmDeviceOp().id(id));
    smmDeviceOp.setDt(ZonedDateTime.now());
    opConsumer.accept(smmDeviceOp);
    smmDeviceOpRepository.save(smmDeviceOp);
  }

  public SmmDeviceOp getSmmDeviceOp(long id) {
    return smmDeviceOpRepository
        .findById(id)
        .orElse(new SmmDeviceOp().id(id).state(OpState.Undefined));
  }

  public void updateSmmBondOp(long id, Consumer<SmmBondOp> opConsumer) {
    SmmBondOp smmBondOp = smmBondOpRepository.findById(id).orElse(new SmmBondOp().id(id));
    smmBondOp.setDt(ZonedDateTime.now());
    opConsumer.accept(smmBondOp);
    smmBondOpRepository.save(smmBondOp);
  }

  public SmmBondOp getSmmBondOp(long id) {
    return smmBondOpRepository.findById(id).orElse(new SmmBondOp().id(id).state(OpState.Undefined));
  }

  public void updateSmsChargerOp(long id, Consumer<SmsChargerOp> opConsumer) {
    SmsChargerOp smsChargerOp =
        smsChargerOpRepository.findById(id).orElse(new SmsChargerOp().id(id));
    smsChargerOp.setDt(ZonedDateTime.now());
    opConsumer.accept(smsChargerOp);
    smsChargerOpRepository.save(smsChargerOp);
  }

  public SmsChargerOp getSmsChargerOp(long id) {
    return smsChargerOpRepository
        .findById(id)
        .orElse(new SmsChargerOp().id(id).state(OpState.Undefined));
  }

  public void updateSmsDeviceOp(long id, Consumer<SmsDeviceOp> opConsumer) {
    SmsDeviceOp smsDeviceOp = smsDeviceOpRepository.findById(id).orElse(new SmsDeviceOp().id(id));
    smsDeviceOp.setDt(ZonedDateTime.now());
    opConsumer.accept(smsDeviceOp);
    smsDeviceOpRepository.save(smsDeviceOp);
  }

  public SmsDeviceOp getSmsDeviceOp(long id) {
    return smsDeviceOpRepository
        .findById(id)
        .orElse(new SmsDeviceOp().id(id).state(OpState.Undefined));
  }

  public void updateSmsBondOp(long id, Consumer<SmsBondOp> opConsumer) {
    SmsBondOp smsBondOp = smsBondOpRepository.findById(id).orElse(new SmsBondOp().id(id));
    smsBondOp.setDt(ZonedDateTime.now());
    opConsumer.accept(smsBondOp);
    smsBondOpRepository.save(smsBondOp);
  }

  public SmsBondOp getSmsBondOp(long id) {
    return smsBondOpRepository.findById(id).orElse(new SmsBondOp().id(id).state(OpState.Undefined));
  }
}
