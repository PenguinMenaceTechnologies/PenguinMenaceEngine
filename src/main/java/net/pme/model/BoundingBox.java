package net.pme.model;

import net.pme.Game;
import net.pme.core.math.Matrix;
import net.pme.core.math.Vector3d;

import java.util.List;

/**
 * A bounding box that can be used for fast collision detection.
 *
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
    private final double sphereRadius;

    private final Model model;

    /**
     * Create a bounding box for the given model.
     *
     * This looks at all vertices to enable you to build hidden bounding boxes not included in the faces.
     *
     * @param model The model to create the bounding box for.
     */
    BoundingBox(Model model) {
        this.model = model;
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

        double calcShpere = 0;

        for (Vector3d edge: edges) {
            if (edge.length() > calcShpere) {
                calcShpere = edge.length();
            }
        }

        sphereRadius = calcShpere;
    }

    /**
     * Get a list of the bounding edges.
     * @return The edges.
     */
    public Vector3d[] getEdges() {
        return edges;
    }

    /**
     * Test if 2 bounding boxes intersect.
     * @param other The other bounding box.
     * @return Whether there is an intersection or not.
     */
    public final boolean intersect(Vector3d position, Vector3d front, Vector3d up, BoundingBox other, Vector3d position2, Vector3d front2, Vector3d up2) {
        Vector3d pd = position.clone().subtract(position2);
        if (pd.length() < sphereRadius) {
            Matrix rot = Matrix.axes(front.clone().crossProduct(up), up, front);
            Vector3d fd = front2.clone().transformCoords(rot);
            Vector3d ud = up2.clone().transformCoords(rot);
            rot = Matrix.axes(fd.clone().crossProduct(ud), ud, fd);
            for (Vector3d edge: other.edges) {
                edge = edge.clone().transformCoords(rot).add(pd);
                // TODO check if edge is inside this bounding box.
            }
            return false;
        } else {
            return false;
        }
    }
}
