package io.scadatom.electron.repository;

import io.scadatom.electron.domain.ParticleOp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ParticleOp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParticleOpRepository extends JpaRepository<ParticleOp, Long> {

}
