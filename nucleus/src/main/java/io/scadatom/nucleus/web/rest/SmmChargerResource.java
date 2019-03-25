package io.scadatom.nucleus.web.rest;
import io.scadatom.neutron.SmmChargerDTO;
import io.scadatom.nucleus.service.SmmChargerService;
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
 * REST controller for managing SmmCharger.
 */
@RestController
@RequestMapping("/api")
public class SmmChargerResource {

    private final Logger log = LoggerFactory.getLogger(SmmChargerResource.class);

    private static final String ENTITY_NAME = "smmCharger";

    private final SmmChargerService smmChargerService;

    public SmmChargerResource(SmmChargerService smmChargerService) {
        this.smmChargerService = smmChargerService;
    }

    /**
     * POST  /smm-chargers : Create a new smmCharger.
     *
     * @param smmChargerDTO the smmChargerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smmChargerDTO, or with status 400 (Bad Request) if the smmCharger has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/smm-chargers")
    public ResponseEntity<SmmChargerDTO> createSmmCharger(@Valid @RequestBody SmmChargerDTO smmChargerDTO) throws URISyntaxException {
        log.debug("REST request to save SmmCharger : {}", smmChargerDTO);
        if (smmChargerDTO.getId() != null) {
            throw new BadRequestAlertException("A new smmCharger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmmChargerDTO result = smmChargerService.save(smmChargerDTO);
        return ResponseEntity.created(new URI("/api/smm-chargers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /smm-chargers : Updates an existing smmCharger.
     *
     * @param smmChargerDTO the smmChargerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smmChargerDTO,
     * or with status 400 (Bad Request) if the smmChargerDTO is not valid,
     * or with status 500 (Internal Server Error) if the smmChargerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/smm-chargers")
    public ResponseEntity<SmmChargerDTO> updateSmmCharger(@Valid @RequestBody SmmChargerDTO smmChargerDTO) throws URISyntaxException {
        log.debug("REST request to update SmmCharger : {}", smmChargerDTO);
        if (smmChargerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmmChargerDTO result = smmChargerService.save(smmChargerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, smmChargerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /smm-chargers : get all the smmChargers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of smmChargers in body
     */
    @GetMapping("/smm-chargers")
    public List<SmmChargerDTO> getAllSmmChargers() {
        log.debug("REST request to get all SmmChargers");
        return smmChargerService.findAll();
    }

    /**
     * GET  /smm-chargers/:id : get the "id" smmCharger.
     *
     * @param id the id of the smmChargerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smmChargerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/smm-chargers/{id}")
    public ResponseEntity<SmmChargerDTO> getSmmCharger(@PathVariable Long id) {
        log.debug("REST request to get SmmCharger : {}", id);
        Optional<SmmChargerDTO> smmChargerDTO = smmChargerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smmChargerDTO);
    }

    /**
     * DELETE  /smm-chargers/:id : delete the "id" smmCharger.
     *
     * @param id the id of the smmChargerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/smm-chargers/{id}")
    public ResponseEntity<Void> deleteSmmCharger(@PathVariable Long id) {
        log.debug("REST request to delete SmmCharger : {}", id);
        smmChargerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
