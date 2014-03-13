package net.pme.core.math;

import java.util.Iterator;
/**
 * @author Johannes Schuck <jojoschuck@googlemail.com>
 * @version 0.1
 * @since 2014-03-13
 */
public class Vector3i extends VectorI implements Iterable<Integer> {
    int vector3i[];

    /**
     * Creates a new three dimensional integer type vector.
     *
     */
    public Vector3i(){
        this(0, 0, 0);
    }

    public Vector3i(int x, int y, int z) {
        vector3i = new int[]{x, y, z};
    }

    public int getDimension() {
        return 3;
    }

    @Override
    public Iterator<Integer> iterator() {
        Iterator<Integer> it = new Iterator<Integer>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < 2;
            }

            @Override
            public Integer next() {
                return vector3i[currentIndex++];
            }

            @Override
            public void remove() {
                vector3i[currentIndex] = 0;
            }
        };
        return it;
    }

    public int getX() {
        return vector3i[0];
    }

    public void setX(int x) {
        this.vector3i[0] = x;
    }

    public int getY() {
        return vector3i[1];
    }

    public void setY(int y) {
        this.vector3i[1] = y;
    }

    public int getZ() {
        return vector3i[2];
    }

    public void setZ(int z) {
        this.vector3i[2] = z;
    }
}
