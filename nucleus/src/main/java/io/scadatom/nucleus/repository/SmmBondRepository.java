package io.scadatom.nucleus.repository;

import io.scadatom.nucleus.domain.SmmBond;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SmmBond entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmmBondRepository extends JpaRepository<SmmBond, Long> {

}
