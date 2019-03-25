package io.scadatom.nucleus.web.rest;

import io.scadatom.neutron.RegType;
import io.scadatom.neutron.SmmBondDTO;
import io.scadatom.neutron.ValueType;
import io.scadatom.nucleus.NucleusApp;

import io.scadatom.nucleus.domain.SmmBond;
import io.scadatom.nucleus.domain.SmmDevice;
import io.scadatom.nucleus.repository.SmmBondRepository;
import io.scadatom.nucleus.service.SmmBondService;
import io.scadatom.nucleus.service.mapper.SmmBondMapper;
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
 * Test class for the SmmBondResource REST controller.
 *
 * @see SmmBondResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NucleusApp.class)
public class SmmBondResourceIntTest {

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
    private SmmBondRepository smmBondRepository;

    @Autowired
    private SmmBondMapper smmBondMapper;

    @Autowired
    private SmmBondService smmBondService;

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

    private MockMvc restSmmBondMockMvc;

    private SmmBond smmBond;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SmmBondResource smmBondResource = new SmmBondResource(smmBondService);
        this.restSmmBondMockMvc = MockMvcBuilders.standaloneSetup(smmBondResource)
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
    public static SmmBond createEntity(EntityManager em) {
        SmmBond smmBond = new SmmBond()
            .enabled(DEFAULT_ENABLED)
            .regType(DEFAULT_REG_TYPE)
            .reg(DEFAULT_REG)
            .valueType(DEFAULT_VALUE_TYPE)
            .exprIn(DEFAULT_EXPR_IN)
            .exprOut(DEFAULT_EXPR_OUT);
        // Add required entity
        SmmDevice smmDevice = SmmDeviceResourceIntTest.createEntity(em);
        em.persist(smmDevice);
        em.flush();
        smmBond.setSmmDevice(smmDevice);
        return smmBond;
    }

    @Before
    public void initTest() {
        smmBond = createEntity(em);
    }

