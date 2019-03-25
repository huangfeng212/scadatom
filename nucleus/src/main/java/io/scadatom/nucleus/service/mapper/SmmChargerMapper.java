package io.scadatom.nucleus.service.mapper;

import io.scadatom.neutron.SmmChargerDTO;
import io.scadatom.nucleus.domain.SmmCharger;
import org.mapstruct.Mapper;

/** Mapper for the entity SmmCharger and its DTO SmmChargerDTO. */
@Mapper(
    componentModel = "spring",
    uses = {ElectronMapper.class})
public interface SmmChargerMapper extends EntityMapper<SmmChargerDTO, SmmCharger> {

  default SmmCharger fromId(Long id) {
    if (id == null) {
      return null;
    }
    SmmCharger smmCharger = new SmmCharger();
    smmCharger.setId(id);
    return smmCharger;
  }
}
