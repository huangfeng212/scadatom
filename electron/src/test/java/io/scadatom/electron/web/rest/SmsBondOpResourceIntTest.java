package io.scadatom.electron.web.rest;

import io.scadatom.electron.ElectronApp;

import io.scadatom.electron.domain.SmsBondOp;
import io.scadatom.electron.repository.SmsBondOpRepository;
import io.scadatom.electron.service.SmsBondOpService;
import io.scadatom.electron.service.mapper.SmsBondOpMapper;
import io.scadatom.electron.web.rest.errors.ExceptionTranslator;

import io.scadatom.neutron.OpState;
import io.scadatom.neutron.SmsBondOpDTO;
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
 * Test class for the SmsBondOpResource REST controller.
 *
 * @see SmsBondOpResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElectronApp.class)
public class SmsBondOpResourceIntTest {

    private static final OpState DEFAULT_STATE = OpState.Uninitialized;
    private static final OpState UPDATED_STATE = OpState.Initialized;

    private static final ZonedDateTime DEFAULT_DT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_BYTES = "AAAAAAAAAA";
    private static final String UPDATED_BYTES = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_WRITTEN_DT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_WRITTEN_DT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_WRITTEN_BYTES = "AAAAAAAAAA";
    private static final String UPDATED_WRITTEN_BYTES = "BBBBBBBBBB";

    @Autowired
    private SmsBondOpRepository smsBondOpRepository;

    @Autowired
    private SmsBondOpMapper smsBondOpMapper;

    @Autowired
    private SmsBondOpService smsBondOpService;

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

    private MockMvc restSmsBondOpMockMvc;

    private SmsBondOp smsBondOp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SmsBondOpResource smsBondOpResource = new SmsBondOpResource(smsBondOpService);
        this.restSmsBondOpMockMvc = MockMvcBuilders.standaloneSetup(smsBondOpResource)
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
    public static SmsBondOp createEntity(EntityManager em) {
        SmsBondOp smsBondOp = new SmsBondOp()
            .state(DEFAULT_STATE)
            .dt(DEFAULT_DT)
            .bytes(DEFAULT_BYTES)
            .writtenDt(DEFAULT_WRITTEN_DT)
            .writtenBytes(DEFAULT_WRITTEN_BYTES);
        return smsBondOp;
    }

    @Before
    public void initTest() {
        smsBondOp = createEntity(em);
    }