    @Test
    @Transactional
    public void createSmmBond() throws Exception {
        int databaseSizeBeforeCreate = smmBondRepository.findAll().size();

        // Create the SmmBond
        SmmBondDTO smmBondDTO = smmBondMapper.toDto(smmBond);
        restSmmBondMockMvc.perform(post("/api/smm-bonds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmBondDTO)))
            .andExpect(status().isCreated());

        // Validate the SmmBond in the database
        List<SmmBond> smmBondList = smmBondRepository.findAll();
        assertThat(smmBondList).hasSize(databaseSizeBeforeCreate + 1);
        SmmBond testSmmBond = smmBondList.get(smmBondList.size() - 1);
        assertThat(testSmmBond.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testSmmBond.getRegType()).isEqualTo(DEFAULT_REG_TYPE);
        assertThat(testSmmBond.getReg()).isEqualTo(DEFAULT_REG);
        assertThat(testSmmBond.getValueType()).isEqualTo(DEFAULT_VALUE_TYPE);
        assertThat(testSmmBond.getExprIn()).isEqualTo(DEFAULT_EXPR_IN);
        assertThat(testSmmBond.getExprOut()).isEqualTo(DEFAULT_EXPR_OUT);
    }

    @Test
    @Transactional
    public void createSmmBondWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smmBondRepository.findAll().size();

        // Create the SmmBond with an existing ID
        smmBond.setId(1L);
        SmmBondDTO smmBondDTO = smmBondMapper.toDto(smmBond);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmmBondMockMvc.perform(post("/api/smm-bonds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmBondDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmmBond in the database
        List<SmmBond> smmBondList = smmBondRepository.findAll();
        assertThat(smmBondList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = smmBondRepository.findAll().size();
        // set the field null
        smmBond.setEnabled(null);

        // Create the SmmBond, which fails.
        SmmBondDTO smmBondDTO = smmBondMapper.toDto(smmBond);

        restSmmBondMockMvc.perform(post("/api/smm-bonds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmBondDTO)))
            .andExpect(status().isBadRequest());

        List<SmmBond> smmBondList = smmBondRepository.findAll();
        assertThat(smmBondList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = smmBondRepository.findAll().size();
        // set the field null
        smmBond.setRegType(null);

        // Create the SmmBond, which fails.
        SmmBondDTO smmBondDTO = smmBondMapper.toDto(smmBond);

        restSmmBondMockMvc.perform(post("/api/smm-bonds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmBondDTO)))
            .andExpect(status().isBadRequest());

        List<SmmBond> smmBondList = smmBondRepository.findAll();
        assertThat(smmBondList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegIsRequired() throws Exception {
        int databaseSizeBeforeTest = smmBondRepository.findAll().size();
        // set the field null
        smmBond.setReg(null);

        // Create the SmmBond, which fails.
        SmmBondDTO smmBondDTO = smmBondMapper.toDto(smmBond);

        restSmmBondMockMvc.perform(post("/api/smm-bonds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmBondDTO)))
            .andExpect(status().isBadRequest());

        List<SmmBond> smmBondList = smmBondRepository.findAll();
        assertThat(smmBondList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSmmBonds() throws Exception {
        // Initialize the database
        smmBondRepository.saveAndFlush(smmBond);

        // Get all the smmBondList
        restSmmBondMockMvc.perform(get("/api/smm-bonds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smmBond.getId().intValue())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].regType").value(hasItem(DEFAULT_REG_TYPE.toString())))
            .andExpect(jsonPath("$.[*].reg").value(hasItem(DEFAULT_REG.toString())))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].exprIn").value(hasItem(DEFAULT_EXPR_IN.toString())))
            .andExpect(jsonPath("$.[*].exprOut").value(hasItem(DEFAULT_EXPR_OUT.toString())));
    }
    
    @Test
    @Transactional
    public void getSmmBond() throws Exception {
        // Initialize the database
        smmBondRepository.saveAndFlush(smmBond);

        // Get the smmBond
        restSmmBondMockMvc.perform(get("/api/smm-bonds/{id}", smmBond.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(smmBond.getId().intValue()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.regType").value(DEFAULT_REG_TYPE.toString()))
            .andExpect(jsonPath("$.reg").value(DEFAULT_REG.toString()))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE.toString()))
            .andExpect(jsonPath("$.exprIn").value(DEFAULT_EXPR_IN.toString()))
            .andExpect(jsonPath("$.exprOut").value(DEFAULT_EXPR_OUT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSmmBond() throws Exception {
        // Get the smmBond
        restSmmBondMockMvc.perform(get("/api/smm-bonds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSmmBond() throws Exception {
        // Initialize the database
        smmBondRepository.saveAndFlush(smmBond);

        int databaseSizeBeforeUpdate = smmBondRepository.findAll().size();

        // Update the smmBond
        SmmBond updatedSmmBond = smmBondRepository.findById(smmBond.getId()).get();
        // Disconnect from session so that the updates on updatedSmmBond are not directly saved in db
        em.detach(updatedSmmBond);
        updatedSmmBond
            .enabled(UPDATED_ENABLED)
            .regType(UPDATED_REG_TYPE)
            .reg(UPDATED_REG)
            .valueType(UPDATED_VALUE_TYPE)
            .exprIn(UPDATED_EXPR_IN)
            .exprOut(UPDATED_EXPR_OUT);
        SmmBondDTO smmBondDTO = smmBondMapper.toDto(updatedSmmBond);

        restSmmBondMockMvc.perform(put("/api/smm-bonds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmBondDTO)))
            .andExpect(status().isOk());

        // Validate the SmmBond in the database
        List<SmmBond> smmBondList = smmBondRepository.findAll();
        assertThat(smmBondList).hasSize(databaseSizeBeforeUpdate);
        SmmBond testSmmBond = smmBondList.get(smmBondList.size() - 1);
        assertThat(testSmmBond.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testSmmBond.getRegType()).isEqualTo(UPDATED_REG_TYPE);
        assertThat(testSmmBond.getReg()).isEqualTo(UPDATED_REG);
        assertThat(testSmmBond.getValueType()).isEqualTo(UPDATED_VALUE_TYPE);
        assertThat(testSmmBond.getExprIn()).isEqualTo(UPDATED_EXPR_IN);
        assertThat(testSmmBond.getExprOut()).isEqualTo(UPDATED_EXPR_OUT);
    }

    @Test
    @Transactional
    public void updateNonExistingSmmBond() throws Exception {
        int databaseSizeBeforeUpdate = smmBondRepository.findAll().size();

        // Create the SmmBond
        SmmBondDTO smmBondDTO = smmBondMapper.toDto(smmBond);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmmBondMockMvc.perform(put("/api/smm-bonds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmBondDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmmBond in the database
        List<SmmBond> smmBondList = smmBondRepository.findAll();
        assertThat(smmBondList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSmmBond() throws Exception {
        // Initialize the database
        smmBondRepository.saveAndFlush(smmBond);

        int databaseSizeBeforeDelete = smmBondRepository.findAll().size();

        // Delete the smmBond
        restSmmBondMockMvc.perform(delete("/api/smm-bonds/{id}", smmBond.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SmmBond> smmBondList = smmBondRepository.findAll();
        assertThat(smmBondList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmmBond.class);
        SmmBond smmBond1 = new SmmBond();
        smmBond1.setId(1L);
        SmmBond smmBond2 = new SmmBond();
        smmBond2.setId(smmBond1.getId());
        assertThat(smmBond1).isEqualTo(smmBond2);
        smmBond2.setId(2L);
        assertThat(smmBond1).isNotEqualTo(smmBond2);
        smmBond1.setId(null);
        assertThat(smmBond1).isNotEqualTo(smmBond2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmmBondDTO.class);
        SmmBondDTO smmBondDTO1 = new SmmBondDTO();
        smmBondDTO1.setId(1L);
        SmmBondDTO smmBondDTO2 = new SmmBondDTO();
        assertThat(smmBondDTO1).isNotEqualTo(smmBondDTO2);
        smmBondDTO2.setId(smmBondDTO1.getId());
        assertThat(smmBondDTO1).isEqualTo(smmBondDTO2);
        smmBondDTO2.setId(2L);
        assertThat(smmBondDTO1).isNotEqualTo(smmBondDTO2);
        smmBondDTO1.setId(null);
        assertThat(smmBondDTO1).isNotEqualTo(smmBondDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(smmBondMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(smmBondMapper.fromId(null)).isNull();
    }
}
