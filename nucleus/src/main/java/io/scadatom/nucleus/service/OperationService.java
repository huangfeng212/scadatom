package io.scadatom.nucleus.service;

import static io.scadatom.neutron.Intents.REGISTER_ELECTRON;
import static io.scadatom.neutron.OpResult.FAILURE;
import static io.scadatom.neutron.OpResult.SUCCESS;
import static io.scadatom.neutron.OpResult.TIMEOUT;
import static io.scadatom.nucleus.config.RabbitmqConfig.ROUTING_TO_ELECTRON;
import static io.scadatom.nucleus.config.RabbitmqConfig.TOPIC_SCADATOM;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.scadatom.neutron.ElectronInitReq;
import io.scadatom.neutron.ElectronOpDTO;
import io.scadatom.neutron.FlattenedMessage;
import io.scadatom.neutron.FlattenedMessageHandler;
import io.scadatom.neutron.Intents;
import io.scadatom.neutron.OpCtrlReq;
import io.scadatom.neutron.OpException;
import io.scadatom.neutron.OpResult;
import io.scadatom.neutron.OpViewReq;
import io.scadatom.neutron.ParticleOpDTO;
import io.scadatom.nucleus.domain.Electron;
import io.scadatom.nucleus.domain.Particle;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
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
  private final Map<String, Function<FlattenedMessage, FlattenedMessage>> inboundRequestHandlers =
      new HashMap<>();
  private final FlattenedMessageHandler flattenedMessageHandler;

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
    inboundRequestHandlers.put(REGISTER_ELECTRON, this::handleRegisterElectron);
    flattenedMessageHandler = new FlattenedMessageHandler(inboundRequestHandlers);
  }

  private FlattenedMessage handleRegisterElectron(FlattenedMessage flattenedMessage) {
    try {
      OpCtrlReq opCtrlReq = flattenedMessage.peel(OpCtrlReq.class);
      Optional<Electron> optionalElectron = electronRepository.findById(opCtrlReq.getId());
      if (optionalElectron.isPresent()) {
        return new FlattenedMessage(SUCCESS, makeConfig(optionalElectron.get()));
      } else {
        return new FlattenedMessage(OpResult.INVALID + ":id not found");
      }
    } catch (IOException e) {
      e.printStackTrace();
      return new FlattenedMessage(FAILURE + ":can not parse payload");
    }
  }

  private ElectronInitReq makeConfig(Electron electron) {
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

  public void initElectron(long id) throws OpException {
    try {
      Optional<Electron> optionalElectron = electronRepository.findById(id);
      if (optionalElectron.isPresent()) {
        Object resp =
            rabbitTemplate.convertSendAndReceive(
                TOPIC_SCADATOM,
                ROUTING_TO_ELECTRON + id,
                new FlattenedMessage(Intents.INIT_ELECTRON, makeConfig(optionalElectron.get()))
                    .flat());
        parseResp(resp, Void.class);
      } else {
        throw new OpException(OpResult.INVALID + ":id not exist");
      }
    } catch (Exception e) {
      throw new OpException(OpResult.FAILURE + ":" + e.getClass().getSimpleName());
    }
  }

  private <T> T parseResp(Object resp, Class<T> clazz) throws OpException {
    if (resp == null) {
      throw new OpException(TIMEOUT);
    }
    try {
      FlattenedMessage flattenedMessage = FlattenedMessage.inflate(resp.toString());
      if (!flattenedMessage.getTitle().equals(SUCCESS)) {
        throw new OpException(flattenedMessage.getTitle()); // specific error
      } else if (clazz != Void.class) {
        return flattenedMessage.peel(clazz);
      }
      return null;
    } catch (IOException e) {
      throw new OpException(FAILURE + ":can not parse payload");
    }
  }

  public ElectronOpDTO viewElectron(Long id) throws OpException {
    try {
      if (electronRepository.existsById(id)) {
        Object resp =
            rabbitTemplate.convertSendAndReceive(
                TOPIC_SCADATOM,
                ROUTING_TO_ELECTRON + id,
                new FlattenedMessage(Intents.VIEW_ELECTRON, new OpViewReq().id(id)).flat());
        return parseResp(resp, ElectronOpDTO.class);
      } else {
        throw new OpException(OpResult.INVALID + ":id not exist");
      }
    } catch (Exception e) {
      throw new OpException(OpResult.FAILURE + ":" + e.getClass().getSimpleName());
    }
  }

  public void ctrlElectron(OpCtrlReq opCtrlReq) throws OpException {
    try {
      Optional<Electron> optionalElectron = electronRepository.findById(opCtrlReq.getId());
      if (optionalElectron.isPresent()) {
        Electron electron = optionalElectron.get();
        Object resp =
            rabbitTemplate.convertSendAndReceive(
                TOPIC_SCADATOM,
                ROUTING_TO_ELECTRON + electron.getId(),
                new FlattenedMessage(
                        Intents.CTRL_ELECTRON,
                        new OpCtrlReq()
                            .id(electron.getId())
                            .command(opCtrlReq.getCommand())
                            .user(
                                SecurityContextHolder.getContext()
                                    .getAuthentication()
                                    .getPrincipal()
                                    .toString()))
                    .flat());
        parseResp(resp, Void.class);
      } else {
        throw new OpException(OpResult.INVALID + ":id not exist");
      }
    } catch (Exception e) {
      throw new OpException(OpResult.FAILURE + ":" + e.getClass().getSimpleName());
    }
  }

  public ParticleOpDTO viewParticle(Long id) throws OpException {
    try {
      Optional<Particle> optionalParticle = particleRepository.findById(id);
      if (optionalParticle.isPresent()) {
        Particle particle = optionalParticle.get();
        Object resp =
            rabbitTemplate.convertSendAndReceive(
                TOPIC_SCADATOM,
                ROUTING_TO_ELECTRON + particle.getElectron().getId(),
                new FlattenedMessage(Intents.VIEW_PARTICLE, new OpViewReq().id(id)).flat());
        return parseResp(resp, ParticleOpDTO.class);
      } else {
        throw new OpException(OpResult.INVALID + ":id not exist");
      }
    } catch (Exception e) {
      throw new OpException(OpResult.FAILURE + ":" + e.getClass().getSimpleName());
    }
  }

  public void ctrlParticle(OpCtrlReq opCtrlReq) throws OpException {
    try {
      Optional<Particle> optionalParticle = particleRepository.findById(opCtrlReq.getId());
      if (optionalParticle.isPresent()) {
        Particle particle = optionalParticle.get();
        Object resp =
            rabbitTemplate.convertSendAndReceive(
                TOPIC_SCADATOM,
                ROUTING_TO_ELECTRON + particle.getElectron().getId(),
                new FlattenedMessage(
                        Intents.CTRL_PARTICLE,
                        new OpCtrlReq()
                            .id(particle.getId())
                            .command(opCtrlReq.getCommand())
                            .user(
                                SecurityContextHolder.getContext()
                                    .getAuthentication()
                                    .getPrincipal()
                                    .toString()))
                    .flat());
        parseResp(resp, Void.class);
      } else {
        throw new OpException(OpResult.INVALID + ":id not exist");
      }
    } catch (Exception e) {
      throw new OpException(OpResult.FAILURE + ":" + e.getClass().getSimpleName());
    }
  }

  @RabbitListener(queues = "#{queueInboundRequest.name}")
  public String onInboundRequest(String message) {
    try {
      return flattenedMessageHandler.handle(message);
    } catch (IOException e) {
      return FAILURE + ":can not parse payload";
    }
  }
}