package io.scadatom.nucleus.service.mapper;

import io.scadatom.neutron.SmsDeviceDTO;
import io.scadatom.nucleus.domain.SmsDevice;
import org.mapstruct.Mapper;

/** Mapper for the entity SmsDevice and its DTO SmsDeviceDTO. */
@Mapper(
    componentModel = "spring",
    uses = {SmsChargerMapper.class})
public interface SmsDeviceMapper extends EntityMapper<SmsDeviceDTO, SmsDevice> {

  default SmsDevice fromId(Long id) {
    if (id == null) {
      return null;
    }
    SmsDevice smsDevice = new SmsDevice();
    smsDevice.setId(id);
    return smsDevice;
  }
}
