package io.scadatom.electron.web.rest;

import io.scadatom.electron.ElectronApp;

import io.scadatom.electron.domain.ElectronOp;
import io.scadatom.electron.repository.ElectronOpRepository;
import io.scadatom.electron.service.ElectronOpService;
import io.scadatom.electron.service.mapper.ElectronOpMapper;
import io.scadatom.electron.web.rest.errors.ExceptionTranslator;

import io.scadatom.neutron.ElectronOpDTO;
import io.scadatom.neutron.OpState;
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
 * Test class for the ElectronOpResource REST controller.
 *
 * @see ElectronOpResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElectronApp.class)
public class ElectronOpResourceIntTest {

    private static final OpState DEFAULT_STATE = OpState.Uninitialized;
    private static final OpState UPDATED_STATE = OpState.Initialized;

    private static final ZonedDateTime DEFAULT_DT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ElectronOpRepository electronOpRepository;

    @Autowired
    private ElectronOpMapper electronOpMapper;

    @Autowired
    private ElectronOpService electronOpService;

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

    private MockMvc restElectronOpMockMvc;

    private ElectronOp electronOp;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ElectronOpResource electronOpResource = new ElectronOpResource(electronOpService);
        this.restElectronOpMockMvc = MockMvcBuilders.standaloneSetup(electronOpResource)
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
    public static ElectronOp createEntity(EntityManager em) {
        ElectronOp electronOp = new ElectronOp()
            .state(DEFAULT_STATE)
            .dt(DEFAULT_DT);
        return electronOp;
    }

    @Before
    public void initTest() {
        electronOp = createEntity(em);
    }

    @Test
    @Transactional
    public void createElectronOp() throws Exception {
        int databaseSizeBeforeCreate = electronOpRepository.findAll().size();

        // Create the ElectronOp
        ElectronOpDTO electronOpDTO = electronOpMapper.toDto(electronOp);
        restElectronOpMockMvc.perform(post("/api/electron-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electronOpDTO)))
            .andExpect(status().isCreated());

        // Validate the ElectronOp in the database
        List<ElectronOp> electronOpList = electronOpRepository.findAll();
        assertThat(electronOpList).hasSize(databaseSizeBeforeCreate + 1);
        ElectronOp testElectronOp = electronOpList.get(electronOpList.size() - 1);
        assertThat(testElectronOp.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testElectronOp.getDt()).isEqualTo(DEFAULT_DT);
    }

    @Test
    @Transactional
    public void createElectronOpWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = electronOpRepository.findAll().size();

        // Create the ElectronOp with an existing ID
        electronOp.setId(1L);
        ElectronOpDTO electronOpDTO = electronOpMapper.toDto(electronOp);

        // An entity with an existing ID cannot be created, so this API call must fail
        restElectronOpMockMvc.perform(post("/api/electron-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electronOpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ElectronOp in the database
        List<ElectronOp> electronOpList = electronOpRepository.findAll();
        assertThat(electronOpList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllElectronOps() throws Exception {
        // Initialize the database
        electronOpRepository.saveAndFlush(electronOp);

        // Get all the electronOpList
        restElectronOpMockMvc.perform(get("/api/electron-ops?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(electronOp.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].dt").value(hasItem(sameInstant(DEFAULT_DT))));
    }
    
    @Test
    @Transactional
    public void getElectronOp() throws Exception {
        // Initialize the database
        electronOpRepository.saveAndFlush(electronOp);

        // Get the electronOp
        restElectronOpMockMvc.perform(get("/api/electron-ops/{id}", electronOp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(electronOp.getId().intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.dt").value(sameInstant(DEFAULT_DT)));
    }

    @Test
    @Transactional
    public void getNonExistingElectronOp() throws Exception {
        // Get the electronOp
        restElectronOpMockMvc.perform(get("/api/electron-ops/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateElectronOp() throws Exception {
        // Initialize the database
        electronOpRepository.saveAndFlush(electronOp);

        int databaseSizeBeforeUpdate = electronOpRepository.findAll().size();

        // Update the electronOp
        ElectronOp updatedElectronOp = electronOpRepository.findById(electronOp.getId()).get();
        // Disconnect from session so that the updates on updatedElectronOp are not directly saved in db
        em.detach(updatedElectronOp);
        updatedElectronOp
            .state(UPDATED_STATE)
            .dt(UPDATED_DT);
        ElectronOpDTO electronOpDTO = electronOpMapper.toDto(updatedElectronOp);

        restElectronOpMockMvc.perform(put("/api/electron-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electronOpDTO)))
            .andExpect(status().isOk());

        // Validate the ElectronOp in the database
        List<ElectronOp> electronOpList = electronOpRepository.findAll();
        assertThat(electronOpList).hasSize(databaseSizeBeforeUpdate);
        ElectronOp testElectronOp = electronOpList.get(electronOpList.size() - 1);
        assertThat(testElectronOp.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testElectronOp.getDt()).isEqualTo(UPDATED_DT);
    }

    @Test
    @Transactional
    public void updateNonExistingElectronOp() throws Exception {
        int databaseSizeBeforeUpdate = electronOpRepository.findAll().size();

        // Create the ElectronOp
        ElectronOpDTO electronOpDTO = electronOpMapper.toDto(electronOp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restElectronOpMockMvc.perform(put("/api/electron-ops")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electronOpDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ElectronOp in the database
        List<ElectronOp> electronOpList = electronOpRepository.findAll();
        assertThat(electronOpList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteElectronOp() throws Exception {
        // Initialize the database
        electronOpRepository.saveAndFlush(electronOp);

        int databaseSizeBeforeDelete = electronOpRepository.findAll().size();

        // Delete the electronOp
        restElectronOpMockMvc.perform(delete("/api/electron-ops/{id}", electronOp.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ElectronOp> electronOpList = electronOpRepository.findAll();
        assertThat(electronOpList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ElectronOp.class);
        ElectronOp electronOp1 = new ElectronOp();
        electronOp1.setId(1L);
        ElectronOp electronOp2 = new ElectronOp();
        electronOp2.setId(electronOp1.getId());
        assertThat(electronOp1).isEqualTo(electronOp2);
        electronOp2.setId(2L);
        assertThat(electronOp1).isNotEqualTo(electronOp2);
        electronOp1.setId(null);
        assertThat(electronOp1).isNotEqualTo(electronOp2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ElectronOpDTO.class);
        ElectronOpDTO electronOpDTO1 = new ElectronOpDTO();
        electronOpDTO1.setId(1L);
        ElectronOpDTO electronOpDTO2 = new ElectronOpDTO();
        assertThat(electronOpDTO1).isNotEqualTo(electronOpDTO2);
        electronOpDTO2.setId(electronOpDTO1.getId());
        assertThat(electronOpDTO1).isEqualTo(electronOpDTO2);
        electronOpDTO2.setId(2L);
        assertThat(electronOpDTO1).isNotEqualTo(electronOpDTO2);
        electronOpDTO1.setId(null);
        assertThat(electronOpDTO1).isNotEqualTo(electronOpDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(electronOpMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(electronOpMapper.fromId(null)).isNull();
    }
}
