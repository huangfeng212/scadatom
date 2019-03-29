package io.scadatom.neutron;

/**
 * The OpState enumeration.
 * Charger can be Uninitialized, Initialized, Disabled, Started, Aborted
 * Device can be Uninitialized, Initialized, Disabled
 * Bond can be Uninitialized, Initialized, Disabled
 * Electron can be Uninitialized, Initialized, Disabled, Started, Aborted
 * Particle can be Uninitialized, Initialized
 */
public enum OpState {
    /**
     * Applied to All
     */
    Uninitialized,
    /**
     * Applied to All
     */
    Initialized,
    /**
     * Not Used
     */
    Undefined,
    /**
     * Applied to Charger, Device, Bond, Electron
     */
    Disabled,
    /**
     * Not Used
     */
    Stopped,
    /**
     * Applied to Charger, Electron
     */
    Started,
    /**
     * Applied to Charger, Electron
     */
    Aborted
}
