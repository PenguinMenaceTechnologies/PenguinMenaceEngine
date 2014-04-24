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
	 * 
	 * Warning: This method can be slow in some cases.
	 * 
	 * @param key
	 *            The model name.
	 * @return The id to the model. (-1 if the required model cannot be found)
	 */
	public Model get(final String key) throws IOException{
		if (!MAP.containsKey(key)) {
			MAP.put(key, new Model(key));
		}

		return MAP.get(key);
	}

	/**
	 * Remove all models.
	 * 
	 * (Free Memory)
	 */
	public void clear() {
		for (String s : MAP.keySet()) {
			//deinitializeCore(s);
			//Model.unloadModel(MAP.get(s));
		}
		MAP.clear();
	}

	/*/**
	 * Unload a given model.
	 * 
	 * @param key
	 *            The key for the model to deinitializeCore.
	 */
	/*private void deinitializeCore(final String key) {
		if (MAP.containsKey(key)) {
			Model.unloadModel(MAP.get(key));
			MAP.remove(key);
		}
	}*/

    @Override
    public void finalize() {
        clear();
    }
}
