package net.pme;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Loads the required OpenGL libraries.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
final class LibraryLoader {
	private LibraryLoader() {
	}

	/**
	 * Load the libraries from the given path.
	 * 
	 * @param path
	 *            The path where the binaries are stored in.
	 * @throws Exception
	 */
	final static void loadLibraries(final String path) throws Exception {
		if (path == null || path.equals("")) {
			throw new IllegalArgumentException("Invalid argument!");
		}
		if (System.getProperty("os.name").equals("Mac OS X")) {
			addLibraryPath(path + "/macosx");
		} else if (System.getProperty("os.name").equals("Linux")) {
			addLibraryPath(path + "/linux");
		} else {
			if (System.getProperty("os.arch").equals("amd64")
					|| System.getProperty("os.arch").equals("x86_64")) {
				addLibraryPath(path + "\\windows64");
				System.loadLibrary("OpenAL64");
			} else {
				addLibraryPath(path + "\\windows32");
				System.loadLibrary("OpenAL32");
			}
		}
	}

	private static void addLibraryPath(String s) throws Exception {
		final Field usr_paths_field = ClassLoader.class
				.getDeclaredField("usr_paths");
		usr_paths_field.setAccessible(true);

		final String[] paths = (String[]) usr_paths_field.get(null);

		for (String path : paths) {
			if (path.equals(s)) {
				return;
			}
		}

		final String[] new_paths = Arrays.copyOf(paths, paths.length + 1);
		new_paths[new_paths.length - 1] = s;
		usr_paths_field.set(null, new_paths);
	}
}
