package io.scadatom.electron.service;

import io.scadatom.electron.domain.ElectronOp;
import io.scadatom.electron.repository.ElectronOpRepository;
import io.scadatom.electron.service.mapper.ElectronOpMapper;
import io.scadatom.neutron.ElectronOpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ElectronOp.
 */
@Service
@Transactional
public class ElectronOpService {

    private final Logger log = LoggerFactory.getLogger(ElectronOpService.class);

    private final ElectronOpRepository electronOpRepository;

    private final ElectronOpMapper electronOpMapper;

    public ElectronOpService(ElectronOpRepository electronOpRepository, ElectronOpMapper electronOpMapper) {
        this.electronOpRepository = electronOpRepository;
        this.electronOpMapper = electronOpMapper;
    }

    /**
     * Save a electronOp.
     *
     * @param electronOpDTO the entity to save
     * @return the persisted entity
     */
    public ElectronOpDTO save(ElectronOpDTO electronOpDTO) {
        log.debug("Request to save ElectronOp : {}", electronOpDTO);
        ElectronOp electronOp = electronOpMapper.toEntity(electronOpDTO);
        electronOp = electronOpRepository.save(electronOp);
        return electronOpMapper.toDto(electronOp);
    }

    /**
     * Get all the electronOps.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ElectronOpDTO> findAll() {
        log.debug("Request to get all ElectronOps");
        return electronOpRepository.findAll().stream()
            .map(electronOpMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one electronOp by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ElectronOpDTO> findOne(Long id) {
        log.debug("Request to get ElectronOp : {}", id);
        return electronOpRepository.findById(id)
            .map(electronOpMapper::toDto);
    }

    /**
     * Delete the electronOp by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ElectronOp : {}", id);        electronOpRepository.deleteById(id);
    }
}
