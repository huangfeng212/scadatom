package io.scadatom.nucleus.service.mapper;

import io.scadatom.neutron.SmsBondDTO;
import io.scadatom.nucleus.domain.SmsBond;
import org.mapstruct.Mapper;

/** Mapper for the entity SmsBond and its DTO SmsBondDTO. */
@Mapper(
    componentModel = "spring",
    uses = {ParticleMapper.class, SmsDeviceMapper.class})
public interface SmsBondMapper extends EntityMapper<SmsBondDTO, SmsBond> {

  default SmsBond fromId(Long id) {
    if (id == null) {
      return null;
    }
    SmsBond smsBond = new SmsBond();
    smsBond.setId(id);
    return smsBond;
  }
}
