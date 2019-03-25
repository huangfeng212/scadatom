package io.scadatom.nucleus.service;

import io.scadatom.neutron.SmmDeviceDTO;
import io.scadatom.nucleus.domain.SmmDevice;
import io.scadatom.nucleus.repository.SmmDeviceRepository;
import io.scadatom.nucleus.service.mapper.SmmDeviceMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service Implementation for managing SmmDevice. */
@Service
@Transactional
public class SmmDeviceService {

  private final Logger log = LoggerFactory.getLogger(SmmDeviceService.class);

  private final SmmDeviceRepository smmDeviceRepository;

  private final SmmDeviceMapper smmDeviceMapper;

  public SmmDeviceService(
      SmmDeviceRepository smmDeviceRepository, SmmDeviceMapper smmDeviceMapper) {
    this.smmDeviceRepository = smmDeviceRepository;
    this.smmDeviceMapper = smmDeviceMapper;
  }

  /**
   * Save a smmDevice.
   *
   * @param smmDeviceDTO the entity to save
   * @return the persisted entity
   */
  public SmmDeviceDTO save(SmmDeviceDTO smmDeviceDTO) {
    log.debug("Request to save SmmDevice : {}", smmDeviceDTO);
    SmmDevice smmDevice = smmDeviceMapper.toEntity(smmDeviceDTO);
    smmDevice = smmDeviceRepository.save(smmDevice);
    return smmDeviceMapper.toDto(smmDevice);
  }

  /**
   * Get all the smmDevices.
   *
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<SmmDeviceDTO> findAll() {
    log.debug("Request to get all SmmDevices");
    return smmDeviceRepository.findAll().stream()
        .map(smmDeviceMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one smmDevice by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public Optional<SmmDeviceDTO> findOne(Long id) {
    log.debug("Request to get SmmDevice : {}", id);
    return smmDeviceRepository.findById(id).map(smmDeviceMapper::toDto);
  }

  /**
   * Delete the smmDevice by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete SmmDevice : {}", id);
    smmDeviceRepository.deleteById(id);
  }
}
