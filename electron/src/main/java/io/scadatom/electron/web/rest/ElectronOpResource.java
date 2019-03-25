package io.scadatom.electron.web.rest;
import io.scadatom.electron.service.ElectronOpService;
import io.scadatom.electron.web.rest.errors.BadRequestAlertException;
import io.scadatom.electron.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.scadatom.neutron.ElectronOpDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ElectronOp.
 */
@RestController
@RequestMapping("/api")
public class ElectronOpResource {

    private final Logger log = LoggerFactory.getLogger(ElectronOpResource.class);

    private static final String ENTITY_NAME = "electronOp";

    private final ElectronOpService electronOpService;

    public ElectronOpResource(ElectronOpService electronOpService) {
        this.electronOpService = electronOpService;
    }

    /**
     * POST  /electron-ops : Create a new electronOp.
     *
     * @param electronOpDTO the electronOpDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new electronOpDTO, or with status 400 (Bad Request) if the electronOp has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/electron-ops")
    public ResponseEntity<ElectronOpDTO> createElectronOp(@RequestBody ElectronOpDTO electronOpDTO) throws URISyntaxException {
        log.debug("REST request to save ElectronOp : {}", electronOpDTO);
        if (electronOpDTO.getId() != null) {
            throw new BadRequestAlertException("A new electronOp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ElectronOpDTO result = electronOpService.save(electronOpDTO);
        return ResponseEntity.created(new URI("/api/electron-ops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /electron-ops : Updates an existing electronOp.
     *
     * @param electronOpDTO the electronOpDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated electronOpDTO,
     * or with status 400 (Bad Request) if the electronOpDTO is not valid,
     * or with status 500 (Internal Server Error) if the electronOpDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/electron-ops")
    public ResponseEntity<ElectronOpDTO> updateElectronOp(@RequestBody ElectronOpDTO electronOpDTO) throws URISyntaxException {
        log.debug("REST request to update ElectronOp : {}", electronOpDTO);
        if (electronOpDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ElectronOpDTO result = electronOpService.save(electronOpDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, electronOpDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /electron-ops : get all the electronOps.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of electronOps in body
     */
    @GetMapping("/electron-ops")
    public List<ElectronOpDTO> getAllElectronOps() {
        log.debug("REST request to get all ElectronOps");
        return electronOpService.findAll();
    }

    /**
     * GET  /electron-ops/:id : get the "id" electronOp.
     *
     * @param id the id of the electronOpDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the electronOpDTO, or with status 404 (Not Found)
     */
    @GetMapping("/electron-ops/{id}")
    public ResponseEntity<ElectronOpDTO> getElectronOp(@PathVariable Long id) {
        log.debug("REST request to get ElectronOp : {}", id);
        Optional<ElectronOpDTO> electronOpDTO = electronOpService.findOne(id);
        return ResponseUtil.wrapOrNotFound(electronOpDTO);
    }

    /**
     * DELETE  /electron-ops/:id : delete the "id" electronOp.
     *
     * @param id the id of the electronOpDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/electron-ops/{id}")
    public ResponseEntity<Void> deleteElectronOp(@PathVariable Long id) {
        log.debug("REST request to delete ElectronOp : {}", id);
        electronOpService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
