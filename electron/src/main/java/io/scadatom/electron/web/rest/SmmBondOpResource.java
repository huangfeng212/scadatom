package io.scadatom.electron.web.rest;
import io.scadatom.electron.service.SmmBondOpService;
import io.scadatom.electron.web.rest.errors.BadRequestAlertException;
import io.scadatom.electron.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.scadatom.neutron.SmmBondOpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SmmBondOp.
 */
@RestController
@RequestMapping("/api")
public class SmmBondOpResource {

    private final Logger log = LoggerFactory.getLogger(SmmBondOpResource.class);

    private static final String ENTITY_NAME = "smmBondOp";

    private final SmmBondOpService smmBondOpService;

    public SmmBondOpResource(SmmBondOpService smmBondOpService) {
        this.smmBondOpService = smmBondOpService;
    }

    /**
     * POST  /smm-bond-ops : Create a new smmBondOp.
     *
     * @param smmBondOpDTO the smmBondOpDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smmBondOpDTO, or with status 400 (Bad Request) if the smmBondOp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/smm-bond-ops")
    public ResponseEntity<SmmBondOpDTO> createSmmBondOp(@RequestBody SmmBondOpDTO smmBondOpDTO) throws URISyntaxException {
        log.debug("REST request to save SmmBondOp : {}", smmBondOpDTO);
        if (smmBondOpDTO.getId() != null) {
            throw new BadRequestAlertException("A new smmBondOp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmmBondOpDTO result = smmBondOpService.save(smmBondOpDTO);
        return ResponseEntity.created(new URI("/api/smm-bond-ops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /smm-bond-ops : Updates an existing smmBondOp.
     *
     * @param smmBondOpDTO the smmBondOpDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smmBondOpDTO,
     * or with status 400 (Bad Request) if the smmBondOpDTO is not valid,
     * or with status 500 (Internal Server Error) if the smmBondOpDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/smm-bond-ops")
    public ResponseEntity<SmmBondOpDTO> updateSmmBondOp(@RequestBody SmmBondOpDTO smmBondOpDTO) throws URISyntaxException {
        log.debug("REST request to update SmmBondOp : {}", smmBondOpDTO);
        if (smmBondOpDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmmBondOpDTO result = smmBondOpService.save(smmBondOpDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, smmBondOpDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /smm-bond-ops : get all the smmBondOps.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of smmBondOps in body
     */
    @GetMapping("/smm-bond-ops")
    public List<SmmBondOpDTO> getAllSmmBondOps() {
        log.debug("REST request to get all SmmBondOps");
        return smmBondOpService.findAll();
    }

    /**
     * GET  /smm-bond-ops/:id : get the "id" smmBondOp.
     *
     * @param id the id of the smmBondOpDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smmBondOpDTO, or with status 404 (Not Found)
     */
    @GetMapping("/smm-bond-ops/{id}")
    public ResponseEntity<SmmBondOpDTO> getSmmBondOp(@PathVariable Long id) {
        log.debug("REST request to get SmmBondOp : {}", id);
        Optional<SmmBondOpDTO> smmBondOpDTO = smmBondOpService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smmBondOpDTO);
    }

    /**
     * DELETE  /smm-bond-ops/:id : delete the "id" smmBondOp.
     *
     * @param id the id of the smmBondOpDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/smm-bond-ops/{id}")
    public ResponseEntity<Void> deleteSmmBondOp(@PathVariable Long id) {
        log.debug("REST request to delete SmmBondOp : {}", id);
        smmBondOpService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
