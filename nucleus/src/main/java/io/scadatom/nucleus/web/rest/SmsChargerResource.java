package io.scadatom.nucleus.web.rest;
import io.scadatom.neutron.SmsChargerDTO;
import io.scadatom.nucleus.service.SmsChargerService;
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
 * REST controller for managing SmsCharger.
 */
@RestController
@RequestMapping("/api")
public class SmsChargerResource {

    private final Logger log = LoggerFactory.getLogger(SmsChargerResource.class);

    private static final String ENTITY_NAME = "smsCharger";

    private final SmsChargerService smsChargerService;

    public SmsChargerResource(SmsChargerService smsChargerService) {
        this.smsChargerService = smsChargerService;
    }

    /**
     * POST  /sms-chargers : Create a new smsCharger.
     *
     * @param smsChargerDTO the smsChargerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smsChargerDTO, or with status 400 (Bad Request) if the smsCharger has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sms-chargers")
    public ResponseEntity<SmsChargerDTO> createSmsCharger(@Valid @RequestBody SmsChargerDTO smsChargerDTO) throws URISyntaxException {
        log.debug("REST request to save SmsCharger : {}", smsChargerDTO);
        if (smsChargerDTO.getId() != null) {
            throw new BadRequestAlertException("A new smsCharger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmsChargerDTO result = smsChargerService.save(smsChargerDTO);
        return ResponseEntity.created(new URI("/api/sms-chargers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sms-chargers : Updates an existing smsCharger.
     *
     * @param smsChargerDTO the smsChargerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smsChargerDTO,
     * or with status 400 (Bad Request) if the smsChargerDTO is not valid,
     * or with status 500 (Internal Server Error) if the smsChargerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sms-chargers")
    public ResponseEntity<SmsChargerDTO> updateSmsCharger(@Valid @RequestBody SmsChargerDTO smsChargerDTO) throws URISyntaxException {
        log.debug("REST request to update SmsCharger : {}", smsChargerDTO);
        if (smsChargerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmsChargerDTO result = smsChargerService.save(smsChargerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, smsChargerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sms-chargers : get all the smsChargers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of smsChargers in body
     */
    @GetMapping("/sms-chargers")
    public List<SmsChargerDTO> getAllSmsChargers() {
        log.debug("REST request to get all SmsChargers");
        return smsChargerService.findAll();
    }

    /**
     * GET  /sms-chargers/:id : get the "id" smsCharger.
     *
     * @param id the id of the smsChargerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smsChargerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sms-chargers/{id}")
    public ResponseEntity<SmsChargerDTO> getSmsCharger(@PathVariable Long id) {
        log.debug("REST request to get SmsCharger : {}", id);
        Optional<SmsChargerDTO> smsChargerDTO = smsChargerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smsChargerDTO);
    }

    /**
     * DELETE  /sms-chargers/:id : delete the "id" smsCharger.
     *
     * @param id the id of the smsChargerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sms-chargers/{id}")
    public ResponseEntity<Void> deleteSmsCharger(@PathVariable Long id) {
        log.debug("REST request to delete SmsCharger : {}", id);
        smsChargerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
