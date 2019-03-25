package io.scadatom.electron.web.rest;
import io.scadatom.electron.service.SmsChargerOpService;
import io.scadatom.electron.web.rest.errors.BadRequestAlertException;
import io.scadatom.electron.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.scadatom.neutron.SmsChargerOpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SmsChargerOp.
 */
@RestController
@RequestMapping("/api")
public class SmsChargerOpResource {

    private final Logger log = LoggerFactory.getLogger(SmsChargerOpResource.class);

    private static final String ENTITY_NAME = "smsChargerOp";

    private final SmsChargerOpService smsChargerOpService;

    public SmsChargerOpResource(SmsChargerOpService smsChargerOpService) {
        this.smsChargerOpService = smsChargerOpService;
    }

    /**
     * POST  /sms-charger-ops : Create a new smsChargerOp.
     *
     * @param smsChargerOpDTO the smsChargerOpDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smsChargerOpDTO, or with status 400 (Bad Request) if the smsChargerOp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sms-charger-ops")
    public ResponseEntity<SmsChargerOpDTO> createSmsChargerOp(@RequestBody SmsChargerOpDTO smsChargerOpDTO) throws URISyntaxException {
        log.debug("REST request to save SmsChargerOp : {}", smsChargerOpDTO);
        if (smsChargerOpDTO.getId() != null) {
            throw new BadRequestAlertException("A new smsChargerOp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmsChargerOpDTO result = smsChargerOpService.save(smsChargerOpDTO);
        return ResponseEntity.created(new URI("/api/sms-charger-ops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sms-charger-ops : Updates an existing smsChargerOp.
     *
     * @param smsChargerOpDTO the smsChargerOpDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smsChargerOpDTO,
     * or with status 400 (Bad Request) if the smsChargerOpDTO is not valid,
     * or with status 500 (Internal Server Error) if the smsChargerOpDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sms-charger-ops")
    public ResponseEntity<SmsChargerOpDTO> updateSmsChargerOp(@RequestBody SmsChargerOpDTO smsChargerOpDTO) throws URISyntaxException {
        log.debug("REST request to update SmsChargerOp : {}", smsChargerOpDTO);
        if (smsChargerOpDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmsChargerOpDTO result = smsChargerOpService.save(smsChargerOpDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, smsChargerOpDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sms-charger-ops : get all the smsChargerOps.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of smsChargerOps in body
     */
    @GetMapping("/sms-charger-ops")
    public List<SmsChargerOpDTO> getAllSmsChargerOps() {
        log.debug("REST request to get all SmsChargerOps");
        return smsChargerOpService.findAll();
    }

    /**
     * GET  /sms-charger-ops/:id : get the "id" smsChargerOp.
     *
     * @param id the id of the smsChargerOpDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smsChargerOpDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sms-charger-ops/{id}")
    public ResponseEntity<SmsChargerOpDTO> getSmsChargerOp(@PathVariable Long id) {
        log.debug("REST request to get SmsChargerOp : {}", id);
        Optional<SmsChargerOpDTO> smsChargerOpDTO = smsChargerOpService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smsChargerOpDTO);
    }

    /**
     * DELETE  /sms-charger-ops/:id : delete the "id" smsChargerOp.
     *
     * @param id the id of the smsChargerOpDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sms-charger-ops/{id}")
    public ResponseEntity<Void> deleteSmsChargerOp(@PathVariable Long id) {
        log.debug("REST request to delete SmsChargerOp : {}", id);
        smsChargerOpService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
