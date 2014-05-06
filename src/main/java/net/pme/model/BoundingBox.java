package net.pme.model;

import net.pme.core.math.Vector3d;

import java.util.List;

/**
 * @author <a href="mailto:mail@michaelfuerst.de>Michael Fürst</a>
 * @version 1.0
 * @since 2014-05-06
 */
public final class BoundingBox {
    /**
     * The edges of the bounding box.
     * Starting in the front left lower corner.
     * Starting at the ground side anti-clockwise and then the upper also anti-clockwise.
     */
    private Vector3d[] edges = new Vector3d[8];

    /**
     * Create a bounding box for the given model.
     *
     * This looks at all vertices to enable you to build hidden bounding boxes not included in the faces.
     *
     * @param model The model to create the bounding box for.
     */
    BoundingBox(Model model) {
        // Get the vertices of the model. This will also use invisible vertices. (Not used in a face.)
        List<Vector3d> vertices = model.getVertices();

        // Initialize the edges on zero.
        for (int i = 0; i < edges.length; i++) {
            edges[i] = new Vector3d(0,0,0);
        }

        // Create the bounding box by pushing out the box when a vertex more outside is detected.
        for (Vector3d v: vertices) {
            // Lower
            if (edges[0].getX() > v.getX()) {
                edges[0].setX(v.getX());
            }
            if (edges[0].getY() > v.getY()) {
                edges[0].setY(v.getY());
            }
            if (edges[0].getZ() > v.getZ()) {
                edges[0].setZ(v.getZ());
            }

            if (edges[1].getX() < v.getX()) {
                edges[1].setX(v.getX());
            }
            if (edges[1].getY() > v.getY()) {
                edges[1].setY(v.getY());
            }
            if (edges[1].getZ() > v.getZ()) {
                edges[1].setZ(v.getZ());
            }

            if (edges[2].getX() < v.getX()) {
                edges[2].setX(v.getX());
            }
            if (edges[2].getY() > v.getY()) {
                edges[2].setY(v.getY());
            }
            if (edges[2].getZ() < v.getZ()) {
                edges[2].setZ(v.getZ());
            }

            if (edges[3].getX() > v.getX()) {
                edges[3].setX(v.getX());
            }
            if (edges[3].getY() > v.getY()) {
                edges[3].setY(v.getY());
            }
            if (edges[3].getZ() < v.getZ()) {
                edges[3].setZ(v.getZ());
            }

            // Upper
            if (edges[4].getX() > v.getX()) {
                edges[4].setX(v.getX());
            }
            if (edges[4].getY() < v.getY()) {
                edges[4].setY(v.getY());
            }
            if (edges[4].getZ() > v.getZ()) {
                edges[4].setZ(v.getZ());
            }

            if (edges[5].getX() < v.getX()) {
                edges[5].setX(v.getX());
            }
            if (edges[5].getY() < v.getY()) {
                edges[5].setY(v.getY());
            }
            if (edges[5].getZ() > v.getZ()) {
                edges[5].setZ(v.getZ());
            }

            if (edges[6].getX() < v.getX()) {
                edges[6].setX(v.getX());
            }
            if (edges[6].getY() < v.getY()) {
                edges[6].setY(v.getY());
            }
            if (edges[6].getZ() < v.getZ()) {
                edges[6].setZ(v.getZ());
            }

            if (edges[7].getX() > v.getX()) {
                edges[7].setX(v.getX());
            }
            if (edges[7].getY() < v.getY()) {
                edges[7].setY(v.getY());
            }
            if (edges[7].getZ() < v.getZ()) {
                edges[7].setZ(v.getZ());
            }
        }
    }

    /**
     * Test if 2 bounding boxes intersect.
     * @param other The other bounding box.
     * @return Whether there is an intersection or not.
     */
    public final boolean intersect(BoundingBox other) {
        return false;
    }
}
