package io.scadatom.electron.web.rest;

import io.scadatom.electron.ElectronApp;

import io.scadatom.electron.domain.SmmChargerOp;
import io.scadatom.electron.repository.SmmChargerOpRepository;
import io.scadatom.electron.service.SmmChargerOpService;
import io.scadatom.electron.service.mapper.SmmChargerOpMapper;
import io.scadatom.electron.web.rest.errors.ExceptionTranslator;

import io.scadatom.neutron.OpState;
import io.scadatom.neutron.SmmChargerOpDTO;
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
import java.time.temporal.ChronoUnit;
import java.util.List;


import static io.scadatom.electron.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SmmChargerOpResource REST controller.
 *
 * @see SmmChargerOpResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElectronApp.class)
public class SmmChargerOpResourceIntTest {

    private static final OpState DEFAULT_STATE = OpState.Uninitialized;
    private static final OpState UPDATED_STATE = OpState.Initialized;

    private static final Instant DEFAULT_DT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SmmChargerOpRepository smmChargerOpRepository;

    @Autowired
    private SmmChargerOpMapper smmChargerOpMapper;

    @Autowired
    private SmmChargerOpService smmChargerOpService;

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

    private MockMvc restSmmChargerOpMockMvc;

    private SmmChargerOp smmChargerOp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SmmChargerOpResource smmChargerOpResource = new SmmChargerOpResource(smmChargerOpService);
        this.restSmmChargerOpMockMvc = MockMvcBuilders.standaloneSetup(smmChargerOpResource)
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
    public static SmmChargerOp createEntity(EntityManager em) {
        SmmChargerOp smmChargerOp = new SmmChargerOp()
            .state(DEFAULT_STATE)
            .dt(DEFAULT_DT);
        return smmChargerOp;
    }

    @Before
    public void initTest() {
        smmChargerOp = createEntity(em);
    }

