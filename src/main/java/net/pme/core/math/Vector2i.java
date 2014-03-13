package net.pme.core.math;

import java.util.Iterator;
/**
 * @author Johannes Schuck <jojoschuck@googlemail.com>
 * @version 0.1
 * @since 2014-03-13
 */
public class Vector2i extends VectorI implements Iterable<Integer> {
    int vector2i[];

    /**
     * Creates a new three dimensional integer type vector.
     *
     */
    public Vector2i(){
        this(0, 0);
    }

    public Vector2i(int x, int y) {
        vector2i = new int[]{x, y};
    }

    public int getDimension() {
        return 2;
    }

    @Override
    public Iterator<Integer> iterator() {
        Iterator<Integer> it = new Iterator<Integer>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < 1;
            }

            @Override
            public Integer next() {
                return vector2i[currentIndex++];
            }

            @Override
            public void remove() {
                vector2i[currentIndex] = 0;
            }
        };
        return it;
    }

    public int getX() {
        return vector2i[0];
    }

    public void setX(int x) {
        this.vector2i[0] = x;
    }

    public int getY() {
        return vector2i[1];
    }

    public void setY(int y) {
        this.vector2i[1] = y;
    }
}
