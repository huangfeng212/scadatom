package io.scadatom.nucleus.repository;

import io.scadatom.nucleus.domain.SmmCharger;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SmmCharger entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmmChargerRepository extends JpaRepository<SmmCharger, Long> {

}
