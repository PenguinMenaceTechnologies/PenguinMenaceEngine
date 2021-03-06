package net.pme.graphics;
/**
 *
 */

import net.pme.core.math.Matrix;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

/**
 * Shader binding to manage them.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public class Shader {
    private static final int UNIFORM4F_LENGTH = 4;
    private int vsId = 0;
    private int fsId = 0;
    private int program = 0;
    private HashMap<Integer, float[]> uniforms = new HashMap<>();
    private HashMap<Integer, Matrix> uniformsMat = new HashMap<>();
    private HashMap<Integer, FloatBuffer> uniformsMatBuffers = new HashMap<>();

    /**
     * Create a new shader from files in the given path.
     * <p/>
     * There will be automatically loaded path.fsh and path.vsh.
     *
     * @param path The path to the files, without the file ending. There must be a path.vsh and a path.fsh.
     */
    public Shader(String path) {
        this(readFromFile(path + ".vsh"), readFromFile(path + ".fsh"));
    }

    /**
     * Create a shader with the given id and shader strings.
     *
     * @param vsh The vertex shader. (as a string)
     * @param fsh The fragment shader. (as a string)
     */
    public Shader(final String vsh, final String fsh) {
        if (!Display.isCreated()) {
            throw new IllegalStateException("You need to initialize the display module first.");
        }
        try {
            if (vsh != null) {
                vsId = createShader(vsh, ARBVertexShader.GL_VERTEX_SHADER_ARB);
            }
            if (fsh != null) {
                fsId = createShader(fsh,
                        ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
            }
            program = ARBShaderObjects.glCreateProgramObjectARB();

            if (program <= 0) {
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
     * Load a shader from a file into a string.
     *
     * @param pathname The file to open.
     * @return The string read from the file or null if not found.
     */
    static private String readFromFile(String pathname) {
        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int) file.length());
        try {
            Scanner scanner = new Scanner(file);
            String lineSeparator = System.getProperty("line.separator");

            try {
                while (scanner.hasNextLine()) {
                    fileContents.append(scanner.nextLine() + lineSeparator);
                }
                return fileContents.toString();
            } finally {
                scanner.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Make the shader active for all triangles drawn after this call. (Be
     * careful when calling this. Objects of the Engine automatically call this
     * if they have a shader attached.)
     */
    public final void bind() {
        if (program > 0) {
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
     * @param uniform The name of the uniform.
     * @param value   The array of size 4 which to bind to that uniform.
     * @return Weather binding was successful or not.
     */
    public final boolean setUniform4f(final String uniform, final float[] value) {
        boolean result = false;

        if (uniform != null && program > 0 && GL20.glGetUniformLocation(program, uniform) >= 0
                && value != null && value.length == UNIFORM4F_LENGTH) {
            uniforms.put(GL20.glGetUniformLocation(program, uniform), value);
            result = true;
        }

        return result;
    }

    /**
     * Bind a java array to be a uniform4f in your shader code. In the shader it
     * is used by "uniform mat4 uniform_name;"
     *
     * @param uniform The name of the uniform.
     * @param value   The array of size 4 which to bind to that uniform.
     * @return Weather binding was successful or not.
     */
    public final boolean setUniformMat(final String uniform, final Matrix value) {
        boolean result = false;

        if (uniform != null && program > 0 && GL20.glGetUniformLocation(program, uniform) >= 0
                && value != null) {
            uniformsMat.put(GL20.glGetUniformLocation(program, uniform), value);
            uniformsMatBuffers.put(GL20.glGetUniformLocation(program, uniform), value.getValuesF(null));
            result = true;
        }

        return result;
    }

    /**
     * Remove a uniform binding.
     *
     * @param uniform The uniform to remove.
     * @return Weather the removal was successful or not. (It is successful as
     * long as the uniform was registered before)
     */
    public final boolean removeUniform4f(final String uniform) {
        return uniforms.remove(GL20.glGetUniformLocation(program, uniform)) != null;
    }

    /**
     * Remove a uniform binding.
     *
     * @param uniform The uniform to remove.
     * @return Weather the removal was successful or not. (It is successful as
     * long as the uniform was registered before)
     */
    public final boolean removeUniformMat(final String uniform) {
        uniformsMatBuffers.remove(GL20.glGetUniformLocation(program, uniform));
        return uniformsMat.remove(GL20.glGetUniformLocation(program, uniform)) != null;
    }

    /**
     * Delete the shader.
     * <p/>
     * Should be called every time before you null an object.
     * <p/>
     * This automatically removes all uniform bindings, whereas they are no
     * longer usefull.
     */
    public final void delete() {
        Set<Integer> keys = uniforms.keySet();
        for (Integer k : keys) {
            uniforms.remove(k);
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
        Set<Integer> keys = uniforms.keySet();
        for (Integer k : keys) {
            float[] value = uniforms.get(k);
            GL20.glUniform4f(k, value[0],
                    value[1], value[2], value[3]);
        }

        keys = uniformsMat.keySet();
        for (Integer k : keys) {
            Matrix value = uniformsMat.get(k);
            FloatBuffer buffer = uniformsMatBuffers.get(k);
            buffer.rewind();
            GL20.glUniformMatrix4(k, false, value.getValuesF(buffer));
        }
    }

    /**
     * Create a shader.
     *
     * @param shaderString The string describing the shader.
     * @param shaderType   The type of the shader.
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
     *
     * @param obj The shader for which to get the log.
     * @return The log info.
     */
    private String getLogInfo(final int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects
                .glGetObjectParameteriARB(obj,
                        ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    public void setupTexture(String name, int texture, int pos) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + pos);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);

        int loc = GL20.glGetUniformLocation(program, name);
        GL20.glUniform1i(loc, pos);
    }

    @Override
    public void finalize() {
        delete();
    }

    /**
     * Get an instance of the default shader.
     * @return The default shader.
     */
    public static Shader getDefaultShader() {
        // TODO implement.
        return null;
    }
}
