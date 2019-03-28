package io.scadatom.electron.service.operation;

public interface CommandWatcher {

    void onCommandWritten(String newCmd);

    String getCommand();

    void clearCommand();

    default boolean hasPendingCommand() {
        return getCommand() != null;
    }
}
