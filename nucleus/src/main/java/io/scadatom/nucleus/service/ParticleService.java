package io.scadatom.nucleus.service;

import io.scadatom.neutron.ParticleDTO;
import io.scadatom.nucleus.domain.Particle;
import io.scadatom.nucleus.domain.SmmDevice;
import io.scadatom.nucleus.domain.SmsDevice;
import io.scadatom.nucleus.repository.ParticleRepository;
import io.scadatom.nucleus.service.mapper.ParticleMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service Implementation for managing Particle. */
@Service
@Transactional
public class ParticleService {

  private final Logger log = LoggerFactory.getLogger(ParticleService.class);

  private final ParticleRepository particleRepository;

  private final ParticleMapper particleMapper;

  public ParticleService(ParticleRepository particleRepository, ParticleMapper particleMapper) {
    this.particleRepository = particleRepository;
    this.particleMapper = particleMapper;
  }

  /**
   * Save a particle.
   *
   * @param particleDTO the entity to save
   * @return the persisted entity
   */
  public ParticleDTO save(ParticleDTO particleDTO) {
    log.debug("Request to save Particle : {}", particleDTO);
    Particle particle = particleMapper.toEntity(particleDTO);
    particle = particleRepository.save(particle);
    return particleMapper.toDto(particle);
  }

  /**
   * Get all the particles.
   *
   * @return the list of entities
   */
  @Transactional(readOnly = true)
  public List<ParticleDTO> findAll() {
    log.debug("Request to get all Particles");
    return particleRepository.findAll().stream()
        .map(particleMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one particle by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Transactional(readOnly = true)
  public Optional<ParticleDTO> findOne(Long id) {
    log.debug("Request to get Particle : {}", id);
    return particleRepository.findById(id).map(particleMapper::toDto);
  }

  /**
   * Delete the particle by id.
   *
   * @param id the id of the entity
   */
  public void delete(Long id) {
    log.debug("Request to delete Particle : {}", id);
    particleRepository.deleteById(id);
  }

  @Transactional(readOnly = true)
  public List<ParticleDTO> findAllWhereSmsBondIsNullAndInSmsDevice(String optId) {
    return particleRepository
        .findBySmsBond_IdIsNullAndElectron_SmsCharger_SmsDevicesContains(
            new SmsDevice().id(Long.valueOf(optId.trim())))
        .stream()
        .map(particleMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  @Transactional(readOnly = true)
  public List<ParticleDTO> findAllWhereSmmBondIsNullAndInSmmDevice(String optId) {
    return particleRepository
        .findBySmmBond_IdIsNullAndElectron_SmmCharger_SmmDevicesContains(
            new SmmDevice().id(Long.valueOf(optId.trim())))
        .stream()
        .map(particleMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }
}
