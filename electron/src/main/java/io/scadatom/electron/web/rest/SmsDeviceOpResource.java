package io.scadatom.electron.web.rest;
import io.scadatom.electron.service.SmsDeviceOpService;
import io.scadatom.electron.web.rest.errors.BadRequestAlertException;
import io.scadatom.electron.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.scadatom.neutron.SmsDeviceOpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SmsDeviceOp.
 */
@RestController
@RequestMapping("/api")
public class SmsDeviceOpResource {

    private final Logger log = LoggerFactory.getLogger(SmsDeviceOpResource.class);

    private static final String ENTITY_NAME = "smsDeviceOp";

    private final SmsDeviceOpService smsDeviceOpService;

    public SmsDeviceOpResource(SmsDeviceOpService smsDeviceOpService) {
        this.smsDeviceOpService = smsDeviceOpService;
    }

    /**
     * POST  /sms-device-ops : Create a new smsDeviceOp.
     *
     * @param smsDeviceOpDTO the smsDeviceOpDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smsDeviceOpDTO, or with status 400 (Bad Request) if the smsDeviceOp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sms-device-ops")
    public ResponseEntity<SmsDeviceOpDTO> createSmsDeviceOp(@RequestBody SmsDeviceOpDTO smsDeviceOpDTO) throws URISyntaxException {
        log.debug("REST request to save SmsDeviceOp : {}", smsDeviceOpDTO);
        if (smsDeviceOpDTO.getId() != null) {
            throw new BadRequestAlertException("A new smsDeviceOp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmsDeviceOpDTO result = smsDeviceOpService.save(smsDeviceOpDTO);
        return ResponseEntity.created(new URI("/api/sms-device-ops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sms-device-ops : Updates an existing smsDeviceOp.
     *
     * @param smsDeviceOpDTO the smsDeviceOpDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smsDeviceOpDTO,
     * or with status 400 (Bad Request) if the smsDeviceOpDTO is not valid,
     * or with status 500 (Internal Server Error) if the smsDeviceOpDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sms-device-ops")
    public ResponseEntity<SmsDeviceOpDTO> updateSmsDeviceOp(@RequestBody SmsDeviceOpDTO smsDeviceOpDTO) throws URISyntaxException {
        log.debug("REST request to update SmsDeviceOp : {}", smsDeviceOpDTO);
        if (smsDeviceOpDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmsDeviceOpDTO result = smsDeviceOpService.save(smsDeviceOpDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, smsDeviceOpDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sms-device-ops : get all the smsDeviceOps.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of smsDeviceOps in body
     */
    @GetMapping("/sms-device-ops")
    public List<SmsDeviceOpDTO> getAllSmsDeviceOps() {
        log.debug("REST request to get all SmsDeviceOps");
        return smsDeviceOpService.findAll();
    }

    /**
     * GET  /sms-device-ops/:id : get the "id" smsDeviceOp.
     *
     * @param id the id of the smsDeviceOpDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smsDeviceOpDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sms-device-ops/{id}")
    public ResponseEntity<SmsDeviceOpDTO> getSmsDeviceOp(@PathVariable Long id) {
        log.debug("REST request to get SmsDeviceOp : {}", id);
        Optional<SmsDeviceOpDTO> smsDeviceOpDTO = smsDeviceOpService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smsDeviceOpDTO);
    }

    /**
     * DELETE  /sms-device-ops/:id : delete the "id" smsDeviceOp.
     *
     * @param id the id of the smsDeviceOpDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sms-device-ops/{id}")
    public ResponseEntity<Void> deleteSmsDeviceOp(@PathVariable Long id) {
        log.debug("REST request to delete SmsDeviceOp : {}", id);
        smsDeviceOpService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
