package io.scadatom.electron.service;

import io.scadatom.electron.domain.SmsDeviceOp;
import io.scadatom.electron.repository.SmsDeviceOpRepository;
import io.scadatom.electron.service.mapper.SmsDeviceOpMapper;
import io.scadatom.neutron.SmsDeviceOpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SmsDeviceOp.
 */
@Service
@Transactional
public class SmsDeviceOpService {

    private final Logger log = LoggerFactory.getLogger(SmsDeviceOpService.class);

    private final SmsDeviceOpRepository smsDeviceOpRepository;

    private final SmsDeviceOpMapper smsDeviceOpMapper;

    public SmsDeviceOpService(SmsDeviceOpRepository smsDeviceOpRepository, SmsDeviceOpMapper smsDeviceOpMapper) {
        this.smsDeviceOpRepository = smsDeviceOpRepository;
        this.smsDeviceOpMapper = smsDeviceOpMapper;
    }

    /**
     * Save a smsDeviceOp.
     *
     * @param smsDeviceOpDTO the entity to save
     * @return the persisted entity
     */
    public SmsDeviceOpDTO save(SmsDeviceOpDTO smsDeviceOpDTO) {
        log.debug("Request to save SmsDeviceOp : {}", smsDeviceOpDTO);
        SmsDeviceOp smsDeviceOp = smsDeviceOpMapper.toEntity(smsDeviceOpDTO);
        smsDeviceOp = smsDeviceOpRepository.save(smsDeviceOp);
        return smsDeviceOpMapper.toDto(smsDeviceOp);
    }

    /**
     * Get all the smsDeviceOps.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SmsDeviceOpDTO> findAll() {
        log.debug("Request to get all SmsDeviceOps");
        return smsDeviceOpRepository.findAll().stream()
            .map(smsDeviceOpMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one smsDeviceOp by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SmsDeviceOpDTO> findOne(Long id) {
        log.debug("Request to get SmsDeviceOp : {}", id);
        return smsDeviceOpRepository.findById(id)
            .map(smsDeviceOpMapper::toDto);
    }

    /**
     * Delete the smsDeviceOp by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SmsDeviceOp : {}", id);        smsDeviceOpRepository.deleteById(id);
    }
}
