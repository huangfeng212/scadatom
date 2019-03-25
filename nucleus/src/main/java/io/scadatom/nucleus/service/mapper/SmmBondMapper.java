package io.scadatom.nucleus.service.mapper;

import io.scadatom.neutron.SmmBondDTO;
import io.scadatom.nucleus.domain.SmmBond;
import org.mapstruct.Mapper;

/** Mapper for the entity SmmBond and its DTO SmmBondDTO. */
@Mapper(
    componentModel = "spring",
    uses = {ParticleMapper.class, SmmDeviceMapper.class})
public interface SmmBondMapper extends EntityMapper<SmmBondDTO, SmmBond> {

  default SmmBond fromId(Long id) {
    if (id == null) {
      return null;
    }
    SmmBond smmBond = new SmmBond();
    smmBond.setId(id);
    return smmBond;
  }
}
