package net.pme;

import java.util.HashMap;

/**
 * Manages all settings of the game.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public abstract class GameSettings {
	private static GameSettings instance;
	private String libraryPath;

	/**
	 * Get the game settings.
	 * 
	 * @return The current settings.
	 */
	public static final GameSettings get() {
		return instance;
	}

	/**
	 * Set the game Settings
	 * 
	 * @param settings
	 *            The new game settings.
	 */
	public static final void set(GameSettings settings) {
		instance = settings;
	}

	/**
	 * Give back the key mapping.
	 * 
	 * @return A hash map containing the key mapping.
	 */
	final HashMap<String, Integer> getKeyMapping() {
		// TODO try loading from file. Iff failure call setDefaultKeyMapping().
		return setDefaultKeyMapping();
	}

	/**
	 * When there cannot be loaded a key mapping, this defines the default
	 * mapping.
	 */
	protected abstract HashMap<String, Integer> setDefaultKeyMapping();

	/**
	 * Give back the path where the binaries are located.
	 * 
	 * @return The path to the binaries.
	 */
	final String getLibraryPath() {
		// TODO try loading from file. Iff failure call setDefaultLibraryPath().
		libraryPath = setDefaultLibraryPath();
		return libraryPath;
	}

	/**
	 * The default library path will be asked if there is no setting already
	 * made for that.
	 * 
	 * @return The default LibraryPath.
	 */
	protected abstract String setDefaultLibraryPath();
}
