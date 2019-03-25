package io.scadatom.electron.service.mapper;

import io.scadatom.electron.domain.*;

import io.scadatom.neutron.ElectronOpDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity ElectronOp and its DTO ElectronOpDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ElectronOpMapper extends EntityMapper<ElectronOpDTO, ElectronOp> {



    default ElectronOp fromId(Long id) {
        if (id == null) {
            return null;
        }
        ElectronOp electronOp = new ElectronOp();
        electronOp.setId(id);
        return electronOp;
    }
}
