package io.scadatom.electron.web.rest;

import io.scadatom.electron.ElectronApp;

import io.scadatom.electron.domain.SmmBondOp;
import io.scadatom.electron.repository.SmmBondOpRepository;
import io.scadatom.electron.service.SmmBondOpService;
import io.scadatom.electron.service.mapper.SmmBondOpMapper;
import io.scadatom.electron.web.rest.errors.ExceptionTranslator;

import io.scadatom.neutron.OpState;
import io.scadatom.neutron.SmmBondOpDTO;
import io.scadatom.neutron.SmmPollStatus;
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
 * Test class for the SmmBondOpResource REST controller.
 *
 * @see SmmBondOpResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElectronApp.class)
public class SmmBondOpResourceIntTest {

    private static final OpState DEFAULT_STATE = OpState.Uninitialized;
    private static final OpState UPDATED_STATE = OpState.Initialized;

    private static final Instant DEFAULT_DT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final SmmPollStatus DEFAULT_POLL_STATUS = SmmPollStatus.NA;
    private static final SmmPollStatus UPDATED_POLL_STATUS = SmmPollStatus.Normal;

    private static final String DEFAULT_READ_REQUEST = "AAAAAAAAAA";
    private static final String UPDATED_READ_REQUEST = "BBBBBBBBBB";

    private static final String DEFAULT_READ_RESPONSE = "AAAAAAAAAA";
    private static final String UPDATED_READ_RESPONSE = "BBBBBBBBBB";

    private static final String DEFAULT_WRITE_REQUEST = "AAAAAAAAAA";
    private static final String UPDATED_WRITE_REQUEST = "BBBBBBBBBB";

    private static final String DEFAULT_WRITE_RESPONSE = "AAAAAAAAAA";
    private static final String UPDATED_WRITE_RESPONSE = "BBBBBBBBBB";

    private static final Instant DEFAULT_WRITE_REQUEST_DT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_WRITE_REQUEST_DT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SmmBondOpRepository smmBondOpRepository;

    @Autowired
    private SmmBondOpMapper smmBondOpMapper;

    @Autowired
    private SmmBondOpService smmBondOpService;

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

    private MockMvc restSmmBondOpMockMvc;

    private SmmBondOp smmBondOp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SmmBondOpResource smmBondOpResource = new SmmBondOpResource(smmBondOpService);
        this.restSmmBondOpMockMvc = MockMvcBuilders.standaloneSetup(smmBondOpResource)
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
    public static SmmBondOp createEntity(EntityManager em) {
        SmmBondOp smmBondOp = new SmmBondOp()
            .state(DEFAULT_STATE)
            .dt(DEFAULT_DT)
            .pollStatus(DEFAULT_POLL_STATUS)
            .readRequest(DEFAULT_READ_REQUEST)
            .readResponse(DEFAULT_READ_RESPONSE)
            .writeRequest(DEFAULT_WRITE_REQUEST)
            .writeResponse(DEFAULT_WRITE_RESPONSE)
            .writeRequestDt(DEFAULT_WRITE_REQUEST_DT);
        return smmBondOp;
    }

    @Before
    public void initTest() {
        smmBondOp = createEntity(em);
    }

