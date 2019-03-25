package io.scadatom.nucleus.service;

import io.scadatom.neutron.SmsBondDTO;
import io.scadatom.nucleus.domain.SmsBond;
import io.scadatom.nucleus.repository.SmsBondRepository;
import io.scadatom.nucleus.service.mapper.SmsBondMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service Implementation for managing SmsBond. */
@Service
@Transactional
public class SmsBondService {

  private final Logger log = LoggerFactory.getLogger(SmsBondService.class);

  private final SmsBondRepository smsBondRepository;

  private final SmsBondMapper smsBondMapper;

  public SmsBondService(SmsBondRepository smsBondRepository, SmsBondMapper smsBondMapper) {
    this.smsBondRepository = smsBondRepository;
    this.smsBondMapper = smsBondMapper;
  }

  /**
   * Save a smsBond.
   *
   * @param smsBondDTO the entity to save
   * @return the persisted entity
   */
  public SmsBondDTO save(SmsBondDTO smsBondDTO) {
    log.debug("Request to save SmsBond : {}", smsBondDTO);
    SmsBond smsBond = smsBondMapper.toEntity(smsBondDTO);
    smsBond = smsBondRepository.save(smsBond);
    return smsBondMapper.toDto(smsBond);
  }

  /**
   * Get all the smsBonds.
   *
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<SmsBondDTO> findAll() {
    log.debug("Request to get all SmsBonds");
    return smsBondRepository.findAll().stream()
        .map(smsBondMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one smsBond by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public Optional<SmsBondDTO> findOne(Long id) {
    log.debug("Request to get SmsBond : {}", id);
    return smsBondRepository.findById(id).map(smsBondMapper::toDto);
  }

  /**
   * Delete the smsBond by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete SmsBond : {}", id);
    smsBondRepository.deleteById(id);
  }
}
