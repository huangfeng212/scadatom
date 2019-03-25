package io.scadatom.electron.service.mapper;

import io.scadatom.electron.domain.*;

import io.scadatom.neutron.SmmChargerOpDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity SmmChargerOp and its DTO SmmChargerOpDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmmChargerOpMapper extends EntityMapper<SmmChargerOpDTO, SmmChargerOp> {



    default SmmChargerOp fromId(Long id) {
        if (id == null) {
            return null;
        }
        SmmChargerOp smmChargerOp = new SmmChargerOp();
        smmChargerOp.setId(id);
        return smmChargerOp;
    }
}
