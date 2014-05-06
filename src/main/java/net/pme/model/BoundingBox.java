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
     */
    private Vector3d[] edges;

    /**
     * Create a bounding box for the given model.
     * @param model The model to create the bounding box for.
     */
    BoundingBox(Model model) {
        List<Vector3d> vertices = model.getVertices();

        for (Vector3d v: vertices) {
            // TODO
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
