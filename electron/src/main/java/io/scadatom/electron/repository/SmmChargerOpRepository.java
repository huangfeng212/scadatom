package io.scadatom.electron.repository;

import io.scadatom.electron.domain.SmmChargerOp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SmmChargerOp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmmChargerOpRepository extends JpaRepository<SmmChargerOp, Long> {

}
