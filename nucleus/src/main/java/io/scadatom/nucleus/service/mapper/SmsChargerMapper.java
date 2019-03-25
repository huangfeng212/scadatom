package io.scadatom.nucleus.service.mapper;

import io.scadatom.neutron.SmsChargerDTO;
import io.scadatom.nucleus.domain.SmsCharger;
import org.mapstruct.Mapper;

/** Mapper for the entity SmsCharger and its DTO SmsChargerDTO. */
@Mapper(
    componentModel = "spring",
    uses = {ElectronMapper.class})
public interface SmsChargerMapper extends EntityMapper<SmsChargerDTO, SmsCharger> {

  default SmsCharger fromId(Long id) {
    if (id == null) {
      return null;
    }
    SmsCharger smsCharger = new SmsCharger();
    smsCharger.setId(id);
    return smsCharger;
  }
}
