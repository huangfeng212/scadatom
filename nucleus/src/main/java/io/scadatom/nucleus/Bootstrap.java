package io.scadatom.nucleus;

import io.scadatom.neutron.Parity;
import io.scadatom.neutron.RegType;
import io.scadatom.neutron.Stopbit;
import io.scadatom.neutron.ValueType;
import io.scadatom.nucleus.domain.Electron;
import io.scadatom.nucleus.domain.Particle;
import io.scadatom.nucleus.domain.SmmBond;
import io.scadatom.nucleus.domain.SmmCharger;
import io.scadatom.nucleus.domain.SmmDevice;
import io.scadatom.nucleus.domain.SmsBond;
import io.scadatom.nucleus.domain.SmsCharger;
import io.scadatom.nucleus.domain.SmsDevice;
import io.scadatom.nucleus.repository.ElectronRepository;
import io.scadatom.nucleus.repository.ParticleRepository;
import io.scadatom.nucleus.repository.SmmBondRepository;
import io.scadatom.nucleus.repository.SmmChargerRepository;
import io.scadatom.nucleus.repository.SmmDeviceRepository;
import io.scadatom.nucleus.repository.SmsBondRepository;
import io.scadatom.nucleus.repository.SmsChargerRepository;
import io.scadatom.nucleus.repository.SmsDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements ApplicationRunner {
  @Autowired ElectronRepository electronRepository;
  @Autowired ParticleRepository particleRepository;
  @Autowired SmmChargerRepository smmChargerRepository;
  @Autowired SmmDeviceRepository smmDeviceRepository;
  @Autowired SmmBondRepository smmBondRepository;
  @Autowired SmsChargerRepository smsChargerRepository;
  @Autowired SmsDeviceRepository smsDeviceRepository;
  @Autowired SmsBondRepository smsBondRepository;

  /**
   * Callback used to run the bean.
   *
   * @param args incoming application arguments
   * @throws Exception on error
   */
  @Override
  public void run(ApplicationArguments args) throws Exception {
    smsBondRepository.deleteAll();
    smsDeviceRepository.deleteAll();
    smsChargerRepository.deleteAll();
    smmBondRepository.deleteAll();
    smmDeviceRepository.deleteAll();
    smmChargerRepository.deleteAll();
    particleRepository.deleteAll();
    electronRepository.deleteAll();
    // Electron 1 to test modbus
    Electron electron1 = electronRepository.save(new Electron().name("Electron 1").enabled(true));
    Particle particle1_1 =
        particleRepository.save(
            new Particle()
                .name("Particle 1_1")
                .decimalFormat("0.0")
                .initValue("0")
                .electron(electron1));
    Particle particle1_2 =
        particleRepository.save(
            new Particle()
                .name("Particle 1_2")
                .decimalFormat("0.0")
                .initValue("1.1")
                .electron(electron1));
    SmmCharger smmCharger1 =
        smmChargerRepository.save(
            new SmmCharger()
                .enabled(true)
                .port("CNCA0")
                .baud(19200)
                .databit(8)
                .parity(Parity.Even)
                .stopbit(Stopbit.One)
                .batchDelay(10000)
                .retry(3)
                .timeout(200)
                .transDelay(0)
                .electron(electron1));
    SmmDevice smmDevice1_1 =
        smmDeviceRepository.save(
            new SmmDevice()
                .address("0x1")
                .enabled(true)
                .name("SmmDevice 1_1")
                .smmCharger(smmCharger1));
    SmmBond smmBond1_1_1 =
        smmBondRepository.save(
            new SmmBond()
                .enabled(true)
                .reg("0x100")
                .regType(RegType.HoldingReg)
                .valueType(ValueType.Int16)
                .exprIn("0.1x")
                .exprOut("10x")
                .particle(particle1_1)
                .smmDevice(smmDevice1_1));
    SmmBond smmBond1_1_2 =
        smmBondRepository.save(
            new SmmBond()
                .enabled(true)
                .reg("0x200")
                .regType(RegType.InputReg)
                .valueType(ValueType.Int16)
                .exprIn("0.1x")
                .particle(particle1_2)
                .smmDevice(smmDevice1_1));
    SmsCharger smsCharger1 =
        smsChargerRepository.save(
            new SmsCharger()
                .enabled(true)
                .port("CNCA2")
                .baud(19200)
                .databit(8)
                .parity(Parity.Even)
                .stopbit(Stopbit.One)
                .respDelay(0)
                .electron(electron1));
    SmsDevice smsDevice1_1 =
        smsDeviceRepository.save(
            new SmsDevice()
                .name("SmsDevice 1_1")
                .address("0x1")
                .enabled(true)
                .smsCharger(smsCharger1));
    SmsBond smsBond1_1_1 =
        smsBondRepository.save(
            new SmsBond()
                .enabled(true)
                .reg("0x100")
                .regType(RegType.HoldingReg)
                .valueType(ValueType.Int16)
                .exprIn("10x")
                .exprOut("0.1x")
                .particle(particle1_1)
                .smsDevice(smsDevice1_1));
    SmsBond smsBond1_1_2 =
        smsBondRepository.save(
            new SmsBond()
                .enabled(true)
                .reg("0x200")
                .regType(RegType.InputReg)
                .valueType(ValueType.Int16)
                .exprIn("10x")
                .particle(particle1_2)
                .smsDevice(smsDevice1_1));
                // Electron 2 to test no charger
    Electron electron2 = electronRepository.save(new Electron().name("Electron 2").enabled(true));
    Particle particle2_1 =
        particleRepository.save(
            new Particle()
                .name("Particle 2_1")
                .initValue("Winter is coming")
                .electron(electron2));
    Particle particle2_2 =
        particleRepository.save(
            new Particle()
                .name("Particle 2_2")
                .initValue("You know nothing")
                .electron(electron2));
  }
}
