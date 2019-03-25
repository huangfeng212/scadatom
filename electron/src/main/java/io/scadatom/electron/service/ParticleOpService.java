package io.scadatom.electron.service;

import io.scadatom.electron.domain.ParticleOp;
import io.scadatom.electron.repository.ParticleOpRepository;
import io.scadatom.electron.service.mapper.ParticleOpMapper;
import io.scadatom.neutron.ParticleOpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ParticleOp.
 */
@Service
@Transactional
public class ParticleOpService {

    private final Logger log = LoggerFactory.getLogger(ParticleOpService.class);

    private final ParticleOpRepository particleOpRepository;

    private final ParticleOpMapper particleOpMapper;

    public ParticleOpService(ParticleOpRepository particleOpRepository, ParticleOpMapper particleOpMapper) {
        this.particleOpRepository = particleOpRepository;
        this.particleOpMapper = particleOpMapper;
    }

    /**
     * Save a particleOp.
     *
     * @param particleOpDTO the entity to save
     * @return the persisted entity
     */
    public ParticleOpDTO save(ParticleOpDTO particleOpDTO) {
        log.debug("Request to save ParticleOp : {}", particleOpDTO);
        ParticleOp particleOp = particleOpMapper.toEntity(particleOpDTO);
        particleOp = particleOpRepository.save(particleOp);
        return particleOpMapper.toDto(particleOp);
    }

    /**
     * Get all the particleOps.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ParticleOpDTO> findAll() {
        log.debug("Request to get all ParticleOps");
        return particleOpRepository.findAll().stream()
            .map(particleOpMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one particleOp by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ParticleOpDTO> findOne(Long id) {
        log.debug("Request to get ParticleOp : {}", id);
        return particleOpRepository.findById(id)
            .map(particleOpMapper::toDto);
    }

    /**
     * Delete the particleOp by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ParticleOp : {}", id);        particleOpRepository.deleteById(id);
    }
}
