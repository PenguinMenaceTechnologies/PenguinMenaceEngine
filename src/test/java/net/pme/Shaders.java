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
    public static String fsh = "#version 120\n" +
            "uniform vec4 color;\n" +
            "uniform mat4 testMat;\n" +
            "uniform sampler2D texture1;\n"
            + "void main(void) {\n"
            + "gl_FragColor = testMat[1][1] * ( texture2D( texture1, gl_TexCoord[0].st ) / 2.0) + ( color / 2.0 );\n"
            + "}";
}