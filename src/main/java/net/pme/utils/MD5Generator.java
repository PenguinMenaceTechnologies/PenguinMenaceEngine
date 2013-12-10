package net.pme.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A md5 generator.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public final class MD5Generator {
	private static final byte MASK = (byte) 0xFF;

	/**
	 * Generate a md5 string of a given string.
	 * 
	 * @param string
	 *            Given string.
	 * @return md5 string.
	 */
	public static String generate(final String string) {
		byte[] bytesOfMessage;
		try {
			bytesOfMessage = string.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}

		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
		byte[] digest = md.digest(bytesOfMessage);

		StringBuffer toReturn = new StringBuffer(digest.length);
		for (byte tiledigest : digest) {
			toReturn.append(Integer.toHexString(MASK & tiledigest));
		}

		return toReturn.toString();
	}

	/**
	 * Utility class.
	 */
	private MD5Generator() {
	}
}