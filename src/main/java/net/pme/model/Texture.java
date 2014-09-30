/**
 *
 */
package net.pme.model;

import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.io.IOException;

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
     * @param image The buffered image.
     */
    public Texture(final BufferedImage image) {
        textureId = TextureLoader.loadTextureForceReload(image);
    }

    /**
     * Create texture from a given filename.
     *
     * @param filename The filename.
     * @param resourcePath The resource path.
     * @throws IOException When the searched file does not exist or not contain an
     *                     image.
     */
    public Texture(final String filename, final Class callee, final String resourcePath) throws IOException {
        textureId = TextureLoader.loadFromFile(filename, callee, resourcePath);
    }

    /**
     * Unbind a texture from a given target.
     *
     * @param target The target to unbind from. (Usually GL_TEXTURE_2D):
     */
    public static void unbind(final int target) {
        if (target != GL11.GL_TEXTURE_2D) {
            GL11.glDisable(target);
        }
        GL11.glBindTexture(target, 0);
    }

    /**
     * Bind a texture to a given target.
     *
     * @param target The target to bind to. (Usually GL_TEXTURE_2D):
     */
    public final void bind(final int target) {
        if (target != GL11.GL_TEXTURE_2D) {
            GL11.glEnable(target);
        }
        GL11.glBindTexture(target, textureId);
    }

    /**
     * Free the memory this texture is using.
     */
    public final void free() {
        TextureLoader.free(textureId);
    }
}
