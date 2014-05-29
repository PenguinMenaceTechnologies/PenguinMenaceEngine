package net.pme.core.math;

import org.lwjgl.util.vector.Vector4f;

/**
 * @author Johannes Schuck <jojoschuck@googlemail.com>, Michael Fürst
 * @version 0.1
 * @since 2014-03-12
 */
public class Vector4d extends Vector<Vector4f> {

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

    @Override
    public double length() {
        return Math.sqrt(x*x+y*y+z*z+w*w);
    }

    @Override
    public Vector4d add(final Vector other) {
        x += other.x;
        y += other.y;
        z += other.z;
        w += other.w;
        return this;
    }

    @Override
    public Vector4d subtract(final Vector other) {
        x -= other.x;
        y -= other.y;
        z -= other.z;
        w -= other.w;
        return this;
    }

    @Override
    public double dotProduct(Vector other) {
        return 0;
    }

    @Override
    public boolean equals(Vector other, double precision) {
        return Math.abs(other.x - this.x) < precision
                && Math.abs(other.y - this.y) < precision
                && Math.abs(other.z - this.z) < precision
                && Math.abs(other.w - this.w) < precision;
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
    public final boolean equals(final Vector other) {
        return equals(other, PRECISION);
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

    @Override
    public Vector4f getLwjglVector() {
        return new Vector4f((float) x, (float) y, (float) z, (float) w);
    }

}
