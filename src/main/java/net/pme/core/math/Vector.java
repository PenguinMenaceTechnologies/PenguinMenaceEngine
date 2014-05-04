package net.pme.core.math;

/**
 * @author Johannes Schuck <jojoschuck@googlemail.com>
 * @version 0.1
 * @since 2014-03-12
 */
public abstract class Vector {
    protected static final int SHIFT = 32;
    protected static final double PRECISION = 10E-9;

    /**
     * The x component.
     */
    protected double x;

    /**
     * The y component.
     */
    protected double y;

    /**
     * The z component.
     */
    protected double z;

    /**
     * The w component.
     */
    protected double w;

    protected Vector() {

    }

    /**
     * Calculate the length of a vector. (||v||)
     *
     * @return The length of the vector.
     */
    public abstract double length();

    /**
     * Normalize a given vector (v / length(v)).
     *
     * @return The normalized vector.
     */
    public abstract Vector normalize();

    /**
     * Multiply a vector with a scalar.
     *
     * @param scalar The scalar.
     * @return The scaled vector.
     */
    public abstract Vector scale(final double scalar);

    public abstract boolean equals(Object other);

    public abstract int hashCode();

    public abstract String toString();

    /**
     * Returns an array of the vector.
     *
     * @return The array of the vector.
     */
    public abstract double[] toArray();

    /**
     * Returns a float array of the vector.
     *
     * @return The float array of the vector.
     */
    public abstract float[] toFloatArray();

}
