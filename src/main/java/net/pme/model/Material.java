/**
 * 
 */
package net.pme.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import net.pme.core.utils.FileFormatException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 * Models a material of a model.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
class Material {
	private String name;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the ambient
	 */
	public Vector3f getAmbient() {
		return ambient;
	}

	/**
	 * @return the diffuse
	 */
	public Vector3f getDiffuse() {
		return diffuse;
	}

	/**
	 * @return the specular
	 */
	public Vector3f getSpecular() {
		return specular;
	}

	/**
	 * @return the specularCoefficient
	 */
	public float getSpecularCoefficient() {
		return specularCoefficient;
	}

	/**
	 * @return the transparency
	 */
	public float getTransparency() {
		return transparency;
	}

	/**
	 * @return the illuminationModel
	 */
	public int getIlluminationModel() {
		return illuminationModel;
	}

	/**
	 * @return the ambientMap
	 */
	public Texture getAmbientMap() {
		return ambientMap;
	}

	/**
	 * @return the diffuseMap
	 */
	public Texture getDiffuseMap() {
		return diffuseMap;
	}

	/**
	 * @return the specularMap
	 */
	public Texture getSpecularMap() {
		return specularMap;
	}

	/**
	 * @return the specularHighlightMap
	 */
	public Texture getSpecularHighlightMap() {
		return specularHighlightMap;
	}

	/**
	 * @return the transparencyMap
	 */
	public Texture getTransparencyMap() {
		return transparencyMap;
	}

	/**
	 * @return the bumpMap
	 */
	public Texture getBumpMap() {
		return bumpMap;
	}

	/**
	 * @return the displacementMap
	 */
	public Texture getDisplacementMap() {
		return displacementMap;
	}

	/**
	 * @return the decalMap
	 */
	public Texture getDecalMap() {
		return decalMap;
	}

	private Vector3f ambient = new Vector3f(1, 1, 1);
	private Vector3f diffuse = new Vector3f(1, 1, 1);
	private Vector3f specular = new Vector3f(1, 1, 1);
	private float specularCoefficient = 0;
	private float transparency = 0;
	private int illuminationModel = 1;
	private Texture ambientMap = null;
	private Texture diffuseMap = null;
	private Texture specularMap = null;
	private Texture specularHighlightMap = null;
	private Texture transparencyMap = null;
	private Texture bumpMap = null;
	private Texture displacementMap = null;
	private Texture decalMap = null;

	/**
	 * Create a new material.
	 * 
	 * @param name
	 *            The name of the material.
	 */
	Material(final String name) {
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

	/**
	 * Create a material from a file.
	 * 
	 * @param f
	 *            The file.
	 * @param map
	 *            the material map where to add materials to.
	 * @throws IOException When 
	 */
	static void loadMaterials(final File f, final HashMap<String, Material> map)
			throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line;
		Material mtl = null;
		int lineNumber = 0;
		while ((line = reader.readLine()) != null) {
			try {
				while (line.startsWith(" ") || line.startsWith("\t")) {
					line = line.substring(1);
				}
				line = line.replaceAll("  ", " ");
				if (line.startsWith("newmtl ")) {
					String[] splitline = line.split(" ");
					if (mtl != null) {
						map.put(mtl.name, mtl);
					}
					mtl = new Material(splitline[1]);
				} else if (line.startsWith("Ka ")) {
					String[] splitline = line.split(" ");
					float r = Float.parseFloat(splitline[1]);
					float g = Float.parseFloat(splitline[2]);
					float b = Float.parseFloat(splitline[3]);
					mtl.ambient = new Vector3f(r, g, b);
				} else if (line.startsWith("Kd ")) {
					String[] splitline = line.split(" ");
					float r = Float.parseFloat(splitline[1]);
					float g = Float.parseFloat(splitline[2]);
					float b = Float.parseFloat(splitline[3]);
					mtl.diffuse = new Vector3f(r, g, b);
				} else if (line.startsWith("Ks ")) {
					String[] splitline = line.split(" ");
					float r = Float.parseFloat(splitline[1]);
					float g = Float.parseFloat(splitline[2]);
					float b = Float.parseFloat(splitline[3]);
					mtl.specular = new Vector3f(r, g, b);
				} else if (line.startsWith("Ns ")) {
					String[] splitline = line.split(" ");
					mtl.specularCoefficient = Float.parseFloat(splitline[1]);
				} else if (line.startsWith("d ") || line.startsWith("Tr ")) {
					String[] splitline = line.split(" ");
					mtl.transparency = Float.parseFloat(splitline[1]);
				} else if (line.startsWith("illum ")) {
					String[] splitline = line.split(" ");
					mtl.illuminationModel = Integer.parseInt(splitline[1]);
				} else if (line.startsWith("map_Ka ")) {
					String[] splitline = line.split(" ");
					String s = "";
					if (!splitline[1].startsWith("/")) {
						s = "/";
					}
					mtl.ambientMap = new Texture(f.getParent() + s
							+ splitline[1]);
				} else if (line.startsWith("map_Kd ")) {
					String[] splitline = line.split(" ");
					String s = "";
					if (!splitline[1].startsWith("/")) {
						s = "/";
					}
					mtl.diffuseMap = new Texture(f.getParent() + s
							+ splitline[1]);
				} else if (line.startsWith("map_Ks ")) {
					String[] splitline = line.split(" ");
					String s = "";
					if (!splitline[1].startsWith("/")) {
						s = "/";
					}
					mtl.specularMap = new Texture(f.getParent() + s
							+ splitline[1]);
				} else if (line.startsWith("map_Ns ")) {
					String[] splitline = line.split(" ");
					String s = "";
					if (!splitline[1].startsWith("/")) {
						s = "/";
					}
					mtl.specularHighlightMap = new Texture(f.getParent() + s
							+ splitline[1]);
				} else if (line.startsWith("map_bump ")
						|| line.startsWith("bump ")) {
					String[] splitline = line.split(" ");
					String s = "";
					if (!splitline[1].startsWith("/")) {
						s = "/";
					}
					mtl.bumpMap = new Texture(f.getParent() + s
							+ splitline[1]);
				} else if (line.startsWith("disp ")) {
					String[] splitline = line.split(" ");
					String s = "";
					if (!splitline[1].startsWith("/")) {
						s = "/";
					}
					mtl.displacementMap = new Texture(f.getParent() + s
							+ splitline[1]);
				} else if (line.startsWith("decal ")) {
					String[] splitline = line.split(" ");
					String s = "";
					if (!splitline[1].startsWith("/")) {
						s = "/";
					}
					mtl.decalMap = new Texture(f.getParent() + s
							+ splitline[1]);
				}
			} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
				reader.close();
				throw new FileFormatException("Invalid mtl file format.",
						f.getAbsolutePath(), lineNumber);
			}
			lineNumber++;
		}
		if (mtl != null) {
			map.put(mtl.name, mtl);
		}
		reader.close();
	}
}
