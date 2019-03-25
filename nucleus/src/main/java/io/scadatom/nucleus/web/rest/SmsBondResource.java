package io.scadatom.nucleus.web.rest;
import io.scadatom.neutron.SmsBondDTO;
import io.scadatom.nucleus.service.SmsBondService;
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
 * REST controller for managing SmsBond.
 */
@RestController
@RequestMapping("/api")
public class SmsBondResource {

    private final Logger log = LoggerFactory.getLogger(SmsBondResource.class);

    private static final String ENTITY_NAME = "smsBond";

    private final SmsBondService smsBondService;

    public SmsBondResource(SmsBondService smsBondService) {
        this.smsBondService = smsBondService;
    }

    /**
     * POST  /sms-bonds : Create a new smsBond.
     *
     * @param smsBondDTO the smsBondDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smsBondDTO, or with status 400 (Bad Request) if the smsBond has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sms-bonds")
    public ResponseEntity<SmsBondDTO> createSmsBond(@Valid @RequestBody SmsBondDTO smsBondDTO) throws URISyntaxException {
        log.debug("REST request to save SmsBond : {}", smsBondDTO);
        if (smsBondDTO.getId() != null) {
            throw new BadRequestAlertException("A new smsBond cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmsBondDTO result = smsBondService.save(smsBondDTO);
        return ResponseEntity.created(new URI("/api/sms-bonds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sms-bonds : Updates an existing smsBond.
     *
     * @param smsBondDTO the smsBondDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smsBondDTO,
     * or with status 400 (Bad Request) if the smsBondDTO is not valid,
     * or with status 500 (Internal Server Error) if the smsBondDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sms-bonds")
    public ResponseEntity<SmsBondDTO> updateSmsBond(@Valid @RequestBody SmsBondDTO smsBondDTO) throws URISyntaxException {
        log.debug("REST request to update SmsBond : {}", smsBondDTO);
        if (smsBondDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmsBondDTO result = smsBondService.save(smsBondDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, smsBondDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sms-bonds : get all the smsBonds.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of smsBonds in body
     */
    @GetMapping("/sms-bonds")
    public List<SmsBondDTO> getAllSmsBonds() {
        log.debug("REST request to get all SmsBonds");
        return smsBondService.findAll();
    }

    /**
     * GET  /sms-bonds/:id : get the "id" smsBond.
     *
     * @param id the id of the smsBondDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smsBondDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sms-bonds/{id}")
    public ResponseEntity<SmsBondDTO> getSmsBond(@PathVariable Long id) {
        log.debug("REST request to get SmsBond : {}", id);
        Optional<SmsBondDTO> smsBondDTO = smsBondService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smsBondDTO);
    }

    /**
     * DELETE  /sms-bonds/:id : delete the "id" smsBond.
     *
     * @param id the id of the smsBondDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sms-bonds/{id}")
    public ResponseEntity<Void> deleteSmsBond(@PathVariable Long id) {
        log.debug("REST request to delete SmsBond : {}", id);
        smsBondService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
