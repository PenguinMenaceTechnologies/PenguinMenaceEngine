package net.pme.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import net.pme.core.utils.FileFormatException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 * Loads a model from a given File.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public class Model {
	private static final float COLOR = 0.5f;
	private static final int VECTOR_LENGTH = 4;
	private List<Vector3f> vertices = new ArrayList<Vector3f>();
	private List<Vector3f> normals = new ArrayList<Vector3f>();
	private List<Vector3f> textureCoords = new ArrayList<Vector3f>();
	private List<Vector3f> spaceVertices = new ArrayList<Vector3f>();
	private List<Face> faces = new ArrayList<Face>();
	private HashMap<String, Material> mtllibs = new HashMap<String, Material>();
	private HashMap<Integer, String> mtls = new HashMap<Integer, String>();
	private HashMap<Integer, String> smoothing = new HashMap<Integer, String>();

	/**
	 * Package visibility for model only.
	 * 
	 * @param file
	 *            The file from which to load the model.
	 * 
	 * @throws IOException
	 *             When the given file cannot be loaded.
	 */
	Model(final File file) throws IOException {
		loadModelFromFile(file);
	}

	/**
	 * @return the vertices
	 */
	public final List<Vector3f> getVertices() {
		return vertices;
	}

	/**
	 * @param vertices
	 *            the vertices to set
	 */
	public final void setVertices(final List<Vector3f> vertices) {
		this.vertices = vertices;
	}

	/**
	 * @return the normals
	 */
	public final List<Vector3f> getNormals() {
		return normals;
	}

	/**
	 * @param normals
	 *            the normals to set
	 */
	public final void setNormals(final List<Vector3f> normals) {
		this.normals = normals;
	}

	/**
	 * @return the textureCoords
	 */
	public final List<Vector3f> getTextureCoords() {
		return textureCoords;
	}

	/**
	 * @param textureCoords
	 *            the textureCoords to set
	 */
	public final void setTextureCoords(final List<Vector3f> textureCoords) {
		this.textureCoords = textureCoords;
	}

	/**
	 * @return the spaceVertices
	 */
	public final List<Vector3f> getSpaceVertices() {
		return spaceVertices;
	}

	/**
	 * @param spaceVertices
	 *            the spaceVertices to set
	 */
	public final void setSpaceVertices(final List<Vector3f> spaceVertices) {
		this.spaceVertices = spaceVertices;
	}

	/**
	 * @return the faces
	 */
	public final List<Face> getFaces() {
		return faces;
	}

	/**
	 * @param faces
	 *            the faces to set
	 */
	public final void setFaces(final List<Face> faces) {
		this.faces = faces;
	}

	/**
	 * @return the mtllibs
	 */
	public final HashMap<String, Material> getMtllibs() {
		return mtllibs;
	}

	/**
	 * @param mtllibs
	 *            the mtllibs to set
	 */
	public final void setMtllibs(final HashMap<String, Material> mtllibs) {
		this.mtllibs = mtllibs;
	}

	/**
	 * @return the mtls
	 */
	public final HashMap<Integer, String> getMtls() {
		return mtls;
	}

	/**
	 * @param mtls
	 *            the mtls to set
	 */
	public final void setMtls(final HashMap<Integer, String> mtls) {
		this.mtls = mtls;
	}

	/**
	 * @return the smoothing
	 */
	public final HashMap<Integer, String> getSmoothing() {
		return smoothing;
	}

	/**
	 * @param smoothing
	 *            the smoothing to set
	 */
	public final void setSmoothing(final HashMap<Integer, String> smoothing) {
		this.smoothing = smoothing;
	}

	/**
	 * Loads the model into the GL lists and compiles it.
	 * 
	 * @param path
	 *            The path to the file where the model is in.
	 * @return The id of the genList.
	 */
	public static int loadModel(final String path) {
		return loadModel(new File(path));
	}

	/**
	 * Loads the model into the GL lists and compiles it.
	 * 
	 * The model has other axis.
	 * 
	 * @param path
	 *            The path to the file where the model is in.
	 * @return The id of the genList.
	 */
	public static Integer loadModelSpecialCoords(final String path) {
		return loadModelSpecialCoords(new File(path));

	}

	/**
	 * Loads the model into the GL lists and compiles it.
	 * 
	 * The model has other axis.
	 * 
	 * @param file
	 *            The file where the model is in.
	 * @return The id of the genList.
	 */
	public static Integer loadModelSpecialCoords(final File file) {
		int model = GL11.glGenLists(1);
		GL11.glNewList(model, GL11.GL_COMPILE);

		Model m = null;
		try {
			m = new Model(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		GL11.glBegin(GL11.GL_TRIANGLES);
		int i = 0;
		boolean smoothing = true;
		for (Face face : m.faces) {
			if (m.smoothing.containsKey(i)) {
				if (m.smoothing.get(i).equals("off")) {
					smoothing = false;
				} else {
					smoothing = true;
				}
			}
			if (m.mtls.containsKey(i)) {
				m.mtllibs.get(m.mtls.get(i)).use();
			}
			GL11.glColor3f(COLOR, COLOR, COLOR);
			Vector3f n1 = m.normals.get((int) face.getNormal().y - 1);
			if (!smoothing) {
				GL11.glNormal3f(n1.y, n1.x, -n1.z);
			}
            if (face.getTexture() != null) {
			    Vector3f t1 = m.textureCoords.get((int) face.getTexture().y - 1);
			    GL11.glTexCoord3f(t1.y, t1.x, -t1.z);
            }
			Vector3f v1 = m.vertices.get((int) face.getVertex().y - 1);
			GL11.glVertex3f(v1.y, v1.x, -v1.z);
			Vector3f n2 = m.normals.get((int) face.getNormal().x - 1);
			if (!smoothing) {
				GL11.glNormal3f(n2.y, n2.x, -n2.z);
			}
            if (face.getTexture() != null) {
			    Vector3f t2 = m.textureCoords.get((int) face.getTexture().x - 1);
			    GL11.glTexCoord3f(t2.y, t2.x, -t2.z);
            }
			Vector3f v2 = m.vertices.get((int) face.getVertex().x - 1);
			GL11.glVertex3f(v2.y, v2.x, -v2.z);
			Vector3f n3 = m.normals.get((int) face.getNormal().z - 1);
			if (!smoothing) {
				GL11.glNormal3f(n3.y, n3.x, -n3.z);
			}
            if (face.getTexture() != null) {
			    Vector3f t3 = m.textureCoords.get((int) face.getTexture().z - 1);
			    GL11.glTexCoord3f(t3.y, t3.x, -t3.z);
            }
			Vector3f v3 = m.vertices.get((int) face.getVertex().z - 1);
			GL11.glVertex3f(v3.y, v3.x, -v3.z);
			i++;
		}
		Material.resetMaterial();
		GL11.glEnd();
		GL11.glEndList();
		return model;
	}

	/**
	 * Loads the model into the GL lists and compiles it.
	 * 
	 * @param file
	 *            The file where the model is in.
	 * @return The id of the genList.
	 */
	public static int loadModel(final File file) {
		int model = GL11.glGenLists(1);
		GL11.glNewList(model, GL11.GL_COMPILE);
		Model m = null;
		try {
			m = new Model(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		GL11.glBegin(GL11.GL_TRIANGLES);
		int i = 0;
		boolean smoothing = true;
		for (Face face : m.faces) {
			if (m.smoothing.containsKey(i)) {
				if (m.smoothing.get(i).equals("off")) {
					smoothing = false;
				} else {
					smoothing = true;
				}
			}
			if (m.mtls.containsKey(i)) {
				m.mtllibs.get(m.mtls.get(i)).use();
			}
			GL11.glColor3f(COLOR, COLOR, COLOR);
			Vector3f n1 = m.normals.get((int) face.getNormal().x - 1);
			if (!smoothing) {
				GL11.glNormal3f(n1.x, n1.y, n1.z);
			}
            if (face.getTexture() != null) {
			    Vector3f t1 = m.textureCoords.get((int) face.getTexture().x - 1);
			    GL11.glTexCoord3f(t1.x, -t1.y, t1.z);
            }
			Vector3f v1 = m.vertices.get((int) face.getVertex().x - 1);
			GL11.glVertex3f(v1.y, v1.x, -v1.z);
			Vector3f n2 = m.normals.get((int) face.getNormal().y - 1);
			if (!smoothing) {
				GL11.glNormal3f(n2.x, n2.y, n2.z);
            }
            if (face.getTexture() != null) {
			    Vector3f t2 = m.textureCoords.get((int) face.getTexture().y - 1);
			    GL11.glTexCoord3f(t2.x, -t2.y, t2.z);
            }
			Vector3f v2 = m.vertices.get((int) face.getVertex().y - 1);
			GL11.glVertex3f(v2.y, v2.x, -v2.z);
			Vector3f n3 = m.normals.get((int) face.getNormal().z - 1);
			if (!smoothing) {
				GL11.glNormal3f(n3.x, n3.y, n3.z);
			}
            if (face.getTexture() != null) {
			    Vector3f t3 = m.textureCoords.get((int) face.getTexture().z - 1);
			    GL11.glTexCoord3f(t3.x, -t3.y, t3.z);
            }
			Vector3f v3 = m.vertices.get((int) face.getVertex().z - 1);
			GL11.glVertex3f(v3.y, v3.x, -v3.z);
			i++;
		}
		Material.resetMaterial();
		GL11.glEnd();

		GL11.glEndList();
		return model;
	}

	/**
	 * Load a model from a file.
	 * 
	 * @param f
	 *            The file where the model is stored in.
	 * @throws IOException
	 *             When the file cannot be loaded.
	 */
	private void loadModelFromFile(final File f) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		Model m = this;
		String line;
		int lineNumber = 0;
		while ((line = reader.readLine()) != null) {
			try {
				while (line.startsWith(" ") || line.startsWith("\t")) {
					line = line.substring(1);
				}
				line = line.replaceAll("  ", " ");
				// parse the obj file
				if (line.startsWith("mtllib ")) {
					String[] splitline = line.split(" ");
					String mtl = splitline[1];
					String s = "";
					if (!splitline[1].startsWith("/")) {
						s = "/";
					}
					Material.loadMaterials(new File(f.getParent() + s + mtl),
							m.mtllibs);
				} else if (line.startsWith("usemtl ")) {
					String[] splitline = line.split(" ");
					String mtl = splitline[1];
					m.mtls.put(m.faces.size(), mtl);
				} else if (line.startsWith("s ")) {
					String[] splitline = line.split(" ");
					String smooth = splitline[1];
					m.smoothing.put(m.faces.size(), smooth);
				} else if (line.startsWith("v ")) {
					String[] splitline = line.split(" ");
					float x = Float.parseFloat(splitline[1]);
					float y = Float.parseFloat(splitline[2]);
					float z = Float.parseFloat(splitline[3]);
					m.vertices.add(new Vector3f(x, y, z));
				} else if (line.startsWith("vn ")) {
					String[] splitline = line.split(" ");
					float x = Float.parseFloat(splitline[1]);
					float y = Float.parseFloat(splitline[2]);
					float z = Float.parseFloat(splitline[3]);
					m.normals.add(new Vector3f(x, y, z));
				} else if (line.startsWith("vt ")) {
					String[] splitline = line.split(" ");
					float x = Float.parseFloat(splitline[1]);
					float y = Float.parseFloat(splitline[2]);
					float z = 0;
					if (splitline.length >= VECTOR_LENGTH) {
						z = Float.parseFloat(splitline[3]);
					}
					m.textureCoords.add(new Vector3f(x, y, z));
				} else if (line.startsWith("vp ")) {
					String[] splitline = line.split(" ");
					float x = Float.parseFloat(splitline[1]);
					float y = 0;
					if (splitline.length >= 3) {
						y = Float.parseFloat(splitline[2]);
					}
					float z = 0;
					if (splitline.length >= VECTOR_LENGTH) {
						z = Float.parseFloat(splitline[3]);
					}
					m.spaceVertices.add(new Vector3f(x, y, z));
				} else if (line.startsWith("f ")) {
					Vector3f vertexIndicies = new Vector3f(
							Float.parseFloat(line.split(" ")[1].split("/")[0]),
							Float.parseFloat(line.split(" ")[2].split("/")[0]),
							Float.parseFloat(line.split(" ")[3].split("/")[0]));
                    Vector3f textureIndicies;
                    if(line.split(" ")[1].split("/")[1].equals("")) {
                        textureIndicies = null;
                    } else {
                        textureIndicies = new Vector3f(
							Float.parseFloat(line.split(" ")[1].split("/")[1]),
							Float.parseFloat(line.split(" ")[2].split("/")[1]),
							Float.parseFloat(line.split(" ")[3].split("/")[1]));
                    }
					Vector3f normalIndicies = new Vector3f(
							Float.parseFloat(line.split(" ")[1].split("/")[2]),
							Float.parseFloat(line.split(" ")[2].split("/")[2]),
							Float.parseFloat(line.split(" ")[3].split("/")[2]));
					m.faces.add(new Face(vertexIndicies, textureIndicies,
							normalIndicies));
					if (line.split(" ").length == 5) {
						vertexIndicies = new Vector3f(
								Float.parseFloat(line.split(" ")[4].split("/")[0]),
								Float.parseFloat(line.split(" ")[1].split("/")[0]),
								Float.parseFloat(line.split(" ")[3].split("/")[0]));
                        if(line.split(" ")[1].split("/")[1].equals("")) {
                            textureIndicies = null;
                        } else {
                            textureIndicies = new Vector3f(
                                    Float.parseFloat(line.split(" ")[4].split("/")[1]),
                                    Float.parseFloat(line.split(" ")[1].split("/")[1]),
                                    Float.parseFloat(line.split(" ")[3].split("/")[1]));
                        }
						normalIndicies = new Vector3f(
								Float.parseFloat(line.split(" ")[4].split("/")[2]),
								Float.parseFloat(line.split(" ")[1].split("/")[2]),
								Float.parseFloat(line.split(" ")[3].split("/")[2]));
						m.faces.add(new Face(vertexIndicies, textureIndicies,
								normalIndicies));
					}
				}
			} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
				reader.close();
				throw new FileFormatException("Invalid obj file format.",
						f.getAbsolutePath(), lineNumber);
			}
			lineNumber++;
		}
		reader.close();
	}

	/**
	 * Unload the given model.
	 * 
	 * @param model
	 *            The ID of the model that should be removed.
	 */
	public static void unloadModel(final int model) {
		GL11.glDeleteLists(model, 1);
	}
}
