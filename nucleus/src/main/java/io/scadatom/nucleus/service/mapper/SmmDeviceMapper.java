package io.scadatom.nucleus.service.mapper;

import io.scadatom.neutron.SmmDeviceDTO;
import io.scadatom.nucleus.domain.SmmDevice;
import org.mapstruct.Mapper;

/** Mapper for the entity SmmDevice and its DTO SmmDeviceDTO. */
@Mapper(
    componentModel = "spring",
    uses = {SmmChargerMapper.class})
public interface SmmDeviceMapper extends EntityMapper<SmmDeviceDTO, SmmDevice> {

  default SmmDevice fromId(Long id) {
    if (id == null) {
      return null;
    }
    SmmDevice smmDevice = new SmmDevice();
    smmDevice.setId(id);
    return smmDevice;
  }
}
