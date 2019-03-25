package io.scadatom.nucleus.web.rest;
import io.scadatom.neutron.ParticleDTO;
import io.scadatom.nucleus.service.ParticleService;
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
 * REST controller for managing Particle.
 */
@RestController
@RequestMapping("/api")
public class ParticleResource {

    private final Logger log = LoggerFactory.getLogger(ParticleResource.class);

    private static final String ENTITY_NAME = "particle";

    private final ParticleService particleService;

    public ParticleResource(ParticleService particleService) {
        this.particleService = particleService;
    }

    /**
     * POST  /particles : Create a new particle.
     *
     * @param particleDTO the particleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new particleDTO, or with status 400 (Bad Request) if the particle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/particles")
    public ResponseEntity<ParticleDTO> createParticle(@Valid @RequestBody ParticleDTO particleDTO) throws URISyntaxException {
        log.debug("REST request to save Particle : {}", particleDTO);
        if (particleDTO.getId() != null) {
            throw new BadRequestAlertException("A new particle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParticleDTO result = particleService.save(particleDTO);
        return ResponseEntity.created(new URI("/api/particles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /particles : Updates an existing particle.
     *
     * @param particleDTO the particleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated particleDTO,
     * or with status 400 (Bad Request) if the particleDTO is not valid,
     * or with status 500 (Internal Server Error) if the particleDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/particles")
    public ResponseEntity<ParticleDTO> updateParticle(@Valid @RequestBody ParticleDTO particleDTO) throws URISyntaxException {
        log.debug("REST request to update Particle : {}", particleDTO);
        if (particleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ParticleDTO result = particleService.save(particleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, particleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /particles : get all the particles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of particles in body
     */
    @GetMapping("/particles")
    public List<ParticleDTO> getAllParticles(@RequestParam(required = false) String filter, @RequestParam(required = false) String optId) {
        if ("smmbond-is-null-and-in-smmdevice".equals(filter)) {
            log.debug(
                "REST request to get all Particles where smmBond is null and can reside in smmDevice {}'s electron",
                optId);
            return particleService.findAllWhereSmmBondIsNullAndInSmmDevice(
                optId);
        }
        if ("smsbond-is-null-and-in-smsdevice".equals(filter)) {
            log.debug(
                "REST request to get all Particles where smsBond is null and can reside in smsDevice {}'s electron",
                optId);
            return particleService.findAllWhereSmsBondIsNullAndInSmsDevice(
                optId);
        }
        log.debug("REST request to get all Particles");
        return particleService.findAll();
    }

    /**
     * GET  /particles/:id : get the "id" particle.
     *
     * @param id the id of the particleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the particleDTO, or with status 404 (Not Found)
     */
    @GetMapping("/particles/{id}")
    public ResponseEntity<ParticleDTO> getParticle(@PathVariable Long id) {
        log.debug("REST request to get Particle : {}", id);
        Optional<ParticleDTO> particleDTO = particleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(particleDTO);
    }

    /**
     * DELETE  /particles/:id : delete the "id" particle.
     *
     * @param id the id of the particleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/particles/{id}")
    public ResponseEntity<Void> deleteParticle(@PathVariable Long id) {
        log.debug("REST request to delete Particle : {}", id);
        particleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
