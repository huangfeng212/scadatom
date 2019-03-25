package io.scadatom.electron.web.rest;
import io.scadatom.electron.service.SmmDeviceOpService;
import io.scadatom.electron.web.rest.errors.BadRequestAlertException;
import io.scadatom.electron.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.scadatom.neutron.SmmDeviceOpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SmmDeviceOp.
 */
@RestController
@RequestMapping("/api")
public class SmmDeviceOpResource {

    private final Logger log = LoggerFactory.getLogger(SmmDeviceOpResource.class);

    private static final String ENTITY_NAME = "smmDeviceOp";

    private final SmmDeviceOpService smmDeviceOpService;

    public SmmDeviceOpResource(SmmDeviceOpService smmDeviceOpService) {
        this.smmDeviceOpService = smmDeviceOpService;
    }

    /**
     * POST  /smm-device-ops : Create a new smmDeviceOp.
     *
     * @param smmDeviceOpDTO the smmDeviceOpDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smmDeviceOpDTO, or with status 400 (Bad Request) if the smmDeviceOp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/smm-device-ops")
    public ResponseEntity<SmmDeviceOpDTO> createSmmDeviceOp(@RequestBody SmmDeviceOpDTO smmDeviceOpDTO) throws URISyntaxException {
        log.debug("REST request to save SmmDeviceOp : {}", smmDeviceOpDTO);
        if (smmDeviceOpDTO.getId() != null) {
            throw new BadRequestAlertException("A new smmDeviceOp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmmDeviceOpDTO result = smmDeviceOpService.save(smmDeviceOpDTO);
        return ResponseEntity.created(new URI("/api/smm-device-ops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /smm-device-ops : Updates an existing smmDeviceOp.
     *
     * @param smmDeviceOpDTO the smmDeviceOpDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smmDeviceOpDTO,
     * or with status 400 (Bad Request) if the smmDeviceOpDTO is not valid,
     * or with status 500 (Internal Server Error) if the smmDeviceOpDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/smm-device-ops")
    public ResponseEntity<SmmDeviceOpDTO> updateSmmDeviceOp(@RequestBody SmmDeviceOpDTO smmDeviceOpDTO) throws URISyntaxException {
        log.debug("REST request to update SmmDeviceOp : {}", smmDeviceOpDTO);
        if (smmDeviceOpDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmmDeviceOpDTO result = smmDeviceOpService.save(smmDeviceOpDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, smmDeviceOpDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /smm-device-ops : get all the smmDeviceOps.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of smmDeviceOps in body
     */
    @GetMapping("/smm-device-ops")
    public List<SmmDeviceOpDTO> getAllSmmDeviceOps() {
        log.debug("REST request to get all SmmDeviceOps");
        return smmDeviceOpService.findAll();
    }

    /**
     * GET  /smm-device-ops/:id : get the "id" smmDeviceOp.
     *
     * @param id the id of the smmDeviceOpDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smmDeviceOpDTO, or with status 404 (Not Found)
     */
    @GetMapping("/smm-device-ops/{id}")
    public ResponseEntity<SmmDeviceOpDTO> getSmmDeviceOp(@PathVariable Long id) {
        log.debug("REST request to get SmmDeviceOp : {}", id);
        Optional<SmmDeviceOpDTO> smmDeviceOpDTO = smmDeviceOpService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smmDeviceOpDTO);
    }

    /**
     * DELETE  /smm-device-ops/:id : delete the "id" smmDeviceOp.
     *
     * @param id the id of the smmDeviceOpDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/smm-device-ops/{id}")
    public ResponseEntity<Void> deleteSmmDeviceOp(@PathVariable Long id) {
        log.debug("REST request to delete SmmDeviceOp : {}", id);
        smmDeviceOpService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
