package net.pme.sound;

import net.pme.core.utils.IOUtils;

import java.io.File;
import java.io.IOException;

/**
 * A sound to be played back.
 *
 * @author <a href="mailto:mail@michaelfuerst.de>Michael Fürst</a>
 * @version 1.0
 * @since 2014-05-06
 */
public class Sound {
    public Sound(final String path, final Class callee) throws IOException {
        File file = IOUtils.getFile(path, callee);
        // TODO make something playable from the file.
    }

    /**
     * Play the sound.
     */
    public void play() {
        // TODO implement.
    }

    /**
     * Pause the sound.
     */
    public void pause() {
        // TODO implement.
    }

    /**
     * Stop the sound.
     */
    public void stop() {
        // TODO implement.
    }
}
