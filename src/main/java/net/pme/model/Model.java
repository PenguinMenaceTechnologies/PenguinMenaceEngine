package net.pme.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import net.pme.utils.FileFormatException;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL32;
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

	private int vbo;
	private int ibo;
	private int nbo;
	private int tbo;
	
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

		vbo = GL15.glGenBuffers();
		ibo = GL15.glGenBuffers();
			
		FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.size()*3);
		FloatBuffer nbuffer = BufferUtils.createFloatBuffer(normals.size()*3);
		FloatBuffer tbuffer = BufferUtils.createFloatBuffer(textureCoords.size()*3);
		
		FloatBuffer ibuffer = BufferUtils.createFloatBuffer(faces.size()*(3*3));
		
		for(Vector3f t: vertices) {
			buffer.put(t.x);
			buffer.put(t.y);
			buffer.put(t.z);
		}
		for(Vector3f t: normals) {
			nbuffer.put(t.x);
			nbuffer.put(t.y);
			nbuffer.put(t.z);
		}
		for(Vector3f t: textureCoords) {
			tbuffer.put(t.x);
			tbuffer.put(t.y);
			tbuffer.put(t.z);
		}
		for(Face f: faces) {
			Vector3f n = f.getNormal();
			Vector3f t = f.getTexture();
			Vector3f v = f.getVertex();
			ibuffer.put(v.x);
			ibuffer.put(v.y);
			ibuffer.put(v.z);
			ibuffer.put(t.x);
			ibuffer.put(t.y);
			ibuffer.put(t.z);
			ibuffer.put(n.x);
			ibuffer.put(n.y);
			ibuffer.put(n.z);
		}
		
		buffer.rewind();
		ibuffer.rewind();
		nbuffer.rewind();
		tbuffer.rewind();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, nbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, nbuffer, GL15.GL_STATIC_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, tbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, tbuffer, GL15.GL_STATIC_DRAW);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, ibuffer, GL15.GL_STATIC_DRAW);
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
	 * @throws IOException When the file format is wrong.
	 */
	public static Model loadModel(final String path) throws IOException {
		return new Model(new File(path));
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
					Vector3f textureIndicies = new Vector3f(
							Float.parseFloat(line.split(" ")[1].split("/")[1]),
							Float.parseFloat(line.split(" ")[2].split("/")[1]),
							Float.parseFloat(line.split(" ")[3].split("/")[1]));
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
						textureIndicies = new Vector3f(
								Float.parseFloat(line.split(" ")[4].split("/")[1]),
								Float.parseFloat(line.split(" ")[1].split("/")[1]),
								Float.parseFloat(line.split(" ")[3].split("/")[1]));
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
	 */
	public void unloadModel() {
		GL15.glDeleteBuffers(ibo);
		GL15.glDeleteBuffers(vbo);
		GL15.glDeleteBuffers(nbo);
		GL15.glDeleteBuffers(tbo);
	}

	public void draw() {
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
	    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
	    GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
	    
	    GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
	    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, nbo);
	    GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0);
	    
	    GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, tbo);
	    GL11.glTexCoordPointer(3, GL11.GL_FLOAT, 0, 0);
	    
	    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
	  
	    int start = 0;
	    int end = faces.size()*3;
	    
	    //The alternate glDrawElements.    
	    GL12.glDrawRangeElements(GL11.GL_TRIANGLES, start, end, end-start,
						GL11.GL_UNSIGNED_INT, 0);
	    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		  
	}
}
