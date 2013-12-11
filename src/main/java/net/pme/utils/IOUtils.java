package net.pme.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Utility class for reading and writing to files.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public final class IOUtils {

	/**
	 * The UTF-8 character set.
	 */
	private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
	private static final int MAXLEN = 65535;

	/**
	 * Writes a string to the buffer.
	 * 
	 * @param out
	 *            The buffer.
	 * @param str
	 *            The string.
	 * @throws IOException
	 *             Loading the file failed.
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
	 * @param out
	 *            The buffer.
	 * @param str
	 *            The string.
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
	 * @param in
	 *            The buffer.
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
	 * @param in
	 *            The buffer.
	 * @return The string.
	 * @throws IOException When loading the file failed.
	 */
	public static String readUtf8String(final DataInputStream in) throws IOException {
		int len = in.readUnsignedShort();

		byte[] bytes = new byte[len];
		in.readFully(bytes);

		return new String(bytes, CHARSET_UTF8);
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private IOUtils() {

	}

}
