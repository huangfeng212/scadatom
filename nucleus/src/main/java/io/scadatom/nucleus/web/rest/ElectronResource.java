package io.scadatom.nucleus.web.rest;
import io.scadatom.neutron.ElectronDTO;
import io.scadatom.nucleus.service.ElectronService;
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
 * REST controller for managing Electron.
 */
@RestController
@RequestMapping("/api")
public class ElectronResource {

    private final Logger log = LoggerFactory.getLogger(ElectronResource.class);

    private static final String ENTITY_NAME = "electron";

    private final ElectronService electronService;

    public ElectronResource(ElectronService electronService) {
        this.electronService = electronService;
    }

    /**
     * POST  /electrons : Create a new electron.
     *
     * @param electronDTO the electronDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new electronDTO, or with status 400 (Bad Request) if the electron has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/electrons")
    public ResponseEntity<ElectronDTO> createElectron(@Valid @RequestBody ElectronDTO electronDTO) throws URISyntaxException {
        log.debug("REST request to save Electron : {}", electronDTO);
        if (electronDTO.getId() != null) {
            throw new BadRequestAlertException("A new electron cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ElectronDTO result = electronService.save(electronDTO);
        return ResponseEntity.created(new URI("/api/electrons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /electrons : Updates an existing electron.
     *
     * @param electronDTO the electronDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated electronDTO,
     * or with status 400 (Bad Request) if the electronDTO is not valid,
     * or with status 500 (Internal Server Error) if the electronDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/electrons")
    public ResponseEntity<ElectronDTO> updateElectron(@Valid @RequestBody ElectronDTO electronDTO) throws URISyntaxException {
        log.debug("REST request to update Electron : {}", electronDTO);
        if (electronDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ElectronDTO result = electronService.save(electronDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, electronDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /electrons : get all the electrons.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of electrons in body
     */
    @GetMapping("/electrons")
    public List<ElectronDTO> getAllElectrons() {
        log.debug("REST request to get all Electrons");
        return electronService.findAll();
    }

    /**
     * GET  /electrons/:id : get the "id" electron.
     *
     * @param id the id of the electronDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the electronDTO, or with status 404 (Not Found)
     */
    @GetMapping("/electrons/{id}")
    public ResponseEntity<ElectronDTO> getElectron(@PathVariable Long id) {
        log.debug("REST request to get Electron : {}", id);
        Optional<ElectronDTO> electronDTO = electronService.findOne(id);
        return ResponseUtil.wrapOrNotFound(electronDTO);
    }

    /**
     * DELETE  /electrons/:id : delete the "id" electron.
     *
     * @param id the id of the electronDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/electrons/{id}")
    public ResponseEntity<Void> deleteElectron(@PathVariable Long id) {
        log.debug("REST request to delete Electron : {}", id);
        electronService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
