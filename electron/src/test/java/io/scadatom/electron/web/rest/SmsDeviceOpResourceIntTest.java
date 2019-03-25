package io.scadatom.electron.web.rest;

import io.scadatom.electron.ElectronApp;

import io.scadatom.electron.domain.SmsDeviceOp;
import io.scadatom.electron.repository.SmsDeviceOpRepository;
import io.scadatom.electron.service.SmsDeviceOpService;
import io.scadatom.electron.service.mapper.SmsDeviceOpMapper;
import io.scadatom.electron.web.rest.errors.ExceptionTranslator;

import io.scadatom.neutron.OpState;
import io.scadatom.neutron.SmsDeviceOpDTO;
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
 * Test class for the SmsDeviceOpResource REST controller.
 *
 * @see SmsDeviceOpResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElectronApp.class)
public class SmsDeviceOpResourceIntTest {

    private static final OpState DEFAULT_STATE = OpState.Uninitialized;
    private static final OpState UPDATED_STATE = OpState.Initialized;

    private static final ZonedDateTime DEFAULT_DT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private SmsDeviceOpRepository smsDeviceOpRepository;

    @Autowired
    private SmsDeviceOpMapper smsDeviceOpMapper;

    @Autowired
    private SmsDeviceOpService smsDeviceOpService;

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

    private MockMvc restSmsDeviceOpMockMvc;

    private SmsDeviceOp smsDeviceOp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SmsDeviceOpResource smsDeviceOpResource = new SmsDeviceOpResource(smsDeviceOpService);
        this.restSmsDeviceOpMockMvc = MockMvcBuilders.standaloneSetup(smsDeviceOpResource)
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
    public static SmsDeviceOp createEntity(EntityManager em) {
        SmsDeviceOp smsDeviceOp = new SmsDeviceOp()
            .state(DEFAULT_STATE)
            .dt(DEFAULT_DT);
        return smsDeviceOp;
    }

    @Before
    public void initTest() {
        smsDeviceOp = createEntity(em);
    }