    @Test
    @Transactional
    public void createSmmChargerOp() throws Exception {
        int databaseSizeBeforeCreate = smmChargerOpRepository.findAll().size();

        // Create the SmmChargerOp
        SmmChargerOpDTO smmChargerOpDTO = smmChargerOpMapper.toDto(smmChargerOp);
        restSmmChargerOpMockMvc.perform(post("/api/smm-charger-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmChargerOpDTO)))
            .andExpect(status().isCreated());

        // Validate the SmmChargerOp in the database
        List<SmmChargerOp> smmChargerOpList = smmChargerOpRepository.findAll();
        assertThat(smmChargerOpList).hasSize(databaseSizeBeforeCreate + 1);
        SmmChargerOp testSmmChargerOp = smmChargerOpList.get(smmChargerOpList.size() - 1);
        assertThat(testSmmChargerOp.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testSmmChargerOp.getDt()).isEqualTo(DEFAULT_DT);
    }

    @Test
    @Transactional
    public void createSmmChargerOpWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smmChargerOpRepository.findAll().size();

        // Create the SmmChargerOp with an existing ID
        smmChargerOp.setId(1L);
        SmmChargerOpDTO smmChargerOpDTO = smmChargerOpMapper.toDto(smmChargerOp);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmmChargerOpMockMvc.perform(post("/api/smm-charger-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmChargerOpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmmChargerOp in the database
        List<SmmChargerOp> smmChargerOpList = smmChargerOpRepository.findAll();
        assertThat(smmChargerOpList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSmmChargerOps() throws Exception {
        // Initialize the database
        smmChargerOpRepository.saveAndFlush(smmChargerOp);

        // Get all the smmChargerOpList
        restSmmChargerOpMockMvc.perform(get("/api/smm-charger-ops?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smmChargerOp.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].dt").value(hasItem(DEFAULT_DT.toString())));
    }
    
    @Test
    @Transactional
    public void getSmmChargerOp() throws Exception {
        // Initialize the database
        smmChargerOpRepository.saveAndFlush(smmChargerOp);

        // Get the smmChargerOp
        restSmmChargerOpMockMvc.perform(get("/api/smm-charger-ops/{id}", smmChargerOp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(smmChargerOp.getId().intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.dt").value(DEFAULT_DT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSmmChargerOp() throws Exception {
        // Get the smmChargerOp
        restSmmChargerOpMockMvc.perform(get("/api/smm-charger-ops/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSmmChargerOp() throws Exception {
        // Initialize the database
        smmChargerOpRepository.saveAndFlush(smmChargerOp);

        int databaseSizeBeforeUpdate = smmChargerOpRepository.findAll().size();

        // Update the smmChargerOp
        SmmChargerOp updatedSmmChargerOp = smmChargerOpRepository.findById(smmChargerOp.getId()).get();
        // Disconnect from session so that the updates on updatedSmmChargerOp are not directly saved in db
        em.detach(updatedSmmChargerOp);
        updatedSmmChargerOp
            .state(UPDATED_STATE)
            .dt(UPDATED_DT);
        SmmChargerOpDTO smmChargerOpDTO = smmChargerOpMapper.toDto(updatedSmmChargerOp);

        restSmmChargerOpMockMvc.perform(put("/api/smm-charger-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmChargerOpDTO)))
            .andExpect(status().isOk());

        // Validate the SmmChargerOp in the database
        List<SmmChargerOp> smmChargerOpList = smmChargerOpRepository.findAll();
        assertThat(smmChargerOpList).hasSize(databaseSizeBeforeUpdate);
        SmmChargerOp testSmmChargerOp = smmChargerOpList.get(smmChargerOpList.size() - 1);
        assertThat(testSmmChargerOp.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testSmmChargerOp.getDt()).isEqualTo(UPDATED_DT);
    }

    @Test
    @Transactional
    public void updateNonExistingSmmChargerOp() throws Exception {
        int databaseSizeBeforeUpdate = smmChargerOpRepository.findAll().size();

        // Create the SmmChargerOp
        SmmChargerOpDTO smmChargerOpDTO = smmChargerOpMapper.toDto(smmChargerOp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmmChargerOpMockMvc.perform(put("/api/smm-charger-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmChargerOpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmmChargerOp in the database
        List<SmmChargerOp> smmChargerOpList = smmChargerOpRepository.findAll();
        assertThat(smmChargerOpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSmmChargerOp() throws Exception {
        // Initialize the database
        smmChargerOpRepository.saveAndFlush(smmChargerOp);

        int databaseSizeBeforeDelete = smmChargerOpRepository.findAll().size();

        // Delete the smmChargerOp
        restSmmChargerOpMockMvc.perform(delete("/api/smm-charger-ops/{id}", smmChargerOp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SmmChargerOp> smmChargerOpList = smmChargerOpRepository.findAll();
        assertThat(smmChargerOpList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmmChargerOp.class);
        SmmChargerOp smmChargerOp1 = new SmmChargerOp();
        smmChargerOp1.setId(1L);
        SmmChargerOp smmChargerOp2 = new SmmChargerOp();
        smmChargerOp2.setId(smmChargerOp1.getId());
        assertThat(smmChargerOp1).isEqualTo(smmChargerOp2);
        smmChargerOp2.setId(2L);
        assertThat(smmChargerOp1).isNotEqualTo(smmChargerOp2);
        smmChargerOp1.setId(null);
        assertThat(smmChargerOp1).isNotEqualTo(smmChargerOp2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmmChargerOpDTO.class);
        SmmChargerOpDTO smmChargerOpDTO1 = new SmmChargerOpDTO();
        smmChargerOpDTO1.setId(1L);
        SmmChargerOpDTO smmChargerOpDTO2 = new SmmChargerOpDTO();
        assertThat(smmChargerOpDTO1).isNotEqualTo(smmChargerOpDTO2);
        smmChargerOpDTO2.setId(smmChargerOpDTO1.getId());
        assertThat(smmChargerOpDTO1).isEqualTo(smmChargerOpDTO2);
        smmChargerOpDTO2.setId(2L);
        assertThat(smmChargerOpDTO1).isNotEqualTo(smmChargerOpDTO2);
        smmChargerOpDTO1.setId(null);
        assertThat(smmChargerOpDTO1).isNotEqualTo(smmChargerOpDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(smmChargerOpMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(smmChargerOpMapper.fromId(null)).isNull();
    }
}
