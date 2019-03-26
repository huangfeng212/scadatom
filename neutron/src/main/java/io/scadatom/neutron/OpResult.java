package io.scadatom.neutron;

public enum  OpResult {
  /**
   * return by rpc
   */
  Success,Failure,Unsupported,Unknown,Other,
  /**
   * failed before rpc
   */
  Timeout,Invalid
}
