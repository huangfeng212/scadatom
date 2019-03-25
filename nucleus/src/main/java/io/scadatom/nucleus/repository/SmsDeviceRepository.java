package io.scadatom.nucleus.repository;

import io.scadatom.nucleus.domain.SmsDevice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SmsDevice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmsDeviceRepository extends JpaRepository<SmsDevice, Long> {

}
