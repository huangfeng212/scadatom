package io.scadatom.neutron;

public class OpResult {
  /** return by rpc */
  public static final String SUCCESS = "SUCCESS";

  public static final String FAILURE = "FAILURE";
  public static final String UNSUPPORTED = "UNSUPPORTED";
  public static final String UNKNOWN = "UNKNOWN";
  public static final String OTHER = "OTHER";

  /** failed before rpc */
  public static final String TIMEOUT = "TIMEOUT";

  public static final String INVALID = "INVALID";
}
