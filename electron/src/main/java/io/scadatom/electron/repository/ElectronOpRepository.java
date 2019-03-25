package io.scadatom.electron.repository;

import io.scadatom.electron.domain.ElectronOp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ElectronOp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ElectronOpRepository extends JpaRepository<ElectronOp, Long> {

}
