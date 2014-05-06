package net.pme.graphics.data;

import net.pme.core.math.Vector2d;
import net.pme.core.math.Vector3d;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Johannes Schuck <jojoschuck@googlemail.com>
 * @version 0.1
 * @since 2014-03-12
 */
public class VertexData {
    private final boolean hasNormal;
    private final boolean hasColor;
    private final boolean hasUV;
    private final int VERTEX_COMPONENTS = 3;
    private final int NORMAL_COMPONENTS = 3;
    private final int COLOR_COMPONENTS = 3;
    private final int UV_COMPONENTS = 2;
    private List<Vector3d> vertices;
    private List<Vector3d> normals;
    private List<Vector2d> uvs;
    private List<Vector3d> colors;
    private List<int[]> faces;
    private int numComponents = 1;

    public VertexData() {
        this(false, false, false);
    }

    public VertexData(boolean normal, boolean color, boolean uv) {
        vertices = new ArrayList<Vector3d>();
        faces = new ArrayList<int[]>();


        this.hasNormal = normal;
        this.hasColor = color;
        this.hasUV = uv;

        if (hasNormal) {
            normals = new ArrayList<Vector3d>();
            numComponents++;
        }

        if (hasUV) {
            uvs = new ArrayList<Vector2d>();
            numComponents++;
        }

        if (hasColor) {
            colors = new ArrayList<Vector3d>();
            numComponents++;
        }
    }

    /**
     * Checks if the VertexData is valid and could be sent to the GPU.
     *
     * @return true if valid, false otherwise.
     */
    // TODO: implement
    public boolean isValid() {
        return true;
    }

    public int[] getIndices() {
        int indexCount = 0;
        for (int[] face : faces) {
            indexCount += (face.length / numComponents - 2) * 3;
        }
        int[] indexData = new int[indexCount];

        int i = 0;
        for (int[] currFace : faces) {
            for (int j = 0; j < currFace.length / 4 - 2; j++) {
                indexData[i * 3] = i + j;
                indexData[i * 3 + 1] = i + j + 1;
                indexData[i * 3 + 2] = i + j + 2;
                i++;
                j++;
            }
        }

        return indexData;
    }

    public float[] getVertexData() {
        // Calculate the stride
        int numFloats = VERTEX_COMPONENTS;
        if (hasColor)
            numFloats += COLOR_COMPONENTS;
        if (hasNormal)
            numFloats += NORMAL_COMPONENTS;
        if (hasUV)
            numFloats += UV_COMPONENTS;

        int bufferCount = 0;
        for (int[] face : faces) {
            bufferCount += face.length / numComponents;
        }

        float vertexData[] = new float[numFloats * bufferCount];

        int i = 0;
        for (int[] face : faces) {
            for (int j = 0; j < face.length / numComponents; j++) {
                int currOffset = 0;
                int p = 0;
                vertexData[i * numFloats + p++] = (float) vertices.get(face[currOffset]).getX();
                vertexData[i * numFloats + p++] = (float) vertices.get(face[currOffset]).getY();
                vertexData[i * numFloats + p++] = (float) vertices.get(face[currOffset]).getZ();

                if (hasNormal) {
                    currOffset++;
                    vertexData[i * numFloats + p++] = (float) vertices.get(face[currOffset]).getX();
                    vertexData[i * numFloats + p++] = (float) vertices.get(face[currOffset]).getY();
                    vertexData[i * numFloats + p++] = (float) vertices.get(face[currOffset]).getZ();
                }

                if (hasColor) {
                    currOffset++;
                    vertexData[i * numFloats + p++] = (float) vertices.get(face[currOffset]).getX();
                    vertexData[i * numFloats + p++] = (float) vertices.get(face[currOffset]).getY();
                    vertexData[i * numFloats + p++] = (float) vertices.get(face[currOffset]).getZ();
                }

                if (hasUV) {
                    currOffset++;
                    vertexData[i * numFloats + p++] = (float) vertices.get(face[currOffset]).getX();
                    vertexData[i * numFloats + p++] = (float) vertices.get(face[currOffset]).getY();
                }
                i++;
            }
        }

        return vertexData;
    }

    public void addVertex(float x, float y, float z) {
        vertices.add(new Vector3d(x, y, z));
    }

    public void addVertex(float x, float y, float z, float w) {
        vertices.add(new Vector3d(x / w, y / w, z / w));
    }

    public void addVertex(Vector3d vertex) {
        vertices.add(vertex);
    }

    public void addNormal(float x, float y, float z) {
        normals.add(new Vector3d(x, y, z));
    }

    public void addNormal(Vector3d normal) {
        normals.add(normal);
    }

    public void addColor(int r, int g, int b) {
        colors.add(new Vector3d((double) r / 255.0, (double) g / 255.0, (double) b / 255.0));
    }

    public void addColor(Vector3d color) {
        colors.add(color);
    }

    public void addTexCoord(float u, float v) {
        uvs.add(new Vector2d(u, v));
    }

    public void addTexCoord(Vector2d texCoord) {
        uvs.add(texCoord);
    }

    /**
     * Adds a face to the vertex data.
     *
     * @param face The vector of the current face.
     */
    public void addFace(int[] face) {
        if (face.length / numComponents < 3 && face.length % numComponents != 0) {
            throw new IllegalArgumentException("A face must at least have 3 vertices");
        }

        faces.add(face);
    }

    public final int getVertexOffset() {
        return 0;
    }

    public final int getNormalOffset() {
        return VERTEX_COMPONENTS;
    }

    public final int getColorOffset() {
        return VERTEX_COMPONENTS + NORMAL_COMPONENTS;
    }

    public final int getUVOffset() {
        return VERTEX_COMPONENTS + NORMAL_COMPONENTS + COLOR_COMPONENTS;
    }

}
