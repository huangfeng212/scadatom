package io.scadatom.electron.web.rest;

import io.scadatom.electron.ElectronApp;

import io.scadatom.electron.domain.SmmDeviceOp;
import io.scadatom.electron.repository.SmmDeviceOpRepository;
import io.scadatom.electron.service.SmmDeviceOpService;
import io.scadatom.electron.service.mapper.SmmDeviceOpMapper;
import io.scadatom.electron.web.rest.errors.ExceptionTranslator;

import io.scadatom.neutron.OpState;
import io.scadatom.neutron.SmmDeviceOpDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;


import static io.scadatom.electron.web.rest.TestUtil.sameInstant;
import static io.scadatom.electron.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SmmDeviceOpResource REST controller.
 *
 * @see SmmDeviceOpResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElectronApp.class)
public class SmmDeviceOpResourceIntTest {

    private static final OpState DEFAULT_STATE = OpState.Uninitialized;
    private static final OpState UPDATED_STATE = OpState.Initialized;

    private static final ZonedDateTime DEFAULT_DT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private SmmDeviceOpRepository smmDeviceOpRepository;

    @Autowired
    private SmmDeviceOpMapper smmDeviceOpMapper;

    @Autowired
    private SmmDeviceOpService smmDeviceOpService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restSmmDeviceOpMockMvc;

