package io.scadatom.electron.service;

import io.scadatom.electron.domain.SmmChargerOp;
import io.scadatom.electron.repository.SmmChargerOpRepository;
import io.scadatom.electron.service.mapper.SmmChargerOpMapper;
import io.scadatom.neutron.SmmChargerOpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SmmChargerOp.
 */
@Service
@Transactional
public class SmmChargerOpService {

    private final Logger log = LoggerFactory.getLogger(SmmChargerOpService.class);

    private final SmmChargerOpRepository smmChargerOpRepository;

    private final SmmChargerOpMapper smmChargerOpMapper;

    public SmmChargerOpService(SmmChargerOpRepository smmChargerOpRepository, SmmChargerOpMapper smmChargerOpMapper) {
        this.smmChargerOpRepository = smmChargerOpRepository;
        this.smmChargerOpMapper = smmChargerOpMapper;
    }

    /**
     * Save a smmChargerOp.
     *
     * @param smmChargerOpDTO the entity to save
     * @return the persisted entity
     */
    public SmmChargerOpDTO save(SmmChargerOpDTO smmChargerOpDTO) {
        log.debug("Request to save SmmChargerOp : {}", smmChargerOpDTO);
        SmmChargerOp smmChargerOp = smmChargerOpMapper.toEntity(smmChargerOpDTO);
        smmChargerOp = smmChargerOpRepository.save(smmChargerOp);
        return smmChargerOpMapper.toDto(smmChargerOp);
    }

    /**
     * Get all the smmChargerOps.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SmmChargerOpDTO> findAll() {
        log.debug("Request to get all SmmChargerOps");
        return smmChargerOpRepository.findAll().stream()
            .map(smmChargerOpMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one smmChargerOp by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SmmChargerOpDTO> findOne(Long id) {
        log.debug("Request to get SmmChargerOp : {}", id);
        return smmChargerOpRepository.findById(id)
            .map(smmChargerOpMapper::toDto);
    }

    /**
     * Delete the smmChargerOp by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SmmChargerOp : {}", id);        smmChargerOpRepository.deleteById(id);
    }
}
