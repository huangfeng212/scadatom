package io.scadatom.nucleus.web.rest;

import io.scadatom.neutron.Parity;
import io.scadatom.neutron.SmmChargerDTO;
import io.scadatom.neutron.Stopbit;
import io.scadatom.nucleus.NucleusApp;

import io.scadatom.nucleus.domain.SmmCharger;
import io.scadatom.nucleus.repository.SmmChargerRepository;
import io.scadatom.nucleus.service.SmmChargerService;
import io.scadatom.nucleus.service.mapper.SmmChargerMapper;
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
 * Test class for the SmmChargerResource REST controller.
 *
 * @see SmmChargerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NucleusApp.class)
public class SmmChargerResourceIntTest {

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String DEFAULT_PORT = "AAAAAAAAAA";
    private static final String UPDATED_PORT = "BBBBBBBBBB";

    private static final Integer DEFAULT_BAUD = 0;
    private static final Integer UPDATED_BAUD = 1;

    private static final Integer DEFAULT_DATABIT = 0;
    private static final Integer UPDATED_DATABIT = 1;

    private static final Parity DEFAULT_PARITY = Parity.None;
    private static final Parity UPDATED_PARITY = Parity.Odd;

    private static final Stopbit DEFAULT_STOPBIT = Stopbit.NA;
    private static final Stopbit UPDATED_STOPBIT = Stopbit.One;

    private static final Integer DEFAULT_TIMEOUT = 0;
    private static final Integer UPDATED_TIMEOUT = 1;

    private static final Integer DEFAULT_RETRY = 0;
    private static final Integer UPDATED_RETRY = 1;

    private static final Integer DEFAULT_TRANS_DELAY = 0;
    private static final Integer UPDATED_TRANS_DELAY = 1;

    private static final Integer DEFAULT_BATCH_DELAY = 0;
    private static final Integer UPDATED_BATCH_DELAY = 1;

    @Autowired
    private SmmChargerRepository smmChargerRepository;

    @Autowired
    private SmmChargerMapper smmChargerMapper;

    @Autowired
    private SmmChargerService smmChargerService;

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

    private MockMvc restSmmChargerMockMvc;

    private SmmCharger smmCharger;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SmmChargerResource smmChargerResource = new SmmChargerResource(smmChargerService);
        this.restSmmChargerMockMvc = MockMvcBuilders.standaloneSetup(smmChargerResource)
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
    public static SmmCharger createEntity(EntityManager em) {
        SmmCharger smmCharger = new SmmCharger()
            .enabled(DEFAULT_ENABLED)
            .port(DEFAULT_PORT)
            .baud(DEFAULT_BAUD)
            .databit(DEFAULT_DATABIT)
            .parity(DEFAULT_PARITY)
            .stopbit(DEFAULT_STOPBIT)
            .timeout(DEFAULT_TIMEOUT)
            .retry(DEFAULT_RETRY)
            .transDelay(DEFAULT_TRANS_DELAY)
            .batchDelay(DEFAULT_BATCH_DELAY);
        return smmCharger;
    }

    @Before
    public void initTest() {
        smmCharger = createEntity(em);
    }

