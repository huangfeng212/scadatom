package io.scadatom.nucleus.service;

import io.scadatom.neutron.SmsChargerDTO;
import io.scadatom.nucleus.domain.SmsCharger;
import io.scadatom.nucleus.repository.SmsChargerRepository;
import io.scadatom.nucleus.service.mapper.SmsChargerMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service Implementation for managing SmsCharger. */
@Service
@Transactional
public class SmsChargerService {

  private final Logger log = LoggerFactory.getLogger(SmsChargerService.class);

  private final SmsChargerRepository smsChargerRepository;

  private final SmsChargerMapper smsChargerMapper;

  public SmsChargerService(
      SmsChargerRepository smsChargerRepository, SmsChargerMapper smsChargerMapper) {
    this.smsChargerRepository = smsChargerRepository;
    this.smsChargerMapper = smsChargerMapper;
  }

  /**
   * Save a smsCharger.
   *
   * @param smsChargerDTO the entity to save
   * @return the persisted entity
   */
  public SmsChargerDTO save(SmsChargerDTO smsChargerDTO) {
    log.debug("Request to save SmsCharger : {}", smsChargerDTO);
    SmsCharger smsCharger = smsChargerMapper.toEntity(smsChargerDTO);
    smsCharger = smsChargerRepository.save(smsCharger);
    return smsChargerMapper.toDto(smsCharger);
  }

  /**
   * Get all the smsChargers.
   *
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<SmsChargerDTO> findAll() {
    log.debug("Request to get all SmsChargers");
    return smsChargerRepository.findAll().stream()
        .map(smsChargerMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one smsCharger by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public Optional<SmsChargerDTO> findOne(Long id) {
    log.debug("Request to get SmsCharger : {}", id);
    return smsChargerRepository.findById(id).map(smsChargerMapper::toDto);
  }

  /**
   * Delete the smsCharger by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete SmsCharger : {}", id);
    smsChargerRepository.deleteById(id);
  }
}
