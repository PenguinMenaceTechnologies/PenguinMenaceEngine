package net.pme;

import net.pme.core.GameObject;
import net.pme.core.math.Matrix;
import net.pme.core.math.Vector3d;
import net.pme.graphics.RenderAttachment;
import net.pme.graphics.Shader;
import net.pme.jobcenter.LoopableAttachment;
import net.pme.model.Model;
import org.lwjgl.opengl.GL11;

/**
 * A simple cube.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public class TestModelStatic extends GameObject {
    /**
     * A Test cube.
     *
     * @param id       The id.
     * @param position The position.
     * @param front    The front axis.
     * @param up       The up axis.
     * @param graphics The up axis.
     */
    public TestModelStatic(long id, Vector3d position, Vector3d front, Vector3d up, Model graphics) {
        super(id, position, front, up);
        final GameObject parent = this;
        setRenderAttachment(new RenderAttachment(this, graphics) {});

        Shader sh = new Shader(null, Shaders.fsh);
        float[] uniform = new float[]{1f,1f,1f,1f};
        sh.setUniform4f("color", uniform);
        sh.setUniformMat("testMat", new Matrix());
        this.getRenderAttachment().setShader(sh);
        setLoopableAttachment(new LoopableAttachment() {
            @Override
            public void update(double elapsedTime) {
                parent.rotateAroundUpAxis(elapsedTime * 10);
            }
        });
    }
}