    @Test
    @Transactional
    public void createSmsBondOp() throws Exception {
        int databaseSizeBeforeCreate = smsBondOpRepository.findAll().size();

        // Create the SmsBondOp
        SmsBondOpDTO smsBondOpDTO = smsBondOpMapper.toDto(smsBondOp);
        restSmsBondOpMockMvc.perform(post("/api/sms-bond-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsBondOpDTO)))
            .andExpect(status().isCreated());

        // Validate the SmsBondOp in the database
        List<SmsBondOp> smsBondOpList = smsBondOpRepository.findAll();
        assertThat(smsBondOpList).hasSize(databaseSizeBeforeCreate + 1);
        SmsBondOp testSmsBondOp = smsBondOpList.get(smsBondOpList.size() - 1);
        assertThat(testSmsBondOp.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testSmsBondOp.getDt()).isEqualTo(DEFAULT_DT);
        assertThat(testSmsBondOp.getBytes()).isEqualTo(DEFAULT_BYTES);
        assertThat(testSmsBondOp.getWrittenDt()).isEqualTo(DEFAULT_WRITTEN_DT);
        assertThat(testSmsBondOp.getWrittenBytes()).isEqualTo(DEFAULT_WRITTEN_BYTES);
    }

    @Test
    @Transactional
    public void createSmsBondOpWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smsBondOpRepository.findAll().size();

        // Create the SmsBondOp with an existing ID
        smsBondOp.setId(1L);
        SmsBondOpDTO smsBondOpDTO = smsBondOpMapper.toDto(smsBondOp);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmsBondOpMockMvc.perform(post("/api/sms-bond-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsBondOpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsBondOp in the database
        List<SmsBondOp> smsBondOpList = smsBondOpRepository.findAll();
        assertThat(smsBondOpList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSmsBondOps() throws Exception {
        // Initialize the database
        smsBondOpRepository.saveAndFlush(smsBondOp);

        // Get all the smsBondOpList
        restSmsBondOpMockMvc.perform(get("/api/sms-bond-ops?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smsBondOp.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].dt").value(hasItem(sameInstant(DEFAULT_DT))))
            .andExpect(jsonPath("$.[*].bytes").value(hasItem(DEFAULT_BYTES.toString())))
            .andExpect(jsonPath("$.[*].writtenDt").value(hasItem(sameInstant(DEFAULT_WRITTEN_DT))))
            .andExpect(jsonPath("$.[*].writtenBytes").value(hasItem(DEFAULT_WRITTEN_BYTES.toString())));
    }
    
    @Test
    @Transactional
    public void getSmsBondOp() throws Exception {
        // Initialize the database
        smsBondOpRepository.saveAndFlush(smsBondOp);

        // Get the smsBondOp
        restSmsBondOpMockMvc.perform(get("/api/sms-bond-ops/{id}", smsBondOp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(smsBondOp.getId().intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.dt").value(sameInstant(DEFAULT_DT)))
            .andExpect(jsonPath("$.bytes").value(DEFAULT_BYTES.toString()))
            .andExpect(jsonPath("$.writtenDt").value(sameInstant(DEFAULT_WRITTEN_DT)))
            .andExpect(jsonPath("$.writtenBytes").value(DEFAULT_WRITTEN_BYTES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSmsBondOp() throws Exception {
        // Get the smsBondOp
        restSmsBondOpMockMvc.perform(get("/api/sms-bond-ops/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSmsBondOp() throws Exception {
        // Initialize the database
        smsBondOpRepository.saveAndFlush(smsBondOp);

        int databaseSizeBeforeUpdate = smsBondOpRepository.findAll().size();

        // Update the smsBondOp
        SmsBondOp updatedSmsBondOp = smsBondOpRepository.findById(smsBondOp.getId()).get();
        // Disconnect from session so that the updates on updatedSmsBondOp are not directly saved in db
        em.detach(updatedSmsBondOp);
        updatedSmsBondOp
            .state(UPDATED_STATE)
            .dt(UPDATED_DT)
            .bytes(UPDATED_BYTES)
            .writtenDt(UPDATED_WRITTEN_DT)
            .writtenBytes(UPDATED_WRITTEN_BYTES);
        SmsBondOpDTO smsBondOpDTO = smsBondOpMapper.toDto(updatedSmsBondOp);

        restSmsBondOpMockMvc.perform(put("/api/sms-bond-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsBondOpDTO)))
            .andExpect(status().isOk());

        // Validate the SmsBondOp in the database
        List<SmsBondOp> smsBondOpList = smsBondOpRepository.findAll();
        assertThat(smsBondOpList).hasSize(databaseSizeBeforeUpdate);
        SmsBondOp testSmsBondOp = smsBondOpList.get(smsBondOpList.size() - 1);
        assertThat(testSmsBondOp.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testSmsBondOp.getDt()).isEqualTo(UPDATED_DT);
        assertThat(testSmsBondOp.getBytes()).isEqualTo(UPDATED_BYTES);
        assertThat(testSmsBondOp.getWrittenDt()).isEqualTo(UPDATED_WRITTEN_DT);
        assertThat(testSmsBondOp.getWrittenBytes()).isEqualTo(UPDATED_WRITTEN_BYTES);
    }

    @Test
    @Transactional
    public void updateNonExistingSmsBondOp() throws Exception {
        int databaseSizeBeforeUpdate = smsBondOpRepository.findAll().size();

        // Create the SmsBondOp
        SmsBondOpDTO smsBondOpDTO = smsBondOpMapper.toDto(smsBondOp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsBondOpMockMvc.perform(put("/api/sms-bond-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsBondOpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsBondOp in the database
        List<SmsBondOp> smsBondOpList = smsBondOpRepository.findAll();
        assertThat(smsBondOpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSmsBondOp() throws Exception {
        // Initialize the database
        smsBondOpRepository.saveAndFlush(smsBondOp);

        int databaseSizeBeforeDelete = smsBondOpRepository.findAll().size();

        // Delete the smsBondOp
        restSmsBondOpMockMvc.perform(delete("/api/sms-bond-ops/{id}", smsBondOp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SmsBondOp> smsBondOpList = smsBondOpRepository.findAll();
        assertThat(smsBondOpList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsBondOp.class);
        SmsBondOp smsBondOp1 = new SmsBondOp();
        smsBondOp1.setId(1L);
        SmsBondOp smsBondOp2 = new SmsBondOp();
        smsBondOp2.setId(smsBondOp1.getId());
        assertThat(smsBondOp1).isEqualTo(smsBondOp2);
        smsBondOp2.setId(2L);
        assertThat(smsBondOp1).isNotEqualTo(smsBondOp2);
        smsBondOp1.setId(null);
        assertThat(smsBondOp1).isNotEqualTo(smsBondOp2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsBondOpDTO.class);
        SmsBondOpDTO smsBondOpDTO1 = new SmsBondOpDTO();
        smsBondOpDTO1.setId(1L);
        SmsBondOpDTO smsBondOpDTO2 = new SmsBondOpDTO();
        assertThat(smsBondOpDTO1).isNotEqualTo(smsBondOpDTO2);
        smsBondOpDTO2.setId(smsBondOpDTO1.getId());
        assertThat(smsBondOpDTO1).isEqualTo(smsBondOpDTO2);
        smsBondOpDTO2.setId(2L);
        assertThat(smsBondOpDTO1).isNotEqualTo(smsBondOpDTO2);
        smsBondOpDTO1.setId(null);
        assertThat(smsBondOpDTO1).isNotEqualTo(smsBondOpDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(smsBondOpMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(smsBondOpMapper.fromId(null)).isNull();
    }
}
