package io.scadatom.nucleus.web.rest;

import io.scadatom.neutron.Parity;
import io.scadatom.neutron.SmsChargerDTO;
import io.scadatom.neutron.Stopbit;
import io.scadatom.nucleus.NucleusApp;

import io.scadatom.nucleus.domain.SmsCharger;
import io.scadatom.nucleus.repository.SmsChargerRepository;
import io.scadatom.nucleus.service.SmsChargerService;
import io.scadatom.nucleus.service.mapper.SmsChargerMapper;
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
 * Test class for the SmsChargerResource REST controller.
 *
 * @see SmsChargerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NucleusApp.class)
public class SmsChargerResourceIntTest {

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

    private static final Integer DEFAULT_RESP_DELAY = 0;
    private static final Integer UPDATED_RESP_DELAY = 1;

    @Autowired
    private SmsChargerRepository smsChargerRepository;

    @Autowired
    private SmsChargerMapper smsChargerMapper;

    @Autowired
    private SmsChargerService smsChargerService;

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

    private MockMvc restSmsChargerMockMvc;

    private SmsCharger smsCharger;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SmsChargerResource smsChargerResource = new SmsChargerResource(smsChargerService);
        this.restSmsChargerMockMvc = MockMvcBuilders.standaloneSetup(smsChargerResource)
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
    public static SmsCharger createEntity(EntityManager em) {
        SmsCharger smsCharger = new SmsCharger()
            .enabled(DEFAULT_ENABLED)
            .port(DEFAULT_PORT)
            .baud(DEFAULT_BAUD)
            .databit(DEFAULT_DATABIT)
            .parity(DEFAULT_PARITY)
            .stopbit(DEFAULT_STOPBIT)
            .respDelay(DEFAULT_RESP_DELAY);
        return smsCharger;
    }

    @Before
    public void initTest() {
        smsCharger = createEntity(em);
    }

