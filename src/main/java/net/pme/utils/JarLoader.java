/**
 * 
 */
package net.pme.utils;

/**
 * @author Michael
 * 
 */
public class JarLoader {
	public static String translateString(String path) {
		if (path == null) {
			return null;
		}
		return JarLoader.class.getResource(path).getFile();
	}
}
