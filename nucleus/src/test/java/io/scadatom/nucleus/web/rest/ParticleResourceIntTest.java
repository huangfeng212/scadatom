package io.scadatom.nucleus.web.rest;

import io.scadatom.neutron.ParticleDTO;
import io.scadatom.nucleus.NucleusApp;

import io.scadatom.nucleus.domain.Particle;
import io.scadatom.nucleus.domain.Electron;
import io.scadatom.nucleus.repository.ParticleRepository;
import io.scadatom.nucleus.service.ParticleService;
import io.scadatom.nucleus.service.mapper.ParticleMapper;
import io.scadatom.nucleus.web.rest.errors.ExceptionTranslator;

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
import java.util.List;


import static io.scadatom.nucleus.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ParticleResource REST controller.
 *
 * @see ParticleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NucleusApp.class)
public class ParticleResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DECIMAL_FORMAT = "AAAAAAAAAA";
    private static final String UPDATED_DECIMAL_FORMAT = "BBBBBBBBBB";

    private static final String DEFAULT_INIT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_INIT_VALUE = "BBBBBBBBBB";

    @Autowired
    private ParticleRepository particleRepository;

    @Autowired
    private ParticleMapper particleMapper;

    @Autowired
    private ParticleService particleService;

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

    private MockMvc restParticleMockMvc;

    private Particle particle;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParticleResource particleResource = new ParticleResource(particleService);
        this.restParticleMockMvc = MockMvcBuilders.standaloneSetup(particleResource)
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
    public static Particle createEntity(EntityManager em) {
        Particle particle = new Particle()
            .name(DEFAULT_NAME)
            .decimalFormat(DEFAULT_DECIMAL_FORMAT)
            .initValue(DEFAULT_INIT_VALUE);
        // Add required entity
        Electron electron = ElectronResourceIntTest.createEntity(em);
        em.persist(electron);
        em.flush();
        particle.setElectron(electron);
        return particle;
    }

    @Before
    public void initTest() {
        particle = createEntity(em);
    }

    @Test
    @Transactional
    public void createParticle() throws Exception {
        int databaseSizeBeforeCreate = particleRepository.findAll().size();

        // Create the Particle
        ParticleDTO particleDTO = particleMapper.toDto(particle);
        restParticleMockMvc.perform(post("/api/particles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(particleDTO)))
            .andExpect(status().isCreated());

        // Validate the Particle in the database
        List<Particle> particleList = particleRepository.findAll();
        assertThat(particleList).hasSize(databaseSizeBeforeCreate + 1);
        Particle testParticle = particleList.get(particleList.size() - 1);
        assertThat(testParticle.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testParticle.getDecimalFormat()).isEqualTo(DEFAULT_DECIMAL_FORMAT);
        assertThat(testParticle.getInitValue()).isEqualTo(DEFAULT_INIT_VALUE);
    }

    @Test
    @Transactional
    public void createParticleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = particleRepository.findAll().size();

        // Create the Particle with an existing ID
        particle.setId(1L);
        ParticleDTO particleDTO = particleMapper.toDto(particle);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParticleMockMvc.perform(post("/api/particles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(particleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Particle in the database
        List<Particle> particleList = particleRepository.findAll();
        assertThat(particleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = particleRepository.findAll().size();
        // set the field null
        particle.setName(null);

        // Create the Particle, which fails.
        ParticleDTO particleDTO = particleMapper.toDto(particle);

        restParticleMockMvc.perform(post("/api/particles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(particleDTO)))
            .andExpect(status().isBadRequest());

        List<Particle> particleList = particleRepository.findAll();
        assertThat(particleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllParticles() throws Exception {
        // Initialize the database
        particleRepository.saveAndFlush(particle);

        // Get all the particleList
        restParticleMockMvc.perform(get("/api/particles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(particle.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].decimalFormat").value(hasItem(DEFAULT_DECIMAL_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].initValue").value(hasItem(DEFAULT_INIT_VALUE.toString())));
    }
    
    @Test
    @Transactional
    public void getParticle() throws Exception {
        // Initialize the database
        particleRepository.saveAndFlush(particle);

        // Get the particle
        restParticleMockMvc.perform(get("/api/particles/{id}", particle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(particle.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.decimalFormat").value(DEFAULT_DECIMAL_FORMAT.toString()))
            .andExpect(jsonPath("$.initValue").value(DEFAULT_INIT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingParticle() throws Exception {
        // Get the particle
        restParticleMockMvc.perform(get("/api/particles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParticle() throws Exception {
        // Initialize the database
        particleRepository.saveAndFlush(particle);

        int databaseSizeBeforeUpdate = particleRepository.findAll().size();

        // Update the particle
        Particle updatedParticle = particleRepository.findById(particle.getId()).get();
        // Disconnect from session so that the updates on updatedParticle are not directly saved in db
        em.detach(updatedParticle);
        updatedParticle
            .name(UPDATED_NAME)
            .decimalFormat(UPDATED_DECIMAL_FORMAT)
            .initValue(UPDATED_INIT_VALUE);
        ParticleDTO particleDTO = particleMapper.toDto(updatedParticle);

        restParticleMockMvc.perform(put("/api/particles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(particleDTO)))
            .andExpect(status().isOk());

        // Validate the Particle in the database
        List<Particle> particleList = particleRepository.findAll();
        assertThat(particleList).hasSize(databaseSizeBeforeUpdate);
        Particle testParticle = particleList.get(particleList.size() - 1);
        assertThat(testParticle.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testParticle.getDecimalFormat()).isEqualTo(UPDATED_DECIMAL_FORMAT);
        assertThat(testParticle.getInitValue()).isEqualTo(UPDATED_INIT_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingParticle() throws Exception {
        int databaseSizeBeforeUpdate = particleRepository.findAll().size();

        // Create the Particle
        ParticleDTO particleDTO = particleMapper.toDto(particle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParticleMockMvc.perform(put("/api/particles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(particleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Particle in the database
        List<Particle> particleList = particleRepository.findAll();
        assertThat(particleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteParticle() throws Exception {
        // Initialize the database
        particleRepository.saveAndFlush(particle);

        int databaseSizeBeforeDelete = particleRepository.findAll().size();

        // Delete the particle
        restParticleMockMvc.perform(delete("/api/particles/{id}", particle.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Particle> particleList = particleRepository.findAll();
        assertThat(particleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Particle.class);
        Particle particle1 = new Particle();
        particle1.setId(1L);
        Particle particle2 = new Particle();
        particle2.setId(particle1.getId());
        assertThat(particle1).isEqualTo(particle2);
        particle2.setId(2L);
        assertThat(particle1).isNotEqualTo(particle2);
        particle1.setId(null);
        assertThat(particle1).isNotEqualTo(particle2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParticleDTO.class);
        ParticleDTO particleDTO1 = new ParticleDTO();
        particleDTO1.setId(1L);
        ParticleDTO particleDTO2 = new ParticleDTO();
        assertThat(particleDTO1).isNotEqualTo(particleDTO2);
        particleDTO2.setId(particleDTO1.getId());
        assertThat(particleDTO1).isEqualTo(particleDTO2);
        particleDTO2.setId(2L);
        assertThat(particleDTO1).isNotEqualTo(particleDTO2);
        particleDTO1.setId(null);
        assertThat(particleDTO1).isNotEqualTo(particleDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(particleMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(particleMapper.fromId(null)).isNull();
    }
}
