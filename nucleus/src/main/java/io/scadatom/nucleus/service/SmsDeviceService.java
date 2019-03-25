package io.scadatom.nucleus.service;

import io.scadatom.neutron.SmsDeviceDTO;
import io.scadatom.nucleus.domain.SmsDevice;
import io.scadatom.nucleus.repository.SmsDeviceRepository;
import io.scadatom.nucleus.service.mapper.SmsDeviceMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service Implementation for managing SmsDevice. */
@Service
@Transactional
public class SmsDeviceService {

  private final Logger log = LoggerFactory.getLogger(SmsDeviceService.class);

  private final SmsDeviceRepository smsDeviceRepository;

  private final SmsDeviceMapper smsDeviceMapper;

  public SmsDeviceService(
      SmsDeviceRepository smsDeviceRepository, SmsDeviceMapper smsDeviceMapper) {
    this.smsDeviceRepository = smsDeviceRepository;
    this.smsDeviceMapper = smsDeviceMapper;
  }

  /**
   * Save a smsDevice.
   *
   * @param smsDeviceDTO the entity to save
   * @return the persisted entity
   */
  public SmsDeviceDTO save(SmsDeviceDTO smsDeviceDTO) {
    log.debug("Request to save SmsDevice : {}", smsDeviceDTO);
    SmsDevice smsDevice = smsDeviceMapper.toEntity(smsDeviceDTO);
    smsDevice = smsDeviceRepository.save(smsDevice);
    return smsDeviceMapper.toDto(smsDevice);
  }

  /**
   * Get all the smsDevices.
   *
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<SmsDeviceDTO> findAll() {
    log.debug("Request to get all SmsDevices");
    return smsDeviceRepository.findAll().stream()
        .map(smsDeviceMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one smsDevice by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public Optional<SmsDeviceDTO> findOne(Long id) {
    log.debug("Request to get SmsDevice : {}", id);
    return smsDeviceRepository.findById(id).map(smsDeviceMapper::toDto);
  }

  /**
   * Delete the smsDevice by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete SmsDevice : {}", id);
    smsDeviceRepository.deleteById(id);
  }
}
