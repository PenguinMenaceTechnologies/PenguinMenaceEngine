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
     * Private constructor for util classes.
     */
    private MathUtils() {

    }
}
