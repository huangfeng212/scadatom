package io.scadatom.nucleus.web.rest;

import io.scadatom.neutron.ElectronDTO;
import io.scadatom.nucleus.NucleusApp;

import io.scadatom.nucleus.domain.Electron;
import io.scadatom.nucleus.repository.ElectronRepository;
import io.scadatom.nucleus.service.ElectronService;
import io.scadatom.nucleus.service.mapper.ElectronMapper;
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
 * Test class for the ElectronResource REST controller.
 *
 * @see ElectronResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NucleusApp.class)
public class ElectronResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ElectronRepository electronRepository;

    @Autowired
    private ElectronMapper electronMapper;

    @Autowired
    private ElectronService electronService;

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

    private MockMvc restElectronMockMvc;

    private Electron electron;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ElectronResource electronResource = new ElectronResource(electronService);
        this.restElectronMockMvc = MockMvcBuilders.standaloneSetup(electronResource)
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
    public static Electron createEntity(EntityManager em) {
        Electron electron = new Electron()
            .name(DEFAULT_NAME);
        return electron;
    }

    @Before
    public void initTest() {
        electron = createEntity(em);
    }

    @Test
    @Transactional
    public void createElectron() throws Exception {
        int databaseSizeBeforeCreate = electronRepository.findAll().size();

        // Create the Electron
        ElectronDTO electronDTO = electronMapper.toDto(electron);
        restElectronMockMvc.perform(post("/api/electrons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electronDTO)))
            .andExpect(status().isCreated());

        // Validate the Electron in the database
        List<Electron> electronList = electronRepository.findAll();
        assertThat(electronList).hasSize(databaseSizeBeforeCreate + 1);
        Electron testElectron = electronList.get(electronList.size() - 1);
        assertThat(testElectron.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createElectronWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = electronRepository.findAll().size();

        // Create the Electron with an existing ID
        electron.setId(1L);
        ElectronDTO electronDTO = electronMapper.toDto(electron);

        // An entity with an existing ID cannot be created, so this API call must fail
        restElectronMockMvc.perform(post("/api/electrons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electronDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Electron in the database
        List<Electron> electronList = electronRepository.findAll();
        assertThat(electronList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = electronRepository.findAll().size();
        // set the field null
        electron.setName(null);

        // Create the Electron, which fails.
        ElectronDTO electronDTO = electronMapper.toDto(electron);

        restElectronMockMvc.perform(post("/api/electrons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electronDTO)))
            .andExpect(status().isBadRequest());

        List<Electron> electronList = electronRepository.findAll();
        assertThat(electronList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllElectrons() throws Exception {
        // Initialize the database
        electronRepository.saveAndFlush(electron);

        // Get all the electronList
        restElectronMockMvc.perform(get("/api/electrons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(electron.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getElectron() throws Exception {
        // Initialize the database
        electronRepository.saveAndFlush(electron);

        // Get the electron
        restElectronMockMvc.perform(get("/api/electrons/{id}", electron.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(electron.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingElectron() throws Exception {
        // Get the electron
        restElectronMockMvc.perform(get("/api/electrons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateElectron() throws Exception {
        // Initialize the database
        electronRepository.saveAndFlush(electron);

        int databaseSizeBeforeUpdate = electronRepository.findAll().size();

        // Update the electron
        Electron updatedElectron = electronRepository.findById(electron.getId()).get();
        // Disconnect from session so that the updates on updatedElectron are not directly saved in db
        em.detach(updatedElectron);
        updatedElectron
            .name(UPDATED_NAME);
        ElectronDTO electronDTO = electronMapper.toDto(updatedElectron);

        restElectronMockMvc.perform(put("/api/electrons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electronDTO)))
            .andExpect(status().isOk());

        // Validate the Electron in the database
        List<Electron> electronList = electronRepository.findAll();
        assertThat(electronList).hasSize(databaseSizeBeforeUpdate);
        Electron testElectron = electronList.get(electronList.size() - 1);
        assertThat(testElectron.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingElectron() throws Exception {
        int databaseSizeBeforeUpdate = electronRepository.findAll().size();

        // Create the Electron
        ElectronDTO electronDTO = electronMapper.toDto(electron);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restElectronMockMvc.perform(put("/api/electrons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(electronDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Electron in the database
        List<Electron> electronList = electronRepository.findAll();
        assertThat(electronList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteElectron() throws Exception {
        // Initialize the database
        electronRepository.saveAndFlush(electron);

        int databaseSizeBeforeDelete = electronRepository.findAll().size();

        // Delete the electron
        restElectronMockMvc.perform(delete("/api/electrons/{id}", electron.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Electron> electronList = electronRepository.findAll();
        assertThat(electronList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Electron.class);
        Electron electron1 = new Electron();
        electron1.setId(1L);
        Electron electron2 = new Electron();
        electron2.setId(electron1.getId());
        assertThat(electron1).isEqualTo(electron2);
        electron2.setId(2L);
        assertThat(electron1).isNotEqualTo(electron2);
        electron1.setId(null);
        assertThat(electron1).isNotEqualTo(electron2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ElectronDTO.class);
        ElectronDTO electronDTO1 = new ElectronDTO();
        electronDTO1.setId(1L);
        ElectronDTO electronDTO2 = new ElectronDTO();
        assertThat(electronDTO1).isNotEqualTo(electronDTO2);
        electronDTO2.setId(electronDTO1.getId());
        assertThat(electronDTO1).isEqualTo(electronDTO2);
        electronDTO2.setId(2L);
        assertThat(electronDTO1).isNotEqualTo(electronDTO2);
        electronDTO1.setId(null);
        assertThat(electronDTO1).isNotEqualTo(electronDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(electronMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(electronMapper.fromId(null)).isNull();
    }
}
