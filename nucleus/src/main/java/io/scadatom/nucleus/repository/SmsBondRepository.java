package io.scadatom.nucleus.repository;

import io.scadatom.nucleus.domain.SmsBond;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SmsBond entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmsBondRepository extends JpaRepository<SmsBond, Long> {

}
