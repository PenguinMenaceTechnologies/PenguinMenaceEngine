package net.pme;

import java.util.HashMap;

import net.pme.model.Model;

/**
 * Manage models, so that they don't need to be loaded several times.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public final class ModelManager {
	private final HashMap<String, Integer> MAP = new HashMap<String, Integer>();

	/**
	 * Utility classes should have no public constructor.
	 */
	ModelManager() {
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
	public int get(final String key) {
		if (!MAP.containsKey(key)) {
			MAP.put(key, Model.loadModel(key));
		}

		return MAP.get(key);
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
	public int getSpecialCoords(final String key) {
		if (!MAP.containsKey(key)) {
			MAP.put(key, Model.loadModelSpecialCoords(key));
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
			//unload(s);
			Model.unloadModel(MAP.get(s));
		}
		MAP.clear();
	}

	/**
	 * Unload a given model.
	 * 
	 * @param key
	 *            The key for the model to unload.
	 */
	/*private void unload(final String key) {
		if (MAP.containsKey(key)) {
			Model.unloadModel(MAP.get(key));
			MAP.remove(key);
		}
	}*/
}
