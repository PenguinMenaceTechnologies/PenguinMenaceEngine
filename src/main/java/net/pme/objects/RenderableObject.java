package net.pme.objects;

import java.nio.DoubleBuffer;

import org.lwjgl.opengl.GL11;

import net.pme.math.MathUtils;
import net.pme.math.Matrix;
import net.pme.math.Vector3D;

/**
 * An object that can be rendered.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public class RenderableObject extends MovableObject {
	private int graphics;
	/**
	 * The direction the object is facing at.
	 */
	protected Vector3D front;
	/**
	 * The direction where the top of the object is.
	 */
	protected Vector3D up;
	/**
	 * Determines if the matrix needs to be updated.
	 */
	protected boolean needsUpdate;
	/**
	 * The matrix buffer to avoid recalculations.
	 */
	private DoubleBuffer matrixBuffer;
	/**
	 * A shader for the renderable object;
	 */
	private Shader shader;

	/**
	 * Create a new RenderableObject.
	 * 
	 * @param id
	 *            The id.
	 * @param position
	 *            The position.
	 * @param front
	 *            The front vector.
	 * @param up
	 *            The up vector.
	 * @param graphics
	 *            The graphics identifier.
	 */
	public RenderableObject(long id, Vector3D position, Vector3D front,
			Vector3D up, int graphics) {
		super(id, position);
		this.front = Vector3D.normalize(front);
		this.up = Vector3D.normalize(up);
		this.graphics = graphics;
		this.needsUpdate = true;
		this.matrixBuffer = null;
	}

	/**
	 * Attach a shader to this object.
	 * 
	 * @param shader
	 *            The shader to attach to this object.
	 */
	public void attachShader(Shader shader) {
		this.shader = shader;
	}

	/**
	 * Detach the currently attached shader from this object.
	 */
	public void detachShader() {
		shader = null;
	}

	/**
	 * Rotate the object around it's front axis.
	 * 
	 * @param degree
	 *            The amount it should be rotated in degree.
	 */
	protected final void rotateAroundFrontAxis(double degree) {
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
	protected final void rotateAroundUpAxis(double degree) {
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
	protected final void rotateAroundPitchAxis(double degree) {
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
	protected final void move(Vector3D relative) {
		Vector3D pitch = Vector3D.crossProduct(front, up);

		Matrix m = Matrix.axes(pitch, up, front);

		Vector3D absolute = Vector3D.transformCoords(relative, m);

		position = Vector3D.add(position, absolute);
		needsUpdate = true;
	}

	/**
	 * Set the coordinate system to the center of the ship and render it.
	 * 
	 * Calls the specialFX method.
	 */
	public final void render() {
		GL11.glPushMatrix();

		GL11.glTranslated(position.getX(), position.getY(), position.getZ());

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

	@Override
	public void move(double elapsedTime) {
	}
}
