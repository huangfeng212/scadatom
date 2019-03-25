package io.scadatom.electron.service.mapper;

import io.scadatom.electron.domain.*;

import io.scadatom.neutron.ParticleOpDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity ParticleOp and its DTO ParticleOpDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ParticleOpMapper extends EntityMapper<ParticleOpDTO, ParticleOp> {



    default ParticleOp fromId(Long id) {
        if (id == null) {
            return null;
        }
        ParticleOp particleOp = new ParticleOp();
        particleOp.setId(id);
        return particleOp;
    }
}
