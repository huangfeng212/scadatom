package io.scadatom.electron.web.rest;

import io.scadatom.electron.ElectronApp;

import io.scadatom.electron.domain.SmsChargerOp;
import io.scadatom.electron.repository.SmsChargerOpRepository;
import io.scadatom.electron.service.SmsChargerOpService;
import io.scadatom.electron.service.mapper.SmsChargerOpMapper;
import io.scadatom.electron.web.rest.errors.ExceptionTranslator;

import io.scadatom.neutron.OpState;
import io.scadatom.neutron.SmsChargerOpDTO;
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
 * Test class for the SmsChargerOpResource REST controller.
 *
 * @see SmsChargerOpResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElectronApp.class)
public class SmsChargerOpResourceIntTest {

    private static final OpState DEFAULT_STATE = OpState.Uninitialized;
    private static final OpState UPDATED_STATE = OpState.Initialized;

    private static final ZonedDateTime DEFAULT_DT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private SmsChargerOpRepository smsChargerOpRepository;

    @Autowired
    private SmsChargerOpMapper smsChargerOpMapper;

    @Autowired
    private SmsChargerOpService smsChargerOpService;

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

    private MockMvc restSmsChargerOpMockMvc;

    private SmsChargerOp smsChargerOp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SmsChargerOpResource smsChargerOpResource = new SmsChargerOpResource(smsChargerOpService);
        this.restSmsChargerOpMockMvc = MockMvcBuilders.standaloneSetup(smsChargerOpResource)
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
    public static SmsChargerOp createEntity(EntityManager em) {
        SmsChargerOp smsChargerOp = new SmsChargerOp()
            .state(DEFAULT_STATE)
            .dt(DEFAULT_DT);
        return smsChargerOp;
    }

    @Before
    public void initTest() {
        smsChargerOp = createEntity(em);
    }

