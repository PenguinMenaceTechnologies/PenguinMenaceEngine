package net.pme.sound;

import java.io.IOException;
import java.util.HashMap;

/**
 * Manages all sounds.
 *
 * @author <a href="mailto:mail@michaelfuerst.de>Michael Fürst</a>
 * @version 1.0
 * @since 2014-05-06
 */
public class SoundManager {
    private static HashMap<String, Sound> sounds = new HashMap<>();

    /**
     * Private constructor for utility class.
     */
    private SoundManager() {

    }

    /**
     * Get a sound.
     * @param path The path where to find the sound.
     * @param callee The calling class.
     * @return The sound to play.
     * @throws IOException When the sound cannot be opened or found.
     */
    public static Sound get(final String path, final Class callee) throws IOException {
        if (!sounds.containsKey(path)) {
            sounds.put(path, new Sound(path, callee));
        }
        return sounds.get(path);
    }

    /**
     * Remove all models.
     * <p/>
     * (Free Memory)
     */
    public void clear() {
        sounds.clear();
    }
}
