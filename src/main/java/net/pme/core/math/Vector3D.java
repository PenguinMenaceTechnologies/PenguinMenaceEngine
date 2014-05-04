package net.pme.core.math;

/**
 * A three dimensional vector.
 *
 * @author Michael Fürst, Johannes Schuck
 * @version 1.0
 */
public class Vector3D extends Vector {

    /**
     * Create a null vector.
     */
    public Vector3D() {
        this(0.0, 0.0, 0.0);
    }

    /**
     * Create a vector with the given parameters.
     *
     * @param x x-component
     * @param y y-component
     * @param z z-component
     */
    public Vector3D(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Calculate the length of a vector.
     *
     * @param v The vector.
     * @return The length of the vector.
     */
    public static double length(final Vector3D v) {
        return Math.sqrt((v.x * v.x) + (v.y * v.y) + (v.z * v.z));
    }

    /**
     * Normalize a given vector (v / length(v)).
     *
     * @param v The vector to normalize.
     * @return The normalized vector.
     */
    public static Vector3D normalize(final Vector3D v) {
        return multiply(v, 1.0 / length(v));
    }

    /**
     * Add 2 vectors.
     *
     * @param vect1 The first vector.
     * @param vect2 The second vector.
     * @return The result.
     */
    public static Vector3D add(final Vector3D vect1, final Vector3D vect2) {
        return new Vector3D(vect1.x + vect2.x, vect1.y + vect2.y, vect1.z + vect2.z);
    }

    /**
     * Subtract 2 vectors.
     *
     * @param vect1 The first vector.
     * @param vect2 The second vector.
     * @return The result.
     */
    public static Vector3D subtract(final Vector3D vect1, final Vector3D vect2) {
        return new Vector3D(vect1.x - vect2.x, vect1.y - vect2.y, vect1.z - vect2.z);
    }

    /**
     * Multiply a vector with a scalar.
     *
     * @param vector The vector.
     * @param scalar The scalar.
     * @return The scaled vector.
     */
    public static Vector3D multiply(final Vector3D vector, final double scalar) {
        Vector3D out = new Vector3D();
        out.setX(vector.getX() * scalar);
        out.setY(vector.getY() * scalar);
        out.setZ(vector.getZ() * scalar);
        return out;
    }

    /**
     * Calculate the cross product of 2 vectors (vect1 x vect2).
     *
     * @param vect1 The first vector.
     * @param vect2 The second vector.
     * @return The product.
     */
    public static Vector3D crossProduct(final Vector3D vect1, final Vector3D vect2) {
        double t1 = vect1.y * vect2.z - vect1.z * vect2.y;
        double t2 = vect1.z * vect2.x - vect1.x * vect2.z;
        double t3 = vect1.x * vect2.y - vect1.y * vect2.x;
        return new Vector3D(t1, t2, t3);
    }

    /**
     * Calculate the dot product of 2 vectors (vect1*vect2).
     *
     * @param vect1 The first vector.
     * @param vect2 The second vector.
     * @return The product.
     */
    public static double dotProduct(final Vector3D vect1, final Vector3D vect2) {
        return vect1.x * vect2.x + vect1.y * vect2.y + vect1.z * vect2.z;
    }

    /**
     * Transform a vector by a matrix.
     *
     * @param v The vector to transform.
     * @param m The transformation matrix.
     * @return The transformed vector.
     */
    public static Vector3D transformCoords(final Vector3D v, final Matrix m) {
        double[][] tmp = m.getArray();
        Vector3D vReuslt = new Vector3D(v.x * tmp[0][0] + v.y * tmp[1][0] + v.z * tmp[2][0]
                + tmp[3][0], v.x * tmp[0][1] + v.y * tmp[1][1] + v.z * tmp[2][1] + tmp[3][1], v.x
                * tmp[0][2] + v.y * tmp[1][2] + v.z * tmp[2][2] + tmp[3][2]);
        double w = v.x * tmp[0][3] + v.y * tmp[1][3] + v.z * tmp[2][3] + tmp[3][3];
        if (w != 1.0f) {
            vReuslt = multiply(vReuslt, 1 / w);
        }

        return vReuslt;
    }

    /**
     * Transform normals.
     *
     * @param v The normal to transform.
     * @param m The transformation matrix.
     * @return The transformed normal.
     */
    public static Vector3D transformNormal(final Vector3D v, final Matrix m) {
        double dLength = length(v);
        if (dLength == 0.0) {
            return v;
        }
        double[][] tmp = m.getArray();
        Vector3D vReuslt = new Vector3D(v.x * tmp[0][0] + v.y * tmp[1][0] + v.z * tmp[2][0]
                + tmp[3][0], v.x * tmp[0][1] + v.y * tmp[1][1] + v.z * tmp[2][1] + tmp[3][1], v.x
                * tmp[0][2] + v.y * tmp[1][2] + v.z * tmp[2][2] + tmp[3][2]);
        double w = v.x * tmp[0][3] + v.y * tmp[1][3] + v.z * tmp[2][3] + tmp[3][3];
        if (w != 1.0f) {
            vReuslt = multiply(vReuslt, 1 / w);
        }

        return vReuslt;
    }

    /**
     * Check equality of 2 vectors.
     *
     * @param a The first vector.
     * @param b The second vector.
     * @return The result.
     */
    public static boolean equals(final Vector3D a, final Vector3D b, final double precision) {
        return Math.abs(a.getX() - b.getX()) < precision
                && Math.abs(a.getY() - b.getY()) < precision
                && Math.abs(a.getZ() - b.getZ()) < precision;
    }

    public double length() {
        return length(this);
    }

    public Vector3D normalize() {
        return normalize(this);
    }

    public Vector3D scale(final double scalar) {
        return multiply(this, scalar);
    }

    /**
     * Add 2 vectors.
     *
     * @param other The second vector.
     * @return The result.
     */
    public Vector3D add(final Vector3D other) {
        return add(this, other);
    }

    /**
     * Subtract 2 vectors.
     *
     * @param other The second vector.
     * @return The result.
     */
    public Vector3D subtract(final Vector3D other) {
        return subtract(this, other);
    }

    /**
     * Calculate the cross product of 2 vectors (vect1 x vect2).
     *
     * @param other The second vector.
     * @return The product.
     */
    public Vector3D crossProduct(final Vector3D other) {
        return crossProduct(this, other);
    }

    /**
     * Calculate the dot product of 2 vectors (vect1*vect2).
     *
     * @param other The second vector.
     * @return The product.
     */
    public double dotProduct(final Vector3D other) {
        return dotProduct(this, other);
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
     * Transform a vector by a matrix.
     *
     * @param m The transformation matrix.
     * @return The transformed vector.
     */
    public Vector3D transformCoords(final Matrix m) {
        return transformCoords(this, m);
    }

    /**
     * Transform normals.
     *
     * @param m The transformation matrix.
     * @return The transformed normal.
     */
    public Vector3D transformNormal(final Matrix m) {
        return transformNormal(this, m);
    }

    /**
     * Check equality of 2 vectors.
     *
     * @param other The second vector.
     * @return The result.
     */
    public boolean equals(final Vector3D other, final double precision) {
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
    public final boolean equals(final Object object) {
        if (object instanceof Vector3D) {
            Vector3D other = (Vector3D) object;

            if (Math.abs(other.x - this.x) < PRECISION
                    && Math.abs(other.y - this.y) < PRECISION
                    && Math.abs(other.z - this.z) < PRECISION) {
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
        return result;
    }

    @Override
    public final String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
