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
	private static final HashMap<String, Integer> map = new HashMap<String, Integer>();

	/**
	 * Load a model or just get the link if already loaded.
	 * 
	 * Warning: This method can be slow in some cases.
	 * 
	 * @param key
	 *            The model name.
	 * @return The id to the model. (-1 if the required model cannot be found)
	 */
	public static final int get(String key) {
		if (!map.containsKey(key)) {
			map.put(key, Model.loadModel(key));
		}

		return map.get(key);
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
	public static int getSpecialCoords(String key) {
		if (!map.containsKey(key)) {
			map.put(key, Model.loadModelSpecialCoords(key));
		}

		return map.get(key);
	}

	/**
	 * Remove all models.
	 * 
	 * (Free Memory)
	 */
	public static final void clear() {
		for (String s : map.keySet()) {
			unload(s);
		}
	}

	private static final void unload(String key) {
		if (map.containsKey(key)) {
			Model.unloadModel(map.get(key));
			map.remove(key);
		}
	}
}
