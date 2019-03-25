package io.scadatom.nucleus.web.rest;
import io.scadatom.neutron.SmmBondDTO;
import io.scadatom.nucleus.service.SmmBondService;
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
 * REST controller for managing SmmBond.
 */
@RestController
@RequestMapping("/api")
public class SmmBondResource {

    private final Logger log = LoggerFactory.getLogger(SmmBondResource.class);

    private static final String ENTITY_NAME = "smmBond";

    private final SmmBondService smmBondService;

    public SmmBondResource(SmmBondService smmBondService) {
        this.smmBondService = smmBondService;
    }

    /**
     * POST  /smm-bonds : Create a new smmBond.
     *
     * @param smmBondDTO the smmBondDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smmBondDTO, or with status 400 (Bad Request) if the smmBond has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/smm-bonds")
    public ResponseEntity<SmmBondDTO> createSmmBond(@Valid @RequestBody SmmBondDTO smmBondDTO) throws URISyntaxException {
        log.debug("REST request to save SmmBond : {}", smmBondDTO);
        if (smmBondDTO.getId() != null) {
            throw new BadRequestAlertException("A new smmBond cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmmBondDTO result = smmBondService.save(smmBondDTO);
        return ResponseEntity.created(new URI("/api/smm-bonds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /smm-bonds : Updates an existing smmBond.
     *
     * @param smmBondDTO the smmBondDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smmBondDTO,
     * or with status 400 (Bad Request) if the smmBondDTO is not valid,
     * or with status 500 (Internal Server Error) if the smmBondDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/smm-bonds")
    public ResponseEntity<SmmBondDTO> updateSmmBond(@Valid @RequestBody SmmBondDTO smmBondDTO) throws URISyntaxException {
        log.debug("REST request to update SmmBond : {}", smmBondDTO);
        if (smmBondDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmmBondDTO result = smmBondService.save(smmBondDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, smmBondDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /smm-bonds : get all the smmBonds.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of smmBonds in body
     */
    @GetMapping("/smm-bonds")
    public List<SmmBondDTO> getAllSmmBonds() {
        log.debug("REST request to get all SmmBonds");
        return smmBondService.findAll();
    }

    /**
     * GET  /smm-bonds/:id : get the "id" smmBond.
     *
     * @param id the id of the smmBondDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smmBondDTO, or with status 404 (Not Found)
     */
    @GetMapping("/smm-bonds/{id}")
    public ResponseEntity<SmmBondDTO> getSmmBond(@PathVariable Long id) {
        log.debug("REST request to get SmmBond : {}", id);
        Optional<SmmBondDTO> smmBondDTO = smmBondService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smmBondDTO);
    }

    /**
     * DELETE  /smm-bonds/:id : delete the "id" smmBond.
     *
     * @param id the id of the smmBondDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/smm-bonds/{id}")
    public ResponseEntity<Void> deleteSmmBond(@PathVariable Long id) {
        log.debug("REST request to delete SmmBond : {}", id);
        smmBondService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
