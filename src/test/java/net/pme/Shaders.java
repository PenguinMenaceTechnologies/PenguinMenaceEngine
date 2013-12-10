/**
 * 
 */
package net.pme;

/**
 * Test shaders.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public class Shaders {

	public static String fsh = "uniform vec4 color;" + "void main(void) {"
			+ "gl_FragColor = (gl_Color/2.0)+(color/2.0);" + "}";
}