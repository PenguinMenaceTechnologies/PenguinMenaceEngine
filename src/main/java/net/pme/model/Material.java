/**
 * 
 */
package net.pme.model;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 * Models a material of a model.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
class Material {
	String name;
	Vector3f ambient = new Vector3f(1, 1, 1);
	Vector3f diffuse = new Vector3f(1, 1, 1);
	Vector3f specular = new Vector3f(1, 1, 1);
	float specularCoefficient = 0;
	float transparency = 0;
	int illuminationModel = 1;
	Texture ambientMap = null;
	Texture diffuseMap = null;
	Texture specularMap = null;
	Texture specularHighlightMap = null;
	Texture transparencyMap = null;
	Texture bumpMap = null;
	Texture displacementMap = null;
	Texture decalMap = null;

	Material(String name) {
		this.name = name;
	}

	/**
	 * Use this material. Set LWJGL up for the material.
	 */
	void use() {
		Material.resetMaterial();
		if (diffuseMap != null) {
			diffuseMap.bind(GL11.GL_TEXTURE_2D);
		}
		// TODO use a material
	}

	/**
	 * Reset all settings a material can do to default values.
	 */
	static void resetMaterial() {
		Texture.unbind(GL11.GL_TEXTURE_2D);
		// TODO reset all parameters a material can change.
	}
}
