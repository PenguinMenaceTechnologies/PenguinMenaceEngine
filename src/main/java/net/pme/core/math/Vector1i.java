package net.pme.core.math;

import java.util.Iterator;

/**
 * @author Johannes Schuck <jojoschuck@googlemail.com>
 * @version 0.1
 * @since 2014-03-13
 */
public class Vector1i extends VectorI implements Iterable<Integer> {
    int x;

    /**
     * Creates a new three dimensional integer type vector.
     */
    public Vector1i() {
        this(0);
    }

    public Vector1i(int x) {
        this.x = x;
    }

    public int getDimension() {
        return 1;
    }

    @Override
    public Iterator<Integer> iterator() {
        Iterator<Integer> it = new Iterator<Integer>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Integer next() {
                return x;
            }

            @Override
            public void remove() {
                x = 0;
            }
        };
        return it;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
