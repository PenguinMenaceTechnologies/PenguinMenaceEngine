package net.pme.graphics;

import net.pme.core.GameObject;
import net.pme.core.math.Matrix;
import net.pme.core.math.Vector3d;
import net.pme.graphics.data.VertexData;
import net.pme.model.BoundingBox;
import net.pme.model.Model;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;

/**
 * An object that can be rendered.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public class RenderAttachment {
    private Model model;

    private GameObject parent;
    /**
     * Determines if the matrix needs to be updated.
     */
    private boolean needsUpdate;
    /**
     * The matrix buffer to avoid recalculations.
     */
    private DoubleBuffer matrixBuffer;
    /**
     * A shader for the renderable object.
     */
    private Shader shader;

    /**
     * Vertex data to render.
     */
    private VertexData vertexData;

    /**
     * Draw the bounding frame or not.
     */
    private boolean boundingFrame = false;
    /**
     * Draw model as wireframe.
     */
    private boolean wireframe = false;

    /**
     * Create a new RenderAttachment.
     *
     * @param parent   The parent of this attachment.
     * @param model The graphics identifier.
     */
    public RenderAttachment(final GameObject parent, final Model model) {
        this.parent = parent;
        this.model = model;
        this.needsUpdate = true;
        this.matrixBuffer = null;
    }

    /**
     * @return the graphics
     */
    public final Model getModel() {
        return model;
    }

    /**
     * @param model the graphics to set
     */
    protected final void setModel(final Model model) {
        this.model = model;
        // TODO load model into vertex data correctly here.
        this.vertexData = new VertexData();
        needsUpdate = true;
    }

    /**
     * @return the needsUpdate
     */
    public final boolean isNeedsUpdate() {
        return needsUpdate;
    }

    /**
     * @param needsUpdate the needsUpdate to set
     */
    public final void setNeedsUpdate(final boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
    }

    /**
     * @return the matrixBuffer
     */
    protected final DoubleBuffer getMatrixBuffer() {
        return matrixBuffer;
    }

    /**
     * @param matrixBuffer the matrixBuffer to set
     */
    protected final void setMatrixBuffer(final DoubleBuffer matrixBuffer) {
        this.matrixBuffer = matrixBuffer;
    }

    /**
     * @return the shader
     */
    public final Shader getShader() {
        return shader;
    }

    /**
     * @param shader the shader to set
     */
    public final void setShader(final Shader shader) {
        if (shader == null) {
            this.shader = Shader.getDefaultShader();
        } else {
            this.shader = shader;
        }
    }

    /**
     * Set the state of rendering the bounding frame.
     * @param enabled The bounding frame.
     */
    public final void setBoundingFrame(final boolean enabled) {
        boundingFrame = enabled;
    }

    /**
     * Set to render wireframe or not.
     * @param enabled
     */
    public final void setWireframe(final boolean enabled) {
        this.wireframe = enabled;
    }

    /**
     * Set the coordinate system to the center of the ship and render it.
     * <p/>
     * Calls the specialFX method.
     */
    public final void render() {
        GL11.glPushMatrix();

        GL11.glTranslated(parent.getPosition().getX(), parent.getPosition().getY(), parent.getPosition().getZ());

        if (needsUpdate) {
            Matrix m = Matrix.axes(parent.getUp(), parent.getFront().clone().crossProduct(parent.getUp()), parent.getFront());

            matrixBuffer = m.getValues(matrixBuffer);
            needsUpdate = false;
        }

        matrixBuffer.position(0);
        GL11.glMultMatrix(matrixBuffer);

        if (shader != null) {
            shader.bind();
        }

        if (wireframe) {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        }

        if (model != null) {
            // TODO render vertexdata not the display list.
            if (model.getDisplayList() > 0) {
                GL11.glCallList(model.getDisplayList());
            }
        }

        specialFX();

        if (wireframe) {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        }

        if (shader != null) {
            shader.unbind();
        }

        if (boundingFrame && model != null) {
            if (Shader.getDefaultShader() != null) {
                Shader.getDefaultShader().bind();
            }

            BoundingBox b = model.getBoundingBox();
            Vector3d[] edges = b.getEdges();

            GL11.glLineWidth(5);
            GL11.glBegin(GL11.GL_LINES);

            GL11.glVertex3d(edges[0].getX(), edges[0].getY(), edges[0].getZ());
            GL11.glVertex3d(edges[1].getX(), edges[1].getY(), edges[1].getZ());

            GL11.glVertex3d(edges[1].getX(), edges[1].getY(), edges[1].getZ());
            GL11.glVertex3d(edges[2].getX(), edges[2].getY(), edges[2].getZ());

            GL11.glVertex3d(edges[2].getX(), edges[2].getY(), edges[2].getZ());
            GL11.glVertex3d(edges[3].getX(), edges[3].getY(), edges[3].getZ());

            GL11.glVertex3d(edges[3].getX(), edges[3].getY(), edges[3].getZ());
            GL11.glVertex3d(edges[0].getX(), edges[0].getY(), edges[0].getZ());


            GL11.glVertex3d(edges[4].getX(), edges[4].getY(), edges[4].getZ());
            GL11.glVertex3d(edges[5].getX(), edges[5].getY(), edges[5].getZ());

            GL11.glVertex3d(edges[5].getX(), edges[5].getY(), edges[5].getZ());
            GL11.glVertex3d(edges[6].getX(), edges[6].getY(), edges[6].getZ());

            GL11.glVertex3d(edges[6].getX(), edges[6].getY(), edges[6].getZ());
            GL11.glVertex3d(edges[7].getX(), edges[7].getY(), edges[7].getZ());

            GL11.glVertex3d(edges[7].getX(), edges[7].getY(), edges[7].getZ());
            GL11.glVertex3d(edges[4].getX(), edges[4].getY(), edges[4].getZ());


            GL11.glVertex3d(edges[0].getX(), edges[0].getY(), edges[0].getZ());
            GL11.glVertex3d(edges[4].getX(), edges[4].getY(), edges[4].getZ());

            GL11.glVertex3d(edges[1].getX(), edges[1].getY(), edges[1].getZ());
            GL11.glVertex3d(edges[5].getX(), edges[5].getY(), edges[5].getZ());

            GL11.glVertex3d(edges[2].getX(), edges[2].getY(), edges[2].getZ());
            GL11.glVertex3d(edges[6].getX(), edges[6].getY(), edges[6].getZ());

            GL11.glVertex3d(edges[3].getX(), edges[3].getY(), edges[3].getZ());
            GL11.glVertex3d(edges[7].getX(), edges[7].getY(), edges[7].getZ());

            GL11.glEnd();


            if (Shader.getDefaultShader() != null) {
                Shader.getDefaultShader().unbind();
            }
        }

        GL11.glPopMatrix();
    }

    /**
     * Apply special effects, such as particles, shields, etc.
     * <p/>
     * Is called every frame.
     */
    protected void specialFX() {

    }
}