    @Test
    @Transactional
    public void createSmsCharger() throws Exception {
        int databaseSizeBeforeCreate = smsChargerRepository.findAll().size();

        // Create the SmsCharger
        SmsChargerDTO smsChargerDTO = smsChargerMapper.toDto(smsCharger);
        restSmsChargerMockMvc.perform(post("/api/sms-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsChargerDTO)))
            .andExpect(status().isCreated());

        // Validate the SmsCharger in the database
        List<SmsCharger> smsChargerList = smsChargerRepository.findAll();
        assertThat(smsChargerList).hasSize(databaseSizeBeforeCreate + 1);
        SmsCharger testSmsCharger = smsChargerList.get(smsChargerList.size() - 1);
        assertThat(testSmsCharger.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testSmsCharger.getPort()).isEqualTo(DEFAULT_PORT);
        assertThat(testSmsCharger.getBaud()).isEqualTo(DEFAULT_BAUD);
        assertThat(testSmsCharger.getDatabit()).isEqualTo(DEFAULT_DATABIT);
        assertThat(testSmsCharger.getParity()).isEqualTo(DEFAULT_PARITY);
        assertThat(testSmsCharger.getStopbit()).isEqualTo(DEFAULT_STOPBIT);
        assertThat(testSmsCharger.getRespDelay()).isEqualTo(DEFAULT_RESP_DELAY);
    }

    @Test
    @Transactional
    public void createSmsChargerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smsChargerRepository.findAll().size();

        // Create the SmsCharger with an existing ID
        smsCharger.setId(1L);
        SmsChargerDTO smsChargerDTO = smsChargerMapper.toDto(smsCharger);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmsChargerMockMvc.perform(post("/api/sms-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsChargerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsCharger in the database
        List<SmsCharger> smsChargerList = smsChargerRepository.findAll();
        assertThat(smsChargerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = smsChargerRepository.findAll().size();
        // set the field null
        smsCharger.setEnabled(null);

        // Create the SmsCharger, which fails.
        SmsChargerDTO smsChargerDTO = smsChargerMapper.toDto(smsCharger);

        restSmsChargerMockMvc.perform(post("/api/sms-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsChargerDTO)))
            .andExpect(status().isBadRequest());

        List<SmsCharger> smsChargerList = smsChargerRepository.findAll();
        assertThat(smsChargerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPortIsRequired() throws Exception {
        int databaseSizeBeforeTest = smsChargerRepository.findAll().size();
        // set the field null
        smsCharger.setPort(null);

        // Create the SmsCharger, which fails.
        SmsChargerDTO smsChargerDTO = smsChargerMapper.toDto(smsCharger);

        restSmsChargerMockMvc.perform(post("/api/sms-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsChargerDTO)))
            .andExpect(status().isBadRequest());

        List<SmsCharger> smsChargerList = smsChargerRepository.findAll();
        assertThat(smsChargerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBaudIsRequired() throws Exception {
        int databaseSizeBeforeTest = smsChargerRepository.findAll().size();
        // set the field null
        smsCharger.setBaud(null);

        // Create the SmsCharger, which fails.
        SmsChargerDTO smsChargerDTO = smsChargerMapper.toDto(smsCharger);

        restSmsChargerMockMvc.perform(post("/api/sms-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsChargerDTO)))
            .andExpect(status().isBadRequest());

        List<SmsCharger> smsChargerList = smsChargerRepository.findAll();
        assertThat(smsChargerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDatabitIsRequired() throws Exception {
        int databaseSizeBeforeTest = smsChargerRepository.findAll().size();
        // set the field null
        smsCharger.setDatabit(null);

        // Create the SmsCharger, which fails.
        SmsChargerDTO smsChargerDTO = smsChargerMapper.toDto(smsCharger);

        restSmsChargerMockMvc.perform(post("/api/sms-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsChargerDTO)))
            .andExpect(status().isBadRequest());

        List<SmsCharger> smsChargerList = smsChargerRepository.findAll();
        assertThat(smsChargerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkParityIsRequired() throws Exception {
        int databaseSizeBeforeTest = smsChargerRepository.findAll().size();
        // set the field null
        smsCharger.setParity(null);

        // Create the SmsCharger, which fails.
        SmsChargerDTO smsChargerDTO = smsChargerMapper.toDto(smsCharger);

        restSmsChargerMockMvc.perform(post("/api/sms-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsChargerDTO)))
            .andExpect(status().isBadRequest());

        List<SmsCharger> smsChargerList = smsChargerRepository.findAll();
        assertThat(smsChargerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStopbitIsRequired() throws Exception {
        int databaseSizeBeforeTest = smsChargerRepository.findAll().size();
        // set the field null
        smsCharger.setStopbit(null);

        // Create the SmsCharger, which fails.
        SmsChargerDTO smsChargerDTO = smsChargerMapper.toDto(smsCharger);

        restSmsChargerMockMvc.perform(post("/api/sms-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsChargerDTO)))
            .andExpect(status().isBadRequest());

        List<SmsCharger> smsChargerList = smsChargerRepository.findAll();
        assertThat(smsChargerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRespDelayIsRequired() throws Exception {
        int databaseSizeBeforeTest = smsChargerRepository.findAll().size();
        // set the field null
        smsCharger.setRespDelay(null);

        // Create the SmsCharger, which fails.
        SmsChargerDTO smsChargerDTO = smsChargerMapper.toDto(smsCharger);

        restSmsChargerMockMvc.perform(post("/api/sms-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsChargerDTO)))
            .andExpect(status().isBadRequest());

        List<SmsCharger> smsChargerList = smsChargerRepository.findAll();
        assertThat(smsChargerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSmsChargers() throws Exception {
        // Initialize the database
        smsChargerRepository.saveAndFlush(smsCharger);

        // Get all the smsChargerList
        restSmsChargerMockMvc.perform(get("/api/sms-chargers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smsCharger.getId().intValue())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT.toString())))
            .andExpect(jsonPath("$.[*].baud").value(hasItem(DEFAULT_BAUD)))
            .andExpect(jsonPath("$.[*].databit").value(hasItem(DEFAULT_DATABIT)))
            .andExpect(jsonPath("$.[*].parity").value(hasItem(DEFAULT_PARITY.toString())))
            .andExpect(jsonPath("$.[*].stopbit").value(hasItem(DEFAULT_STOPBIT.toString())))
            .andExpect(jsonPath("$.[*].respDelay").value(hasItem(DEFAULT_RESP_DELAY)));
    }
    
    @Test
    @Transactional
    public void getSmsCharger() throws Exception {
        // Initialize the database
        smsChargerRepository.saveAndFlush(smsCharger);

        // Get the smsCharger
        restSmsChargerMockMvc.perform(get("/api/sms-chargers/{id}", smsCharger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(smsCharger.getId().intValue()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT.toString()))
            .andExpect(jsonPath("$.baud").value(DEFAULT_BAUD))
            .andExpect(jsonPath("$.databit").value(DEFAULT_DATABIT))
            .andExpect(jsonPath("$.parity").value(DEFAULT_PARITY.toString()))
            .andExpect(jsonPath("$.stopbit").value(DEFAULT_STOPBIT.toString()))
            .andExpect(jsonPath("$.respDelay").value(DEFAULT_RESP_DELAY));
    }

    @Test
    @Transactional
    public void getNonExistingSmsCharger() throws Exception {
        // Get the smsCharger
        restSmsChargerMockMvc.perform(get("/api/sms-chargers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSmsCharger() throws Exception {
        // Initialize the database
        smsChargerRepository.saveAndFlush(smsCharger);

        int databaseSizeBeforeUpdate = smsChargerRepository.findAll().size();

        // Update the smsCharger
        SmsCharger updatedSmsCharger = smsChargerRepository.findById(smsCharger.getId()).get();
        // Disconnect from session so that the updates on updatedSmsCharger are not directly saved in db
        em.detach(updatedSmsCharger);
        updatedSmsCharger
            .enabled(UPDATED_ENABLED)
            .port(UPDATED_PORT)
            .baud(UPDATED_BAUD)
            .databit(UPDATED_DATABIT)
            .parity(UPDATED_PARITY)
            .stopbit(UPDATED_STOPBIT)
            .respDelay(UPDATED_RESP_DELAY);
        SmsChargerDTO smsChargerDTO = smsChargerMapper.toDto(updatedSmsCharger);

        restSmsChargerMockMvc.perform(put("/api/sms-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsChargerDTO)))
            .andExpect(status().isOk());

        // Validate the SmsCharger in the database
        List<SmsCharger> smsChargerList = smsChargerRepository.findAll();
        assertThat(smsChargerList).hasSize(databaseSizeBeforeUpdate);
        SmsCharger testSmsCharger = smsChargerList.get(smsChargerList.size() - 1);
        assertThat(testSmsCharger.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testSmsCharger.getPort()).isEqualTo(UPDATED_PORT);
        assertThat(testSmsCharger.getBaud()).isEqualTo(UPDATED_BAUD);
        assertThat(testSmsCharger.getDatabit()).isEqualTo(UPDATED_DATABIT);
        assertThat(testSmsCharger.getParity()).isEqualTo(UPDATED_PARITY);
        assertThat(testSmsCharger.getStopbit()).isEqualTo(UPDATED_STOPBIT);
        assertThat(testSmsCharger.getRespDelay()).isEqualTo(UPDATED_RESP_DELAY);
    }

    @Test
    @Transactional
    public void updateNonExistingSmsCharger() throws Exception {
        int databaseSizeBeforeUpdate = smsChargerRepository.findAll().size();

        // Create the SmsCharger
        SmsChargerDTO smsChargerDTO = smsChargerMapper.toDto(smsCharger);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsChargerMockMvc.perform(put("/api/sms-chargers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsChargerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsCharger in the database
        List<SmsCharger> smsChargerList = smsChargerRepository.findAll();
        assertThat(smsChargerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSmsCharger() throws Exception {
        // Initialize the database
        smsChargerRepository.saveAndFlush(smsCharger);

        int databaseSizeBeforeDelete = smsChargerRepository.findAll().size();

        // Delete the smsCharger
        restSmsChargerMockMvc.perform(delete("/api/sms-chargers/{id}", smsCharger.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SmsCharger> smsChargerList = smsChargerRepository.findAll();
        assertThat(smsChargerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsCharger.class);
        SmsCharger smsCharger1 = new SmsCharger();
        smsCharger1.setId(1L);
        SmsCharger smsCharger2 = new SmsCharger();
        smsCharger2.setId(smsCharger1.getId());
        assertThat(smsCharger1).isEqualTo(smsCharger2);
        smsCharger2.setId(2L);
        assertThat(smsCharger1).isNotEqualTo(smsCharger2);
        smsCharger1.setId(null);
        assertThat(smsCharger1).isNotEqualTo(smsCharger2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsChargerDTO.class);
        SmsChargerDTO smsChargerDTO1 = new SmsChargerDTO();
        smsChargerDTO1.setId(1L);
        SmsChargerDTO smsChargerDTO2 = new SmsChargerDTO();
        assertThat(smsChargerDTO1).isNotEqualTo(smsChargerDTO2);
        smsChargerDTO2.setId(smsChargerDTO1.getId());
        assertThat(smsChargerDTO1).isEqualTo(smsChargerDTO2);
        smsChargerDTO2.setId(2L);
        assertThat(smsChargerDTO1).isNotEqualTo(smsChargerDTO2);
        smsChargerDTO1.setId(null);
        assertThat(smsChargerDTO1).isNotEqualTo(smsChargerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(smsChargerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(smsChargerMapper.fromId(null)).isNull();
    }
}
