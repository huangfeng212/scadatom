package io.scadatom.nucleus.service;

import io.scadatom.neutron.SmmBondDTO;
import io.scadatom.nucleus.domain.SmmBond;
import io.scadatom.nucleus.repository.SmmBondRepository;
import io.scadatom.nucleus.service.mapper.SmmBondMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service Implementation for managing SmmBond. */
@Service
@Transactional
public class SmmBondService {

  private final Logger log = LoggerFactory.getLogger(SmmBondService.class);

  private final SmmBondRepository smmBondRepository;

  private final SmmBondMapper smmBondMapper;

  public SmmBondService(SmmBondRepository smmBondRepository, SmmBondMapper smmBondMapper) {
    this.smmBondRepository = smmBondRepository;
    this.smmBondMapper = smmBondMapper;
  }

  /**
   * Save a smmBond.
   *
   * @param smmBondDTO the entity to save
   * @return the persisted entity
   */
  public SmmBondDTO save(SmmBondDTO smmBondDTO) {
    log.debug("Request to save SmmBond : {}", smmBondDTO);
    SmmBond smmBond = smmBondMapper.toEntity(smmBondDTO);
    smmBond = smmBondRepository.save(smmBond);
    return smmBondMapper.toDto(smmBond);
  }

  /**
   * Get all the smmBonds.
   *
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<SmmBondDTO> findAll() {
    log.debug("Request to get all SmmBonds");
    return smmBondRepository.findAll().stream()
        .map(smmBondMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one smmBond by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public Optional<SmmBondDTO> findOne(Long id) {
    log.debug("Request to get SmmBond : {}", id);
    return smmBondRepository.findById(id).map(smmBondMapper::toDto);
  }

  /**
   * Delete the smmBond by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete SmmBond : {}", id);
    smmBondRepository.deleteById(id);
  }
}
