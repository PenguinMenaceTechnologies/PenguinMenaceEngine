package net.pme.graphics;

import net.pme.core.config.GameSettings;
import net.pme.core.config.MemoryConfiguration;

/**
 * This class maps inputs (integers) to strings.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public final class GameInput {
	private static MemoryConfiguration map = null;
	
	/**
	 * Utility classes should not have public constructor.
	 */
	private GameInput() {
		
	}

	/**
	 * Get a key integer for a string input.
	 * 
	 * @param key
	 *            The input type to get the code for.
	 * @return The integer representing the key.
	 * @throws IllegalAccessException When the requested code was wrong.
	 */
	public static int getCode(final String key) throws IllegalAccessException {
		if (map == null) {
			throw new IllegalAccessException(
					"The keymap has to be loaded first.");
		}
		if (!map.isSet(key)) {
			throw new IllegalArgumentException(
					"The given key was not specified.");
		}
		return map.getInteger(key);
	}

	/**
	 * This loads the key bindings, should be called upon loading.
	 */
	static void load(GameSettings settings) {
		map = settings.getKeyMapping();
	}
}
