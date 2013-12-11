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
	private static final HashMap<String, Integer> MAP = new HashMap<String, Integer>();

	/**
	 * Utility classes should have no public constructor.
	 */
	private ModelManager() {
		
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
	public static int get(final String key) {
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
	public static int getSpecialCoords(final String key) {
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
	public static void clear() {
		for (String s : MAP.keySet()) {
			unload(s);
		}
	}

	/**
	 * Unload a given model.
	 * @param key The key for the model to unload.
	 */
	private static void unload(final String key) {
		if (MAP.containsKey(key)) {
			Model.unloadModel(MAP.get(key));
			MAP.remove(key);
		}
	}
}
