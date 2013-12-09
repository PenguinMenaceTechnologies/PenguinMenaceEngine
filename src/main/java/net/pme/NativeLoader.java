package net.pme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

public class NativeLoader {

	public static final Logger LOG = Logger.getLogger("NativeLoader");
	private static final String[] WINDOWS_32 = {"jinput-dx8.dll", "jinput-raw.dll", "lwjgl.dll", "OpenAL32.dll"};
	private static final String[] WINDOWS_64 = {"jinput-dx8_64.dll", "jinput-raw_64.dll", "lwjgl64.dll", "OpenAL64.dll"};
	private static final String[] LINUX32 = {"libwjgl.so", "libopenal.so", "libjinput-linux.so"};
	private static final String[] LINUX64 = {"libwjgl64.so", "libopenal64.so", "libjinput-linux64.so"};
	private static final String[] MAC = {"liblwjgl.jnilib", "openal.dylib", "libjinput-osx.jnilib"};

	private NativeLoader() {
	}
	
	public static void loadLibraries() {
		String os = System.getProperty("os.arch");
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
	
	private static void loadLibs(String[] libs) {
		NativeLoader l = new NativeLoader();
		for (String s: libs) {
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
			LibraryLoader.addLibraryPath(tmpDir+"/pme-libs");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String saveLibrary(String library) throws IOException {
		InputStream in = null;
		OutputStream out = null;

		try {
			String libraryName = "/"+library;
			in = this.getClass().getResourceAsStream(libraryName);
			if (in == null) {
				throw new IllegalArgumentException("Library not found! "+libraryName);
			}
			String tmpDirName = System.getProperty("java.io.tmpdir");
			File tmpDir = new File(tmpDirName);
			if (!tmpDir.exists()) {
				tmpDir.mkdir();
			}
			File file = new File(tmpDir+"/pme-libs/"+library);
			file.mkdirs();
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			// Clean up the file when exiting
			out = new FileOutputStream(file);

			int cnt;
			byte buf[] = new byte[16 * 1024];
			// copy until done.
			while ((cnt = in.read(buf)) >= 1) {
				out.write(buf, 0, cnt);
			}
			LOG.info("Saved libfile: " + file.getAbsoluteFile());
			return file.getAbsolutePath();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ignore) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException ignore) {
				}
			}
		}
	}
}