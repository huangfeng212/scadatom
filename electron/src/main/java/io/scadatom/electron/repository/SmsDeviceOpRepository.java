package io.scadatom.electron.repository;

import io.scadatom.electron.domain.SmsDeviceOp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SmsDeviceOp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmsDeviceOpRepository extends JpaRepository<SmsDeviceOp, Long> {

}