    private SmmDeviceOp smmDeviceOp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SmmDeviceOpResource smmDeviceOpResource = new SmmDeviceOpResource(smmDeviceOpService);
        this.restSmmDeviceOpMockMvc = MockMvcBuilders.standaloneSetup(smmDeviceOpResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmmDeviceOp createEntity(EntityManager em) {
        SmmDeviceOp smmDeviceOp = new SmmDeviceOp()
            .state(DEFAULT_STATE)
            .dt(DEFAULT_DT);
        return smmDeviceOp;
    }

    @Before
    public void initTest() {
        smmDeviceOp = createEntity(em);
    }

    @Test
    @Transactional
    public void createSmmDeviceOp() throws Exception {
        int databaseSizeBeforeCreate = smmDeviceOpRepository.findAll().size();

        // Create the SmmDeviceOp
        SmmDeviceOpDTO smmDeviceOpDTO = smmDeviceOpMapper.toDto(smmDeviceOp);
        restSmmDeviceOpMockMvc.perform(post("/api/smm-device-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmDeviceOpDTO)))
            .andExpect(status().isCreated());

        // Validate the SmmDeviceOp in the database
        List<SmmDeviceOp> smmDeviceOpList = smmDeviceOpRepository.findAll();
        assertThat(smmDeviceOpList).hasSize(databaseSizeBeforeCreate + 1);
        SmmDeviceOp testSmmDeviceOp = smmDeviceOpList.get(smmDeviceOpList.size() - 1);
        assertThat(testSmmDeviceOp.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testSmmDeviceOp.getDt()).isEqualTo(DEFAULT_DT);
    }

    @Test
    @Transactional
    public void createSmmDeviceOpWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smmDeviceOpRepository.findAll().size();

        // Create the SmmDeviceOp with an existing ID
        smmDeviceOp.setId(1L);
        SmmDeviceOpDTO smmDeviceOpDTO = smmDeviceOpMapper.toDto(smmDeviceOp);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmmDeviceOpMockMvc.perform(post("/api/smm-device-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmDeviceOpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmmDeviceOp in the database
        List<SmmDeviceOp> smmDeviceOpList = smmDeviceOpRepository.findAll();
        assertThat(smmDeviceOpList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSmmDeviceOps() throws Exception {
        // Initialize the database
        smmDeviceOpRepository.saveAndFlush(smmDeviceOp);

        // Get all the smmDeviceOpList
        restSmmDeviceOpMockMvc.perform(get("/api/smm-device-ops?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smmDeviceOp.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].dt").value(hasItem(sameInstant(DEFAULT_DT))));
    }
    
    @Test
    @Transactional
    public void getSmmDeviceOp() throws Exception {
        // Initialize the database
        smmDeviceOpRepository.saveAndFlush(smmDeviceOp);

        // Get the smmDeviceOp
        restSmmDeviceOpMockMvc.perform(get("/api/smm-device-ops/{id}", smmDeviceOp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(smmDeviceOp.getId().intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.dt").value(sameInstant(DEFAULT_DT)));
    }

    @Test
    @Transactional
    public void getNonExistingSmmDeviceOp() throws Exception {
        // Get the smmDeviceOp
        restSmmDeviceOpMockMvc.perform(get("/api/smm-device-ops/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSmmDeviceOp() throws Exception {
        // Initialize the database
        smmDeviceOpRepository.saveAndFlush(smmDeviceOp);

        int databaseSizeBeforeUpdate = smmDeviceOpRepository.findAll().size();

        // Update the smmDeviceOp
        SmmDeviceOp updatedSmmDeviceOp = smmDeviceOpRepository.findById(smmDeviceOp.getId()).get();
        // Disconnect from session so that the updates on updatedSmmDeviceOp are not directly saved in db
        em.detach(updatedSmmDeviceOp);
        updatedSmmDeviceOp
            .state(UPDATED_STATE)
            .dt(UPDATED_DT);
        SmmDeviceOpDTO smmDeviceOpDTO = smmDeviceOpMapper.toDto(updatedSmmDeviceOp);

        restSmmDeviceOpMockMvc.perform(put("/api/smm-device-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmDeviceOpDTO)))
            .andExpect(status().isOk());

        // Validate the SmmDeviceOp in the database
        List<SmmDeviceOp> smmDeviceOpList = smmDeviceOpRepository.findAll();
        assertThat(smmDeviceOpList).hasSize(databaseSizeBeforeUpdate);
        SmmDeviceOp testSmmDeviceOp = smmDeviceOpList.get(smmDeviceOpList.size() - 1);
        assertThat(testSmmDeviceOp.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testSmmDeviceOp.getDt()).isEqualTo(UPDATED_DT);
    }

    @Test
    @Transactional
    public void updateNonExistingSmmDeviceOp() throws Exception {
        int databaseSizeBeforeUpdate = smmDeviceOpRepository.findAll().size();

        // Create the SmmDeviceOp
        SmmDeviceOpDTO smmDeviceOpDTO = smmDeviceOpMapper.toDto(smmDeviceOp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmmDeviceOpMockMvc.perform(put("/api/smm-device-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmDeviceOpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmmDeviceOp in the database
        List<SmmDeviceOp> smmDeviceOpList = smmDeviceOpRepository.findAll();
        assertThat(smmDeviceOpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSmmDeviceOp() throws Exception {
        // Initialize the database
        smmDeviceOpRepository.saveAndFlush(smmDeviceOp);

        int databaseSizeBeforeDelete = smmDeviceOpRepository.findAll().size();

        // Delete the smmDeviceOp
        restSmmDeviceOpMockMvc.perform(delete("/api/smm-device-ops/{id}", smmDeviceOp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SmmDeviceOp> smmDeviceOpList = smmDeviceOpRepository.findAll();
        assertThat(smmDeviceOpList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmmDeviceOp.class);
        SmmDeviceOp smmDeviceOp1 = new SmmDeviceOp();
        smmDeviceOp1.setId(1L);
        SmmDeviceOp smmDeviceOp2 = new SmmDeviceOp();
        smmDeviceOp2.setId(smmDeviceOp1.getId());
        assertThat(smmDeviceOp1).isEqualTo(smmDeviceOp2);
        smmDeviceOp2.setId(2L);
        assertThat(smmDeviceOp1).isNotEqualTo(smmDeviceOp2);
        smmDeviceOp1.setId(null);
        assertThat(smmDeviceOp1).isNotEqualTo(smmDeviceOp2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmmDeviceOpDTO.class);
        SmmDeviceOpDTO smmDeviceOpDTO1 = new SmmDeviceOpDTO();
        smmDeviceOpDTO1.setId(1L);
        SmmDeviceOpDTO smmDeviceOpDTO2 = new SmmDeviceOpDTO();
        assertThat(smmDeviceOpDTO1).isNotEqualTo(smmDeviceOpDTO2);
        smmDeviceOpDTO2.setId(smmDeviceOpDTO1.getId());
        assertThat(smmDeviceOpDTO1).isEqualTo(smmDeviceOpDTO2);
        smmDeviceOpDTO2.setId(2L);
        assertThat(smmDeviceOpDTO1).isNotEqualTo(smmDeviceOpDTO2);
        smmDeviceOpDTO1.setId(null);
        assertThat(smmDeviceOpDTO1).isNotEqualTo(smmDeviceOpDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(smmDeviceOpMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(smmDeviceOpMapper.fromId(null)).isNull();
    }
}
