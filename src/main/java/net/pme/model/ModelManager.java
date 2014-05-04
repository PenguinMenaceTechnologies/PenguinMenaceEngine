package net.pme.model;

import java.io.IOException;
import java.util.HashMap;

/**
 * Manage models, so that they don't need to be loaded several times.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public final class ModelManager {
    private final HashMap<String, Model> MAP = new HashMap<>();

    /**
     * Utility classes should have no public constructor.
     */
    public ModelManager() {
    }

    /**
     * Load a model or just get the link if already loaded.
     * <p/>
     * Warning: This method can be slow in some cases.
     *
     * @param key The model name.
     * @return The id to the model.
     */
    public Model get(final String key) throws IOException {
        if (!MAP.containsKey(key)) {
            MAP.put(key, new Model(key));
        }

        return MAP.get(key);
    }

    /**
     * Remove all models.
     * <p/>
     * (Free Memory)
     */
    public void clear() {
        for (String s : MAP.keySet()) {
            //deinitializeCore(s);
            MAP.get(s).unloadModel();
        }
        MAP.clear();
    }

    @Override
    public void finalize() {
        clear();
    }
}
