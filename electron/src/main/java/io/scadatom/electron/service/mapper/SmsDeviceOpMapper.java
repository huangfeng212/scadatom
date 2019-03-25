package io.scadatom.electron.service.mapper;

import io.scadatom.electron.domain.*;

import io.scadatom.neutron.SmsDeviceOpDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity SmsDeviceOp and its DTO SmsDeviceOpDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmsDeviceOpMapper extends EntityMapper<SmsDeviceOpDTO, SmsDeviceOp> {



    default SmsDeviceOp fromId(Long id) {
        if (id == null) {
            return null;
        }
        SmsDeviceOp smsDeviceOp = new SmsDeviceOp();
        smsDeviceOp.setId(id);
        return smsDeviceOp;
    }
}
