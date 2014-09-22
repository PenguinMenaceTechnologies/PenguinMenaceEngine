package net.pme.core.config;

import java.io.File;
import java.io.IOException;

/**
 * Manages all settings of the game.
 * <p/>
 * The settings are loaded when first requested and saved upon unloading game.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public abstract class GameSettings {
    private FileConfiguration keyMapping = null;
    private FileConfiguration settings = null;
    private String configPath = "config/settings.config";
    private String keyPath = "config/keymapping.config";

    static {
        File f = new File("config");
        f.mkdirs();
    }

    /**
     * Save all settings to files.
     */
    public final void saveAll() {
        if (keyMapping != null) {
            keyMapping.save();
        }
        if (settings != null) {
            settings.save();
        }
    }

    /**
     * Allow to change the path for keymapping.
     * @param path The path.
     */
    public final void setKeyMappingPath(final String path) {
        keyPath = path;
    }

    /**
     * Allow to change the path for settings.
     * @param path The path.
     */
    public final void setSettingsPath(final String path) {
        configPath = path;
    }

    /**
     * Give back the key mapping.
     *
     * @return A hash map containing the key mapping.
     */
    public final MemoryConfiguration getKeyMapping() {
        if (keyMapping != null) {
            return keyMapping;
        }
        File f = new File(keyPath);
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
    public final MemoryConfiguration getSettings() {
        if (settings != null) {
            return settings;
        }
        File f = new File(configPath);
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
     *
     * @return A default key mapping.
     */
    protected abstract MemoryConfiguration setDefaultKeyMapping();

    /**
     * When there cannot be loaded a settings map, this defines the default
     * mapping.
     *
     * @return a default settings mapping.
     */
    protected abstract MemoryConfiguration setDefaultSettings();
}
