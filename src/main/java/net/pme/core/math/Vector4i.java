package net.pme.core.math;

import java.util.Iterator;
/**
 * @author Johannes Schuck <jojoschuck@googlemail.com>
 * @version 0.1
 * @since 2014-03-13
 */
public class Vector4i extends VectorI implements Iterable<Integer> {
    int vector4i[];

    /**
     * Creates a new three dimensional integer type vector.
     *
     */
    public Vector4i(){
        this(0, 0, 0, 0);
    }

    public Vector4i(int x, int y, int z, int w) {
        vector4i = new int[]{x, y, z, w};
    }

    public int getDimension() {
        return 4;
    }

    @Override
    public Iterator<Integer> iterator() {
        Iterator<Integer> it = new Iterator<Integer>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < 4;
            }

            @Override
            public Integer next() {
                return vector4i[currentIndex++];
            }

            @Override
            public void remove() {
                vector4i[currentIndex] = 0;
            }
        };
        return it;
    }

    public int getX() {
        return vector4i[0];
    }

    public void setX(int x) {
        this.vector4i[0] = x;
    }

    public int getY() {
        return vector4i[1];
    }

    public void setY(int y) {
        this.vector4i[1] = y;
    }

    public int getZ() {
        return vector4i[2];
    }

    public void setZ(int z) {
        this.vector4i[2] = z;
    }

    public int getW() {
        return vector4i[3];
    }

    public void setW(int w) {
        this.vector4i[4] = w;
    }
}
