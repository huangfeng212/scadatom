package io.scadatom.nucleus.web.rest;
import io.scadatom.neutron.SmsDeviceDTO;
import io.scadatom.nucleus.service.SmsDeviceService;
import io.scadatom.nucleus.web.rest.errors.BadRequestAlertException;
import io.scadatom.nucleus.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SmsDevice.
 */
@RestController
@RequestMapping("/api")
public class SmsDeviceResource {

    private final Logger log = LoggerFactory.getLogger(SmsDeviceResource.class);

    private static final String ENTITY_NAME = "smsDevice";

    private final SmsDeviceService smsDeviceService;

    public SmsDeviceResource(SmsDeviceService smsDeviceService) {
        this.smsDeviceService = smsDeviceService;
    }

    /**
     * POST  /sms-devices : Create a new smsDevice.
     *
     * @param smsDeviceDTO the smsDeviceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smsDeviceDTO, or with status 400 (Bad Request) if the smsDevice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sms-devices")
    public ResponseEntity<SmsDeviceDTO> createSmsDevice(@Valid @RequestBody SmsDeviceDTO smsDeviceDTO) throws URISyntaxException {
        log.debug("REST request to save SmsDevice : {}", smsDeviceDTO);
        if (smsDeviceDTO.getId() != null) {
            throw new BadRequestAlertException("A new smsDevice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmsDeviceDTO result = smsDeviceService.save(smsDeviceDTO);
        return ResponseEntity.created(new URI("/api/sms-devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sms-devices : Updates an existing smsDevice.
     *
     * @param smsDeviceDTO the smsDeviceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smsDeviceDTO,
     * or with status 400 (Bad Request) if the smsDeviceDTO is not valid,
     * or with status 500 (Internal Server Error) if the smsDeviceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sms-devices")
    public ResponseEntity<SmsDeviceDTO> updateSmsDevice(@Valid @RequestBody SmsDeviceDTO smsDeviceDTO) throws URISyntaxException {
        log.debug("REST request to update SmsDevice : {}", smsDeviceDTO);
        if (smsDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmsDeviceDTO result = smsDeviceService.save(smsDeviceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, smsDeviceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sms-devices : get all the smsDevices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of smsDevices in body
     */
    @GetMapping("/sms-devices")
    public List<SmsDeviceDTO> getAllSmsDevices() {
        log.debug("REST request to get all SmsDevices");
        return smsDeviceService.findAll();
    }

    /**
     * GET  /sms-devices/:id : get the "id" smsDevice.
     *
     * @param id the id of the smsDeviceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smsDeviceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sms-devices/{id}")
    public ResponseEntity<SmsDeviceDTO> getSmsDevice(@PathVariable Long id) {
        log.debug("REST request to get SmsDevice : {}", id);
        Optional<SmsDeviceDTO> smsDeviceDTO = smsDeviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smsDeviceDTO);
    }

    /**
     * DELETE  /sms-devices/:id : delete the "id" smsDevice.
     *
     * @param id the id of the smsDeviceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sms-devices/{id}")
    public ResponseEntity<Void> deleteSmsDevice(@PathVariable Long id) {
        log.debug("REST request to delete SmsDevice : {}", id);
        smsDeviceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
