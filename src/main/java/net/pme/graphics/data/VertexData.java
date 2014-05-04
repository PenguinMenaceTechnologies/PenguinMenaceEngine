package net.pme.graphics.data;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.ArrayList;

import net.pme.core.math.Vector3D;
import net.pme.core.math.Vector2D;
import net.pme.graphics.Shader;
import net.pme.model.Face;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

/**
 * @author Johannes Schuck <jojoschuck@googlemail.com>
 * @author Michael Fürst <mail@michaelfuerst.de>
 * @version 1.0
 * @since 2014-04-24
 */
public class VertexData {
    private static final int MAGIC_FLOAT = 4;
    private List<Vector3D> vertices;
    private List<Vector3D> normals;
    private List<Vector2D> uvs;
    private List<Vector3D> colors;

    private List<int[]> faces;

    private final boolean hasNormal;
    private final boolean hasColor;
    private final boolean hasUV;
    private int numComponents = 1;

    private final int VERTEX_COMPONENTS = 3;
    private final int NORMAL_COMPONENTS = 3;
    private final int COLOR_COMPONENTS = 3;
    private final int UV_COMPONENTS = 2;
    private int buffer = -1;
    private int stride = 0;

    public VertexData() {
        this(false, false, false);
    }

    public VertexData(boolean normal, boolean color, boolean uv) {
        vertices = new ArrayList<>();
        faces = new ArrayList<>();


        this.hasNormal = normal;
        this.hasColor = color;
        this.hasUV = uv;

        // Calculate the stride
        stride = VERTEX_COMPONENTS;
        if (hasColor)
            stride += COLOR_COMPONENTS;
        if (hasNormal)
            stride += NORMAL_COMPONENTS;
        if (hasUV)
            stride += UV_COMPONENTS;

        if (hasNormal) {
            normals = new ArrayList<>();
            numComponents++;
        }

        if (hasUV) {
            uvs = new ArrayList<>();
            numComponents++;
        }

        if (hasColor) {
            colors = new ArrayList<>();
            numComponents++;
        }
    }

    /**
     * Checks if the VertexData is valid and could be sent to the GPU.
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
                indexData[i * 3]     = i + j;
                indexData[i * 3 + 1] = i + j + 1;
                indexData[i * 3 + 2] = i + j + 2;
                i++; j++;
            }
        }

        return indexData;
    }

    public float[] getVertexData() {
        int bufferCount = 0;
        for (int[] face : faces) {
            bufferCount += face.length / numComponents;
        }

        float vertexData[] = new float[stride * bufferCount];

        int i = 0;
        for (int[] face : faces) {
            for (int j = 0; j < face.length / numComponents; j++) {
                int currOffset = 0; int p = 0;
                vertexData[i * stride + p++] = (float) vertices.get(face[currOffset]).getX();
                vertexData[i * stride + p++] = (float) vertices.get(face[currOffset]).getY();
                vertexData[i * stride + p++] = (float) vertices.get(face[currOffset]).getZ();


                if (hasNormal) {
                    currOffset++;
                    vertexData[i * stride + p++] = (float) normals.get(face[currOffset]).getX();
                    vertexData[i * stride + p++] = (float) normals.get(face[currOffset]).getY();
                    vertexData[i * stride + p++] = (float) normals.get(face[currOffset]).getZ();
                }

                if (hasColor) {
                    currOffset++;
                    vertexData[i * stride + p++] = (float) colors.get(face[currOffset]).getX();
                    vertexData[i * stride + p++] = (float) colors.get(face[currOffset]).getY();
                    vertexData[i * stride + p++] = (float) colors.get(face[currOffset]).getZ();
                }

                if (hasUV) {
                    currOffset++;
                    vertexData[i * stride + p++] = (float) uvs.get(face[currOffset]).getX();
                    vertexData[i * stride + p++] = (float) uvs.get(face[currOffset]).getY();
                }
                i++;
            }
        }

        return vertexData;
    }

    public void addVertex(float x, float y, float z) {
        vertices.add(new Vector3D(x, y, z));
    }

    public void addVertex(float x, float y, float z, float w) {
        vertices.add(new Vector3D(x / w, y / w, z / w));
    }

    public void addVertex(Vector3D vertex) {
        vertices.add(vertex);
    }

    public void addNormal(float x, float y, float z) {
        normals.add(new Vector3D(x, y, z));
    }

    public void addNormal(Vector3D normal) {
        normals.add(normal);
    }

    public void addColor(int r, int g, int b) {
        colors.add(new Vector3D((double) r / 255.0, (double) g / 255.0, (double) b / 255.0));
    }

    public void addColor(Vector3D color) {
        colors.add(color);
    }

    public void addTexCoord(float u, float v) {
        uvs.add(new Vector2D(u, v));
    }

    public void addTexCoord(Vector2D texCoord) {
        uvs.add(texCoord);
    }

    /**
     * Adds a face to the vertex data.
     * @param face The vector of the current face.
     */
    public void addFace(int[] face) {
        if (face.length / numComponents < 3 && face.length % numComponents != 0) {
            throw new IllegalArgumentException("A face must at least have 3 vertices");
        }

        faces.add(face);
    }

