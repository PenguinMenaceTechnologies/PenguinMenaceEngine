package net.pme.core.math;

/**
 * A quaternion.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public class Quaternion {
    private double s, x, y, z;

    /**
     * Create a quaternion.
     * @param s The angle component of the rotation.
     * @param x The x of the rotation axis.
     * @param y The y of the rotation axis.
     * @param z The z of the rotation axis.
     */
    public Quaternion(final double s, final double x, final double y, final double z) {
        this.s = s;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Create a new identical instance to the old one.
     * @param q The quaternion to copy.
     */
    public Quaternion(final Quaternion q) {
        this(q.s, q.x, q.y, q.z);
    }

    /**
     * Multiplies this quaternion with another one in the form of this = this * other.
     *
     * @param other Quaternion to multiply with.
     * @return This quaternion for chaining.
     */
    public Quaternion multiply(final Quaternion other) {
        final double newX = this.s * other.x + this.x * other.s + this.y * other.z - this.z * other.y;
        final double newY = this.s * other.y + this.y * other.s + this.z * other.x - this.x * other.z;
        final double newZ = this.s * other.z + this.z * other.s + this.x * other.y - this.y * other.x;
        final double newS = this.s * other.s - this.x * other.x - this.y * other.y - this.z * other.z;
        this.x = newX;
        this.y = newY;
        this.z = newZ;
        this.s = newS;
        return this;
    }
    /**
     * Multiplies this quaternion with another one in the form of this = this * other.
     *
     * @param s the s component of the other quaternion to multiply with.
     * @param x the x component of the other quaternion to multiply with.
     * @param y the y component of the other quaternion to multiply with.
     * @param z the z component of the other quaternion to multiply with.
     * @return This quaternion for chaining.
     */
    public Quaternion multiply(final double s, final double x, final double y, final double z) {
        final double newX = this.s * x + this.x * s + this.y * z - this.z * y;
        final double newY = this.s * y + this.y * s + this.z * x - this.x * z;
        final double newZ = this.s * z + this.z * s + this.x * y - this.y * x;
        final double newS = this.s * s - this.x * x - this.y * y - this.z * z;
        this.x = newX;
        this.y = newY;
        this.z = newZ;
        this.s = newS;
        return this;
    }

    /**
     * Normalizes this quaternion to unit length.
     * @return the quaternion for chaining.
     */
    public Quaternion normalize() {
        double len = length2();
        if (len != 0 && !MathUtils.isEqual(len, 1)) {
            len = Math.sqrt(len);
            s /= len;
            x /= len;
            y /= len;
            z /= len;
        }
        return this;
    }


    /** Conjugate the quaternion.
     *
     * @return This quaternion for chaining
     */
    public Quaternion conjugate () {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    /**
     * @return the length of this quaternion.
     */
    public double length() {
        return Math.sqrt(length2());
    }

    /**
     * @return the length of this quaternion without square root.
     */
    public double length2() {
        return x * x + y * y + z * z + s * s;
    }


    /**
     * Convert a Quaternion into a matrix.
     * @return The matrix representing the quaternion.
     */
    public Matrix toMatrix() {
        return new Matrix(1-2*(y*y+z*z), -2*s*z+2*x*y, 2*s*y+2*x*z, 0.0,
                2*s*z+2*x*y, 1-2*(x*x+z*z), -2*s*x+2*y*z, 0.0,
                -2*s*y+2*x*z, 2*s*x+2*y*z, 1-2*(x*x+y*y), 0.0,
                0.0, 0.0, 0.0, 1.0);
    }

    /**
     * The x component.
     * @return The x component.
     */
    public double getX() {
        return x;
    }

    /**
     * The y component.
     * @return The y component.
     */
    public double getY() {
        return y;
    }

    /**
     * The z component.
     * @return The z component.
     */
    public double getZ() {
        return z;
    }

    /**
     * The s component.
     * @return The s component.
     */
    public double getS() {
        return s;
    }
}
