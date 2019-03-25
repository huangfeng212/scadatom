package io.scadatom.nucleus.web.rest;

import io.scadatom.neutron.RegType;
import io.scadatom.neutron.SmsBondDTO;
import io.scadatom.neutron.ValueType;
import io.scadatom.nucleus.NucleusApp;

import io.scadatom.nucleus.domain.SmsBond;
import io.scadatom.nucleus.domain.SmsDevice;
import io.scadatom.nucleus.repository.SmsBondRepository;
import io.scadatom.nucleus.service.SmsBondService;
import io.scadatom.nucleus.service.mapper.SmsBondMapper;
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
 * Test class for the SmsBondResource REST controller.
 *
 * @see SmsBondResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NucleusApp.class)
public class SmsBondResourceIntTest {

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final RegType DEFAULT_REG_TYPE = RegType.Coil;
    private static final RegType UPDATED_REG_TYPE = RegType.InputDiscrete;

    private static final String DEFAULT_REG = "AAAAAAAAAA";
    private static final String UPDATED_REG = "BBBBBBBBBB";

    private static final ValueType DEFAULT_VALUE_TYPE = ValueType.Uint16;
    private static final ValueType UPDATED_VALUE_TYPE = ValueType.Int16;

    private static final String DEFAULT_EXPR_IN = "AAAAAAAAAA";
    private static final String UPDATED_EXPR_IN = "BBBBBBBBBB";

    private static final String DEFAULT_EXPR_OUT = "AAAAAAAAAA";
    private static final String UPDATED_EXPR_OUT = "BBBBBBBBBB";

    @Autowired
    private SmsBondRepository smsBondRepository;

    @Autowired
    private SmsBondMapper smsBondMapper;

    @Autowired
    private SmsBondService smsBondService;

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

    private MockMvc restSmsBondMockMvc;

    private SmsBond smsBond;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SmsBondResource smsBondResource = new SmsBondResource(smsBondService);
        this.restSmsBondMockMvc = MockMvcBuilders.standaloneSetup(smsBondResource)
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
    public static SmsBond createEntity(EntityManager em) {
        SmsBond smsBond = new SmsBond()
            .enabled(DEFAULT_ENABLED)
            .regType(DEFAULT_REG_TYPE)
            .reg(DEFAULT_REG)
            .valueType(DEFAULT_VALUE_TYPE)
            .exprIn(DEFAULT_EXPR_IN)
            .exprOut(DEFAULT_EXPR_OUT);
        // Add required entity
        SmsDevice smsDevice = SmsDeviceResourceIntTest.createEntity(em);
        em.persist(smsDevice);
        em.flush();
        smsBond.setSmsDevice(smsDevice);
        return smsBond;
    }

    @Before
    public void initTest() {
        smsBond = createEntity(em);
    }

