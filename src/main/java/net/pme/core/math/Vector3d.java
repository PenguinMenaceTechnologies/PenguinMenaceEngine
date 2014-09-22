package net.pme.core.math;

import org.lwjgl.util.vector.Vector3f;

/**
 * A three dimensional vector.
 *
 * @author Michael Fürst, Johannes Schuck
 * @version 1.0
 */
public class Vector3d extends Vector<Vector3f, Vector3d> {

    /**
     * Create a null vector.
     */
    public Vector3d() {
        this(0.0, 0.0, 0.0);
    }

    /**
     * Create a vector from the gl vector.
     * @param vector The gl vector to use.
     */
    public Vector3d(Vector3f vector) {
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
    }

    /**
     * Create a vector with the given parameters.
     *
     * @param x x-component
     * @param y y-component
     * @param z z-component
     */
    public Vector3d(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Transform a vector by a matrix.
     *
     * @param m The transformation matrix.
     * @return This vector.
     */
    public Vector3d transformCoords(final Matrix m) {
        Vector3d v = this;
        double[][] tmp = m.getArray();
        set(v.x * tmp[0][0] + v.y * tmp[1][0] + v.z * tmp[2][0]
                + tmp[3][0], v.x * tmp[0][1] + v.y * tmp[1][1] + v.z * tmp[2][1] + tmp[3][1], v.x
                * tmp[0][2] + v.y * tmp[1][2] + v.z * tmp[2][2] + tmp[3][2]);
        double w = v.x * tmp[0][3] + v.y * tmp[1][3] + v.z * tmp[2][3] + tmp[3][3];
        if (w != 1.0f) {
            scale(1 / w);
        }

        return this;
    }

    /**
     * Set this vector to the values of another.
     * @param other The other vector.
     * @return This vector.
     */
    public Vector3d set(final Vector3d other) {
        return set(other.x, other.y, other.z);
    }

    /**
     * Set the values of this vector.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     * @return This vector.
     */
    public Vector3d set(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     * Transform a vector by a quaternion.
     *
     * @param q The transformation quaternion.
     * @return The transformed vector (v).
     */
    public Vector3d transformCoords(final Quaternion q) {
        Quaternion tmp = new Quaternion(q);
        Quaternion res = new Quaternion(q);

        // q * v * q.conjugate
        res = res.multiply(0, getX(), getY(), getZ()).multiply(tmp.conjugate());

        x = res.getX();
        y = res.getY();
        z = res.getZ();

        return this;
    }

    /**
     * Transform a normal by a quaternion.
     *
     * @param q The transformation quaternion.
     * @return The transformed vector (v).
     */
    public Vector3d transformNormal(final Quaternion q) {
        return transformCoords(q);
    }

    /**
     * Transform normals.
     *
     * @param m The transformation matrix.
     * @return The transformed normal (v).
     */
    public Vector3d transformNormal(final Matrix m) {
        Vector3d v = this;
        double dLength = length();
        if (dLength == 0.0) {
            return v;
        }
        double[][] tmp = m.getArray();
        set(v.x * tmp[0][0] + v.y * tmp[1][0] + v.z * tmp[2][0]
                + tmp[3][0], v.x * tmp[0][1] + v.y * tmp[1][1] + v.z * tmp[2][1] + tmp[3][1], v.x
                * tmp[0][2] + v.y * tmp[1][2] + v.z * tmp[2][2] + tmp[3][2]);
        double w = v.x * tmp[0][3] + v.y * tmp[1][3] + v.z * tmp[2][3] + tmp[3][3];
        if (w != 1.0f) {
            scale(1 / w);
        }

        return this;
    }

    @Override
    public double length() {
        return Math.sqrt((x * x) + (y * y) + (z * z));
    }

    @Override
    public Vector3d add(final Vector other) {
        x += other.x;
        y += other.y;
        z += other.z;
        return this;
    }

    @Override
    public Vector3d subtract(final Vector other) {
        x -= other.x;
        y -= other.y;
        z -= other.z;
        return this;
    }

    /**
     * Calculate the cross product of 2 vectors (vect1 x vect2).
     *
     * @param other The second vector.
     * @return The product.
     */
    public Vector3d crossProduct(final Vector3d other) {
        double t1 = this.y * other.z - this.z * other.y;
        double t2 = this.z * other.x - this.x * other.z;
        double t3 = this.x * other.y - this.y * other.x;
        setX(t1);
        setY(t2);
        setZ(t3);
        return this;
    }

    @Override
    public double dotProduct(final Vector other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public final double[] toArray() {
        double[] res = new double[3];
        res[0] = x;
        res[1] = y;
        res[2] = z;
        return res;
    }

    public final float[] toFloatArray() {
        float[] res = new float[3];
        res[0] = (float) x;
        res[1] = (float) y;
        res[2] = (float) z;
        return res;
    }

    /**
     * Check equality of 2 vectors.
     *
     * @param other The second vector.
     * @return The result.
     */
    public boolean equals(final Vector other, final double precision) {
        return Math.abs(this.x - other.x) < precision
                && Math.abs(this.y - other.y) < precision
                && Math.abs(this.z - other.z) < precision;
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

    @Override
    public final boolean equals(final Vector other) {
        return Math.abs(other.x - this.x) < PRECISION
                && Math.abs(other.y - this.y) < PRECISION
                && Math.abs(other.z - this.z) < PRECISION;
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
        return result;
    }

    @Override
    public final String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    @Override
    public Vector3f getLwjglVector() {
        return new Vector3f((float) x, (float) y, (float) z);
    }

    @Override
    public Vector3d clone() {
        Vector3d v = new Vector3d();
        return v.set(this);
    }
}
