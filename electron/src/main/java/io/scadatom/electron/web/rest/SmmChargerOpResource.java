package io.scadatom.electron.web.rest;
import io.scadatom.electron.service.SmmChargerOpService;
import io.scadatom.electron.web.rest.errors.BadRequestAlertException;
import io.scadatom.electron.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.scadatom.neutron.SmmChargerOpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SmmChargerOp.
 */
@RestController
@RequestMapping("/api")
public class SmmChargerOpResource {

    private final Logger log = LoggerFactory.getLogger(SmmChargerOpResource.class);

    private static final String ENTITY_NAME = "smmChargerOp";

    private final SmmChargerOpService smmChargerOpService;

    public SmmChargerOpResource(SmmChargerOpService smmChargerOpService) {
        this.smmChargerOpService = smmChargerOpService;
    }

    /**
     * POST  /smm-charger-ops : Create a new smmChargerOp.
     *
     * @param smmChargerOpDTO the smmChargerOpDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smmChargerOpDTO, or with status 400 (Bad Request) if the smmChargerOp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/smm-charger-ops")
    public ResponseEntity<SmmChargerOpDTO> createSmmChargerOp(@RequestBody SmmChargerOpDTO smmChargerOpDTO) throws URISyntaxException {
        log.debug("REST request to save SmmChargerOp : {}", smmChargerOpDTO);
        if (smmChargerOpDTO.getId() != null) {
            throw new BadRequestAlertException("A new smmChargerOp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmmChargerOpDTO result = smmChargerOpService.save(smmChargerOpDTO);
        return ResponseEntity.created(new URI("/api/smm-charger-ops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /smm-charger-ops : Updates an existing smmChargerOp.
     *
     * @param smmChargerOpDTO the smmChargerOpDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smmChargerOpDTO,
     * or with status 400 (Bad Request) if the smmChargerOpDTO is not valid,
     * or with status 500 (Internal Server Error) if the smmChargerOpDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/smm-charger-ops")
    public ResponseEntity<SmmChargerOpDTO> updateSmmChargerOp(@RequestBody SmmChargerOpDTO smmChargerOpDTO) throws URISyntaxException {
        log.debug("REST request to update SmmChargerOp : {}", smmChargerOpDTO);
        if (smmChargerOpDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmmChargerOpDTO result = smmChargerOpService.save(smmChargerOpDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, smmChargerOpDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /smm-charger-ops : get all the smmChargerOps.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of smmChargerOps in body
     */
    @GetMapping("/smm-charger-ops")
    public List<SmmChargerOpDTO> getAllSmmChargerOps() {
        log.debug("REST request to get all SmmChargerOps");
        return smmChargerOpService.findAll();
    }

    /**
     * GET  /smm-charger-ops/:id : get the "id" smmChargerOp.
     *
     * @param id the id of the smmChargerOpDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smmChargerOpDTO, or with status 404 (Not Found)
     */
    @GetMapping("/smm-charger-ops/{id}")
    public ResponseEntity<SmmChargerOpDTO> getSmmChargerOp(@PathVariable Long id) {
        log.debug("REST request to get SmmChargerOp : {}", id);
        Optional<SmmChargerOpDTO> smmChargerOpDTO = smmChargerOpService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smmChargerOpDTO);
    }

    /**
     * DELETE  /smm-charger-ops/:id : delete the "id" smmChargerOp.
     *
     * @param id the id of the smmChargerOpDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/smm-charger-ops/{id}")
    public ResponseEntity<Void> deleteSmmChargerOp(@PathVariable Long id) {
        log.debug("REST request to delete SmmChargerOp : {}", id);
        smmChargerOpService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
