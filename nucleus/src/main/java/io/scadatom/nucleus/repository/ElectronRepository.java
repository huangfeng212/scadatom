package io.scadatom.nucleus.repository;

import io.scadatom.nucleus.domain.Electron;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Electron entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ElectronRepository extends JpaRepository<Electron, Long> {

}
