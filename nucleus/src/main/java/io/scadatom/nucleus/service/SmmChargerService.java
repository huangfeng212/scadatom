package io.scadatom.nucleus.service;

import io.scadatom.neutron.SmmChargerDTO;
import io.scadatom.nucleus.domain.SmmCharger;
import io.scadatom.nucleus.repository.SmmChargerRepository;
import io.scadatom.nucleus.service.mapper.SmmChargerMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service Implementation for managing SmmCharger. */
@Service
@Transactional
public class SmmChargerService {

  private final Logger log = LoggerFactory.getLogger(SmmChargerService.class);

  private final SmmChargerRepository smmChargerRepository;

  private final SmmChargerMapper smmChargerMapper;

  public SmmChargerService(
      SmmChargerRepository smmChargerRepository, SmmChargerMapper smmChargerMapper) {
    this.smmChargerRepository = smmChargerRepository;
    this.smmChargerMapper = smmChargerMapper;
  }

  /**
   * Save a smmCharger.
   *
   * @param smmChargerDTO the entity to save
   * @return the persisted entity
   */
  public SmmChargerDTO save(SmmChargerDTO smmChargerDTO) {
    log.debug("Request to save SmmCharger : {}", smmChargerDTO);
    SmmCharger smmCharger = smmChargerMapper.toEntity(smmChargerDTO);
    smmCharger = smmChargerRepository.save(smmCharger);
    return smmChargerMapper.toDto(smmCharger);
  }

  /**
   * Get all the smmChargers.
   *
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<SmmChargerDTO> findAll() {
    log.debug("Request to get all SmmChargers");
    return smmChargerRepository.findAll().stream()
        .map(smmChargerMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one smmCharger by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public Optional<SmmChargerDTO> findOne(Long id) {
    log.debug("Request to get SmmCharger : {}", id);
    return smmChargerRepository.findById(id).map(smmChargerMapper::toDto);
  }

  /**
   * Delete the smmCharger by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete SmmCharger : {}", id);
    smmChargerRepository.deleteById(id);
  }
}
