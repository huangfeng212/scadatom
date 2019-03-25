package io.scadatom.nucleus.repository;

import io.scadatom.nucleus.domain.SmmDevice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SmmDevice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmmDeviceRepository extends JpaRepository<SmmDevice, Long> {

}
