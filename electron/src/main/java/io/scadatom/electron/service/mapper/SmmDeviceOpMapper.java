package io.scadatom.electron.service.mapper;

import io.scadatom.electron.domain.*;

import io.scadatom.neutron.SmmDeviceOpDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity SmmDeviceOp and its DTO SmmDeviceOpDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmmDeviceOpMapper extends EntityMapper<SmmDeviceOpDTO, SmmDeviceOp> {



    default SmmDeviceOp fromId(Long id) {
        if (id == null) {
            return null;
        }
        SmmDeviceOp smmDeviceOp = new SmmDeviceOp();
        smmDeviceOp.setId(id);
        return smmDeviceOp;
    }
}
