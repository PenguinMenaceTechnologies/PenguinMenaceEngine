package net.pme.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Loads a model from a given File.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public class Model {
	List<Vector3f> vertices = new ArrayList<Vector3f>();
	List<Vector3f> normals = new ArrayList<Vector3f>();
	List<Vector3f> textureCoords = new ArrayList<Vector3f>();
	List<Vector3f> spaceVertices = new ArrayList<Vector3f>();
	List<Face> faces = new ArrayList<Face>();
	HashMap<String,Material> mtllibs = new HashMap<String,Material>();
	HashMap<Integer,String> mtls = new HashMap<Integer,String>();
	HashMap<Integer,String> smoothing = new HashMap<Integer,String>();

	Model() {
	}

	/**
	 * Loads the model into the GL lists and compiles it.
	 * 
	 * @param path
	 *            The path to the file where the model is in.
	 * @return The id of the genList.
	 */
	public static int loadModel(String path) {
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
	public static Integer loadModelSpecialCoords(String path) {
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
	public static Integer loadModelSpecialCoords(File file) {
		int model = glGenLists(1);
		glNewList(model, GL_COMPILE);
		{
			Model m = null;
			try {
				m = OBJLoader.loadModel(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return -1;
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
			glBegin(GL_TRIANGLES);
			int i = 0;
			boolean smoothing = true;
			for (Face face : m.faces) {
				if (m.smoothing.containsKey(i)) {
					if(m.smoothing.get(i).equals("off")) {
						smoothing = false;
					} else {
						smoothing = true;
					}
				}
				if (m.mtls.containsKey(i)) {
					m.mtllibs.get(m.mtls.get(i)).use();
				}
				GL11.glColor3f(0.5f, 0.5f, 0.5f);
				Vector3f n1 = m.normals.get((int) face.normal.y - 1);
				if (!smoothing) glNormal3f(n1.y, n1.x, -n1.z);
				Vector3f t1 = m.textureCoords.get((int) face.texture.y - 1);
				glTexCoord3f(t1.y, t1.x, -t1.z);
				Vector3f v1 = m.vertices.get((int) face.vertex.y - 1);
				glVertex3f(v1.y, v1.x, -v1.z);
				Vector3f n2 = m.normals.get((int) face.normal.x - 1);
				if (!smoothing) glNormal3f(n2.y, n2.x, -n2.z);
				Vector3f t2 = m.textureCoords.get((int) face.texture.x - 1);
				glTexCoord3f(t2.y, t2.x, -t2.z);
				Vector3f v2 = m.vertices.get((int) face.vertex.x - 1);
				glVertex3f(v2.y, v2.x, -v2.z);
				Vector3f n3 = m.normals.get((int) face.normal.z - 1);
				if (!smoothing) glNormal3f(n3.y, n3.x, -n3.z);
				Vector3f t3 = m.textureCoords.get((int) face.texture.z - 1);
				glTexCoord3f(t3.y, t3.x, -t3.z);
				Vector3f v3 = m.vertices.get((int) face.vertex.z - 1);
				glVertex3f(v3.y, v3.x, -v3.z);
				i++;
			}
			Material.resetMaterial();
			glEnd();
		}
		glEndList();
		return model;
	}

	/**
	 * Loads the model into the GL lists and compiles it.
	 * 
	 * @param file
	 *            The file where the model is in.
	 * @return The id of the genList.
	 */
	public static int loadModel(File file) {
		int model = glGenLists(1);
		glNewList(model, GL_COMPILE);
		{
			Model m = null;
			try {
				m = OBJLoader.loadModel(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return -1;
			} catch (IOException e) {
				e.printStackTrace();
				return -1;
			}
			glBegin(GL_TRIANGLES);
			int i = 0;
			boolean smoothing = true;
			for (Face face : m.faces) {
				if (m.smoothing.containsKey(i)) {
					if(m.smoothing.get(i).equals("off")) {
						smoothing = false;
					} else {
						smoothing = true;
					}
				}
				if (m.mtls.containsKey(i)) {
					m.mtllibs.get(m.mtls.get(i)).use();
				}
				GL11.glColor3f(0.5f, 0.5f, 0.5f);
				Vector3f n1 = m.normals.get((int) face.normal.x - 1);
				if (!smoothing) glNormal3f(n1.x, n1.y, n1.z);
				Vector3f t1 = m.textureCoords.get((int) face.texture.x - 1);
				glTexCoord3f(t1.x, t1.y, t1.z);
				Vector3f v1 = m.vertices.get((int) face.vertex.x - 1);
				glVertex3f(v1.x, v1.y, v1.z);
				Vector3f n2 = m.normals.get((int) face.normal.y - 1);
				if (!smoothing) glNormal3f(n2.x, n2.y, n2.z);
				Vector3f t2 = m.textureCoords.get((int) face.texture.y - 1);
				glTexCoord3f(t2.x, t2.y, t2.z);
				Vector3f v2 = m.vertices.get((int) face.vertex.y - 1);
				glVertex3f(v2.x, v2.y, v2.z);
				Vector3f n3 = m.normals.get((int) face.normal.z - 1);
				if (!smoothing) glNormal3f(n3.x, n3.y, n3.z);
				Vector3f t3 = m.textureCoords.get((int) face.texture.z - 1);
				glTexCoord3f(t3.x, t3.y, t3.z);
				Vector3f v3 = m.vertices.get((int) face.vertex.z - 1);
				glVertex3f(v3.x, v3.y, v3.z);
				i++;
			}
			Material.resetMaterial();
			glEnd();
		}
		glEndList();
		return model;
	}

	/**
	 * Unload the given model.
	 * 
	 * @param model
	 *            The ID of the model that should be removed.
	 */
	public static void unloadModel(int model) {
		glDeleteLists(model, 1);
	}
}
