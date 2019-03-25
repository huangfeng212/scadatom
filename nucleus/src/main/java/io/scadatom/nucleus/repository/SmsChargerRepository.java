package io.scadatom.nucleus.repository;

import io.scadatom.nucleus.domain.SmsCharger;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SmsCharger entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmsChargerRepository extends JpaRepository<SmsCharger, Long> {

}
