/**
 * 
 */
package net.pme.objects;

import net.pme.objects.Shader;

/**
 * @author Michael
 *
 */
public class PostprocessingShader extends Shader {
	private static final String VS = null;
	private static final String FS = ""
			+ "#version 110\n"
			+ "uniform sampler2D texture1;"
			+ "void main(void) {"
			+ "gl_FragColor = texture2D(texture1, gl_TexCoord[0].st);" + "}";
	

	public PostprocessingShader(long id) {
		super(id, VS, FS);
	}
}
