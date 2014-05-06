package net.pme.core.utils;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Utility class for reading and writing to files.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public final class IOUtils {
    /**
     * The buffer size for file unzipping.
     */
    private static final int BUFFER_SIZE = 16 * 1024;

    /**
     * The UTF-8 character set.
     */
    private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
    private static final int MAXLEN = 65535;

    /**
     * Default private constructor to prevent instantiation.
     */
    private IOUtils() {

    }

    public static File getFile(String path, Class callee) throws IOException{
        if (path.startsWith("resource://")) {
            path = path.substring(11);
            path = extractFile(path, callee);
        }
        return new File(path);
    }

    /**
     * Save a library to a temp file.
     *
     * @param library The library to save.
     * @return The path where the temp file was created.
     * @throws IOException When something went wrong.
     */
    private static String extractFile(final String library, Class callee) throws IOException {
        InputStream in = null;
        OutputStream out = null;

        try {
            String libraryName = "/" + library;
            in = callee.getResourceAsStream(libraryName);
            if (in == null) {
                throw new IllegalArgumentException("File not found! "
                        + libraryName);
            }
            String tmpDirName = System.getProperty("java.io.tmpdir");
            File tmpDir = new File(tmpDirName);
            if (!tmpDir.exists()) {
                tmpDir.mkdir();
            }
            File file = new File(tmpDir + "/pme/" + library);
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

    /**
     * Writes a string to the buffer.
     *
     * @param out The buffer.
     * @param str The string.
     * @throws IOException Loading the file failed.
     */
    public static void writeString(final DataOutputStream out, final String str)
            throws IOException {
        int len = str.length();
        if (len > MAXLEN) {
            throw new IllegalArgumentException("String too long.");
        }

        out.writeShort(len);
        for (int i = 0; i < len; ++i) {
            out.writeChar(str.charAt(i));
        }
    }

    /**
     * Writes a UTF-8 string to the buffer.
     *
     * @param out The buffer.
     * @param str The string.
     * @throws IOException When opening the file failed.
     */
    public static void writeUtf8String(final DataOutputStream out, final String str)
            throws IOException {
        byte[] bytes = str.getBytes(CHARSET_UTF8);
        if (bytes.length > MAXLEN) {
            throw new IllegalArgumentException("Encoded UTF-8 string too long.");
        }

        out.writeShort(bytes.length);
        out.write(bytes);
    }

    /**
     * Reads a string from the buffer.
     *
     * @param in The buffer.
     * @return The string.
     * @throws IOException When opening the file failed.
     */
    public static String readString(final DataInputStream in) throws IOException {
        int len = in.readUnsignedShort();

        char[] characters = new char[len];
        for (int i = 0; i < len; i++) {
            characters[i] = in.readChar();
        }

        return new String(characters);
    }

    /**
     * Reads a UTF-8 encoded string from the buffer.
     *
     * @param in The buffer.
     * @return The string.
     * @throws IOException When loading the file failed.
     */
    public static String readUtf8String(final DataInputStream in) throws IOException {
        int len = in.readUnsignedShort();

        byte[] bytes = new byte[len];
        in.readFully(bytes);

        return new String(bytes, CHARSET_UTF8);
    }

}
