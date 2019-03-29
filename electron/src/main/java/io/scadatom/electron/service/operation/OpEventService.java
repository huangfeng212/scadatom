package io.scadatom.electron.service.operation;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import io.scadatom.electron.domain.ParticleOp;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OpEventService {
  private final Logger log = LoggerFactory.getLogger(OpEventService.class);

  private final Multimap<Long, ValueWatcher> valueWatcherMultimap =
      MultimapBuilder.treeKeys().arrayListValues().build();

  private final Multimap<Long, CommandWatcher> commandWatcherMultimap =
      MultimapBuilder.treeKeys().arrayListValues().build();

  private final OpRepoService opRepoService;
  private final OpConfigService opConfigService;

  public OpEventService(OpRepoService opRepoService, OpConfigService opConfigService) {
    this.opRepoService = opRepoService;
    this.opConfigService = opConfigService;
  }

  public void onValueRead(long particleId, double newValueDouble, String source) {
    opConfigService
        .getParticleOperation(particleId)
        .ifPresent(
            particleOperation -> {
              onValueRead(
                  particleId, particleOperation.getDecimalFormat().format(newValueDouble), source);
            });
  }

  /**
   * Value read from external device by master charger, reported here. Persist the value and
   * broadcast.
   */
  public void onValueRead(long particleId, String newValue, String source) {
    String currentParticleOpValue = opRepoService.getParticleOp(particleId).getValue();
    ParticleOp updateParticleOp =
        opRepoService.updateParticleOp(
            particleId,
            particleOp -> {
              particleOp.setValue(newValue);
              particleOp.setWrittenBy(source);
              particleOp.setWrittenDt(Instant.now());
            });
    log.debug("valueRead: {}", updateParticleOp);
    // if not equal to existing sts, send out update to observer
    if (!currentParticleOpValue.equals(newValue)) {
      notifyValueChange(particleId, newValue);
    }
  }

  private void notifyValueChange(long particleId, String newValue) {
    valueWatcherMultimap
        .get(particleId)
        .forEach(valueWatcher -> valueWatcher.onValueChange(newValue));
  }

    public void onCommandWritten(long particleId, double newCommandDouble, String source) {
      opConfigService.getParticleOperation(particleId).ifPresent(particleOperation -> {
          onCommandWritten(particleId,particleOperation.getDecimalFormat().format(newCommandDouble),source);
      });
    }

  /** Command written from external device by slave charger, */
  public void onCommandWritten(long particleId, String newCommand, String source) {
    ParticleOp updateParticleOp =
        opRepoService.updateParticleOp(
            particleId,
            particleOp -> {
              particleOp.setWrittenBy(source);
              particleOp.setWrittenDt(Instant.now());
            });
    log.debug("commandWritten: {}", updateParticleOp);
    notifyCommandWritten(particleId, newCommand);
    // decide whether to accept the command, now it is always consent
    updateParticleOp =
        opRepoService.updateParticleOp(
            particleId,
            particleOp -> {
              particleOp.setValue(newCommand);
            });
    log.debug("valueRead: {}", updateParticleOp);
    notifyValueChange(particleId, newCommand);
  }

  /** Command change requested by either user or external device received by slave charger */
  private void notifyCommandWritten(long particleId, String newCommand) {
    commandWatcherMultimap
        .get(particleId)
        .forEach(commandWatcher -> commandWatcher.onCommandWritten(newCommand));
  }

  public void addCommandWatcher(long particleId, CommandWatcher commandWatcher) {
    commandWatcherMultimap.put(particleId, commandWatcher);
  }

  public void removeCommandWatcher(long particleId, CommandWatcher commandWatcher) {
    commandWatcherMultimap.remove(particleId, commandWatcher);
  }

  public void removeValueWatcher(long particleId, ValueWatcher valueWatcher) {
    valueWatcherMultimap.remove(particleId, valueWatcher);
  }

  public void addValueWatcher(long particleId, ValueWatcher valueWatcher) {
    valueWatcherMultimap.put(particleId, valueWatcher);
  }

  public void dismissAll() {
    commandWatcherMultimap.clear();
    valueWatcherMultimap.clear();
  }
}