    @Test
    @Transactional
    public void createSmmCharger() throws Exception {
        int databaseSizeBeforeCreate = smmChargerRepository.findAll().size();

        // Create the SmmCharger
        SmmChargerDTO smmChargerDTO = smmChargerMapper.toDto(smmCharger);
        restSmmChargerMockMvc.perform(post("/api/smm-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmChargerDTO)))
            .andExpect(status().isCreated());

        // Validate the SmmCharger in the database
        List<SmmCharger> smmChargerList = smmChargerRepository.findAll();
        assertThat(smmChargerList).hasSize(databaseSizeBeforeCreate + 1);
        SmmCharger testSmmCharger = smmChargerList.get(smmChargerList.size() - 1);
        assertThat(testSmmCharger.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testSmmCharger.getPort()).isEqualTo(DEFAULT_PORT);
        assertThat(testSmmCharger.getBaud()).isEqualTo(DEFAULT_BAUD);
        assertThat(testSmmCharger.getDatabit()).isEqualTo(DEFAULT_DATABIT);
        assertThat(testSmmCharger.getParity()).isEqualTo(DEFAULT_PARITY);
        assertThat(testSmmCharger.getStopbit()).isEqualTo(DEFAULT_STOPBIT);
        assertThat(testSmmCharger.getTimeout()).isEqualTo(DEFAULT_TIMEOUT);
        assertThat(testSmmCharger.getRetry()).isEqualTo(DEFAULT_RETRY);
        assertThat(testSmmCharger.getTransDelay()).isEqualTo(DEFAULT_TRANS_DELAY);
        assertThat(testSmmCharger.getBatchDelay()).isEqualTo(DEFAULT_BATCH_DELAY);
    }

    @Test
    @Transactional
    public void createSmmChargerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smmChargerRepository.findAll().size();

        // Create the SmmCharger with an existing ID
        smmCharger.setId(1L);
        SmmChargerDTO smmChargerDTO = smmChargerMapper.toDto(smmCharger);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmmChargerMockMvc.perform(post("/api/smm-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmChargerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmmCharger in the database
        List<SmmCharger> smmChargerList = smmChargerRepository.findAll();
        assertThat(smmChargerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = smmChargerRepository.findAll().size();
        // set the field null
        smmCharger.setEnabled(null);

        // Create the SmmCharger, which fails.
        SmmChargerDTO smmChargerDTO = smmChargerMapper.toDto(smmCharger);

        restSmmChargerMockMvc.perform(post("/api/smm-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmChargerDTO)))
            .andExpect(status().isBadRequest());

        List<SmmCharger> smmChargerList = smmChargerRepository.findAll();
        assertThat(smmChargerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPortIsRequired() throws Exception {
        int databaseSizeBeforeTest = smmChargerRepository.findAll().size();
        // set the field null
        smmCharger.setPort(null);

        // Create the SmmCharger, which fails.
        SmmChargerDTO smmChargerDTO = smmChargerMapper.toDto(smmCharger);

        restSmmChargerMockMvc.perform(post("/api/smm-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmChargerDTO)))
            .andExpect(status().isBadRequest());

        List<SmmCharger> smmChargerList = smmChargerRepository.findAll();
        assertThat(smmChargerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBaudIsRequired() throws Exception {
        int databaseSizeBeforeTest = smmChargerRepository.findAll().size();
        // set the field null
        smmCharger.setBaud(null);

        // Create the SmmCharger, which fails.
        SmmChargerDTO smmChargerDTO = smmChargerMapper.toDto(smmCharger);

        restSmmChargerMockMvc.perform(post("/api/smm-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmChargerDTO)))
            .andExpect(status().isBadRequest());

        List<SmmCharger> smmChargerList = smmChargerRepository.findAll();
        assertThat(smmChargerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDatabitIsRequired() throws Exception {
        int databaseSizeBeforeTest = smmChargerRepository.findAll().size();
        // set the field null
        smmCharger.setDatabit(null);

        // Create the SmmCharger, which fails.
        SmmChargerDTO smmChargerDTO = smmChargerMapper.toDto(smmCharger);

        restSmmChargerMockMvc.perform(post("/api/smm-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmChargerDTO)))
            .andExpect(status().isBadRequest());

        List<SmmCharger> smmChargerList = smmChargerRepository.findAll();
        assertThat(smmChargerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkParityIsRequired() throws Exception {
        int databaseSizeBeforeTest = smmChargerRepository.findAll().size();
        // set the field null
        smmCharger.setParity(null);

        // Create the SmmCharger, which fails.
        SmmChargerDTO smmChargerDTO = smmChargerMapper.toDto(smmCharger);

        restSmmChargerMockMvc.perform(post("/api/smm-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmChargerDTO)))
            .andExpect(status().isBadRequest());

        List<SmmCharger> smmChargerList = smmChargerRepository.findAll();
        assertThat(smmChargerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStopbitIsRequired() throws Exception {
        int databaseSizeBeforeTest = smmChargerRepository.findAll().size();
        // set the field null
        smmCharger.setStopbit(null);

        // Create the SmmCharger, which fails.
        SmmChargerDTO smmChargerDTO = smmChargerMapper.toDto(smmCharger);

        restSmmChargerMockMvc.perform(post("/api/smm-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmChargerDTO)))
            .andExpect(status().isBadRequest());

        List<SmmCharger> smmChargerList = smmChargerRepository.findAll();
        assertThat(smmChargerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimeoutIsRequired() throws Exception {
        int databaseSizeBeforeTest = smmChargerRepository.findAll().size();
        // set the field null
        smmCharger.setTimeout(null);

        // Create the SmmCharger, which fails.
        SmmChargerDTO smmChargerDTO = smmChargerMapper.toDto(smmCharger);

        restSmmChargerMockMvc.perform(post("/api/smm-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmChargerDTO)))
            .andExpect(status().isBadRequest());

        List<SmmCharger> smmChargerList = smmChargerRepository.findAll();
        assertThat(smmChargerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRetryIsRequired() throws Exception {
        int databaseSizeBeforeTest = smmChargerRepository.findAll().size();
        // set the field null
        smmCharger.setRetry(null);

        // Create the SmmCharger, which fails.
        SmmChargerDTO smmChargerDTO = smmChargerMapper.toDto(smmCharger);

        restSmmChargerMockMvc.perform(post("/api/smm-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmChargerDTO)))
            .andExpect(status().isBadRequest());

        List<SmmCharger> smmChargerList = smmChargerRepository.findAll();
        assertThat(smmChargerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransDelayIsRequired() throws Exception {
        int databaseSizeBeforeTest = smmChargerRepository.findAll().size();
        // set the field null
        smmCharger.setTransDelay(null);

        // Create the SmmCharger, which fails.
        SmmChargerDTO smmChargerDTO = smmChargerMapper.toDto(smmCharger);

        restSmmChargerMockMvc.perform(post("/api/smm-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmChargerDTO)))
            .andExpect(status().isBadRequest());

        List<SmmCharger> smmChargerList = smmChargerRepository.findAll();
        assertThat(smmChargerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBatchDelayIsRequired() throws Exception {
        int databaseSizeBeforeTest = smmChargerRepository.findAll().size();
        // set the field null
        smmCharger.setBatchDelay(null);

        // Create the SmmCharger, which fails.
        SmmChargerDTO smmChargerDTO = smmChargerMapper.toDto(smmCharger);

        restSmmChargerMockMvc.perform(post("/api/smm-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmChargerDTO)))
            .andExpect(status().isBadRequest());

        List<SmmCharger> smmChargerList = smmChargerRepository.findAll();
        assertThat(smmChargerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSmmChargers() throws Exception {
        // Initialize the database
        smmChargerRepository.saveAndFlush(smmCharger);

        // Get all the smmChargerList
        restSmmChargerMockMvc.perform(get("/api/smm-chargers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smmCharger.getId().intValue())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT.toString())))
            .andExpect(jsonPath("$.[*].baud").value(hasItem(DEFAULT_BAUD)))
            .andExpect(jsonPath("$.[*].databit").value(hasItem(DEFAULT_DATABIT)))
            .andExpect(jsonPath("$.[*].parity").value(hasItem(DEFAULT_PARITY.toString())))
            .andExpect(jsonPath("$.[*].stopbit").value(hasItem(DEFAULT_STOPBIT.toString())))
            .andExpect(jsonPath("$.[*].timeout").value(hasItem(DEFAULT_TIMEOUT)))
            .andExpect(jsonPath("$.[*].retry").value(hasItem(DEFAULT_RETRY)))
            .andExpect(jsonPath("$.[*].transDelay").value(hasItem(DEFAULT_TRANS_DELAY)))
            .andExpect(jsonPath("$.[*].batchDelay").value(hasItem(DEFAULT_BATCH_DELAY)));
    }
    
    @Test
    @Transactional
    public void getSmmCharger() throws Exception {
        // Initialize the database
        smmChargerRepository.saveAndFlush(smmCharger);

        // Get the smmCharger
        restSmmChargerMockMvc.perform(get("/api/smm-chargers/{id}", smmCharger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(smmCharger.getId().intValue()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT.toString()))
            .andExpect(jsonPath("$.baud").value(DEFAULT_BAUD))
            .andExpect(jsonPath("$.databit").value(DEFAULT_DATABIT))
            .andExpect(jsonPath("$.parity").value(DEFAULT_PARITY.toString()))
            .andExpect(jsonPath("$.stopbit").value(DEFAULT_STOPBIT.toString()))
            .andExpect(jsonPath("$.timeout").value(DEFAULT_TIMEOUT))
            .andExpect(jsonPath("$.retry").value(DEFAULT_RETRY))
            .andExpect(jsonPath("$.transDelay").value(DEFAULT_TRANS_DELAY))
            .andExpect(jsonPath("$.batchDelay").value(DEFAULT_BATCH_DELAY));
    }

    @Test
    @Transactional
    public void getNonExistingSmmCharger() throws Exception {
        // Get the smmCharger
        restSmmChargerMockMvc.perform(get("/api/smm-chargers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSmmCharger() throws Exception {
        // Initialize the database
        smmChargerRepository.saveAndFlush(smmCharger);

        int databaseSizeBeforeUpdate = smmChargerRepository.findAll().size();

        // Update the smmCharger
        SmmCharger updatedSmmCharger = smmChargerRepository.findById(smmCharger.getId()).get();
        // Disconnect from session so that the updates on updatedSmmCharger are not directly saved in db
        em.detach(updatedSmmCharger);
        updatedSmmCharger
            .enabled(UPDATED_ENABLED)
            .port(UPDATED_PORT)
            .baud(UPDATED_BAUD)
            .databit(UPDATED_DATABIT)
            .parity(UPDATED_PARITY)
            .stopbit(UPDATED_STOPBIT)
            .timeout(UPDATED_TIMEOUT)
            .retry(UPDATED_RETRY)
            .transDelay(UPDATED_TRANS_DELAY)
            .batchDelay(UPDATED_BATCH_DELAY);
        SmmChargerDTO smmChargerDTO = smmChargerMapper.toDto(updatedSmmCharger);

        restSmmChargerMockMvc.perform(put("/api/smm-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmChargerDTO)))
            .andExpect(status().isOk());

        // Validate the SmmCharger in the database
        List<SmmCharger> smmChargerList = smmChargerRepository.findAll();
        assertThat(smmChargerList).hasSize(databaseSizeBeforeUpdate);
        SmmCharger testSmmCharger = smmChargerList.get(smmChargerList.size() - 1);
        assertThat(testSmmCharger.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testSmmCharger.getPort()).isEqualTo(UPDATED_PORT);
        assertThat(testSmmCharger.getBaud()).isEqualTo(UPDATED_BAUD);
        assertThat(testSmmCharger.getDatabit()).isEqualTo(UPDATED_DATABIT);
        assertThat(testSmmCharger.getParity()).isEqualTo(UPDATED_PARITY);
        assertThat(testSmmCharger.getStopbit()).isEqualTo(UPDATED_STOPBIT);
        assertThat(testSmmCharger.getTimeout()).isEqualTo(UPDATED_TIMEOUT);
        assertThat(testSmmCharger.getRetry()).isEqualTo(UPDATED_RETRY);
        assertThat(testSmmCharger.getTransDelay()).isEqualTo(UPDATED_TRANS_DELAY);
        assertThat(testSmmCharger.getBatchDelay()).isEqualTo(UPDATED_BATCH_DELAY);
    }

    @Test
    @Transactional
    public void updateNonExistingSmmCharger() throws Exception {
        int databaseSizeBeforeUpdate = smmChargerRepository.findAll().size();

        // Create the SmmCharger
        SmmChargerDTO smmChargerDTO = smmChargerMapper.toDto(smmCharger);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmmChargerMockMvc.perform(put("/api/smm-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmChargerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmmCharger in the database
        List<SmmCharger> smmChargerList = smmChargerRepository.findAll();
        assertThat(smmChargerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSmmCharger() throws Exception {
        // Initialize the database
        smmChargerRepository.saveAndFlush(smmCharger);

        int databaseSizeBeforeDelete = smmChargerRepository.findAll().size();

        // Delete the smmCharger
        restSmmChargerMockMvc.perform(delete("/api/smm-chargers/{id}", smmCharger.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SmmCharger> smmChargerList = smmChargerRepository.findAll();
        assertThat(smmChargerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmmCharger.class);
        SmmCharger smmCharger1 = new SmmCharger();
        smmCharger1.setId(1L);
        SmmCharger smmCharger2 = new SmmCharger();
        smmCharger2.setId(smmCharger1.getId());
        assertThat(smmCharger1).isEqualTo(smmCharger2);
        smmCharger2.setId(2L);
        assertThat(smmCharger1).isNotEqualTo(smmCharger2);
        smmCharger1.setId(null);
        assertThat(smmCharger1).isNotEqualTo(smmCharger2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmmChargerDTO.class);
        SmmChargerDTO smmChargerDTO1 = new SmmChargerDTO();
        smmChargerDTO1.setId(1L);
        SmmChargerDTO smmChargerDTO2 = new SmmChargerDTO();
        assertThat(smmChargerDTO1).isNotEqualTo(smmChargerDTO2);
        smmChargerDTO2.setId(smmChargerDTO1.getId());
        assertThat(smmChargerDTO1).isEqualTo(smmChargerDTO2);
        smmChargerDTO2.setId(2L);
        assertThat(smmChargerDTO1).isNotEqualTo(smmChargerDTO2);
        smmChargerDTO1.setId(null);
        assertThat(smmChargerDTO1).isNotEqualTo(smmChargerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(smmChargerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(smmChargerMapper.fromId(null)).isNull();
    }
}
