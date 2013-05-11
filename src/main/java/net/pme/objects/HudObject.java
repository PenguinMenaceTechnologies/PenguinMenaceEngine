package net.pme.objects;

import java.awt.image.BufferedImage;

/**
 * Models a 2D HUD object.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public abstract class HudObject extends GameObject {
	/**
	 * Create a new hud object.
	 * 
	 * @param id
	 *            The id of the object.
	 * @param x
	 *            The x position.
	 * @param y
	 *            The y position.
	 */
	public HudObject(long id, int x, int y) {
		super(id);
	}

	/**
	 * The move called every frame.
	 * 
	 * @param elapsedTime
	 *            The time the last frame calculation took.
	 */
	public abstract void move(double elapsedTime);

	/**
	 * Render the object to the screen.
	 */
	public final void render() {
		BufferedImage bi = offscreenRendering();
		// TODO render image.
	}

	/**
	 * Render your output to an off screen image.
	 * 
	 * @return The image that should be drawn on the screen at the given
	 *         position then.
	 */
	public abstract BufferedImage offscreenRendering();

}
