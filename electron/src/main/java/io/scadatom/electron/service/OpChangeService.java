package io.scadatom.electron.service;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Service;

@Service
public class OpChangeService {

  private final Multimap<Long, ValueWatcher> valueWatcherMultimap =
      MultimapBuilder.treeKeys().arrayListValues().build();

  private final Multimap<Long, CommandWatcher> commandWatcherMultimap =
      MultimapBuilder.treeKeys().arrayListValues().build();

  private final OpDataService opDataService;

  public OpChangeService(OpDataService opDataService) {
    this.opDataService = opDataService;
  }

  /**
   * Value read from external device by master charger, reported here. Persist the value and
   * broadcast.
   */
  public void onValueRead(long particleId, String newValue, String source) {
    // if not equal to existing sts, send out update to observer
    opDataService.updateParticleOp(
        particleId,
        particleOp -> {
          particleOp.setValue(newValue);
          particleOp.setWrittenBy(source);
          particleOp.setWrittenDt(ZonedDateTime.now());
        });
    notifyValueChange(particleId, newValue);
  }

  private void notifyValueChange(long particleId, String newValue) {
    valueWatcherMultimap
        .get(particleId)
        .forEach(valueWatcher -> valueWatcher.onValueChange(newValue));
  }

  /** Command written from external device by slave charger, */
  public void onCommandWritten(long particleId, String newCommand, String source) {
    opDataService.updateParticleOp(
        particleId,
        particleOp -> {
          particleOp.setWrittenBy(source);
          particleOp.setWrittenDt(ZonedDateTime.now());
        });
    notifyCommandWritten(particleId, newCommand);
    // decide whether to accept the command, now it is always consent
    opDataService.updateParticleOp(
        particleId,
        particleOp -> {
          particleOp.setValue(newCommand);
        });
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
