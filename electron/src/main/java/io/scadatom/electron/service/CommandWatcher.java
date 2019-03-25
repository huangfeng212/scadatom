package io.scadatom.electron.service;

public interface CommandWatcher {

    void onCommandWritten(String newCmd);

    String getCommand();

    void clearCommand();

    default boolean hasCmd() {
        return getCommand() == null;
    }
}
