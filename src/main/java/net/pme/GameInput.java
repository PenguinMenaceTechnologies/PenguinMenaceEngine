package net.pme;

import net.pme.config.MemoryConfiguration;

/**
 * This class maps inputs (integers) to strings.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public final class GameInput {
	private static MemoryConfiguration map = null;

	/**
	 * Get a key integer for a string input.
	 * 
	 * @param key
	 *            The input type to get the code for.
	 * @return The integer representing the key.
	 * @throws IllegalAccessException
	 */
	public static int getCode(String key) throws IllegalAccessException {
		if (map == null) {
			throw new IllegalAccessException(
					"The keymap has to be loaded first.");
		}
		if (map.getInteger(key, -1) == -1) {
			throw new IllegalArgumentException(
					"The given key was not specified.");
		}
		return map.getInteger(key);
	}

	/**
	 * This loads the key bindings, should be called upon loading.
	 */
	static void load() {
		map = GameSettings.get().getKeyMapping();
	}
}
