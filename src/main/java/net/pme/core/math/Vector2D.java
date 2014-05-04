package net.pme.core.math;

/**
 * @author Johannes Schuck <jojoschuck@googlemail.com>
 * @version 0.1
 * @since 2014-03-12
 */
public class Vector2D extends Vector {
    /**
     * Create a null vector.
     */
    public Vector2D() {
        this(0.0, 0.0);
    }

    /**
     * Create a vector with the given parameters.
     *
     * @param x x-component
     * @param y y-component
     */
    public Vector2D(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculate the length of a vector.
     *
     * @param v The vector.
     * @return The length of the vector.
     */
    public static double length(final Vector2D v) {
        return Math.sqrt((v.x * v.x) + (v.y * v.y));
    }

    /**
     * Normalize a given vector (v / length(v)).
     *
     * @param v The vector to normalize.
     * @return The normalized vector.
     */
    public static Vector2D normalize(final Vector2D v) {
        return multiply(v, 1.0 / length(v));
    }

    /**
     * Add 2 vectors.
     *
     * @param vect1 The first vector.
     * @param vect2 The second vector.
     * @return The result.
     */
    public static Vector2D add(final Vector2D vect1, final Vector2D vect2) {
        return new Vector2D(vect1.x + vect2.x, vect1.y + vect2.y);
    }

    /**
     * Subtract 2 vectors.
     *
     * @param vect1 The first vector.
     * @param vect2 The second vector.
     * @return The result.
     */
    public static Vector2D subtract(final Vector2D vect1, final Vector2D vect2) {
        return new Vector2D(vect1.x - vect2.x, vect1.y - vect2.y);
    }

    /**
     * Multiply a vector with a scalar.
     *
     * @param vector The vector.
     * @param scalar The scalar.
     * @return The scaled vector.
     */
    public static Vector2D multiply(final Vector2D vector, final double scalar) {
        Vector2D out = new Vector2D();
        out.setX(vector.getX() * scalar);
        out.setY(vector.getY() * scalar);
        return out;
    }

    /**
     * Calculate the dot product of 2 vectors (vect1*vect2).
     *
     * @param vect1 The first vector.
     * @param vect2 The second vector.
     * @return The product.
     */
    public static double dotProduct(final Vector2D vect1, final Vector2D vect2) {
        return vect1.x * vect2.x + vect1.y * vect2.y;
    }

    /**
     * Check equality of 2 vectors.
     *
     * @param a The first vector.
     * @param b The second vector.
     * @return The result.
     */
    public static boolean equals(final Vector2D a, final Vector2D b, final double precision) {
        return Math.abs(a.getX() - b.getX()) < precision && Math.abs(a.getY() - b.getY()) < precision;
    }

    public double length() {
        return length(this);
    }

    public Vector2D normalize() {
        return normalize(this);
    }

    public Vector2D scale(final double scalar) {
        return multiply(this, scalar);
    }

    /**
     * Add 2 vectors.
     *
     * @param other The second vector.
     * @return The result.
     */
    public Vector2D add(final Vector2D other) {
        return add(this, other);
    }

    /**
     * Subtract 2 vectors.
     *
     * @param other The second vector.
     * @return The result.
     */
    public Vector2D subtract(final Vector2D other) {
        return subtract(this, other);
    }

    /**
     * Calculate the dot product of 2 vectors (vect1*vect2).
     *
     * @param other The second vector.
     * @return The product.
     */
    public double dotProduct(final Vector2D other) {
        return dotProduct(this, other);
    }

    public final double[] toArray() {
        double[] res = new double[2];
        res[0] = x;
        res[1] = y;
        return res;
    }

    public final float[] toFloatArray() {
        float[] res = new float[2];
        res[0] = (float) x;
        res[1] = (float) y;
        return res;
    }

    /**
     * Check equality of 2 vectors.
     *
     * @param other The second vector.
     * @return The result.
     */
    public boolean equals(final Vector2D other, final double precision) {
        return equals(this, other, precision);
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

    @Override
    public final boolean equals(final Object object) {
        if (object instanceof Vector2D) {
            Vector2D other = (Vector2D) object;

            if (Math.abs(other.x - this.x) < PRECISION && Math.abs(other.y - this.y) < PRECISION) {
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
        return result;
    }

    @Override
    public final String toString() {
        return "(" + x + ", " + y + ")";
    }
}
