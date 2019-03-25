package io.scadatom.electron.service.mapper;

import io.scadatom.electron.domain.*;

import io.scadatom.neutron.SmsBondOpDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity SmsBondOp and its DTO SmsBondOpDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmsBondOpMapper extends EntityMapper<SmsBondOpDTO, SmsBondOp> {



    default SmsBondOp fromId(Long id) {
        if (id == null) {
            return null;
        }
        SmsBondOp smsBondOp = new SmsBondOp();
        smsBondOp.setId(id);
        return smsBondOp;
    }
}
