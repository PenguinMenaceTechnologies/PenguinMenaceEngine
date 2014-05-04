package net.pme.model;

import net.pme.core.math.Vector3D;
import org.lwjgl.util.vector.Vector3f;

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
    private Vector3D vertex = new Vector3D(); // no position just indexes!
    /**
     * The normal of the face.
     */
    private Vector3D normal = new Vector3D();
    /**
     * The texture of the face.
     */
    private Vector3D texture = new Vector3D();

    /**
     * Creates a new Face.
     *
     * @param vertex  The indexes.
     * @param normal  The normal vectors.
     * @param texture The texture vector.
     */
    Face(final Vector3D vertex, final Vector3D texture, final Vector3D normal) {
        this.vertex = vertex;
        this.normal = normal;
        this.texture = texture;
    }

    /**
     * @return the vertex
     */
    public Vector3D getVertex() {
        return vertex;
    }

    /**
     * @param vertex the vertex to set
     */
    public void setVertex(final Vector3D vertex) {
        this.vertex = vertex;
    }

    /**
     * @return the normal
     */
    public Vector3D getNormal() {
        return normal;
    }

    /**
     * @param normal the normal to set
     */
    public void setNormal(final Vector3D normal) {
        this.normal = normal;
    }

    /**
     * @return the texture
     */
    public Vector3D getTexture() {
        return texture;
    }

    /**
     * @param texture the texture to set
     */
    public void setTexture(final Vector3D texture) {
        this.texture = texture;
    }
}
