package io.scadatom.electron.repository;

import io.scadatom.electron.domain.SmsChargerOp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SmsChargerOp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmsChargerOpRepository extends JpaRepository<SmsChargerOp, Long> {

}
