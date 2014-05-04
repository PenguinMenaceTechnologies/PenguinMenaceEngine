/**
 *
 */
package net.pme;

import net.pme.graphics.Shader;

/**
 * @author Michael
 */
public class PostprocessingShader extends Shader {
    private static final String VS = null;
    private static final String FS = ""
            + "#version 110\n"
            + "uniform sampler2D texture1;"
            + "void main(void) {"
            + "gl_FragColor = vec4(0.0,0.0,0.4,0.1) + texture2D(texture1, gl_TexCoord[0].st);" + "}";


    public PostprocessingShader() {
        super(VS, FS);
    }
}
