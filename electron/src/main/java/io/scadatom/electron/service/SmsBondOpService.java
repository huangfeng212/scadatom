package io.scadatom.electron.service;

import io.scadatom.electron.domain.SmsBondOp;
import io.scadatom.electron.repository.SmsBondOpRepository;
import io.scadatom.electron.service.mapper.SmsBondOpMapper;
import io.scadatom.neutron.SmsBondOpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SmsBondOp.
 */
@Service
@Transactional
public class SmsBondOpService {

    private final Logger log = LoggerFactory.getLogger(SmsBondOpService.class);

    private final SmsBondOpRepository smsBondOpRepository;

    private final SmsBondOpMapper smsBondOpMapper;

    public SmsBondOpService(SmsBondOpRepository smsBondOpRepository, SmsBondOpMapper smsBondOpMapper) {
        this.smsBondOpRepository = smsBondOpRepository;
        this.smsBondOpMapper = smsBondOpMapper;
    }

    /**
     * Save a smsBondOp.
     *
     * @param smsBondOpDTO the entity to save
     * @return the persisted entity
     */
    public SmsBondOpDTO save(SmsBondOpDTO smsBondOpDTO) {
        log.debug("Request to save SmsBondOp : {}", smsBondOpDTO);
        SmsBondOp smsBondOp = smsBondOpMapper.toEntity(smsBondOpDTO);
        smsBondOp = smsBondOpRepository.save(smsBondOp);
        return smsBondOpMapper.toDto(smsBondOp);
    }

    /**
     * Get all the smsBondOps.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SmsBondOpDTO> findAll() {
        log.debug("Request to get all SmsBondOps");
        return smsBondOpRepository.findAll().stream()
            .map(smsBondOpMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one smsBondOp by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SmsBondOpDTO> findOne(Long id) {
        log.debug("Request to get SmsBondOp : {}", id);
        return smsBondOpRepository.findById(id)
            .map(smsBondOpMapper::toDto);
    }

    /**
     * Delete the smsBondOp by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SmsBondOp : {}", id);        smsBondOpRepository.deleteById(id);
    }
}
