package io.scadatom.electron.web.rest;
import io.scadatom.electron.service.SmsBondOpService;
import io.scadatom.electron.web.rest.errors.BadRequestAlertException;
import io.scadatom.electron.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.scadatom.neutron.SmsBondOpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SmsBondOp.
 */
@RestController
@RequestMapping("/api")
public class SmsBondOpResource {

    private final Logger log = LoggerFactory.getLogger(SmsBondOpResource.class);

    private static final String ENTITY_NAME = "smsBondOp";

    private final SmsBondOpService smsBondOpService;

    public SmsBondOpResource(SmsBondOpService smsBondOpService) {
        this.smsBondOpService = smsBondOpService;
    }

    /**
     * POST  /sms-bond-ops : Create a new smsBondOp.
     *
     * @param smsBondOpDTO the smsBondOpDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smsBondOpDTO, or with status 400 (Bad Request) if the smsBondOp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sms-bond-ops")
    public ResponseEntity<SmsBondOpDTO> createSmsBondOp(@RequestBody SmsBondOpDTO smsBondOpDTO) throws URISyntaxException {
        log.debug("REST request to save SmsBondOp : {}", smsBondOpDTO);
        if (smsBondOpDTO.getId() != null) {
            throw new BadRequestAlertException("A new smsBondOp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmsBondOpDTO result = smsBondOpService.save(smsBondOpDTO);
        return ResponseEntity.created(new URI("/api/sms-bond-ops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sms-bond-ops : Updates an existing smsBondOp.
     *
     * @param smsBondOpDTO the smsBondOpDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smsBondOpDTO,
     * or with status 400 (Bad Request) if the smsBondOpDTO is not valid,
     * or with status 500 (Internal Server Error) if the smsBondOpDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sms-bond-ops")
    public ResponseEntity<SmsBondOpDTO> updateSmsBondOp(@RequestBody SmsBondOpDTO smsBondOpDTO) throws URISyntaxException {
        log.debug("REST request to update SmsBondOp : {}", smsBondOpDTO);
        if (smsBondOpDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmsBondOpDTO result = smsBondOpService.save(smsBondOpDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, smsBondOpDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sms-bond-ops : get all the smsBondOps.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of smsBondOps in body
     */
    @GetMapping("/sms-bond-ops")
    public List<SmsBondOpDTO> getAllSmsBondOps() {
        log.debug("REST request to get all SmsBondOps");
        return smsBondOpService.findAll();
    }

    /**
     * GET  /sms-bond-ops/:id : get the "id" smsBondOp.
     *
     * @param id the id of the smsBondOpDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smsBondOpDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sms-bond-ops/{id}")
    public ResponseEntity<SmsBondOpDTO> getSmsBondOp(@PathVariable Long id) {
        log.debug("REST request to get SmsBondOp : {}", id);
        Optional<SmsBondOpDTO> smsBondOpDTO = smsBondOpService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smsBondOpDTO);
    }

    /**
     * DELETE  /sms-bond-ops/:id : delete the "id" smsBondOp.
     *
     * @param id the id of the smsBondOpDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sms-bond-ops/{id}")
    public ResponseEntity<Void> deleteSmsBondOp(@PathVariable Long id) {
        log.debug("REST request to delete SmsBondOp : {}", id);
        smsBondOpService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
