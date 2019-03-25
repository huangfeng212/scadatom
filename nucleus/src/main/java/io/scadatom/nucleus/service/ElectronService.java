package io.scadatom.nucleus.service;

import io.scadatom.neutron.ElectronDTO;
import io.scadatom.nucleus.domain.Electron;
import io.scadatom.nucleus.repository.ElectronRepository;
import io.scadatom.nucleus.service.mapper.ElectronMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service Implementation for managing Electron. */
@Service
@Transactional
public class ElectronService {

  private final Logger log = LoggerFactory.getLogger(ElectronService.class);

  private final ElectronRepository electronRepository;

  private final ElectronMapper electronMapper;

  public ElectronService(ElectronRepository electronRepository, ElectronMapper electronMapper) {
    this.electronRepository = electronRepository;
    this.electronMapper = electronMapper;
  }

  /**
   * Save a electron.
   *
   * @param electronDTO the entity to save
   * @return the persisted entity
   */
  public ElectronDTO save(ElectronDTO electronDTO) {
    log.debug("Request to save Electron : {}", electronDTO);
    Electron electron = electronMapper.toEntity(electronDTO);
    log.info(electron.toString());
    electron = electronRepository.save(electron);
    return electronMapper.toDto(electron);
  }

  /**
   * Get all the electrons.
   *
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<ElectronDTO> findAll() {
    log.debug("Request to get all Electrons");
    return electronRepository.findAll().stream()
        .map(electronMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one electron by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public Optional<ElectronDTO> findOne(Long id) {
    log.debug("Request to get Electron : {}", id);
    return electronRepository.findById(id).map(electronMapper::toDto);
  }

  /**
   * Delete the electron by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete Electron : {}", id);
    electronRepository.deleteById(id);
  }
}
