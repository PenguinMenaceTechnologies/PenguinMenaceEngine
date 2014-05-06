package net.pme.model;

import net.pme.core.math.Vector3d;

/**
 * A face of a Model.
 *
 * @author Michael Fürst
 * @version 1.0
 */
class Face {
    /**
     * The vertex of a face.
     */
    private Vector3d vertex = new Vector3d(); // no position just indexes!
    /**
     * The normal of the face.
     */
    private Vector3d normal = new Vector3d();
    /**
     * The texture of the face.
     */
    private Vector3d texture = new Vector3d();

    /**
     * Creates a new Face.
     *
     * @param vertex  The indexes.
     * @param normal  The normal vectors.
     * @param texture The texture vector.
     */
    Face(final Vector3d vertex, final Vector3d texture, final Vector3d normal) {
        this.vertex = vertex;
        this.normal = normal;
        this.texture = texture;
    }

    /**
     * @return the vertex
     */
    public Vector3d getVertex() {
        return vertex;
    }

    /**
     * @param vertex the vertex to set
     */
    public void setVertex(final Vector3d vertex) {
        this.vertex = vertex;
    }

    /**
     * @return the normal
     */
    public Vector3d getNormal() {
        return normal;
    }

    /**
     * @param normal the normal to set
     */
    public void setNormal(final Vector3d normal) {
        this.normal = normal;
    }

    /**
     * @return the texture
     */
    public Vector3d getTexture() {
        return texture;
    }

    /**
     * @param texture the texture to set
     */
    public void setTexture(final Vector3d texture) {
        this.texture = texture;
    }
}
