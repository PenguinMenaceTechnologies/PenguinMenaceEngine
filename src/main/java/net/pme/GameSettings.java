package net.pme;

import java.io.File;
import java.io.IOException;

import net.pme.config.FileConfiguration;
import net.pme.config.MemoryConfiguration;

/**
 * Manages all settings of the game.
 * 
 * The settings are loaded when first requested and saved upon unloading game.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public abstract class GameSettings {
	private static GameSettings instance;
	private FileConfiguration keyMapping = null;
	private FileConfiguration settings = null;

	static	{
		File f = new File("config");
		f.mkdirs();
	}
	
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
	 * Save all settings to files.
	 */
	final void saveAll() {
		if (keyMapping != null) {
			keyMapping.save();
		}
		if (settings != null) {
			settings.save();
		}
	}

	/**
	 * Give back the key mapping.
	 * 
	 * @return A hash map containing the key mapping.
	 */
	final MemoryConfiguration getKeyMapping() {
		if (keyMapping != null) {
			return keyMapping;
		}
		File f = new File("config/keymapping.config");
		if (f.exists()) {
			keyMapping = FileConfiguration.parseFromFile(f);
		} else {
			try {
				f.createNewFile();
				keyMapping = new FileConfiguration(f);
			} catch (IOException e) {
				keyMapping = new FileConfiguration();
				e.printStackTrace();
			}
			keyMapping.setMap(setDefaultKeyMapping());
			keyMapping.save();
		}
		return keyMapping;
	}
	
	/**
	 * Give back the setting string.
	 * 
	 * @return A hash map containing the setting strings.
	 */
	final MemoryConfiguration getSettings() {
		if (settings != null) {
			return settings;
		}
		File f = new File("config/settings.config");
		if (f.exists()) {
			settings = FileConfiguration.parseFromFile(f);
		} else {
			try {
				f.createNewFile();
				settings = new FileConfiguration(f);
			} catch (IOException e) {
				settings = new FileConfiguration();
				e.printStackTrace();
			}
			settings.setMap(setDefaultSettings());
			settings.save();
		}
		return settings;
	}

	/**
	 * When there cannot be loaded a key mapping, this defines the default
	 * mapping.
	 */
	protected abstract MemoryConfiguration setDefaultKeyMapping();
	
	/**
	 * When there cannot be loaded a settings map, this defines the default
	 * mapping.
	 */
	protected abstract MemoryConfiguration setDefaultSettings();
}
