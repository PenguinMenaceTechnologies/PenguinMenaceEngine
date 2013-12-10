package net.pme.model;

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
	Vector3f vertex = new Vector3f(); // no position just indexes!
	/**
	 * The normal of the face.
	 */
	Vector3f normal = new Vector3f();
	/**
	 * The texture of the face.
	 */
	Vector3f texture = new Vector3f();

	/**
	 * Creates a new Face.
	 * 
	 * @param vertex
	 *            The indexes.
	 * @param normal
	 *            The normal vectors.
	 * @param normal
	 */
	Face(Vector3f vertex, Vector3f texture, Vector3f normal) {
		this.vertex = vertex;
		this.normal = normal;
		this.texture = texture;
	}
}
