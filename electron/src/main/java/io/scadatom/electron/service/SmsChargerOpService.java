package io.scadatom.electron.service;

import io.scadatom.electron.domain.SmsChargerOp;
import io.scadatom.electron.repository.SmsChargerOpRepository;
import io.scadatom.electron.service.mapper.SmsChargerOpMapper;
import io.scadatom.neutron.SmsChargerOpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SmsChargerOp.
 */
@Service
@Transactional
public class SmsChargerOpService {

    private final Logger log = LoggerFactory.getLogger(SmsChargerOpService.class);

    private final SmsChargerOpRepository smsChargerOpRepository;

    private final SmsChargerOpMapper smsChargerOpMapper;

    public SmsChargerOpService(SmsChargerOpRepository smsChargerOpRepository, SmsChargerOpMapper smsChargerOpMapper) {
        this.smsChargerOpRepository = smsChargerOpRepository;
        this.smsChargerOpMapper = smsChargerOpMapper;
    }

    /**
     * Save a smsChargerOp.
     *
     * @param smsChargerOpDTO the entity to save
     * @return the persisted entity
     */
    public SmsChargerOpDTO save(SmsChargerOpDTO smsChargerOpDTO) {
        log.debug("Request to save SmsChargerOp : {}", smsChargerOpDTO);
        SmsChargerOp smsChargerOp = smsChargerOpMapper.toEntity(smsChargerOpDTO);
        smsChargerOp = smsChargerOpRepository.save(smsChargerOp);
        return smsChargerOpMapper.toDto(smsChargerOp);
    }

    /**
     * Get all the smsChargerOps.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SmsChargerOpDTO> findAll() {
        log.debug("Request to get all SmsChargerOps");
        return smsChargerOpRepository.findAll().stream()
            .map(smsChargerOpMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one smsChargerOp by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SmsChargerOpDTO> findOne(Long id) {
        log.debug("Request to get SmsChargerOp : {}", id);
        return smsChargerOpRepository.findById(id)
            .map(smsChargerOpMapper::toDto);
    }

    /**
     * Delete the smsChargerOp by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SmsChargerOp : {}", id);        smsChargerOpRepository.deleteById(id);
    }
}
