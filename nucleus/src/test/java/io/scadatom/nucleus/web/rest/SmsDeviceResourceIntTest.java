package io.scadatom.nucleus.web.rest;

import io.scadatom.neutron.SmsDeviceDTO;
import io.scadatom.nucleus.NucleusApp;

import io.scadatom.nucleus.domain.SmsDevice;
import io.scadatom.nucleus.domain.SmsCharger;
import io.scadatom.nucleus.repository.SmsDeviceRepository;
import io.scadatom.nucleus.service.SmsDeviceService;
import io.scadatom.nucleus.service.mapper.SmsDeviceMapper;
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
 * Test class for the SmsDeviceResource REST controller.
 *
 * @see SmsDeviceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NucleusApp.class)
public class SmsDeviceResourceIntTest {

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private SmsDeviceRepository smsDeviceRepository;

    @Autowired
    private SmsDeviceMapper smsDeviceMapper;

    @Autowired
    private SmsDeviceService smsDeviceService;

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

    private MockMvc restSmsDeviceMockMvc;

    private SmsDevice smsDevice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SmsDeviceResource smsDeviceResource = new SmsDeviceResource(smsDeviceService);
        this.restSmsDeviceMockMvc = MockMvcBuilders.standaloneSetup(smsDeviceResource)
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
    public static SmsDevice createEntity(EntityManager em) {
        SmsDevice smsDevice = new SmsDevice()
            .enabled(DEFAULT_ENABLED)
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS);
        // Add required entity
        SmsCharger smsCharger = SmsChargerResourceIntTest.createEntity(em);
        em.persist(smsCharger);
        em.flush();
        smsDevice.setSmsCharger(smsCharger);
        return smsDevice;
    }

    @Before
    public void initTest() {
        smsDevice = createEntity(em);
    }

    @Test
    @Transactional
    public void createSmsDevice() throws Exception {
        int databaseSizeBeforeCreate = smsDeviceRepository.findAll().size();

        // Create the SmsDevice
        SmsDeviceDTO smsDeviceDTO = smsDeviceMapper.toDto(smsDevice);
        restSmsDeviceMockMvc.perform(post("/api/sms-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsDeviceDTO)))
            .andExpect(status().isCreated());

        // Validate the SmsDevice in the database
        List<SmsDevice> smsDeviceList = smsDeviceRepository.findAll();
        assertThat(smsDeviceList).hasSize(databaseSizeBeforeCreate + 1);
        SmsDevice testSmsDevice = smsDeviceList.get(smsDeviceList.size() - 1);
        assertThat(testSmsDevice.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testSmsDevice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSmsDevice.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    public void createSmsDeviceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = smsDeviceRepository.findAll().size();

        // Create the SmsDevice with an existing ID
        smsDevice.setId(1L);
        SmsDeviceDTO smsDeviceDTO = smsDeviceMapper.toDto(smsDevice);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmsDeviceMockMvc.perform(post("/api/sms-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsDeviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsDevice in the database
        List<SmsDevice> smsDeviceList = smsDeviceRepository.findAll();
        assertThat(smsDeviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEnabledIsRequired() throws Exception {
        int databaseSizeBeforeTest = smsDeviceRepository.findAll().size();
        // set the field null
        smsDevice.setEnabled(null);

        // Create the SmsDevice, which fails.
        SmsDeviceDTO smsDeviceDTO = smsDeviceMapper.toDto(smsDevice);

        restSmsDeviceMockMvc.perform(post("/api/sms-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsDeviceDTO)))
            .andExpect(status().isBadRequest());

        List<SmsDevice> smsDeviceList = smsDeviceRepository.findAll();
        assertThat(smsDeviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = smsDeviceRepository.findAll().size();
        // set the field null
        smsDevice.setName(null);

        // Create the SmsDevice, which fails.
        SmsDeviceDTO smsDeviceDTO = smsDeviceMapper.toDto(smsDevice);

        restSmsDeviceMockMvc.perform(post("/api/sms-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsDeviceDTO)))
            .andExpect(status().isBadRequest());

        List<SmsDevice> smsDeviceList = smsDeviceRepository.findAll();
        assertThat(smsDeviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = smsDeviceRepository.findAll().size();
        // set the field null
        smsDevice.setAddress(null);

        // Create the SmsDevice, which fails.
        SmsDeviceDTO smsDeviceDTO = smsDeviceMapper.toDto(smsDevice);

        restSmsDeviceMockMvc.perform(post("/api/sms-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsDeviceDTO)))
            .andExpect(status().isBadRequest());

        List<SmsDevice> smsDeviceList = smsDeviceRepository.findAll();
        assertThat(smsDeviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSmsDevices() throws Exception {
        // Initialize the database
        smsDeviceRepository.saveAndFlush(smsDevice);

        // Get all the smsDeviceList
        restSmsDeviceMockMvc.perform(get("/api/sms-devices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smsDevice.getId().intValue())))
            .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }
    
    @Test
    @Transactional
    public void getSmsDevice() throws Exception {
        // Initialize the database
        smsDeviceRepository.saveAndFlush(smsDevice);

        // Get the smsDevice
        restSmsDeviceMockMvc.perform(get("/api/sms-devices/{id}", smsDevice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(smsDevice.getId().intValue()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSmsDevice() throws Exception {
        // Get the smsDevice
        restSmsDeviceMockMvc.perform(get("/api/sms-devices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSmsDevice() throws Exception {
        // Initialize the database
        smsDeviceRepository.saveAndFlush(smsDevice);

        int databaseSizeBeforeUpdate = smsDeviceRepository.findAll().size();

        // Update the smsDevice
        SmsDevice updatedSmsDevice = smsDeviceRepository.findById(smsDevice.getId()).get();
        // Disconnect from session so that the updates on updatedSmsDevice are not directly saved in db
        em.detach(updatedSmsDevice);
        updatedSmsDevice
            .enabled(UPDATED_ENABLED)
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS);
        SmsDeviceDTO smsDeviceDTO = smsDeviceMapper.toDto(updatedSmsDevice);

        restSmsDeviceMockMvc.perform(put("/api/sms-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsDeviceDTO)))
            .andExpect(status().isOk());

        // Validate the SmsDevice in the database
        List<SmsDevice> smsDeviceList = smsDeviceRepository.findAll();
        assertThat(smsDeviceList).hasSize(databaseSizeBeforeUpdate);
        SmsDevice testSmsDevice = smsDeviceList.get(smsDeviceList.size() - 1);
        assertThat(testSmsDevice.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testSmsDevice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSmsDevice.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void updateNonExistingSmsDevice() throws Exception {
        int databaseSizeBeforeUpdate = smsDeviceRepository.findAll().size();

        // Create the SmsDevice
        SmsDeviceDTO smsDeviceDTO = smsDeviceMapper.toDto(smsDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmsDeviceMockMvc.perform(put("/api/sms-devices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(smsDeviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmsDevice in the database
        List<SmsDevice> smsDeviceList = smsDeviceRepository.findAll();
        assertThat(smsDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSmsDevice() throws Exception {
        // Initialize the database
        smsDeviceRepository.saveAndFlush(smsDevice);

        int databaseSizeBeforeDelete = smsDeviceRepository.findAll().size();

        // Delete the smsDevice
        restSmsDeviceMockMvc.perform(delete("/api/sms-devices/{id}", smsDevice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SmsDevice> smsDeviceList = smsDeviceRepository.findAll();
        assertThat(smsDeviceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsDevice.class);
        SmsDevice smsDevice1 = new SmsDevice();
        smsDevice1.setId(1L);
        SmsDevice smsDevice2 = new SmsDevice();
        smsDevice2.setId(smsDevice1.getId());
        assertThat(smsDevice1).isEqualTo(smsDevice2);
        smsDevice2.setId(2L);
        assertThat(smsDevice1).isNotEqualTo(smsDevice2);
        smsDevice1.setId(null);
        assertThat(smsDevice1).isNotEqualTo(smsDevice2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmsDeviceDTO.class);
        SmsDeviceDTO smsDeviceDTO1 = new SmsDeviceDTO();
        smsDeviceDTO1.setId(1L);
        SmsDeviceDTO smsDeviceDTO2 = new SmsDeviceDTO();
        assertThat(smsDeviceDTO1).isNotEqualTo(smsDeviceDTO2);
        smsDeviceDTO2.setId(smsDeviceDTO1.getId());
        assertThat(smsDeviceDTO1).isEqualTo(smsDeviceDTO2);
        smsDeviceDTO2.setId(2L);
        assertThat(smsDeviceDTO1).isNotEqualTo(smsDeviceDTO2);
        smsDeviceDTO1.setId(null);
        assertThat(smsDeviceDTO1).isNotEqualTo(smsDeviceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(smsDeviceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(smsDeviceMapper.fromId(null)).isNull();
    }
}
