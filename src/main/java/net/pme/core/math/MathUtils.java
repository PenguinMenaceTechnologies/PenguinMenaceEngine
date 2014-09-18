package net.pme.core.math;

/**
 * Math constants.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public final class MathUtils {
    private static final double HALF_CIRCLE = 180;
    /**
     * Transform degree to radial by multiplying with this.
     */
    public static final double DEG2RAD = Math.PI / HALF_CIRCLE;
    /**
     * Transform radial to degree by multiplying with this.
     */
    public static final double RAD2DEG = HALF_CIRCLE / Math.PI;

    /**
     * Check if two values are close to each other.
     * @param a The value a.
     * @param b The value b.
     * @return Check if two values are close to each other.
     */
    public static final boolean isEqual(double a, double b) {
        return isEqual(a, b, 0.0001);
    }

    /**
     * Check if two values are closer to each other than the distance.
     * @param a The value a.
     * @param b The value b.
     * @param distance The maximum distance between a and b.
     * @return Check if two values are closer to each other than the distance.
     */
    public static final boolean isEqual(double a, double b, double distance) {
        return Math.abs(a - b) < distance;
    }

    /**
     * Private constructor for util classes.
     */
    private MathUtils() {

    }

    /**
     * Clamp the value v to lower and upper.
     * @param v the value to clamp.
     * @param lower The lower bound.
     * @param upper The upper bound.
     * @return The clamped value.
     */
    public static double clamp(double v, double lower, double upper) {
        return v < lower ? lower : v > upper ? upper : v;
    }
}
