package io.scadatom.electron.service.mapper;

import io.scadatom.electron.domain.*;

import io.scadatom.neutron.SmmBondOpDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity SmmBondOp and its DTO SmmBondOpDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmmBondOpMapper extends EntityMapper<SmmBondOpDTO, SmmBondOp> {



    default SmmBondOp fromId(Long id) {
        if (id == null) {
            return null;
        }
        SmmBondOp smmBondOp = new SmmBondOp();
        smmBondOp.setId(id);
        return smmBondOp;
    }
}