    @Test
    @Transactional
    public void createSmsDeviceOp() throws Exception {
        int databaseSizeBeforeCreate = smsDeviceOpRepository.findAll().size();

        // Create the SmsDeviceOp
        SmsDeviceOpDTO smsDeviceOpDTO = smsDeviceOpMapper.toDto(smsDeviceOp);
        restSmsDeviceOpMockMvc.perform(post("/api/sms-device-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsDeviceOpDTO)))
            .andExpect(status().isCreated());

        // Validate the SmsDeviceOp in the database
        List<SmsDeviceOp> smsDeviceOpList = smsDeviceOpRepository.findAll();
        assertThat(smsDeviceOpList).hasSize(databaseSizeBeforeCreate + 1);
        SmsDeviceOp testSmsDeviceOp = smsDeviceOpList.get(smsDeviceOpList.size() - 1);
        assertThat(testSmsDeviceOp.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testSmsDeviceOp.getDt()).isEqualTo(DEFAULT_DT);
    }

    @Test
    @Transactional
    public void createSmsDeviceOpWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smsDeviceOpRepository.findAll().size();

        // Create the SmsDeviceOp with an existing ID
        smsDeviceOp.setId(1L);
        SmsDeviceOpDTO smsDeviceOpDTO = smsDeviceOpMapper.toDto(smsDeviceOp);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmsDeviceOpMockMvc.perform(post("/api/sms-device-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsDeviceOpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsDeviceOp in the database
        List<SmsDeviceOp> smsDeviceOpList = smsDeviceOpRepository.findAll();
        assertThat(smsDeviceOpList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSmsDeviceOps() throws Exception {
        // Initialize the database
        smsDeviceOpRepository.saveAndFlush(smsDeviceOp);

        // Get all the smsDeviceOpList
        restSmsDeviceOpMockMvc.perform(get("/api/sms-device-ops?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smsDeviceOp.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].dt").value(hasItem(sameInstant(DEFAULT_DT))));
    }
    
    @Test
    @Transactional
    public void getSmsDeviceOp() throws Exception {
        // Initialize the database
        smsDeviceOpRepository.saveAndFlush(smsDeviceOp);

        // Get the smsDeviceOp
        restSmsDeviceOpMockMvc.perform(get("/api/sms-device-ops/{id}", smsDeviceOp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(smsDeviceOp.getId().intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.dt").value(sameInstant(DEFAULT_DT)));
    }

    @Test
    @Transactional
    public void getNonExistingSmsDeviceOp() throws Exception {
        // Get the smsDeviceOp
        restSmsDeviceOpMockMvc.perform(get("/api/sms-device-ops/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSmsDeviceOp() throws Exception {
        // Initialize the database
        smsDeviceOpRepository.saveAndFlush(smsDeviceOp);

        int databaseSizeBeforeUpdate = smsDeviceOpRepository.findAll().size();

        // Update the smsDeviceOp
        SmsDeviceOp updatedSmsDeviceOp = smsDeviceOpRepository.findById(smsDeviceOp.getId()).get();
        // Disconnect from session so that the updates on updatedSmsDeviceOp are not directly saved in db
        em.detach(updatedSmsDeviceOp);
        updatedSmsDeviceOp
            .state(UPDATED_STATE)
            .dt(UPDATED_DT);
        SmsDeviceOpDTO smsDeviceOpDTO = smsDeviceOpMapper.toDto(updatedSmsDeviceOp);

        restSmsDeviceOpMockMvc.perform(put("/api/sms-device-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsDeviceOpDTO)))
            .andExpect(status().isOk());

        // Validate the SmsDeviceOp in the database
        List<SmsDeviceOp> smsDeviceOpList = smsDeviceOpRepository.findAll();
        assertThat(smsDeviceOpList).hasSize(databaseSizeBeforeUpdate);
        SmsDeviceOp testSmsDeviceOp = smsDeviceOpList.get(smsDeviceOpList.size() - 1);
        assertThat(testSmsDeviceOp.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testSmsDeviceOp.getDt()).isEqualTo(UPDATED_DT);
    }

    @Test
    @Transactional
    public void updateNonExistingSmsDeviceOp() throws Exception {
        int databaseSizeBeforeUpdate = smsDeviceOpRepository.findAll().size();

        // Create the SmsDeviceOp
        SmsDeviceOpDTO smsDeviceOpDTO = smsDeviceOpMapper.toDto(smsDeviceOp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsDeviceOpMockMvc.perform(put("/api/sms-device-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsDeviceOpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsDeviceOp in the database
        List<SmsDeviceOp> smsDeviceOpList = smsDeviceOpRepository.findAll();
        assertThat(smsDeviceOpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSmsDeviceOp() throws Exception {
        // Initialize the database
        smsDeviceOpRepository.saveAndFlush(smsDeviceOp);

        int databaseSizeBeforeDelete = smsDeviceOpRepository.findAll().size();

        // Delete the smsDeviceOp
        restSmsDeviceOpMockMvc.perform(delete("/api/sms-device-ops/{id}", smsDeviceOp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SmsDeviceOp> smsDeviceOpList = smsDeviceOpRepository.findAll();
        assertThat(smsDeviceOpList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsDeviceOp.class);
        SmsDeviceOp smsDeviceOp1 = new SmsDeviceOp();
        smsDeviceOp1.setId(1L);
        SmsDeviceOp smsDeviceOp2 = new SmsDeviceOp();
        smsDeviceOp2.setId(smsDeviceOp1.getId());
        assertThat(smsDeviceOp1).isEqualTo(smsDeviceOp2);
        smsDeviceOp2.setId(2L);
        assertThat(smsDeviceOp1).isNotEqualTo(smsDeviceOp2);
        smsDeviceOp1.setId(null);
        assertThat(smsDeviceOp1).isNotEqualTo(smsDeviceOp2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsDeviceOpDTO.class);
        SmsDeviceOpDTO smsDeviceOpDTO1 = new SmsDeviceOpDTO();
        smsDeviceOpDTO1.setId(1L);
        SmsDeviceOpDTO smsDeviceOpDTO2 = new SmsDeviceOpDTO();
        assertThat(smsDeviceOpDTO1).isNotEqualTo(smsDeviceOpDTO2);
        smsDeviceOpDTO2.setId(smsDeviceOpDTO1.getId());
        assertThat(smsDeviceOpDTO1).isEqualTo(smsDeviceOpDTO2);
        smsDeviceOpDTO2.setId(2L);
        assertThat(smsDeviceOpDTO1).isNotEqualTo(smsDeviceOpDTO2);
        smsDeviceOpDTO1.setId(null);
        assertThat(smsDeviceOpDTO1).isNotEqualTo(smsDeviceOpDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(smsDeviceOpMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(smsDeviceOpMapper.fromId(null)).isNull();
    }
}
