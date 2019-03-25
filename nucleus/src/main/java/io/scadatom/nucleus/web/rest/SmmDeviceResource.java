package io.scadatom.nucleus.web.rest;
import io.scadatom.neutron.SmmDeviceDTO;
import io.scadatom.nucleus.service.SmmDeviceService;
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
 * REST controller for managing SmmDevice.
 */
@RestController
@RequestMapping("/api")
public class SmmDeviceResource {

    private final Logger log = LoggerFactory.getLogger(SmmDeviceResource.class);

    private static final String ENTITY_NAME = "smmDevice";

    private final SmmDeviceService smmDeviceService;

    public SmmDeviceResource(SmmDeviceService smmDeviceService) {
        this.smmDeviceService = smmDeviceService;
    }

    /**
     * POST  /smm-devices : Create a new smmDevice.
     *
     * @param smmDeviceDTO the smmDeviceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new smmDeviceDTO, or with status 400 (Bad Request) if the smmDevice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/smm-devices")
    public ResponseEntity<SmmDeviceDTO> createSmmDevice(@Valid @RequestBody SmmDeviceDTO smmDeviceDTO) throws URISyntaxException {
        log.debug("REST request to save SmmDevice : {}", smmDeviceDTO);
        if (smmDeviceDTO.getId() != null) {
            throw new BadRequestAlertException("A new smmDevice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SmmDeviceDTO result = smmDeviceService.save(smmDeviceDTO);
        return ResponseEntity.created(new URI("/api/smm-devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /smm-devices : Updates an existing smmDevice.
     *
     * @param smmDeviceDTO the smmDeviceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated smmDeviceDTO,
     * or with status 400 (Bad Request) if the smmDeviceDTO is not valid,
     * or with status 500 (Internal Server Error) if the smmDeviceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/smm-devices")
    public ResponseEntity<SmmDeviceDTO> updateSmmDevice(@Valid @RequestBody SmmDeviceDTO smmDeviceDTO) throws URISyntaxException {
        log.debug("REST request to update SmmDevice : {}", smmDeviceDTO);
        if (smmDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SmmDeviceDTO result = smmDeviceService.save(smmDeviceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, smmDeviceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /smm-devices : get all the smmDevices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of smmDevices in body
     */
    @GetMapping("/smm-devices")
    public List<SmmDeviceDTO> getAllSmmDevices() {
        log.debug("REST request to get all SmmDevices");
        return smmDeviceService.findAll();
    }

    /**
     * GET  /smm-devices/:id : get the "id" smmDevice.
     *
     * @param id the id of the smmDeviceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the smmDeviceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/smm-devices/{id}")
    public ResponseEntity<SmmDeviceDTO> getSmmDevice(@PathVariable Long id) {
        log.debug("REST request to get SmmDevice : {}", id);
        Optional<SmmDeviceDTO> smmDeviceDTO = smmDeviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smmDeviceDTO);
    }

    /**
     * DELETE  /smm-devices/:id : delete the "id" smmDevice.
     *
     * @param id the id of the smmDeviceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/smm-devices/{id}")
    public ResponseEntity<Void> deleteSmmDevice(@PathVariable Long id) {
        log.debug("REST request to delete SmmDevice : {}", id);
        smmDeviceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
