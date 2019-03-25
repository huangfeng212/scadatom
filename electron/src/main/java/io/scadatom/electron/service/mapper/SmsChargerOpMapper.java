package io.scadatom.electron.service.mapper;

import io.scadatom.electron.domain.*;

import io.scadatom.neutron.SmsChargerOpDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity SmsChargerOp and its DTO SmsChargerOpDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SmsChargerOpMapper extends EntityMapper<SmsChargerOpDTO, SmsChargerOp> {



    default SmsChargerOp fromId(Long id) {
        if (id == null) {
            return null;
        }
        SmsChargerOp smsChargerOp = new SmsChargerOp();
        smsChargerOp.setId(id);
        return smsChargerOp;
    }
}
