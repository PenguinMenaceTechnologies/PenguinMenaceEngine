package net.pme.model;

import org.lwjgl.util.vector.Vector3f;

/**
 * A face of a Model.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public class Face {
	/**
	 * The vertex of a face.
	 */
	private Vector3f vertex = new Vector3f(); // no position just indexes!
	/**
	 * The normal of the face.
	 */
	private Vector3f normal = new Vector3f();
	/**
	 * The texture of the face.
	 */
	private Vector3f texture = new Vector3f();

	/**
	 * @return the vertex
	 */
	public Vector3f getVertex() {
		return vertex;
	}

	/**
	 * @param vertex the vertex to set
	 */
	public void setVertex(final Vector3f vertex) {
		this.vertex = vertex;
	}

	/**
	 * @return the normal
	 */
	public Vector3f getNormal() {
		return normal;
	}

	/**
	 * @param normal the normal to set
	 */
	public void setNormal(final Vector3f normal) {
		this.normal = normal;
	}

	/**
	 * @return the texture
	 */
	public Vector3f getTexture() {
		return texture;
	}

	/**
	 * @param texture the texture to set
	 */
	public void setTexture(final Vector3f texture) {
		this.texture = texture;
	}

	/**
	 * Creates a new Face.
	 * 
	 * @param vertex
	 *            The indexes.
	 * @param normal
	 *            The normal vectors.
	 * @param texture
	 * 			  The texture vector.
	 */
	Face(final Vector3f vertex, final Vector3f texture, final Vector3f normal) {
		this.vertex = vertex;
		this.normal = normal;
		this.texture = texture;
	}
}
