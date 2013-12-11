/**
 * 
 */
package net.pme.utils;

/**
 * Whenever a file is formatted incorrect.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public class FileFormatException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Generate a file format exception.
	 * 
	 * @param text
	 *            The text to print.
	 * @param file
	 *            The file where the error was.
	 * @param line
	 *            The line where the error was.
	 */
	public FileFormatException(final String text, final String file, final int line) {
		super(text + "(" + file + " l." + line + ")");
	}
}
