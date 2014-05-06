package net.pme.model;

import net.pme.core.math.Vector3d;
import net.pme.core.utils.FileFormatException;
import org.lwjgl.opengl.GL11;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Loads a model from a given File.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public class Model {
    private static final float COLOR = 0.5f;
    private static final int VECTOR_LENGTH = 4;
    private int displayList;
    private List<Vector3d> vertices = new ArrayList<>();
    private List<Vector3d> normals = new ArrayList<>();
    private List<Vector3d> textureCoords = new ArrayList<>();
    private List<Vector3d> spaceVertices = new ArrayList<>();
    private List<Face> faces = new ArrayList<>();
    private HashMap<String, Material> mtllibs = new HashMap<>();
    private HashMap<Integer, String> mtls = new HashMap<>();
    private HashMap<Integer, String> smoothing = new HashMap<>();

    /**
     * Package visibility for model only.
     *
     * @param path The path from which to load the model.
     * @throws IOException When the given file cannot be loaded.
     */
    public Model(final String path) throws IOException {
        this(new File(path));
    }
    /**
     * Package visibility for model only.
     *
     * @param file The file from which to load the model.
     * @throws IOException When the given file cannot be loaded.
     */
    public Model(final File file) throws IOException {
        setupDisplayList(file); // TODO on removal mve the loadModelFromFile to here!
    }

    @Deprecated
    private void setupDisplayList(File file) throws IOException {
        displayList = GL11.glGenLists(1);
        GL11.glNewList(displayList, GL11.GL_COMPILE);
        loadModelFromFile(file);
        GL11.glBegin(GL11.GL_TRIANGLES);
        int i = 0;
        boolean smoothing = true;
        for (Face face : faces) {
            if (this.smoothing.containsKey(i)) {
                if (this.smoothing.get(i).equals("off")) {
                    smoothing = false;
                } else {
                    smoothing = true;
                }
            }
            if (mtls.containsKey(i)) {
                mtllibs.get(mtls.get(i)).use();
            }
            GL11.glColor3f(COLOR, COLOR, COLOR);
            Vector3d n1 = normals.get((int) face.getNormal().getX() - 1);
            if (!smoothing) {
                GL11.glNormal3d(n1.getX(), n1.getY(), n1.getZ());
            }
            if (face.getTexture() != null) {
                Vector3d t1 = textureCoords.get((int) face.getTexture().getX() - 1);
                GL11.glTexCoord3d(t1.getX(), -t1.getY(), t1.getZ());
            }
            Vector3d v1 = vertices.get((int) face.getVertex().getX() - 1);
            GL11.glVertex3d(v1.getY(), v1.getX(), -v1.getZ());
            Vector3d n2 = normals.get((int) face.getNormal().getY() - 1);
            if (!smoothing) {
                GL11.glNormal3d(n2.getX(), n2.getY(), n2.getZ());
            }
            if (face.getTexture() != null) {
                Vector3d t2 = textureCoords.get((int) face.getTexture().getY() - 1);
                GL11.glTexCoord3d(t2.getX(), -t2.getY(), t2.getZ());
            }
            Vector3d v2 = vertices.get((int) face.getVertex().getY() - 1);
            GL11.glVertex3d(v2.getY(), v2.getX(), -v2.getZ());
            Vector3d n3 = normals.get((int) face.getNormal().getZ() - 1);
            if (!smoothing) {
                GL11.glNormal3d(n3.getX(), n3.getY(), n3.getZ());
            }
            if (face.getTexture() != null) {
                Vector3d t3 = textureCoords.get((int) face.getTexture().getZ() - 1);
                GL11.glTexCoord3d(t3.getX(), -t3.getY(), t3.getZ());
            }
            Vector3d v3 = vertices.get((int) face.getVertex().getZ() - 1);
            GL11.glVertex3d(v3.getY(), v3.getX(), -v3.getZ());
            i++;
        }
        Material.resetMaterial();
        GL11.glEnd();

        GL11.glEndList();
    }

    /**
     * Unload the given model.
     */
    public void unloadModel() {
        GL11.glDeleteLists(displayList, 1);
    }

    /**
     * @return the vertices
     */
    public final List<Vector3d> getVertices() {
        return vertices;
    }

    /**
     * @param vertices the vertices to set
     */
    public final void setVertices(final List<Vector3d> vertices) {
        this.vertices = vertices;
    }

    /**
     * @return the normals
     */
    public final List<Vector3d> getNormals() {
        return normals;
    }

    /**
     * @param normals the normals to set
     */
    public final void setNormals(final List<Vector3d> normals) {
        this.normals = normals;
    }

    /**
     * @return the textureCoords
     */
    public final List<Vector3d> getTextureCoords() {
        return textureCoords;
    }

    /**
     * @param textureCoords the textureCoords to set
     */
    public final void setTextureCoords(final List<Vector3d> textureCoords) {
        this.textureCoords = textureCoords;
    }

    /**
     * @return the spaceVertices
     */
    public final List<Vector3d> getSpaceVertices() {
        return spaceVertices;
    }

    /**
     * @param spaceVertices the spaceVertices to set
     */
    public final void setSpaceVertices(final List<Vector3d> spaceVertices) {
        this.spaceVertices = spaceVertices;
    }

    /**
     * @return the faces
     */
    public final List<Face> getFaces() {
        return faces;
    }

    /**
     * @param faces the faces to set
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
     * @param mtllibs the mtllibs to set
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
     * @param mtls the mtls to set
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
     * @param smoothing the smoothing to set
     */
    public final void setSmoothing(final HashMap<Integer, String> smoothing) {
        this.smoothing = smoothing;
    }

    /**
     * Get the display list of the model.
     * @return The display list of the model.
     */
    @Deprecated
    public final int getDisplayList() {
        return displayList;
    }

    /**
     * Load a model from a file.
     *
     * @param f The file where the model is stored in.
     * @throws IOException When the file cannot be loaded.
     */
    private void loadModelFromFile(final File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
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
                            mtllibs);
                } else if (line.startsWith("usemtl ")) {
                    String[] splitline = line.split(" ");
                    String mtl = splitline[1];
                    mtls.put(faces.size(), mtl);
                } else if (line.startsWith("s ")) {
                    String[] splitline = line.split(" ");
                    String smooth = splitline[1];
                    smoothing.put(faces.size(), smooth);
                } else if (line.startsWith("v ")) {
                    String[] splitline = line.split(" ");
                    float x = Float.parseFloat(splitline[1]);
                    float y = Float.parseFloat(splitline[2]);
                    float z = Float.parseFloat(splitline[3]);
                    vertices.add(new Vector3d(x, y, z));
                } else if (line.startsWith("vn ")) {
                    String[] splitline = line.split(" ");
                    float x = Float.parseFloat(splitline[1]);
                    float y = Float.parseFloat(splitline[2]);
                    float z = Float.parseFloat(splitline[3]);
                    normals.add(new Vector3d(x, y, z));
                } else if (line.startsWith("vt ")) {
                    String[] splitline = line.split(" ");
                    float x = Float.parseFloat(splitline[1]);
                    float y = Float.parseFloat(splitline[2]);
                    float z = 0;
                    if (splitline.length >= VECTOR_LENGTH) {
                        z = Float.parseFloat(splitline[3]);
                    }
                    textureCoords.add(new Vector3d(x, y, z));
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
                    spaceVertices.add(new Vector3d(x, y, z));
                } else if (line.startsWith("f ")) {
                    Vector3d vertexIndicies = new Vector3d(
                            Float.parseFloat(line.split(" ")[1].split("/")[0]),
                            Float.parseFloat(line.split(" ")[2].split("/")[0]),
                            Float.parseFloat(line.split(" ")[3].split("/")[0]));
                    Vector3d textureIndicies;
                    if (line.split(" ")[1].split("/")[1].equals("")) {
                        textureIndicies = null;
                    } else {
                        textureIndicies = new Vector3d(
                                Float.parseFloat(line.split(" ")[1].split("/")[1]),
                                Float.parseFloat(line.split(" ")[2].split("/")[1]),
                                Float.parseFloat(line.split(" ")[3].split("/")[1]));
                    }
                    Vector3d normalIndicies = new Vector3d(
                            Float.parseFloat(line.split(" ")[1].split("/")[2]),
                            Float.parseFloat(line.split(" ")[2].split("/")[2]),
                            Float.parseFloat(line.split(" ")[3].split("/")[2]));
                    faces.add(new Face(vertexIndicies, textureIndicies,
                            normalIndicies));
                    if (line.split(" ").length == 5) {
                        vertexIndicies = new Vector3d(
                                Float.parseFloat(line.split(" ")[4].split("/")[0]),
                                Float.parseFloat(line.split(" ")[1].split("/")[0]),
                                Float.parseFloat(line.split(" ")[3].split("/")[0]));
                        if (line.split(" ")[1].split("/")[1].equals("")) {
                            textureIndicies = null;
                        } else {
                            textureIndicies = new Vector3d(
                                    Float.parseFloat(line.split(" ")[4].split("/")[1]),
                                    Float.parseFloat(line.split(" ")[1].split("/")[1]),
                                    Float.parseFloat(line.split(" ")[3].split("/")[1]));
                        }
                        normalIndicies = new Vector3d(
                                Float.parseFloat(line.split(" ")[4].split("/")[2]),
                                Float.parseFloat(line.split(" ")[1].split("/")[2]),
                                Float.parseFloat(line.split(" ")[3].split("/")[2]));
                        faces.add(new Face(vertexIndicies, textureIndicies,
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
}
