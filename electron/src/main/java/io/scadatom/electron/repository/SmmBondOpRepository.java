package io.scadatom.electron.repository;

import io.scadatom.electron.domain.SmmBondOp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SmmBondOp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmmBondOpRepository extends JpaRepository<SmmBondOp, Long> {

}
