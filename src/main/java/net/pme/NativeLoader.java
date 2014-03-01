package net.pme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * A native library loader.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public final class NativeLoader {
	private static final int BUFFER_SIZE = 16 * 1024;
	private static final String[] WINDOWS_32 = { "jinput-dx8.dll",
			"jinput-raw.dll", "lwjgl.dll", "OpenAL32.dll" };
	private static final String[] WINDOWS_64 = { "jinput-dx8_64.dll",
			"jinput-raw_64.dll", "lwjgl64.dll", "OpenAL64.dll" };
	private static final String[] LINUX32 = { "liblwjgl.so", "libopenal.so",
			"libjinput-linux.so" };
	private static final String[] LINUX64 = { "liblwjgl64.so", "libopenal64.so",
			"libjinput-linux64.so" };
	private static final String[] MAC = { "liblwjgl.jnilib", "openal.dylib",
			"libjinput-osx.jnilib" };

	/**
	 * Utility classes should not have public constructors.
	 */
	private NativeLoader() {
	}

	/**
	 * Add a library path.
	 * @param s The path.
	 * @throws Exception When something goes wrong.
	 */
	private static void addLibraryPath(final String s) throws Exception {
		final Field usrPathsField = ClassLoader.class
				.getDeclaredField("usr_paths");
		usrPathsField.setAccessible(true);

		final String[] paths = (String[]) usrPathsField.get(null);

		for (String path : paths) {
			if (path.equals(s)) {
				return;
			}
		}

		final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
		newPaths[newPaths.length - 1] = s;
		usrPathsField.set(null, newPaths);
	}

	/**
	 * Unload the libraries. Removes the temporary files.
	 */
	static void unloadLibraries() {
		String tmpDirName = System.getProperty("java.io.tmpdir");
		File tmpDir = new File(tmpDirName);
		if (!tmpDir.exists()) {
			tmpDir.mkdir();
		}
		File file = new File(tmpDir + "/pme-libs");
		file.delete();
	}

	/**
	 * Load the native libraries. This creates temporary objects.
	 */
	static void loadLibraries() {
		String os = System.getProperty("os.name");
		String arch = System.getProperty("os.arch");
		if (os.equalsIgnoreCase("mac os x")) {
			loadLibs(MAC);
		} else if (os.equalsIgnoreCase("linux")) {
			if (arch.equalsIgnoreCase("amd64")
					|| arch.equalsIgnoreCase("x86_64")) {
				loadLibs(LINUX64);
			} else {
				loadLibs(LINUX32);
			}
		} else {
			if (arch.equalsIgnoreCase("amd64")
					|| arch.equalsIgnoreCase("x86_64")) {
				loadLibs(WINDOWS_64);
				System.loadLibrary("OpenAL64");
			} else {
				loadLibs(WINDOWS_32);
				System.loadLibrary("OpenAL32");
			}
		}
	}

	/**
	 * Load the given list of libraries.
	 * @param libs The list to load.
	 */
	private static void loadLibs(final String[] libs) {
		NativeLoader l = new NativeLoader();
		for (String s : libs) {
			try {
				l.saveLibrary(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String tmpDirName = System.getProperty("java.io.tmpdir");
		File tmpDir = new File(tmpDirName);
		if (!tmpDir.exists()) {
			tmpDir.mkdir();
		}
		try {
			addLibraryPath(tmpDir + "/pme-libs");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Save a library to a temp file.
	 * @param library The library to save.
	 * @return The path where the temp file was created.
	 * @throws IOException When something went wrong.
	 */
	private String saveLibrary(final String library) throws IOException {
		InputStream in = null;
		OutputStream out = null;

		try {
			String libraryName = "/" + library;
			in = this.getClass().getResourceAsStream(libraryName);
			if (in == null) {
				throw new IllegalArgumentException("Library not found! "
						+ libraryName);
			}
			String tmpDirName = System.getProperty("java.io.tmpdir");
			File tmpDir = new File(tmpDirName);
			if (!tmpDir.exists()) {
				tmpDir.mkdir();
			}
			File file = new File(tmpDir + "/pme-libs/" + library);
			file.mkdirs();
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			// Clean up the file when exiting
			out = new FileOutputStream(file);

			int cnt;
			byte[] buf = new byte[BUFFER_SIZE];
			// copy until done.
			while ((cnt = in.read(buf)) >= 1) {
				out.write(buf, 0, cnt);
			}
			return file.getAbsolutePath();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}