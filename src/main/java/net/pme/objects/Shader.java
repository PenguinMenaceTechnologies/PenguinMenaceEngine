/**
 * 
 */
package net.pme.objects;

import java.util.HashMap;
import java.util.Set;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 * Shader binding to manage them.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public class Shader extends GameObject {
	private static final int UNIFORM4F_LENGTH = 4;
	private int vsId = 0;
	private int fsId = 0;
	private int program = 0;
	private HashMap<String, float[]> uniforms = new HashMap<String, float[]>();

	/**
	 * Create a shader with the given id and shader strings.
	 * 
	 * @param id
	 *            The id for the shader.
	 * @param vsh
	 *            The vertex shader. (as a string)
	 * @param fsh
	 *            The fragment shader. (as a string)
	 */
	public Shader(final long id, final String vsh, final String fsh) {
		super(id);
		try {
			if (vsh != null) {
				vsId = createShader(vsh, ARBVertexShader.GL_VERTEX_SHADER_ARB);
			}
			if (fsh != null) {
				fsId = createShader(fsh,
						ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
			}
			program = ARBShaderObjects.glCreateProgramObjectARB();

			if (program == 0) {
				throw new RuntimeException("Cannot create shader");
			}

			if (vsId != 0) {
				ARBShaderObjects.glAttachObjectARB(program, vsId);
			}
			if (fsId != 0) {
				ARBShaderObjects.glAttachObjectARB(program, fsId);
			}
			ARBShaderObjects.glLinkProgramARB(program);

			if (ARBShaderObjects.glGetObjectParameteriARB(program,
					ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
				throw new RuntimeException(getLogInfo(program));
			}

			ARBShaderObjects.glValidateProgramARB(program);
			if (ARBShaderObjects.glGetObjectParameteriARB(program,
					ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
				throw new RuntimeException(getLogInfo(program));
			}
		} catch (RuntimeException e) {
			delete();
			throw e;
		}
	}

	/**
	 * Make the shader active for all triangles drawn after this call. (Be
	 * careful when calling this. Objects of the Engine automatically call this
	 * if they have a shader attached.)
	 */
	public final void bind() {
		if (program != 0) {
			ARBShaderObjects.glUseProgramObjectARB(program);
			updateUniforms();
		} else {
			throw new RuntimeException("Cannot bind when shader was deleted!");
		}
	}

	/**
	 * Make the shader inactive. Technically activating a default shader. (Be
	 * careful when calling this. Objects of the Engine automatically call this
	 * if they have a shader attached.)
	 */
	public final void unbind() {
		ARBShaderObjects.glUseProgramObjectARB(0);
	}

	/**
	 * Bind a java array to be a uniform4f in your shader code. In the shader it
	 * is used by "uniform vec4 uniform_name;"
	 * 
	 * @param uniform
	 *            The name of the uniform.
	 * @param value
	 *            The array of size 4 which to bind to that uniform.
	 * @return Weather binding was successful or not.
	 */
	public final boolean setUniform4f(final String uniform, final float[] value) {
		boolean result = false;

		if (uniform != null && GL20.glGetUniformLocation(program, uniform) != 0
				&& value != null && value.length == UNIFORM4F_LENGTH) {
			uniforms.put(uniform, value);
			result = true;
		}

		return result;
	}

	/**
	 * Remove a uniform binding.
	 * 
	 * @param uniform
	 *            The uniform to remove.
	 * @return Weather the removal was successful or not. (It is successful as
	 *         long as the uniform was registered before)
	 */
	public final boolean removeUniform4f(final String uniform) {
		return uniforms.remove(uniform) != null;
	}

	/**
	 * Delete the shader.
	 * 
	 * Should be called every time before you null an object.
	 * 
	 * This automatically removes all uniform bindings, whereas they are no
	 * longer usefull.
	 */
	public final void delete() {
		Set<String> keys = uniforms.keySet();
		for (String k : keys) {
			removeUniform4f(k);
		}
		if (vsId != 0) {
			ARBShaderObjects.glDeleteObjectARB(vsId);
			vsId = 0;
		}
		if (fsId != 0) {
			ARBShaderObjects.glDeleteObjectARB(fsId);
			fsId = 0;
		}
		if (program != 0) {
			ARBShaderObjects.glDeleteObjectARB(program);
			program = 0;
		}
	}

	/**
	 * Update uniforms.
	 */
	private void updateUniforms() {
		Set<String> keys = uniforms.keySet();
		for (String k : keys) {
			float[] value = uniforms.get(k);
			GL20.glUniform4f(GL20.glGetUniformLocation(program, k), value[0],
					value[1], value[2], value[3]);
		}
	}

	/**
	 * Create a shader.
	 * @param shaderString The string describing the shader.
	 * @param shaderType The type of the shader.
	 * @return The int to the shader in gl.
	 */
	private int createShader(final String shaderString, final int shaderType) {
		int shader = 0;
		shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
		if (shader == 0) {
			return 0;
		}
		ARBShaderObjects.glShaderSourceARB(shader, shaderString);
		ARBShaderObjects.glCompileShaderARB(shader);

		if (ARBShaderObjects.glGetObjectParameteriARB(shader,
				ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) {
			throw new RuntimeException("Error creating shader: "
					+ getLogInfo(shader));
		}

		return shader;
	}

	/**
	 * Get the log info for a shader object.
	 * @param obj The shader for which to get the log.
	 * @return The log info.
	 */
	private String getLogInfo(final int obj) {
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects
				.glGetObjectParameteriARB(obj,
						ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}
}
