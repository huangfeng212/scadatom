package io.scadatom.nucleus.service.mapper;

import io.scadatom.neutron.ElectronDTO;
import io.scadatom.nucleus.domain.Electron;
import org.mapstruct.Mapper;

/** Mapper for the entity Electron and its DTO ElectronDTO. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface ElectronMapper extends EntityMapper<ElectronDTO, Electron> {

  default Electron fromId(Long id) {
    if (id == null) {
      return null;
    }
    Electron electron = new Electron();
    electron.setId(id);
    return electron;
  }
}
