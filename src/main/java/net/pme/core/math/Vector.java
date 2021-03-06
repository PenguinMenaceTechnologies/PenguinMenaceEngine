package net.pme.core.math;

/**
 * @author Johannes Schuck <jojoschuck@googlemail.com>, Michael Fürst
 * @version 0.1
 * @since 2014-03-12
 */
public abstract class Vector<T, X> implements Cloneable {
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
    public final X normalize() {
        return scale(1/length());
    }

    /**
     * Multiply a vector with a scalar.
     *
     * @param scalar The scalar.
     * @return The scaled vector.
     */
    public final X scale(final double scalar) {
        x = x * scalar;
        y = y * scalar;
        z = z * scalar;
        w = w * scalar;
        return (X) this;
    }

    /**
     * Add 2 vectors.
     *
     * @param other The second vector.
     * @return The result.
     */
    public abstract X add(final Vector other);

    /**
     * Subtract 2 vectors.
     *
     * @param other The second vector.
     * @return The result.
     */
    public abstract X subtract(final Vector other);

    /**
     * Calculate the dot product of 2 vectors (vect1*vect2).
     *
     * @param other The second vector.
     * @return The product.
     */
    public abstract double dotProduct(final Vector other);

    /**
     * Check equality of 2 vectors.
     *
     * @param other The second vector.
     * @return The result.
     */
    public abstract boolean equals(final Vector other, final double precision);

    public abstract boolean equals(final Vector other);

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

    /**
     * Transform the vector to a vector compatible with lwjgl.
     * @return The lwjgl vector.
     */
    public abstract T getLwjglVector();

    @Override
    public Vector clone() {
        try {
            return (Vector) super.clone();
        } catch (CloneNotSupportedException e){
            return null;
        }
    }

    /**
     * Get the angle the vector has in the xy-plane.
     * Internally calls atan2(y,z).
     * @return The angle in radians.
     */
    public double getAngleXYPlane() {
        return Math.atan2(y,x);
    }

    /**
     * Get the angle the vector has in the yz-plane.
     * Internally calls atan2(z,y).
     * @return The angle in radians.
     */
    public double getAngleYZPlane() {
        return Math.atan2(z, y);
    }

    /**
     * Get the angle the vector has in the zw-plane.
     * Internally calls atan2(w,z).
     * @return The angle in radians.
     */
    public double getAngleZWPlane() {
        return Math.atan2(w, z);
    }
}