    @Test
    @Transactional
    public void createSmmBondOp() throws Exception {
        int databaseSizeBeforeCreate = smmBondOpRepository.findAll().size();

        // Create the SmmBondOp
        SmmBondOpDTO smmBondOpDTO = smmBondOpMapper.toDto(smmBondOp);
        restSmmBondOpMockMvc.perform(post("/api/smm-bond-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmBondOpDTO)))
            .andExpect(status().isCreated());

        // Validate the SmmBondOp in the database
        List<SmmBondOp> smmBondOpList = smmBondOpRepository.findAll();
        assertThat(smmBondOpList).hasSize(databaseSizeBeforeCreate + 1);
        SmmBondOp testSmmBondOp = smmBondOpList.get(smmBondOpList.size() - 1);
        assertThat(testSmmBondOp.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testSmmBondOp.getDt()).isEqualTo(DEFAULT_DT);
        assertThat(testSmmBondOp.getPollStatus()).isEqualTo(DEFAULT_POLL_STATUS);
        assertThat(testSmmBondOp.getReadRequest()).isEqualTo(DEFAULT_READ_REQUEST);
        assertThat(testSmmBondOp.getReadResponse()).isEqualTo(DEFAULT_READ_RESPONSE);
        assertThat(testSmmBondOp.getWriteRequest()).isEqualTo(DEFAULT_WRITE_REQUEST);
        assertThat(testSmmBondOp.getWriteResponse()).isEqualTo(DEFAULT_WRITE_RESPONSE);
        assertThat(testSmmBondOp.getWriteRequestDt()).isEqualTo(DEFAULT_WRITE_REQUEST_DT);
    }

    @Test
    @Transactional
    public void createSmmBondOpWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smmBondOpRepository.findAll().size();

        // Create the SmmBondOp with an existing ID
        smmBondOp.setId(1L);
        SmmBondOpDTO smmBondOpDTO = smmBondOpMapper.toDto(smmBondOp);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmmBondOpMockMvc.perform(post("/api/smm-bond-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmBondOpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmmBondOp in the database
        List<SmmBondOp> smmBondOpList = smmBondOpRepository.findAll();
        assertThat(smmBondOpList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSmmBondOps() throws Exception {
        // Initialize the database
        smmBondOpRepository.saveAndFlush(smmBondOp);

        // Get all the smmBondOpList
        restSmmBondOpMockMvc.perform(get("/api/smm-bond-ops?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smmBondOp.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].dt").value(hasItem(DEFAULT_DT.toString())))
            .andExpect(jsonPath("$.[*].pollStatus").value(hasItem(DEFAULT_POLL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].readRequest").value(hasItem(DEFAULT_READ_REQUEST.toString())))
            .andExpect(jsonPath("$.[*].readResponse").value(hasItem(DEFAULT_READ_RESPONSE.toString())))
            .andExpect(jsonPath("$.[*].writeRequest").value(hasItem(DEFAULT_WRITE_REQUEST.toString())))
            .andExpect(jsonPath("$.[*].writeResponse").value(hasItem(DEFAULT_WRITE_RESPONSE.toString())))
            .andExpect(jsonPath("$.[*].writeRequestDt").value(hasItem(DEFAULT_WRITE_REQUEST_DT.toString())));
    }
    
    @Test
    @Transactional
    public void getSmmBondOp() throws Exception {
        // Initialize the database
        smmBondOpRepository.saveAndFlush(smmBondOp);

        // Get the smmBondOp
        restSmmBondOpMockMvc.perform(get("/api/smm-bond-ops/{id}", smmBondOp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(smmBondOp.getId().intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.dt").value(DEFAULT_DT.toString()))
            .andExpect(jsonPath("$.pollStatus").value(DEFAULT_POLL_STATUS.toString()))
            .andExpect(jsonPath("$.readRequest").value(DEFAULT_READ_REQUEST.toString()))
            .andExpect(jsonPath("$.readResponse").value(DEFAULT_READ_RESPONSE.toString()))
            .andExpect(jsonPath("$.writeRequest").value(DEFAULT_WRITE_REQUEST.toString()))
            .andExpect(jsonPath("$.writeResponse").value(DEFAULT_WRITE_RESPONSE.toString()))
            .andExpect(jsonPath("$.writeRequestDt").value(DEFAULT_WRITE_REQUEST_DT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSmmBondOp() throws Exception {
        // Get the smmBondOp
        restSmmBondOpMockMvc.perform(get("/api/smm-bond-ops/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSmmBondOp() throws Exception {
        // Initialize the database
        smmBondOpRepository.saveAndFlush(smmBondOp);

        int databaseSizeBeforeUpdate = smmBondOpRepository.findAll().size();

        // Update the smmBondOp
        SmmBondOp updatedSmmBondOp = smmBondOpRepository.findById(smmBondOp.getId()).get();
        // Disconnect from session so that the updates on updatedSmmBondOp are not directly saved in db
        em.detach(updatedSmmBondOp);
        updatedSmmBondOp
            .state(UPDATED_STATE)
            .dt(UPDATED_DT)
            .pollStatus(UPDATED_POLL_STATUS)
            .readRequest(UPDATED_READ_REQUEST)
            .readResponse(UPDATED_READ_RESPONSE)
            .writeRequest(UPDATED_WRITE_REQUEST)
            .writeResponse(UPDATED_WRITE_RESPONSE)
            .writeRequestDt(UPDATED_WRITE_REQUEST_DT);
        SmmBondOpDTO smmBondOpDTO = smmBondOpMapper.toDto(updatedSmmBondOp);

        restSmmBondOpMockMvc.perform(put("/api/smm-bond-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmBondOpDTO)))
            .andExpect(status().isOk());

        // Validate the SmmBondOp in the database
        List<SmmBondOp> smmBondOpList = smmBondOpRepository.findAll();
        assertThat(smmBondOpList).hasSize(databaseSizeBeforeUpdate);
        SmmBondOp testSmmBondOp = smmBondOpList.get(smmBondOpList.size() - 1);
        assertThat(testSmmBondOp.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testSmmBondOp.getDt()).isEqualTo(UPDATED_DT);
        assertThat(testSmmBondOp.getPollStatus()).isEqualTo(UPDATED_POLL_STATUS);
        assertThat(testSmmBondOp.getReadRequest()).isEqualTo(UPDATED_READ_REQUEST);
        assertThat(testSmmBondOp.getReadResponse()).isEqualTo(UPDATED_READ_RESPONSE);
        assertThat(testSmmBondOp.getWriteRequest()).isEqualTo(UPDATED_WRITE_REQUEST);
        assertThat(testSmmBondOp.getWriteResponse()).isEqualTo(UPDATED_WRITE_RESPONSE);
        assertThat(testSmmBondOp.getWriteRequestDt()).isEqualTo(UPDATED_WRITE_REQUEST_DT);
    }

    @Test
    @Transactional
    public void updateNonExistingSmmBondOp() throws Exception {
        int databaseSizeBeforeUpdate = smmBondOpRepository.findAll().size();

        // Create the SmmBondOp
        SmmBondOpDTO smmBondOpDTO = smmBondOpMapper.toDto(smmBondOp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmmBondOpMockMvc.perform(put("/api/smm-bond-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmBondOpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmmBondOp in the database
        List<SmmBondOp> smmBondOpList = smmBondOpRepository.findAll();
        assertThat(smmBondOpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSmmBondOp() throws Exception {
        // Initialize the database
        smmBondOpRepository.saveAndFlush(smmBondOp);

        int databaseSizeBeforeDelete = smmBondOpRepository.findAll().size();

        // Delete the smmBondOp
        restSmmBondOpMockMvc.perform(delete("/api/smm-bond-ops/{id}", smmBondOp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SmmBondOp> smmBondOpList = smmBondOpRepository.findAll();
        assertThat(smmBondOpList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmmBondOp.class);
        SmmBondOp smmBondOp1 = new SmmBondOp();
        smmBondOp1.setId(1L);
        SmmBondOp smmBondOp2 = new SmmBondOp();
        smmBondOp2.setId(smmBondOp1.getId());
        assertThat(smmBondOp1).isEqualTo(smmBondOp2);
        smmBondOp2.setId(2L);
        assertThat(smmBondOp1).isNotEqualTo(smmBondOp2);
        smmBondOp1.setId(null);
        assertThat(smmBondOp1).isNotEqualTo(smmBondOp2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmmBondOpDTO.class);
        SmmBondOpDTO smmBondOpDTO1 = new SmmBondOpDTO();
        smmBondOpDTO1.setId(1L);
        SmmBondOpDTO smmBondOpDTO2 = new SmmBondOpDTO();
        assertThat(smmBondOpDTO1).isNotEqualTo(smmBondOpDTO2);
        smmBondOpDTO2.setId(smmBondOpDTO1.getId());
        assertThat(smmBondOpDTO1).isEqualTo(smmBondOpDTO2);
        smmBondOpDTO2.setId(2L);
        assertThat(smmBondOpDTO1).isNotEqualTo(smmBondOpDTO2);
        smmBondOpDTO1.setId(null);
        assertThat(smmBondOpDTO1).isNotEqualTo(smmBondOpDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(smmBondOpMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(smmBondOpMapper.fromId(null)).isNull();
    }
}
