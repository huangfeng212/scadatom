package io.scadatom.nucleus.service.mapper;

import io.scadatom.neutron.ParticleDTO;
import io.scadatom.nucleus.domain.Particle;
import org.mapstruct.Mapper;

/** Mapper for the entity Particle and its DTO ParticleDTO. */
@Mapper(
    componentModel = "spring",
    uses = {ElectronMapper.class})
public interface ParticleMapper extends EntityMapper<ParticleDTO, Particle> {

  default Particle fromId(Long id) {
    if (id == null) {
      return null;
    }
    Particle particle = new Particle();
    particle.setId(id);
    return particle;
  }
}
