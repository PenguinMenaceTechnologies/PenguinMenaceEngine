package net.pme.graphics;

import java.nio.DoubleBuffer;

import org.lwjgl.opengl.GL11;

import net.pme.core.GameObject;
import net.pme.core.math.MathUtils;
import net.pme.core.math.Matrix;
import net.pme.core.math.Vector3D;

/**
 * An object that can be rendered.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public class RenderAttachment {
	private int graphics;

    private GameObject parent;
	/**
	 * The direction the object is facing at.
	 */
	private Vector3D front;
	/**
	 * The direction where the top of the object is.
	 */
	private Vector3D up;
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
	 * Create a new RenderAttachment.
	 * 
	 * @param parent
     *            The parent of this attachment.
	 * @param front
	 *            The front vector.
	 * @param up
	 *            The up vector.
	 * @param graphics
	 *            The graphics identifier.
	 */
	public RenderAttachment(final GameObject parent, final Vector3D front,
                            final Vector3D up, final int graphics) {
        this.parent = parent;
		this.front = Vector3D.normalize(front);
		this.up = Vector3D.normalize(up);
		this.graphics = graphics;
		this.needsUpdate = true;
		this.matrixBuffer = null;
	}

	/**
	 * Attach a shader to this object.
	 * 
	 * @param newShader
	 *            The shader to attach to this object.
	 */
	public final void attachShader(final Shader newShader) {
		this.shader = newShader;
	}

	/**
	 * @return the graphics
	 */
	protected final int getGraphics() {
		return graphics;
	}

	/**
	 * @param graphics the graphics to set
	 */
	protected final void setGraphics(final int graphics) {
		this.graphics = graphics;
		needsUpdate = true;
	}

	/**
	 * @return the front
	 */
    public final Vector3D getFront() {
		return front;
	}

	/**
	 * @param front the front to set
	 */
    public final void setFront(final Vector3D front) {
		this.front = front;
		needsUpdate = true;
	}

	/**
	 * @return the up
	 */
	public final Vector3D getUp() {
		return up;
	}

	/**
	 * @param up the up to set
	 */
    public final void setUp(final Vector3D up) {
		this.up = up;
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
    protected final void setShader(final Shader shader) {
		this.shader = shader;
	}

	/**
	 * Detach the currently attached shader from this object.
	 */
	public final void detachShader() {
		shader = null;
	}

	/**
	 * Rotate the object around it's front axis.
	 * 
	 * @param degree
	 *            The amount it should be rotated in degree.
	 */
	public final void rotateAroundFrontAxis(final double degree) {
		double angle = MathUtils.DEG2RAD * degree;
		up = Vector3D.transformCoords(up, Matrix.rotationAxis(front, angle));
		up = Vector3D.normalize(up);
		needsUpdate = true;

	}

	/**
	 * Rotate the object around it's up axis.
	 * 
	 * @param degree
	 *            The amount it should be rotated in degree.
	 */
    public final void rotateAroundUpAxis(final double degree) {
		double angle = MathUtils.DEG2RAD * degree;
		front = Vector3D.transformCoords(front, Matrix.rotationAxis(up, angle));
		front = Vector3D.normalize(front);
		needsUpdate = true;
	}

	/**
	 * Rotate the object around it's pitch axis.
	 * 
	 * The pitch axis is orthogonal to the up and front axis.
	 * 
	 * @param degree
	 *            The amount it should be rotated in degree.
	 */
    public final void rotateAroundPitchAxis(final double degree) {
		Vector3D pitch = Vector3D.crossProduct(front, up);
		double angle = MathUtils.DEG2RAD * degree;
		up = Vector3D.transformCoords(up, Matrix.rotationAxis(pitch, angle));
		up = Vector3D.normalize(up);
		front = Vector3D.transformCoords(front,
				Matrix.rotationAxis(pitch, angle));
		front = Vector3D.normalize(front);
		needsUpdate = true;
	}

	/**
	 * Move relative to your own coordinate system.
	 * 
	 * @param relative
	 *            The vector defining the motion.
	 */
    public final void move(final Vector3D relative) {
		Vector3D pitch = Vector3D.crossProduct(front, up);

		Matrix m = Matrix.axes(pitch, up, front);

		Vector3D absolute = Vector3D.transformCoords(relative, m);

		parent.setPosition(Vector3D.add(parent.getPosition(), absolute));
		needsUpdate = true;
	}

	/**
	 * Set the coordinate system to the center of the ship and render it.
	 * 
	 * Calls the specialFX method.
	 */
	public final void render() {
		GL11.glPushMatrix();

		GL11.glTranslated(parent.getPosition().getX(), parent.getPosition().getY(), parent.getPosition().getZ());

		if (needsUpdate) {
			Matrix m = Matrix.axes(up, Vector3D.crossProduct(front, up), front);

			matrixBuffer = m.getValues(matrixBuffer);
			needsUpdate = false;
		}
		matrixBuffer.position(0);
		GL11.glMultMatrix(matrixBuffer);

		if (shader != null) {
			shader.bind();
		}
		if (graphics > 0) {
			GL11.glCallList(graphics);
		}

		specialFX();

		if (shader != null) {
			shader.unbind();
		}

		GL11.glPopMatrix();
	}

	/**
	 * Apply special effects, such as particles, shields, etc.
	 * 
	 * Is called every frame.
	 */
	protected void specialFX() {

	}
}