package io.scadatom.electron.repository;

import io.scadatom.electron.domain.SmmDeviceOp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SmmDeviceOp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmmDeviceOpRepository extends JpaRepository<SmmDeviceOp, Long> {

}
