package io.scadatom.electron.web.rest;

import io.scadatom.electron.ElectronApp;

import io.scadatom.electron.domain.ParticleOp;
import io.scadatom.electron.repository.ParticleOpRepository;
import io.scadatom.electron.service.ParticleOpService;
import io.scadatom.electron.service.mapper.ParticleOpMapper;
import io.scadatom.electron.web.rest.errors.ExceptionTranslator;

import io.scadatom.neutron.OpState;
import io.scadatom.neutron.ParticleOpDTO;
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
 * Test class for the ParticleOpResource REST controller.
 *
 * @see ParticleOpResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElectronApp.class)
public class ParticleOpResourceIntTest {

    private static final OpState DEFAULT_STATE = OpState.Uninitialized;
    private static final OpState UPDATED_STATE = OpState.Initialized;

    private static final Instant DEFAULT_DT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_WRITTEN_BY = "AAAAAAAAAA";
    private static final String UPDATED_WRITTEN_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_WRITTEN_DT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_WRITTEN_DT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ParticleOpRepository particleOpRepository;

    @Autowired
    private ParticleOpMapper particleOpMapper;

    @Autowired
    private ParticleOpService particleOpService;

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

    private MockMvc restParticleOpMockMvc;

    private ParticleOp particleOp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParticleOpResource particleOpResource = new ParticleOpResource(particleOpService);
        this.restParticleOpMockMvc = MockMvcBuilders.standaloneSetup(particleOpResource)
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
    public static ParticleOp createEntity(EntityManager em) {
        ParticleOp particleOp = new ParticleOp()
            .state(DEFAULT_STATE)
            .dt(DEFAULT_DT)
            .value(DEFAULT_VALUE)
            .writtenBy(DEFAULT_WRITTEN_BY)
            .writtenDt(DEFAULT_WRITTEN_DT);
        return particleOp;
    }

    @Before
    public void initTest() {
        particleOp = createEntity(em);
    }

