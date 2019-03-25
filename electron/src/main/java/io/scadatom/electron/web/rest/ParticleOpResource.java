package io.scadatom.electron.web.rest;
import io.scadatom.electron.service.ParticleOpService;
import io.scadatom.electron.web.rest.errors.BadRequestAlertException;
import io.scadatom.electron.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.scadatom.neutron.ParticleOpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ParticleOp.
 */
@RestController
@RequestMapping("/api")
public class ParticleOpResource {

    private final Logger log = LoggerFactory.getLogger(ParticleOpResource.class);

    private static final String ENTITY_NAME = "particleOp";

    private final ParticleOpService particleOpService;

    public ParticleOpResource(ParticleOpService particleOpService) {
        this.particleOpService = particleOpService;
    }

    /**
     * POST  /particle-ops : Create a new particleOp.
     *
     * @param particleOpDTO the particleOpDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new particleOpDTO, or with status 400 (Bad Request) if the particleOp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/particle-ops")
    public ResponseEntity<ParticleOpDTO> createParticleOp(@RequestBody ParticleOpDTO particleOpDTO) throws URISyntaxException {
        log.debug("REST request to save ParticleOp : {}", particleOpDTO);
        if (particleOpDTO.getId() != null) {
            throw new BadRequestAlertException("A new particleOp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParticleOpDTO result = particleOpService.save(particleOpDTO);
        return ResponseEntity.created(new URI("/api/particle-ops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /particle-ops : Updates an existing particleOp.
     *
     * @param particleOpDTO the particleOpDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated particleOpDTO,
     * or with status 400 (Bad Request) if the particleOpDTO is not valid,
     * or with status 500 (Internal Server Error) if the particleOpDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/particle-ops")
    public ResponseEntity<ParticleOpDTO> updateParticleOp(@RequestBody ParticleOpDTO particleOpDTO) throws URISyntaxException {
        log.debug("REST request to update ParticleOp : {}", particleOpDTO);
        if (particleOpDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ParticleOpDTO result = particleOpService.save(particleOpDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, particleOpDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /particle-ops : get all the particleOps.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of particleOps in body
     */
    @GetMapping("/particle-ops")
    public List<ParticleOpDTO> getAllParticleOps() {
        log.debug("REST request to get all ParticleOps");
        return particleOpService.findAll();
    }

    /**
     * GET  /particle-ops/:id : get the "id" particleOp.
     *
     * @param id the id of the particleOpDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the particleOpDTO, or with status 404 (Not Found)
     */
    @GetMapping("/particle-ops/{id}")
    public ResponseEntity<ParticleOpDTO> getParticleOp(@PathVariable Long id) {
        log.debug("REST request to get ParticleOp : {}", id);
        Optional<ParticleOpDTO> particleOpDTO = particleOpService.findOne(id);
        return ResponseUtil.wrapOrNotFound(particleOpDTO);
    }

    /**
     * DELETE  /particle-ops/:id : delete the "id" particleOp.
     *
     * @param id the id of the particleOpDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/particle-ops/{id}")
    public ResponseEntity<Void> deleteParticleOp(@PathVariable Long id) {
        log.debug("REST request to delete ParticleOp : {}", id);
        particleOpService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