    public void addFace(Face f) {
        int size = 3;

        if (hasNormal) {
            size += 3;
        }

        if (hasUV) {
            size += 3;
        }

        int[] face = new int[size];

        int i = 0;

        face[i++] = (int)f.getVertex().getX() - 1;

        if (hasNormal) {
            face[i++] = (int)f.getNormal().getX() - 1;
        }

        if (hasUV) {
            face[i++] = (int)f.getTexture().getX() - 1;
        }

        face[i++] = (int)f.getVertex().getY() - 1;

        if (hasNormal) {
            face[i++] = (int)f.getNormal().getY() - 1;
        }

        if (hasUV) {
            face[i++] = (int)f.getTexture().getY() - 1;
        }

        face[i++] = (int)f.getVertex().getZ() - 1;

        if (hasNormal) {
            face[i++] = (int)f.getNormal().getZ() - 1;
        }

        if (hasUV) {
            face[i++] = (int)f.getTexture().getZ() - 1;
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
        return VERTEX_COMPONENTS + (hasNormal?NORMAL_COMPONENTS:0);
    }

    public final int getUVOffset() {
        return VERTEX_COMPONENTS + (hasNormal?NORMAL_COMPONENTS:0) + (hasColor?COLOR_COMPONENTS:0);
    }

    public void unloadFromGraphicsCard() {
        if (buffer >= 0) {
            GL15.glDeleteBuffers(buffer);
            buffer = -1;
        } else {
            throw new RuntimeException("There is no data to unload!");
        }
    }

    public void loadToGraphicsCard() {
        if (buffer < 0) {
            float[] vertexData = getVertexData();
            System.out.println("Stride = "+ stride);
            FloatBuffer data = BufferUtils.createFloatBuffer(vertexData.length);
            data.put(vertexData);
            data.flip();

            buffer = GL15.glGenBuffers();
            bind();
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
            unbind();
        } else {
            throw new RuntimeException("You must unload the vertexdata first!");
        }
    }

    public void bind() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer);
    }

    public void render() {
        bind();

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glVertexPointer(VERTEX_COMPONENTS, GL11.GL_FLOAT, stride * MAGIC_FLOAT, getVertexOffset() * MAGIC_FLOAT);
        if (hasNormal) {
            GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
            GL11.glNormalPointer(GL11.GL_FLOAT, stride * MAGIC_FLOAT, getNormalOffset() * MAGIC_FLOAT);
        }
        if (hasColor) {
            GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
            GL11.glColorPointer(COLOR_COMPONENTS, GL11.GL_FLOAT, stride * MAGIC_FLOAT, getColorOffset() * MAGIC_FLOAT);
        }
        if (hasUV) {
            GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
            GL11.glTexCoordPointer(UV_COMPONENTS, GL11.GL_FLOAT, stride * MAGIC_FLOAT, getUVOffset() * MAGIC_FLOAT);
        }

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, faces.size());

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        if (hasNormal) {
            GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        }
        if (hasColor) {
            GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        }
        if (hasUV) {
            GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        }

        unbind();
    }

    public void unbind() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
}
