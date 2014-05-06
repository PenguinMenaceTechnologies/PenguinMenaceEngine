package net.pme;

import net.pme.core.GameObject;
import net.pme.core.math.Vector3d;
import net.pme.jobcenter.LoopableAttachment;
import net.pme.graphics.RenderAttachment;
import net.pme.graphics.Shader;
import net.pme.model.Model;
import org.lwjgl.opengl.GL11;

/**
 * A simple cube.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public class Ship extends GameObject {
    /**
     * A Test cube.
     *
     * @param id       The id.
     * @param position The position.
     * @param front    The front axis.
     * @param up       The up axis.
     * @param graphics The up axis.
     */
    public Ship(long id, Vector3d position, Vector3d front, Vector3d up, Model graphics) {
        super(id, position, front, up);
        final GameObject parent = this;
        setRenderAttachment(new RenderAttachment(this, graphics) {
            @Override
            protected void specialFX() {
                if (this.getModel() != null) {
                    return;
                }
                GL11.glBegin(GL11.GL_QUADS);
                {
                    // Quader
                    GL11.glColor3f(0.5f, 0.5f, 0.0f);
                    GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
                    GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
                    GL11.glVertex3f(1.0f, 1.0f, -1.0f);
                    GL11.glVertex3f(1.0f, -1.0f, -1.0f);

                    GL11.glColor3f(0.5f, 0.8f, 0.0f);
                    GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
                    GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
                    GL11.glVertex3f(1.0f, 1.0f, 1.0f);
                    GL11.glVertex3f(1.0f, 1.0f, -1.0f);

                    GL11.glColor3f(0.8f, 0.5f, 0.0f);
                    GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
                    GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
                    GL11.glVertex3f(1.0f, -1.0f, 1.0f);
                    GL11.glVertex3f(1.0f, -1.0f, -1.0f);

                    GL11.glColor3f(0.5f, 0.0f, 0.5f);
                    GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
                    GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
                    GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
                    GL11.glVertex3f(-1.0f, 1.0f, -1.0f);

                    GL11.glColor3f(0.5f, 0.5f, 0.5f);
                    GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
                    GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
                    GL11.glVertex3f(1.0f, 1.0f, 1.0f);
                    GL11.glVertex3f(1.0f, -1.0f, 1.0f);

                    GL11.glColor3f(0.0f, 0.0f, 0.5f);
                    GL11.glVertex3f(1.0f, -1.0f, -1.0f);
                    GL11.glVertex3f(1.0f, -1.0f, 1.0f);
                    GL11.glVertex3f(1.0f, 1.0f, 1.0f);
                    GL11.glVertex3f(1.0f, 1.0f, -1.0f);
                }
                GL11.glEnd();

            }
        });
        this.getRenderAttachment().setShader(new Shader(null, Shaders.fsh));
        setLoopableAttachment(new LoopableAttachment() {
            @Override
            public void update(double elapsedTime) {
                parent.rotateAroundUpAxis(elapsedTime * 10);
                parent.move(new Vector3d(0, 0, elapsedTime));
            }
        });
    }
}
