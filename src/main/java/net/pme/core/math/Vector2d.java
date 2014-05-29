package net.pme.core.math;

import org.lwjgl.util.vector.Vector2f;

/**
 * @author Johannes Schuck <jojoschuck@googlemail.com>, Michael Fürst
 * @version 0.1
 * @since 2014-03-12
 */
public class Vector2d extends Vector<Vector2f> {
    /**
     * Create a null vector.
     */
    public Vector2d() {
        this(0.0, 0.0);
    }

    /**
     * Create a vector with the given parameters.
     *
     * @param x x-component
     * @param y y-component
     */
    public Vector2d(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Create a vector from polar coordinate.
     * @param angle The angle.
     * @param radius The radius.
     * @return The vector.
     */
    public static Vector2d fromPolar(final double angle, final double radius) {
        return new Vector2d(radius * Math.cos(angle), radius * Math.sin(angle));
    }

    @Override
    public double length() {
        return Math.sqrt((x*x+y*y));
    }


    @Override
    public Vector2d add(final Vector other) {
        setX(x + other.x);
        setY(y + other.y);
        return this;
    }


    @Override
    public Vector2d subtract(final Vector other) {
        setX(x - other.x);
        setY(y - other.y);
        return this;
    }


    @Override
    public double dotProduct(final Vector other) {
        return this.x * other.x + this.y * other.y;
    }

    @Override
    public final double[] toArray() {
        double[] res = new double[2];
        res[0] = x;
        res[1] = y;
        return res;
    }

    @Override
    public final float[] toFloatArray() {
        float[] res = new float[2];
        res[0] = (float) x;
        res[1] = (float) y;
        return res;
    }

    @Override
    public boolean equals(final Vector other, final double precision) {
        return Math.abs(this.x - other.x) < precision && Math.abs(this.y - other.y) < precision;
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
    public final boolean equals(final Vector other) {
        return Math.abs(other.x - this.x) < PRECISION && Math.abs(other.y - this.y) < PRECISION;
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

    @Override
    public Vector2f getLwjglVector() {
        return new Vector2f((float) x, (float) y);
    }
}
