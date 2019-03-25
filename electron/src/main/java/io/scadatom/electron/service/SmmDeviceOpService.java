package io.scadatom.electron.service;

import io.scadatom.electron.domain.SmmDeviceOp;
import io.scadatom.electron.repository.SmmDeviceOpRepository;
import io.scadatom.electron.service.mapper.SmmDeviceOpMapper;
import io.scadatom.neutron.SmmDeviceOpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SmmDeviceOp.
 */
@Service
@Transactional
public class SmmDeviceOpService {

    private final Logger log = LoggerFactory.getLogger(SmmDeviceOpService.class);

    private final SmmDeviceOpRepository smmDeviceOpRepository;

    private final SmmDeviceOpMapper smmDeviceOpMapper;

    public SmmDeviceOpService(SmmDeviceOpRepository smmDeviceOpRepository, SmmDeviceOpMapper smmDeviceOpMapper) {
        this.smmDeviceOpRepository = smmDeviceOpRepository;
        this.smmDeviceOpMapper = smmDeviceOpMapper;
    }

    /**
     * Save a smmDeviceOp.
     *
     * @param smmDeviceOpDTO the entity to save
     * @return the persisted entity
     */
    public SmmDeviceOpDTO save(SmmDeviceOpDTO smmDeviceOpDTO) {
        log.debug("Request to save SmmDeviceOp : {}", smmDeviceOpDTO);
        SmmDeviceOp smmDeviceOp = smmDeviceOpMapper.toEntity(smmDeviceOpDTO);
        smmDeviceOp = smmDeviceOpRepository.save(smmDeviceOp);
        return smmDeviceOpMapper.toDto(smmDeviceOp);
    }

    /**
     * Get all the smmDeviceOps.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SmmDeviceOpDTO> findAll() {
        log.debug("Request to get all SmmDeviceOps");
        return smmDeviceOpRepository.findAll().stream()
            .map(smmDeviceOpMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one smmDeviceOp by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SmmDeviceOpDTO> findOne(Long id) {
        log.debug("Request to get SmmDeviceOp : {}", id);
        return smmDeviceOpRepository.findById(id)
            .map(smmDeviceOpMapper::toDto);
    }

    /**
     * Delete the smmDeviceOp by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SmmDeviceOp : {}", id);        smmDeviceOpRepository.deleteById(id);
    }
}
