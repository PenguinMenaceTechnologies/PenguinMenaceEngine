package net.pme.core.math;

/**
 * @author Johannes Schuck <jojoschuck@googlemail.com>
 * @version 0.1
 * @since 2014-03-12
 */
public class Vector4d extends Vector {

    /**
     * Create a null vector.
     */
    public Vector4d() {
        this(0.0, 0.0, 0.0, 0.0);
    }

    /**
     * Create a vector with the given parameters.
     *
     * @param x x-component
     * @param y y-component
     * @param z z-component
     * @param w w-component
     */
    public Vector4d(final double x, final double y, final double z, final double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Calculate the length of a vector.
     *
     * @param v The vector.
     * @return The length of the vector.
     */
    public static double length(final Vector4d v) {
        return Math.sqrt((v.x * v.x) + (v.y * v.y) + (v.z * v.z) + (v.w * v.w));
    }

    /**
     * Normalize a given vector (v / length(v)).
     *
     * @param v The vector to normalize.
     * @return The normalized vector.
     */
    public static Vector4d normalize(final Vector4d v) {
        return multiply(v, 1.0 / length(v));
    }

    /**
     * Add 2 vectors.
     *
     * @param vect1 The first vector.
     * @param vect2 The second vector.
     * @return The result.
     */
    public static Vector4d add(final Vector4d vect1, final Vector4d vect2) {
        return new Vector4d(vect1.x + vect2.x, vect1.y + vect2.y, vect1.z + vect2.z, vect1.w + vect2.w);
    }

    /**
     * Subtract 2 vectors.
     *
     * @param vect1 The first vector.
     * @param vect2 The second vector.
     * @return The result.
     */
    public static Vector4d subtract(final Vector4d vect1, final Vector4d vect2) {
        return new Vector4d(vect1.x - vect2.x, vect1.y - vect2.y, vect1.z - vect2.z, vect1.w - vect2.w);
    }

    /**
     * Multiply a vector with a scalar.
     *
     * @param vector The vector.
     * @param scalar The scalar.
     * @return The scaled vector.
     */
    public static Vector4d multiply(final Vector4d vector, final double scalar) {
        Vector4d out = new Vector4d();
        out.setX(vector.getX() * scalar);
        out.setY(vector.getY() * scalar);
        out.setZ(vector.getZ() * scalar);
        out.setW(vector.getW() * scalar);
        return out;
    }

    public double length() {
        return length(this);
    }

    public Vector4d normalize() {
        return normalize(this);
    }

    public Vector4d scale(final double scalar) {
        return multiply(this, scalar);
    }

    /**
     * Add 2 vectors.
     *
     * @param other The second vector.
     * @return The result.
     */
    public Vector4d add(final Vector4d other) {
        return add(this, other);
    }

    /**
     * Subtract 2 vectors.
     *
     * @param other The second vector.
     * @return The result.
     */
    public Vector4d subtract(final Vector4d other) {
        return subtract(this, other);
    }

    @Override
    public final double[] toArray() {
        double[] res = new double[4];
        res[0] = x;
        res[1] = y;
        res[2] = z;
        res[3] = w;
        return res;
    }

    @Override
    public final float[] toFloatArray() {
        float[] res = new float[4];
        res[0] = (float) x;
        res[1] = (float) y;
        res[2] = (float) z;
        res[3] = (float) w;
        return res;
    }

    /**
     * @return the x
     */
    public final double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public final void setX(final double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public final double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public final void setY(final double y) {
        this.y = y;
    }

    /**
     * @return the z
     */
    public final double getZ() {
        return z;
    }

    /**
     * @param z the z to set
     */
    public final void setZ(final double z) {
        this.z = z;
    }

    /**
     * @return the w
     */
    public final double getW() {
        return w;
    }

    /**
     * @param w the w to set
     */
    public final void setW(final double w) {
        this.w = w;
    }

    @Override
    public final boolean equals(final Object object) {
        if (object instanceof Vector4d) {
            Vector4d other = (Vector4d) object;

            if (Math.abs(other.x - this.x) < PRECISION
                    && Math.abs(other.y - this.y) < PRECISION
                    && Math.abs(other.z - this.z) < PRECISION
                    && Math.abs(other.w - this.w) < PRECISION) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> SHIFT));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> SHIFT));
        temp = Double.doubleToLongBits(z);
        result = prime * result + (int) (temp ^ (temp >>> SHIFT));
        temp = Double.doubleToLongBits(w);
        result = prime * result + (int) (temp ^ (temp >>> SHIFT));
        return result;
    }

    @Override
    public final String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }
}