    @Test
    @Transactional
    public void createSmsBond() throws Exception {
        int databaseSizeBeforeCreate = smsBondRepository.findAll().size();

        // Create the SmsBond
        SmsBondDTO smsBondDTO = smsBondMapper.toDto(smsBond);
        restSmsBondMockMvc.perform(post("/api/sms-bonds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsBondDTO)))
            .andExpect(status().isCreated());

        // Validate the SmsBond in the database
        List<SmsBond> smsBondList = smsBondRepository.findAll();
        assertThat(smsBondList).hasSize(databaseSizeBeforeCreate + 1);
        SmsBond testSmsBond = smsBondList.get(smsBondList.size() - 1);
        assertThat(testSmsBond.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testSmsBond.getRegType()).isEqualTo(DEFAULT_REG_TYPE);
        assertThat(testSmsBond.getReg()).isEqualTo(DEFAULT_REG);
        assertThat(testSmsBond.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
        assertThat(testSmsBond.getExprIn()).isEqualTo(DEFAULT_EXPR_IN);
        assertThat(testSmsBond.getExprOut()).isEqualTo(DEFAULT_EXPR_OUT);
    }

    @Test
    @Transactional
    public void createSmsBondWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smsBondRepository.findAll().size();

        // Create the SmsBond with an existing ID
        smsBond.setId(1L);
        SmsBondDTO smsBondDTO = smsBondMapper.toDto(smsBond);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmsBondMockMvc.perform(post("/api/sms-bonds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsBondDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsBond in the database
        List<SmsBond> smsBondList = smsBondRepository.findAll();
        assertThat(smsBondList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = smsBondRepository.findAll().size();
        // set the field null
        smsBond.setEnabled(null);

        // Create the SmsBond, which fails.
        SmsBondDTO smsBondDTO = smsBondMapper.toDto(smsBond);

        restSmsBondMockMvc.perform(post("/api/sms-bonds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsBondDTO)))
            .andExpect(status().isBadRequest());

        List<SmsBond> smsBondList = smsBondRepository.findAll();
        assertThat(smsBondList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = smsBondRepository.findAll().size();
        // set the field null
        smsBond.setRegType(null);

        // Create the SmsBond, which fails.
        SmsBondDTO smsBondDTO = smsBondMapper.toDto(smsBond);

        restSmsBondMockMvc.perform(post("/api/sms-bonds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsBondDTO)))
            .andExpect(status().isBadRequest());

        List<SmsBond> smsBondList = smsBondRepository.findAll();
        assertThat(smsBondList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegIsRequired() throws Exception {
        int databaseSizeBeforeTest = smsBondRepository.findAll().size();
        // set the field null
        smsBond.setReg(null);

        // Create the SmsBond, which fails.
        SmsBondDTO smsBondDTO = smsBondMapper.toDto(smsBond);

        restSmsBondMockMvc.perform(post("/api/sms-bonds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsBondDTO)))
            .andExpect(status().isBadRequest());

        List<SmsBond> smsBondList = smsBondRepository.findAll();
        assertThat(smsBondList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSmsBonds() throws Exception {
        // Initialize the database
        smsBondRepository.saveAndFlush(smsBond);

        // Get all the smsBondList
        restSmsBondMockMvc.perform(get("/api/sms-bonds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smsBond.getId().intValue())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].regType").value(hasItem(DEFAULT_REG_TYPE.toString())))
            .andExpect(jsonPath("$.[*].reg").value(hasItem(DEFAULT_REG.toString())))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].exprIn").value(hasItem(DEFAULT_EXPR_IN.toString())))
            .andExpect(jsonPath("$.[*].exprOut").value(hasItem(DEFAULT_EXPR_OUT.toString())));
    }
    
    @Test
    @Transactional
    public void getSmsBond() throws Exception {
        // Initialize the database
        smsBondRepository.saveAndFlush(smsBond);

        // Get the smsBond
        restSmsBondMockMvc.perform(get("/api/sms-bonds/{id}", smsBond.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(smsBond.getId().intValue()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.regType").value(DEFAULT_REG_TYPE.toString()))
            .andExpect(jsonPath("$.reg").value(DEFAULT_REG.toString()))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE.toString()))
            .andExpect(jsonPath("$.exprIn").value(DEFAULT_EXPR_IN.toString()))
            .andExpect(jsonPath("$.exprOut").value(DEFAULT_EXPR_OUT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSmsBond() throws Exception {
        // Get the smsBond
        restSmsBondMockMvc.perform(get("/api/sms-bonds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSmsBond() throws Exception {
        // Initialize the database
        smsBondRepository.saveAndFlush(smsBond);

        int databaseSizeBeforeUpdate = smsBondRepository.findAll().size();

        // Update the smsBond
        SmsBond updatedSmsBond = smsBondRepository.findById(smsBond.getId()).get();
        // Disconnect from session so that the updates on updatedSmsBond are not directly saved in db
        em.detach(updatedSmsBond);
        updatedSmsBond
            .enabled(UPDATED_ENABLED)
            .regType(UPDATED_REG_TYPE)
            .reg(UPDATED_REG)
            .valueType(UPDATED_VALUE_TYPE)
            .exprIn(UPDATED_EXPR_IN)
            .exprOut(UPDATED_EXPR_OUT);
        SmsBondDTO smsBondDTO = smsBondMapper.toDto(updatedSmsBond);

        restSmsBondMockMvc.perform(put("/api/sms-bonds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsBondDTO)))
            .andExpect(status().isOk());

        // Validate the SmsBond in the database
        List<SmsBond> smsBondList = smsBondRepository.findAll();
        assertThat(smsBondList).hasSize(databaseSizeBeforeUpdate);
        SmsBond testSmsBond = smsBondList.get(smsBondList.size() - 1);
        assertThat(testSmsBond.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testSmsBond.getRegType()).isEqualTo(UPDATED_REG_TYPE);
        assertThat(testSmsBond.getReg()).isEqualTo(UPDATED_REG);
        assertThat(testSmsBond.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testSmsBond.getExprIn()).isEqualTo(UPDATED_EXPR_IN);
        assertThat(testSmsBond.getExprOut()).isEqualTo(UPDATED_EXPR_OUT);
    }

    @Test
    @Transactional
    public void updateNonExistingSmsBond() throws Exception {
        int databaseSizeBeforeUpdate = smsBondRepository.findAll().size();

        // Create the SmsBond
        SmsBondDTO smsBondDTO = smsBondMapper.toDto(smsBond);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsBondMockMvc.perform(put("/api/sms-bonds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsBondDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsBond in the database
        List<SmsBond> smsBondList = smsBondRepository.findAll();
        assertThat(smsBondList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSmsBond() throws Exception {
        // Initialize the database
        smsBondRepository.saveAndFlush(smsBond);

        int databaseSizeBeforeDelete = smsBondRepository.findAll().size();

        // Delete the smsBond
        restSmsBondMockMvc.perform(delete("/api/sms-bonds/{id}", smsBond.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SmsBond> smsBondList = smsBondRepository.findAll();
        assertThat(smsBondList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsBond.class);
        SmsBond smsBond1 = new SmsBond();
        smsBond1.setId(1L);
        SmsBond smsBond2 = new SmsBond();
        smsBond2.setId(smsBond1.getId());
        assertThat(smsBond1).isEqualTo(smsBond2);
        smsBond2.setId(2L);
        assertThat(smsBond1).isNotEqualTo(smsBond2);
        smsBond1.setId(null);
        assertThat(smsBond1).isNotEqualTo(smsBond2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsBondDTO.class);
        SmsBondDTO smsBondDTO1 = new SmsBondDTO();
        smsBondDTO1.setId(1L);
        SmsBondDTO smsBondDTO2 = new SmsBondDTO();
        assertThat(smsBondDTO1).isNotEqualTo(smsBondDTO2);
        smsBondDTO2.setId(smsBondDTO1.getId());
        assertThat(smsBondDTO1).isEqualTo(smsBondDTO2);
        smsBondDTO2.setId(2L);
        assertThat(smsBondDTO1).isNotEqualTo(smsBondDTO2);
        smsBondDTO1.setId(null);
        assertThat(smsBondDTO1).isNotEqualTo(smsBondDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(smsBondMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(smsBondMapper.fromId(null)).isNull();
    }
}
