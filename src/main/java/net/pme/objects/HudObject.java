package net.pme.objects;

import java.awt.image.BufferedImage;

import org.lwjgl.opengl.GL11;

import net.pme.model.TextureLoader;

/**
 * Models a 2D HUD object.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public abstract class HudObject extends GameObject {
	private int x, y;

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
	public HudObject(final long id, final int x, final int y) {
		super(id);
		this.x = x;
		this.y = y;
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

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2i(x - bi.getWidth() / 2, y - bi.getHeight() / 2);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2i(x + bi.getWidth() / 2, y - bi.getHeight() / 2);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2i(x + bi.getWidth() / 2, y + bi.getHeight() / 2);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2i(x - bi.getWidth() / 2, y + bi.getHeight() / 2);
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glEnd();

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
