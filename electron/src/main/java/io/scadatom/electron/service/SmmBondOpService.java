package io.scadatom.electron.service;

import io.scadatom.electron.domain.SmmBondOp;
import io.scadatom.electron.repository.SmmBondOpRepository;
import io.scadatom.electron.service.mapper.SmmBondOpMapper;
import io.scadatom.neutron.SmmBondOpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SmmBondOp.
 */
@Service
@Transactional
public class SmmBondOpService {

    private final Logger log = LoggerFactory.getLogger(SmmBondOpService.class);

    private final SmmBondOpRepository smmBondOpRepository;

    private final SmmBondOpMapper smmBondOpMapper;

    public SmmBondOpService(SmmBondOpRepository smmBondOpRepository, SmmBondOpMapper smmBondOpMapper) {
        this.smmBondOpRepository = smmBondOpRepository;
        this.smmBondOpMapper = smmBondOpMapper;
    }

    /**
     * Save a smmBondOp.
     *
     * @param smmBondOpDTO the entity to save
     * @return the persisted entity
     */
    public SmmBondOpDTO save(SmmBondOpDTO smmBondOpDTO) {
        log.debug("Request to save SmmBondOp : {}", smmBondOpDTO);
        SmmBondOp smmBondOp = smmBondOpMapper.toEntity(smmBondOpDTO);
        smmBondOp = smmBondOpRepository.save(smmBondOp);
        return smmBondOpMapper.toDto(smmBondOp);
    }

    /**
     * Get all the smmBondOps.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SmmBondOpDTO> findAll() {
        log.debug("Request to get all SmmBondOps");
        return smmBondOpRepository.findAll().stream()
            .map(smmBondOpMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one smmBondOp by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SmmBondOpDTO> findOne(Long id) {
        log.debug("Request to get SmmBondOp : {}", id);
        return smmBondOpRepository.findById(id)
            .map(smmBondOpMapper::toDto);
    }

    /**
     * Delete the smmBondOp by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SmmBondOp : {}", id);        smmBondOpRepository.deleteById(id);
    }
}
