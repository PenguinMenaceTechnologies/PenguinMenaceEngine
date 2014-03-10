package net.pme.core.config;

import java.util.HashMap;

/**
 * A MemoryConfiguration contains keys and values in a format that can be stored
 * and read.
 * 
 * @author Michael Fürst, Daniel Wieland
 * @version 1.0
 */
public class MemoryConfiguration {
	/**
	 * Modeling the values of the configuration.
	 */
	private HashMap<String, String> values;

	/**
	 * Create a new memory configuration.
	 */
	public MemoryConfiguration() {
		this.values = new HashMap<String, String>();
	}


	/**
	 * Get the values of this object.
	 * @return The values.
	 */
	protected final HashMap<String, String> getValues() {
		return values;
	}
	
	/**
	 * Get a string to the given key.
	 * 
	 * @param key
	 *            The key.
	 * @return The string for the key.
	 */
	public final String getString(final String key) {
		return getString(key, null);
	}

	/**
	 * Get a string to the given key.
	 * 
	 * @param key
	 *            The key.
	 * @param def
	 *            The default value.
	 * @return The string for the key.
	 */
	public final String getString(final String key, final String def) {
		String res = values.get(key);

		if (res == null) {
			res = def;
		}

		return res;
	}

    /**
     * Get an integer for the given key.
     *
     * @param key
     *            The key.
     * @return The integer for the key.
     */
    public final int getInteger(final String key) {
        return getInteger(key, 0);
    }

    /**
     * Get an integer for the given key.
     *
     * @param key
     *            The key.
     * @param def
     *            The default value.
     * @return The integer for the key.
     */
    public final int getInteger(final String key, final int def) {
        String res = values.get(key);
        if (res == null) {
            return def;
        }

        try {
            return Integer.parseInt(res);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * Get an float for the given key.
     *
     * @param key
     *            The key.
     * @return The float for the key.
     */
    public final float getFloat(final String key) {
        return getFloat(key, 0);
    }

    /**
     * Get an float for the given key.
     *
     * @param key
     *            The key.
     * @param def
     *            The default value.
     * @return The float for the key.
     */
    public final float getFloat(final String key, final float def) {
        String res = values.get(key);
        if (res == null) {
            return def;
        }

        try {
            return Float.parseFloat(res);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * Get an double for the given key.
     *
     * @param key
     *            The key.
     * @return The double for the key.
     */
    public final double getDouble(final String key) {
        return getDouble(key, 0);
    }

    /**
     * Get an double for the given key.
     *
     * @param key
     *            The key.
     * @param def
     *            The default value.
     * @return The double for the key.
     */
    public final double getDouble(final String key, final double def) {
        String res = values.get(key);
        if (res == null) {
            return def;
        }

        try {
            return Double.parseDouble(res);
        } catch (NumberFormatException e) {
            return def;
        }
    }

	/**
	 * Get a boolean for the given key.
	 * 
	 * @param key
	 *            The key.
	 * @param def
	 *            The default value.
	 * @return The boolean for the key.
	 */
	public final boolean getBoolean(final String key, final boolean def) {
		if (!isSet(key)) {
			return def;
		}

		return getBoolean(key);
	}

	/**
	 * Get a boolean for the given key.
	 * 
	 * @param key
	 *            The key.
	 * @return The boolean for the key.
	 */
	public final boolean getBoolean(final String key) {
		String res = values.get(key);

		return Boolean.parseBoolean(res);
	}

	/**
	 * Is the key defined?
	 * 
	 * @param key
	 *            The key to check.
	 * @return True, if the key is defined.
	 */
	public final boolean isSet(final String key) {
		return values.containsKey(key);
	}

	/**
	 * Set the value for the key.
	 * 
	 * @param key
	 *            The key.
	 * @param value
	 *            The value. This Object must implement toString method.
	 */
	public final void set(final String key, final Object value) {
		if (value == null) {
			return;
		}

		values.put(key, value.toString());
	}

	/**
	 * Remove the given key.
	 * 
	 * @param key
	 *            The key to remove.
	 */
	public final void remove(final String key) {
		values.remove(key);
	}

	/**
	 * Set this map equal to another memory configuration.
	 * 
	 * @param m
	 *            The other memory configuration.
	 */
	public final void setMap(final MemoryConfiguration m) {
		values = m.values;
	}
}