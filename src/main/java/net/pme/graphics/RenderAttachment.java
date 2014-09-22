package net.pme.graphics;

import net.pme.core.GameObject;
import net.pme.core.math.Matrix;
import net.pme.core.math.Vector3d;
import net.pme.graphics.data.VertexData;
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
    protected final Model getModel() {
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

        if (model != null) {
            // TODO render vertexdata not the display list.
            if (model.getDisplayList() > 0) {
                GL11.glCallList(model.getDisplayList());
            }
        }

        specialFX();

        if (shader != null) {
            shader.unbind();
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
