package net.pme.objects;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import net.pme.model.TextureLoader;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

import static org.lwjgl.opengl.GL11.*;

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
		int texture = TextureLoader.loadTextureForceReload(bi);
		
		// TODO render quad with texture at the right position
		
		TextureLoader.forceFree(texture);
	}

	/**
	 * Render your output to an off screen image.
	 * 
	 * @return The image that should be drawn on the screen at the given
	 *         position then.
	 */
	public abstract BufferedImage offscreenRendering();

}
