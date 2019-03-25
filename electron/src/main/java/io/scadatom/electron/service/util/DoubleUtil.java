package io.scadatom.electron.service.util;

public class DoubleUtil {

  private static final double EPSILON = 1E-6;

  /**
   * Comparing 2 double values for equality in logic flow. If any param is NaN, then return false
   * meaning some or all param is not ready to work with
   *
   * @param d1 first double
   * @param d2 second double
   * @return whether the are equal with in EPSILON
   */
  public static boolean doubleEqual(double d1, double d2) {
    if (Double.isNaN(d1) || Double.isNaN(d2)) {
      return false;
    }
    return Math.abs(d1 - d2) < EPSILON;
  }

  /**
   * Convert double to int when casting to digital value
   *
   * @param d value to be converted
   * @return rounded int value
   */
  public static int toInt(double d) {
    return (int) Math.round(d);
  }
}
