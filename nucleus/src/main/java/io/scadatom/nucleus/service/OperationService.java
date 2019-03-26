package io.scadatom.nucleus.service;

import static io.scadatom.nucleus.config.RabbitmqConfig.ROUTING_TO_ELECTRON;
import static io.scadatom.nucleus.config.RabbitmqConfig.TOPIC_SCADATOM;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.scadatom.domain.Particle;
import io.scadatom.neutron.ElectronInitReq;
import io.scadatom.neutron.ElectronOpDTO;
import io.scadatom.neutron.FlattenedMessage;
import io.scadatom.neutron.Intents;
import io.scadatom.neutron.OpCtrlReq;
import io.scadatom.neutron.OpResult;
import io.scadatom.nucleus.domain.Electron;
import io.scadatom.nucleus.repository.ElectronRepository;
import io.scadatom.nucleus.repository.ParticleRepository;
import io.scadatom.nucleus.service.mapper.ElectronMapper;
import io.scadatom.nucleus.service.mapper.ParticleMapper;
import io.scadatom.nucleus.service.mapper.SmmBondMapper;
import io.scadatom.nucleus.service.mapper.SmmChargerMapper;
import io.scadatom.nucleus.service.mapper.SmmDeviceMapper;
import io.scadatom.nucleus.service.mapper.SmsBondMapper;
import io.scadatom.nucleus.service.mapper.SmsChargerMapper;
import io.scadatom.nucleus.service.mapper.SmsDeviceMapper;
import io.scadatom.service.dto.ElectronCtrlReqDTO;
import io.scadatom.service.dto.ElectronCtrlRespDTO;
import io.scadatom.service.dto.ElectronViewRespDTO;
import io.scadatom.service.dto.ParticleCtrlReqDTO;
import io.scadatom.service.dto.ParticleCtrlRespDTO;
import io.scadatom.service.dto.ParticleViewRespDTO;
import io.scadatom.shared.message.InboundRequest;
import io.scadatom.shared.message.OutboundRequest;
import io.scadatom.shared.message.electron2nucleus.ElectronRequestInitReq;
import io.scadatom.shared.message.nucleus2electron.ElectronCtrlReq;
import io.scadatom.shared.message.nucleus2electron.ElectronViewReq;
import io.scadatom.shared.message.nucleus2electron.ParticleCtrlReq;
import io.scadatom.shared.message.nucleus2electron.ParticleViewReq;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OperationService {

  private final Logger log = LoggerFactory.getLogger(OperationService.class);
  private final ElectronRepository electronRepository;
  private final ParticleRepository particleRepository;
  private final ObjectMapper objectMapper;
  private final RabbitTemplate rabbitTemplate;
  private final ElectronMapper electronMapper;
  private final ParticleMapper particleMapper;
  private final SmmChargerMapper smmChargerMapper;
  private final SmmDeviceMapper smmDeviceMapper;
  private final SmmBondMapper smmBondMapper;
  private final SmsChargerMapper smsChargerMapper;
  private final SmsDeviceMapper smsDeviceMapper;
  private final SmsBondMapper smsBondMapper;

  public OperationService(
      ElectronRepository electronRepository,
      ParticleRepository particleRepository,
      ObjectMapper objectMapper,
      RabbitTemplate rabbitTemplate,
      ElectronMapper electronMapper,
      ParticleMapper particleMapper,
      SmmChargerMapper smmChargerMapper,
      SmmDeviceMapper smmDeviceMapper,
      SmmBondMapper smmBondMapper,
      SmsChargerMapper smsChargerMapper,
      SmsDeviceMapper smsDeviceMapper,
      SmsBondMapper smsBondMapper) {
    this.electronRepository = electronRepository;
    this.particleRepository = particleRepository;
    this.objectMapper = objectMapper;
    this.rabbitTemplate = rabbitTemplate;
    this.electronMapper = electronMapper;
    this.particleMapper = particleMapper;
    this.smmChargerMapper = smmChargerMapper;
    this.smmDeviceMapper = smmDeviceMapper;
    this.smmBondMapper = smmBondMapper;
    this.smsChargerMapper = smsChargerMapper;
    this.smsDeviceMapper = smsDeviceMapper;
    this.smsBondMapper = smsBondMapper;
  }

  public FlattenedMessage initElectron(OpCtrlReq opCtrlReq) throws IOException {
    Optional<Electron> optionalElectron = electronRepository.findById(opCtrlReq.getId());
    if (optionalElectron.isPresent()) {
      Object resp =
          rabbitTemplate.convertSendAndReceive(
              TOPIC_SCADATOM,
              ROUTING_TO_ELECTRON + opCtrlReq.getId(),
              new FlattenedMessage(Intents.initElectron, makeConfig(optionalElectron.get())));
      if (resp == null) {
        return new FlattenedMessage(OpResult.Timeout.name());
      }
      return FlattenedMessage.inflate(resp.toString());
    } else {
      return new FlattenedMessage(OpResult.Invalid.name() + ":id not exist");
    }
  }

  public ElectronInitReq makeConfig(Electron electron) {
    ElectronInitReq electronInitReq = new ElectronInitReq();
    electronInitReq.setElectronDTO(electronMapper.toDto(electron));
    electronInitReq.setParticleDTOS(
        electron.getParticles().stream().map(particleMapper::toDto).collect(Collectors.toSet()));
    electronInitReq.setSmmChargerDTO(smmChargerMapper.toDto(electron.getSmmCharger()));
    electronInitReq.setSmmDeviceDTOS(
        electron.getSmmCharger().getSmmDevices().stream()
            .map(smmDeviceMapper::toDto)
            .collect(Collectors.toSet()));
    electronInitReq.setSmmBondDTOS(
        electron.getSmmCharger().getSmmDevices().stream()
            .flatMap(smmDevice -> smmDevice.getSmmBonds().stream())
            .map(smmBondMapper::toDto)
            .collect(Collectors.toSet()));
    electronInitReq.setSmsChargerDTO(smsChargerMapper.toDto(electron.getSmsCharger()));
    electronInitReq.setSmsDeviceDTOS(
        electron.getSmsCharger().getSmsDevices().stream()
            .map(smsDeviceMapper::toDto)
            .collect(Collectors.toSet()));
    electronInitReq.setSmsBondDTOS(
        electron.getSmsCharger().getSmsDevices().stream()
            .flatMap(smsDevice -> smsDevice.getSmsBonds().stream())
            .map(smsBondMapper::toDto)
            .collect(Collectors.toSet()));
    return electronInitReq;
  }

  public ElectronOpDTO viewElectron(Long id) throws JsonProcessingException {
    if (electronRepository.existsById(id)) {
      Object resp =
          rabbitTemplate.convertSendAndReceive(
              TOPIC_SCADATOM,
              ROUTING_TO_ELECTRON + id,
              outboundPayloadOf("viewElectron", new ElectronViewReq(id)));
      return new ElectronViewRespDTO(resp == null ? TIMEOUT : resp.toString());
    } else {
      return new ElectronViewRespDTO(ID_NOT_EXIST);
    }
  }

  private String outboundPayloadOf(String intent, Object payload) throws JsonProcessingException {
    OutboundRequest outboundRequest = new OutboundRequest();
    outboundRequest.setIntent(intent);
    outboundRequest.setPayload(objectMapper.writeValueAsString(payload));
    return objectMapper.writeValueAsString(outboundRequest);
  }

  public ElectronCtrlRespDTO ctrlElectron(ElectronCtrlReqDTO electronCtrlReqDTO)
      throws JsonProcessingException {
    if (electronRepository.existsById(electronCtrlReqDTO.getId())) {
      Object resp =
          rabbitTemplate.convertSendAndReceive(
              TOPIC_SCADATOM,
              ROUTING_TO_ELECTRON + electronCtrlReqDTO.getId(),
              outboundPayloadOf("ctrlElectron", new ElectronCtrlReq(electronCtrlReqDTO.getId())));
      return new ElectronCtrlRespDTO(resp == null ? TIMEOUT : resp.toString());
    } else {
      return new ElectronCtrlRespDTO(ID_NOT_EXIST);
    }
  }

  public ParticleViewRespDTO viewParticle(Long id) throws JsonProcessingException {
    Optional<Particle> optionalParticle = particleRepository.findById(id);
    if (optionalParticle.isPresent()) {
      Particle particle = optionalParticle.get();
      Object resp =
          rabbitTemplate.convertSendAndReceive(
              TOPIC_SCADATOM,
              ROUTING_TO_ELECTRON + particle.getElectron().getId(),
              outboundPayloadOf("viewParticle", new ParticleViewReq(id)));
      return new ParticleViewRespDTO(
          resp == null ? TIMEOUT : resp.toString()); // need resp to respDTO and mapper
    }
    return new ParticleViewRespDTO(ID_NOT_EXIST);
  }

  public ParticleCtrlRespDTO ctrlParticle(ParticleCtrlReqDTO particleCtrlReqDTO)
      throws JsonProcessingException {
    Optional<Particle> optionalParticle = particleRepository.findById(particleCtrlReqDTO.getId());
    if (optionalParticle.isPresent()) {
      Particle particle = optionalParticle.get();
      Object resp =
          rabbitTemplate.convertSendAndReceive(
              TOPIC_SCADATOM,
              ROUTING_TO_ELECTRON + particle.getElectron().getId(),
              outboundPayloadOf(
                  "ctrlParticle",
                  new ParticleCtrlReq(
                      particleCtrlReqDTO.getId(), 888L, particleCtrlReqDTO.getCmd())));
      return new ParticleCtrlRespDTO(
          resp == null ? TIMEOUT : resp.toString()); // need resp to respDTO and mapper
    }
    return new ParticleCtrlRespDTO(ID_NOT_EXIST);
  }

  @RabbitListener(queues = "#{queueInboundRequest.name}")
  public String onInboundRequest(String message) throws IOException {
    InboundRequest inboundRequest = objectMapper.readValue(message, InboundRequest.class);
    String response = FAILURE;
    switch (inboundRequest.getIntent()) {
      case "requestInitElectron":
        ElectronRequestInitReq electronRequestInitReq =
            objectMapper.readValue(inboundRequest.getPayload(), ElectronRequestInitReq.class);
        Optional<ElectronInitReq> optionalElectronInitReq =
            electronRepository
                .findById(electronRequestInitReq.getId())
                .map(operationMapper::toElectronInitReq);
        if (optionalElectronInitReq.isPresent()) {
          ElectronInitReq electronInitReq = optionalElectronInitReq.get();
          rabbitTemplate.convertAndSend(
              TOPIC_SCADATOM,
              ROUTING_TO_ELECTRON + electronRequestInitReq.getId(),
              outboundPayloadOf("initElectron", electronInitReq));
          response = SUCCESS;
        }
        break;
    }
    return response;
  }
}
