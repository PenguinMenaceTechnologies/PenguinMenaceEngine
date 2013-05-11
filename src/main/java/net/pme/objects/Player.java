package net.pme.objects;

import java.nio.DoubleBuffer;

import org.lwjgl.opengl.GL11;

import net.pme.math.Matrix;
import net.pme.math.Vector3D;

/**
 * A player represents the person sitting in front of the computer playing the
 * game.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public abstract class Player extends RenderableObject implements Networkable {
	/**
	 * Create a new player.
	 * 
	 * @param position
	 *            The position.
	 * @param front
	 *            The direction the player is facing to.
	 * @param up
	 *            The direction where the players top is.
	 * @param graphics
	 *            The graphics identifier.
	 */
	public Player(Vector3D position, Vector3D front, Vector3D up, int graphics) {
		super(1, position, front, up, graphics);
	}

	/**
	 * Apply the camera on the scene, so that the scene can be rendered after a
	 * call of this function.
	 */
	public final void applyCamera() {

		Matrix m = Matrix.camera(position, Vector3D.crossProduct(front, up),
				up, front);

		DoubleBuffer db = m.getValues();
		db.position(0);
		GL11.glMultMatrix(db);
	}

	/**
	 * Handles keyboard input.
	 * 
	 * <i>The GameInput class is a huge help, when implementing this method.</i>
	 * 
	 * @param eventKey
	 *            Determines the key that is pressed.
	 * @param eventKeyState
	 *            If the key is down.
	 */
	public abstract void keyboardInputHandler(int eventKey,
			boolean eventKeyState);

	/**
	 * Handles mouse input.
	 * 
	 * <i>The GameInput class is a huge help, when implementing this method.</i>
	 * 
	 * @param eventButton
	 *            Determines the mouse button that is pressed.
	 * @param eventButtonState
	 *            If the button is down.
	 */
	public abstract void mouseInputHandler(int eventButton,
			boolean eventButtonState);

	/**
	 * Handles mouse motion.
	 * 
	 * @param dx
	 *            The dx of the mouse.
	 * @param dy
	 *            The dy of the mouse.
	 */
	public abstract void mouseMoveHandler(int dx, int dy);
}
