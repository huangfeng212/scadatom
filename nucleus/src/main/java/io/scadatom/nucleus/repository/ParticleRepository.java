package io.scadatom.nucleus.repository;

import io.scadatom.nucleus.domain.Particle;
import io.scadatom.nucleus.domain.SmmDevice;
import io.scadatom.nucleus.domain.SmsDevice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Spring Data repository for the Particle entity. */
@SuppressWarnings("unused")
@Repository
public interface ParticleRepository extends JpaRepository<Particle, Long> {
  List<Particle> findBySmmBond_IdIsNullAndElectron_SmmCharger_SmmDevicesContains(
      SmmDevice smmDevice);

  List<Particle> findBySmsBond_IdIsNullAndElectron_SmsCharger_SmsDevicesContains(
      SmsDevice smsDevice);
}
