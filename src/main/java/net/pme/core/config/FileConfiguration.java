package net.pme.core.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

/**
 * Load a MemoryConfiguration from a file using a FileConfiguration.
 * 
 * @author Michael Fürst, Daniel Wieland
 * @version 1.0
 */
public final class FileConfiguration extends MemoryConfiguration {
	private File file;

	/**
	 * Create a file configuration without a file given.
	 */
	public FileConfiguration() {
		this(null);
	}

	/**
	 * Create a file configuration telling in what file to safe changes.
	 * 
	 * @param initialFile
	 *            The file in which to save the configuration when calling
	 *            save().
	 */
	public FileConfiguration(final File initialFile) {
		this.file = initialFile;
	}

	/**
	 * Create a file configuration from a given file.
	 * 
	 * @param file
	 *            The file to parse into a file configuration.
	 * @return The file configuration.
	 */
	public static FileConfiguration parseFromFile(final File file) {
		FileConfiguration configuration = new FileConfiguration(file);

		if (file == null || !file.exists()) {
			return configuration;
		}

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));

			String line = reader.readLine();
			while (line != null) {
				String key, value;
				int i = line.indexOf("=");

				if (i > 0) {
					key = line.substring(0, i);
					value = line.substring(i + 1);

					configuration.set(key, value);
				}

				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return configuration;
	}

	/**
	 * Save the file configuration to a file.
	 * 
	 * @return True, if saving was successful.
	 */
	public boolean save() {
		return save(file);
	}

	/**
	 * Save the file configuration to a file.
	 * 
	 * @param overrideFile
	 *            The file in which to save the configuration.
	 * @return True, if saving was successful.
	 */
	public boolean save(final File overrideFile) {
		if (overrideFile == null) {
			return false;
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(overrideFile,
					false));

			Set<String> keySet = getValues().keySet();
			for (String key : keySet) {
				String value = getValues().get(key);

				writer.write(key);
				writer.write('=');
				writer.write(value);
				writer.newLine();
				writer.flush();
			}

			writer.close();
		} catch (IOException e) {
			return false;
		}

		return true;
	}
}