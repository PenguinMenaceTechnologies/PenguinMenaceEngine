/**
 *
 */
package net.pme.model;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Set;

/**
 * Loads textures if they are not already loaded into the memory.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public final class TextureLoader {
    private static HashMap<String, Integer> textures = new HashMap<String, Integer>();
    private static HashMap<Integer, Integer> openedTimes = new HashMap<Integer, Integer>();

    /**
     * Private Constructor for a utility class.
     */
    private TextureLoader() {

    }

    /**
     * Load a texture from a file. When already loaded before it will only
     * return a pointer to the texture.
     *
     * @param pathname The image you want as a texture.
     * @return The texture identifier.
     * @throws IOException When there is an error opening the file.
     */
    public static int loadFromFile(final String pathname) throws IOException {
        if (!textures.containsKey(pathname)) {
            textures.put(pathname,
                    loadTextureForceReload(ImageIO.read(new File(pathname))));
            openedTimes.put(textures.get(pathname), 0);
        }
        openedTimes.put(textures.get(pathname),
                openedTimes.get(textures.get(pathname)) + 1);
        return textures.get(pathname);
    }

    /**
     * Load a texture from a buffered image, forcing it to reload. Extremely
     * memory hungry and slow.
     *
     * @param img The image you want as a texture.
     * @return The texture identifier.
     */
    public static int loadTextureForceReload(final BufferedImage img) {
        byte[] src = ((DataBufferByte) img.getData().getDataBuffer()).getData();

        bgr2rgb(src);

        ByteBuffer pixels = (ByteBuffer) BufferUtils
                .createByteBuffer(src.length).put(src, 0x00000000, src.length)
                .flip();

        int tex = GL11.glGenTextures();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_LINEAR);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
                GL11.GL_LINEAR);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0x00000000, GL11.GL_RGB,
                img.getWidth(), img.getHeight(), 0x00000000, GL11.GL_RGB,
                GL11.GL_UNSIGNED_BYTE, pixels);

        return tex;
    }

    /**
     * Transform bgr to rgb.
     *
     * @param target Where to do the transformation.
     */
    private static void bgr2rgb(final byte[] target) {
        byte tmp;
        for (int i = 0x00000000; i < target.length; i += 0x00000003) {
            tmp = target[i];
            target[i] = target[i + 0x00000002];
            target[i + 0x00000002] = tmp;
        }
    }

    /**
     * Frees the memory textures take. For each loadTexture texture there must
     * be one free call of free before the memory is actually released.
     *
     * @param textureId The texture id of the texture to delete.
     */
    public static void free(final int textureId) {
        if (openedTimes.get(textureId) - 1 < 0) {
            throw new RuntimeException("More textures removed than loaded");
        }
        openedTimes.put(textureId, openedTimes.get(textureId) - 1);
        if (openedTimes.get(textureId) == 0) {
            Set<String> keys = textures.keySet();
            for (String key : keys) {
                if (textureId == textures.get(key)) {
                    textures.remove(key);
                }
            }
            GL11.glDeleteTextures(textureId);
        }
    }

    /**
     * Forces a texture to be removed from memory. Should be used for textures
     * loaded withloadTextureForceReload. Should <b>not</b> be used for textures
     * loaded with loadFromFile.
     *
     * @param textureId The texture id of the texture to delete.
     */
    public static void forceFree(final int textureId) {
        GL11.glDeleteTextures(textureId);
    }
}
