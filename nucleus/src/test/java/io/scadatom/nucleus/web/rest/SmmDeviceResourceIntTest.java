package io.scadatom.nucleus.web.rest;

import io.scadatom.neutron.SmmDeviceDTO;
import io.scadatom.nucleus.NucleusApp;

import io.scadatom.nucleus.domain.SmmDevice;
import io.scadatom.nucleus.domain.SmmCharger;
import io.scadatom.nucleus.repository.SmmDeviceRepository;
import io.scadatom.nucleus.service.SmmDeviceService;
import io.scadatom.nucleus.service.mapper.SmmDeviceMapper;
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
 * Test class for the SmmDeviceResource REST controller.
 *
 * @see SmmDeviceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NucleusApp.class)
public class SmmDeviceResourceIntTest {

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private SmmDeviceRepository smmDeviceRepository;

    @Autowired
    private SmmDeviceMapper smmDeviceMapper;

    @Autowired
    private SmmDeviceService smmDeviceService;

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

    private MockMvc restSmmDeviceMockMvc;

    private SmmDevice smmDevice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SmmDeviceResource smmDeviceResource = new SmmDeviceResource(smmDeviceService);
        this.restSmmDeviceMockMvc = MockMvcBuilders.standaloneSetup(smmDeviceResource)
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
    public static SmmDevice createEntity(EntityManager em) {
        SmmDevice smmDevice = new SmmDevice()
            .enabled(DEFAULT_ENABLED)
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS);
        // Add required entity
        SmmCharger smmCharger = SmmChargerResourceIntTest.createEntity(em);
        em.persist(smmCharger);
        em.flush();
        smmDevice.setSmmCharger(smmCharger);
        return smmDevice;
    }

    @Before
    public void initTest() {
        smmDevice = createEntity(em);
    }

    @Test
    @Transactional
    public void createSmmDevice() throws Exception {
        int databaseSizeBeforeCreate = smmDeviceRepository.findAll().size();

        // Create the SmmDevice
        SmmDeviceDTO smmDeviceDTO = smmDeviceMapper.toDto(smmDevice);
        restSmmDeviceMockMvc.perform(post("/api/smm-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmDeviceDTO)))
            .andExpect(status().isCreated());

        // Validate the SmmDevice in the database
        List<SmmDevice> smmDeviceList = smmDeviceRepository.findAll();
        assertThat(smmDeviceList).hasSize(databaseSizeBeforeCreate + 1);
        SmmDevice testSmmDevice = smmDeviceList.get(smmDeviceList.size() - 1);
        assertThat(testSmmDevice.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testSmmDevice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSmmDevice.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    public void createSmmDeviceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smmDeviceRepository.findAll().size();

        // Create the SmmDevice with an existing ID
        smmDevice.setId(1L);
        SmmDeviceDTO smmDeviceDTO = smmDeviceMapper.toDto(smmDevice);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmmDeviceMockMvc.perform(post("/api/smm-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmDeviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmmDevice in the database
        List<SmmDevice> smmDeviceList = smmDeviceRepository.findAll();
        assertThat(smmDeviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = smmDeviceRepository.findAll().size();
        // set the field null
        smmDevice.setEnabled(null);

        // Create the SmmDevice, which fails.
        SmmDeviceDTO smmDeviceDTO = smmDeviceMapper.toDto(smmDevice);

        restSmmDeviceMockMvc.perform(post("/api/smm-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmDeviceDTO)))
            .andExpect(status().isBadRequest());

        List<SmmDevice> smmDeviceList = smmDeviceRepository.findAll();
        assertThat(smmDeviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = smmDeviceRepository.findAll().size();
        // set the field null
        smmDevice.setName(null);

        // Create the SmmDevice, which fails.
        SmmDeviceDTO smmDeviceDTO = smmDeviceMapper.toDto(smmDevice);

        restSmmDeviceMockMvc.perform(post("/api/smm-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmDeviceDTO)))
            .andExpect(status().isBadRequest());

        List<SmmDevice> smmDeviceList = smmDeviceRepository.findAll();
        assertThat(smmDeviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = smmDeviceRepository.findAll().size();
        // set the field null
        smmDevice.setAddress(null);

        // Create the SmmDevice, which fails.
        SmmDeviceDTO smmDeviceDTO = smmDeviceMapper.toDto(smmDevice);

        restSmmDeviceMockMvc.perform(post("/api/smm-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmDeviceDTO)))
            .andExpect(status().isBadRequest());

        List<SmmDevice> smmDeviceList = smmDeviceRepository.findAll();
        assertThat(smmDeviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSmmDevices() throws Exception {
        // Initialize the database
        smmDeviceRepository.saveAndFlush(smmDevice);

        // Get all the smmDeviceList
        restSmmDeviceMockMvc.perform(get("/api/smm-devices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smmDevice.getId().intValue())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }
    
    @Test
    @Transactional
    public void getSmmDevice() throws Exception {
        // Initialize the database
        smmDeviceRepository.saveAndFlush(smmDevice);

        // Get the smmDevice
        restSmmDeviceMockMvc.perform(get("/api/smm-devices/{id}", smmDevice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(smmDevice.getId().intValue()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSmmDevice() throws Exception {
        // Get the smmDevice
        restSmmDeviceMockMvc.perform(get("/api/smm-devices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSmmDevice() throws Exception {
        // Initialize the database
        smmDeviceRepository.saveAndFlush(smmDevice);

        int databaseSizeBeforeUpdate = smmDeviceRepository.findAll().size();

        // Update the smmDevice
        SmmDevice updatedSmmDevice = smmDeviceRepository.findById(smmDevice.getId()).get();
        // Disconnect from session so that the updates on updatedSmmDevice are not directly saved in db
        em.detach(updatedSmmDevice);
        updatedSmmDevice
            .enabled(UPDATED_ENABLED)
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS);
        SmmDeviceDTO smmDeviceDTO = smmDeviceMapper.toDto(updatedSmmDevice);

        restSmmDeviceMockMvc.perform(put("/api/smm-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmDeviceDTO)))
            .andExpect(status().isOk());

        // Validate the SmmDevice in the database
        List<SmmDevice> smmDeviceList = smmDeviceRepository.findAll();
        assertThat(smmDeviceList).hasSize(databaseSizeBeforeUpdate);
        SmmDevice testSmmDevice = smmDeviceList.get(smmDeviceList.size() - 1);
        assertThat(testSmmDevice.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testSmmDevice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSmmDevice.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void updateNonExistingSmmDevice() throws Exception {
        int databaseSizeBeforeUpdate = smmDeviceRepository.findAll().size();

        // Create the SmmDevice
        SmmDeviceDTO smmDeviceDTO = smmDeviceMapper.toDto(smmDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmmDeviceMockMvc.perform(put("/api/smm-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smmDeviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmmDevice in the database
        List<SmmDevice> smmDeviceList = smmDeviceRepository.findAll();
        assertThat(smmDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSmmDevice() throws Exception {
        // Initialize the database
        smmDeviceRepository.saveAndFlush(smmDevice);

        int databaseSizeBeforeDelete = smmDeviceRepository.findAll().size();

        // Delete the smmDevice
        restSmmDeviceMockMvc.perform(delete("/api/smm-devices/{id}", smmDevice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SmmDevice> smmDeviceList = smmDeviceRepository.findAll();
        assertThat(smmDeviceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmmDevice.class);
        SmmDevice smmDevice1 = new SmmDevice();
        smmDevice1.setId(1L);
        SmmDevice smmDevice2 = new SmmDevice();
        smmDevice2.setId(smmDevice1.getId());
        assertThat(smmDevice1).isEqualTo(smmDevice2);
        smmDevice2.setId(2L);
        assertThat(smmDevice1).isNotEqualTo(smmDevice2);
        smmDevice1.setId(null);
        assertThat(smmDevice1).isNotEqualTo(smmDevice2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmmDeviceDTO.class);
        SmmDeviceDTO smmDeviceDTO1 = new SmmDeviceDTO();
        smmDeviceDTO1.setId(1L);
        SmmDeviceDTO smmDeviceDTO2 = new SmmDeviceDTO();
        assertThat(smmDeviceDTO1).isNotEqualTo(smmDeviceDTO2);
        smmDeviceDTO2.setId(smmDeviceDTO1.getId());
        assertThat(smmDeviceDTO1).isEqualTo(smmDeviceDTO2);
        smmDeviceDTO2.setId(2L);
        assertThat(smmDeviceDTO1).isNotEqualTo(smmDeviceDTO2);
        smmDeviceDTO1.setId(null);
        assertThat(smmDeviceDTO1).isNotEqualTo(smmDeviceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(smmDeviceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(smmDeviceMapper.fromId(null)).isNull();
    }
}