    @Test
    @Transactional
    public void createParticleOp() throws Exception {
        int databaseSizeBeforeCreate = particleOpRepository.findAll().size();

        // Create the ParticleOp
        ParticleOpDTO particleOpDTO = particleOpMapper.toDto(particleOp);
        restParticleOpMockMvc.perform(post("/api/particle-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(particleOpDTO)))
            .andExpect(status().isCreated());

        // Validate the ParticleOp in the database
        List<ParticleOp> particleOpList = particleOpRepository.findAll();
        assertThat(particleOpList).hasSize(databaseSizeBeforeCreate + 1);
        ParticleOp testParticleOp = particleOpList.get(particleOpList.size() - 1);
        assertThat(testParticleOp.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testParticleOp.getDt()).isEqualTo(DEFAULT_DT);
        assertThat(testParticleOp.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testParticleOp.getWrittenBy()).isEqualTo(DEFAULT_WRITTEN_BY);
        assertThat(testParticleOp.getWrittenDt()).isEqualTo(DEFAULT_WRITTEN_DT);
    }

    @Test
    @Transactional
    public void createParticleOpWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = particleOpRepository.findAll().size();

        // Create the ParticleOp with an existing ID
        particleOp.setId(1L);
        ParticleOpDTO particleOpDTO = particleOpMapper.toDto(particleOp);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParticleOpMockMvc.perform(post("/api/particle-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(particleOpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ParticleOp in the database
        List<ParticleOp> particleOpList = particleOpRepository.findAll();
        assertThat(particleOpList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllParticleOps() throws Exception {
        // Initialize the database
        particleOpRepository.saveAndFlush(particleOp);

        // Get all the particleOpList
        restParticleOpMockMvc.perform(get("/api/particle-ops?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(particleOp.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].dt").value(hasItem(DEFAULT_DT.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].writtenBy").value(hasItem(DEFAULT_WRITTEN_BY.toString())))
            .andExpect(jsonPath("$.[*].writtenDt").value(hasItem(DEFAULT_WRITTEN_DT.toString())));
    }
    
    @Test
    @Transactional
    public void getParticleOp() throws Exception {
        // Initialize the database
        particleOpRepository.saveAndFlush(particleOp);

        // Get the particleOp
        restParticleOpMockMvc.perform(get("/api/particle-ops/{id}", particleOp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(particleOp.getId().intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.dt").value(DEFAULT_DT.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.writtenBy").value(DEFAULT_WRITTEN_BY.toString()))
            .andExpect(jsonPath("$.writtenDt").value(DEFAULT_WRITTEN_DT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParticleOp() throws Exception {
        // Get the particleOp
        restParticleOpMockMvc.perform(get("/api/particle-ops/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParticleOp() throws Exception {
        // Initialize the database
        particleOpRepository.saveAndFlush(particleOp);

        int databaseSizeBeforeUpdate = particleOpRepository.findAll().size();

        // Update the particleOp
        ParticleOp updatedParticleOp = particleOpRepository.findById(particleOp.getId()).get();
        // Disconnect from session so that the updates on updatedParticleOp are not directly saved in db
        em.detach(updatedParticleOp);
        updatedParticleOp
            .state(UPDATED_STATE)
            .dt(UPDATED_DT)
            .value(UPDATED_VALUE)
            .writtenBy(UPDATED_WRITTEN_BY)
            .writtenDt(UPDATED_WRITTEN_DT);
        ParticleOpDTO particleOpDTO = particleOpMapper.toDto(updatedParticleOp);

        restParticleOpMockMvc.perform(put("/api/particle-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(particleOpDTO)))
            .andExpect(status().isOk());

        // Validate the ParticleOp in the database
        List<ParticleOp> particleOpList = particleOpRepository.findAll();
        assertThat(particleOpList).hasSize(databaseSizeBeforeUpdate);
        ParticleOp testParticleOp = particleOpList.get(particleOpList.size() - 1);
        assertThat(testParticleOp.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testParticleOp.getDt()).isEqualTo(UPDATED_DT);
        assertThat(testParticleOp.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testParticleOp.getWrittenBy()).isEqualTo(UPDATED_WRITTEN_BY);
        assertThat(testParticleOp.getWrittenDt()).isEqualTo(UPDATED_WRITTEN_DT);
    }

    @Test
    @Transactional
    public void updateNonExistingParticleOp() throws Exception {
        int databaseSizeBeforeUpdate = particleOpRepository.findAll().size();

        // Create the ParticleOp
        ParticleOpDTO particleOpDTO = particleOpMapper.toDto(particleOp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParticleOpMockMvc.perform(put("/api/particle-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(particleOpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ParticleOp in the database
        List<ParticleOp> particleOpList = particleOpRepository.findAll();
        assertThat(particleOpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteParticleOp() throws Exception {
        // Initialize the database
        particleOpRepository.saveAndFlush(particleOp);

        int databaseSizeBeforeDelete = particleOpRepository.findAll().size();

        // Delete the particleOp
        restParticleOpMockMvc.perform(delete("/api/particle-ops/{id}", particleOp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ParticleOp> particleOpList = particleOpRepository.findAll();
        assertThat(particleOpList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParticleOp.class);
        ParticleOp particleOp1 = new ParticleOp();
        particleOp1.setId(1L);
        ParticleOp particleOp2 = new ParticleOp();
        particleOp2.setId(particleOp1.getId());
        assertThat(particleOp1).isEqualTo(particleOp2);
        particleOp2.setId(2L);
        assertThat(particleOp1).isNotEqualTo(particleOp2);
        particleOp1.setId(null);
        assertThat(particleOp1).isNotEqualTo(particleOp2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParticleOpDTO.class);
        ParticleOpDTO particleOpDTO1 = new ParticleOpDTO();
        particleOpDTO1.setId(1L);
        ParticleOpDTO particleOpDTO2 = new ParticleOpDTO();
        assertThat(particleOpDTO1).isNotEqualTo(particleOpDTO2);
        particleOpDTO2.setId(particleOpDTO1.getId());
        assertThat(particleOpDTO1).isEqualTo(particleOpDTO2);
        particleOpDTO2.setId(2L);
        assertThat(particleOpDTO1).isNotEqualTo(particleOpDTO2);
        particleOpDTO1.setId(null);
        assertThat(particleOpDTO1).isNotEqualTo(particleOpDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(particleOpMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(particleOpMapper.fromId(null)).isNull();
    }
}
