/**
 * 
 */
package net.pme.model;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

/**
 * Represents a texture to be drawn onto a 3d model in lwjgl.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public class Texture {
	private int textureId;

	/**
	 * Creates a texture from a given buffered image.
	 * 
	 * @param image
	 *            The buffered image.
	 */
	public Texture(BufferedImage image) {
		textureId = TextureLoader.loadTextureForceReload(image);
	}

	/**
	 * Create texture from a given filename.
	 * 
	 * @param filename
	 *            The filename.
	 * @throws IOException
	 *             When the searched file does not exist or not contain an
	 *             image.
	 */
	public Texture(String filename) throws IOException {
		textureId = TextureLoader.loadFromFile(filename);
	}

	/**
	 * Bind a texture to a given target.
	 * 
	 * @param target
	 *            The target to bind to. (Usually GL_TEXTURE_2D):
	 */
	public void bind(int target) {
		GL11.glEnable(target);
		GL11.glBindTexture(target, textureId);
	}

	/**
	 * Unbind a texture from a given target.
	 * 
	 * @param target
	 *            The target to unbind from. (Usually GL_TEXTURE_2D):
	 */
	public static void unbind(int target) {
		GL11.glDisable(target);
	}

	/**
	 * Free the memory this texture is using.
	 */
	public void free() {
		TextureLoader.free(textureId);
	}
}
