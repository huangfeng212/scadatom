package io.scadatom.neutron;

/**
 * The OpState enumeration.
 * Charger can be Uninitialized, Initialized, Undefined, Disabled, Stopped, Started, Aborted
 * Device can be Uninitialized, Initialized, Disabled
 * Bond can be Uninitialized, Initialized, Disabled
 * Electron can be Uninitialized, Initialized, Stopped, Started
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
     * Applied to Charger
     */
    Undefined,
    /**
     * Applied to Charger, Device, Bond
     */
    Disabled,
    /**
     * Applied to Charger, Electron
     */
    Stopped,
    /**
     * Applied to Charger, Electron
     */
    Started,
    /**
     * Applied to Charger
     */
    Aborted
}
