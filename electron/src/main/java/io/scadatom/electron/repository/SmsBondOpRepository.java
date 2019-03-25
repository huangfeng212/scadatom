package io.scadatom.electron.repository;

import io.scadatom.electron.domain.SmsBondOp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SmsBondOp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmsBondOpRepository extends JpaRepository<SmsBondOp, Long> {

}