    @Test
    @Transactional
    public void createSmsChargerOp() throws Exception {
        int databaseSizeBeforeCreate = smsChargerOpRepository.findAll().size();

        // Create the SmsChargerOp
        SmsChargerOpDTO smsChargerOpDTO = smsChargerOpMapper.toDto(smsChargerOp);
        restSmsChargerOpMockMvc.perform(post("/api/sms-charger-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsChargerOpDTO)))
            .andExpect(status().isCreated());

        // Validate the SmsChargerOp in the database
        List<SmsChargerOp> smsChargerOpList = smsChargerOpRepository.findAll();
        assertThat(smsChargerOpList).hasSize(databaseSizeBeforeCreate + 1);
        SmsChargerOp testSmsChargerOp = smsChargerOpList.get(smsChargerOpList.size() - 1);
        assertThat(testSmsChargerOp.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testSmsChargerOp.getDt()).isEqualTo(DEFAULT_DT);
    }

    @Test
    @Transactional
    public void createSmsChargerOpWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smsChargerOpRepository.findAll().size();

        // Create the SmsChargerOp with an existing ID
        smsChargerOp.setId(1L);
        SmsChargerOpDTO smsChargerOpDTO = smsChargerOpMapper.toDto(smsChargerOp);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmsChargerOpMockMvc.perform(post("/api/sms-charger-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsChargerOpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsChargerOp in the database
        List<SmsChargerOp> smsChargerOpList = smsChargerOpRepository.findAll();
        assertThat(smsChargerOpList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSmsChargerOps() throws Exception {
        // Initialize the database
        smsChargerOpRepository.saveAndFlush(smsChargerOp);

        // Get all the smsChargerOpList
        restSmsChargerOpMockMvc.perform(get("/api/sms-charger-ops?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smsChargerOp.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].dt").value(hasItem(sameInstant(DEFAULT_DT))));
    }
    
    @Test
    @Transactional
    public void getSmsChargerOp() throws Exception {
        // Initialize the database
        smsChargerOpRepository.saveAndFlush(smsChargerOp);

        // Get the smsChargerOp
        restSmsChargerOpMockMvc.perform(get("/api/sms-charger-ops/{id}", smsChargerOp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(smsChargerOp.getId().intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.dt").value(sameInstant(DEFAULT_DT)));
    }

    @Test
    @Transactional
    public void getNonExistingSmsChargerOp() throws Exception {
        // Get the smsChargerOp
        restSmsChargerOpMockMvc.perform(get("/api/sms-charger-ops/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSmsChargerOp() throws Exception {
        // Initialize the database
        smsChargerOpRepository.saveAndFlush(smsChargerOp);

        int databaseSizeBeforeUpdate = smsChargerOpRepository.findAll().size();

        // Update the smsChargerOp
        SmsChargerOp updatedSmsChargerOp = smsChargerOpRepository.findById(smsChargerOp.getId()).get();
        // Disconnect from session so that the updates on updatedSmsChargerOp are not directly saved in db
        em.detach(updatedSmsChargerOp);
        updatedSmsChargerOp
            .state(UPDATED_STATE)
            .dt(UPDATED_DT);
        SmsChargerOpDTO smsChargerOpDTO = smsChargerOpMapper.toDto(updatedSmsChargerOp);

        restSmsChargerOpMockMvc.perform(put("/api/sms-charger-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsChargerOpDTO)))
            .andExpect(status().isOk());

        // Validate the SmsChargerOp in the database
        List<SmsChargerOp> smsChargerOpList = smsChargerOpRepository.findAll();
        assertThat(smsChargerOpList).hasSize(databaseSizeBeforeUpdate);
        SmsChargerOp testSmsChargerOp = smsChargerOpList.get(smsChargerOpList.size() - 1);
        assertThat(testSmsChargerOp.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testSmsChargerOp.getDt()).isEqualTo(UPDATED_DT);
    }

    @Test
    @Transactional
    public void updateNonExistingSmsChargerOp() throws Exception {
        int databaseSizeBeforeUpdate = smsChargerOpRepository.findAll().size();

        // Create the SmsChargerOp
        SmsChargerOpDTO smsChargerOpDTO = smsChargerOpMapper.toDto(smsChargerOp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsChargerOpMockMvc.perform(put("/api/sms-charger-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsChargerOpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsChargerOp in the database
        List<SmsChargerOp> smsChargerOpList = smsChargerOpRepository.findAll();
        assertThat(smsChargerOpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSmsChargerOp() throws Exception {
        // Initialize the database
        smsChargerOpRepository.saveAndFlush(smsChargerOp);

        int databaseSizeBeforeDelete = smsChargerOpRepository.findAll().size();

        // Delete the smsChargerOp
        restSmsChargerOpMockMvc.perform(delete("/api/sms-charger-ops/{id}", smsChargerOp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SmsChargerOp> smsChargerOpList = smsChargerOpRepository.findAll();
        assertThat(smsChargerOpList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsChargerOp.class);
        SmsChargerOp smsChargerOp1 = new SmsChargerOp();
        smsChargerOp1.setId(1L);
        SmsChargerOp smsChargerOp2 = new SmsChargerOp();
        smsChargerOp2.setId(smsChargerOp1.getId());
        assertThat(smsChargerOp1).isEqualTo(smsChargerOp2);
        smsChargerOp2.setId(2L);
        assertThat(smsChargerOp1).isNotEqualTo(smsChargerOp2);
        smsChargerOp1.setId(null);
        assertThat(smsChargerOp1).isNotEqualTo(smsChargerOp2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsChargerOpDTO.class);
        SmsChargerOpDTO smsChargerOpDTO1 = new SmsChargerOpDTO();
        smsChargerOpDTO1.setId(1L);
        SmsChargerOpDTO smsChargerOpDTO2 = new SmsChargerOpDTO();
        assertThat(smsChargerOpDTO1).isNotEqualTo(smsChargerOpDTO2);
        smsChargerOpDTO2.setId(smsChargerOpDTO1.getId());
        assertThat(smsChargerOpDTO1).isEqualTo(smsChargerOpDTO2);
        smsChargerOpDTO2.setId(2L);
        assertThat(smsChargerOpDTO1).isNotEqualTo(smsChargerOpDTO2);
        smsChargerOpDTO1.setId(null);
        assertThat(smsChargerOpDTO1).isNotEqualTo(smsChargerOpDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(smsChargerOpMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(smsChargerOpMapper.fromId(null)).isNull();
    }
}
