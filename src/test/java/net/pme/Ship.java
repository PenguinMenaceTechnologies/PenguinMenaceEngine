package net.pme;

import net.pme.graphics.Shader;
import net.pme.model.Model;
import org.lwjgl.opengl.GL11;

import net.pme.core.GameObject;
import net.pme.core.math.Vector3D;
import net.pme.gameloop.LoopableAttachment;
import net.pme.graphics.RenderAttachment;
import net.pme.model.ModelManager;

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
	 * @param id
	 *            The id.
	 * @param position
	 *            The position.
	 * @param front
	 *            The front axis.
     * @param up
     *            The up axis.
     * @param model
     *            The up axis.
	 **/
	public Ship(long id, Vector3D position, Vector3D front, Vector3D up,final Model model) {
		super(id, position);
        final GameObject parent = this;
        setRenderAttachment(new RenderAttachment(this, front, up, model) {
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
        //this.getRenderAttachment().attachShader(new Shader(null, Shaders.fsh));
        setLoopableAttachment(new LoopableAttachment() {
            @Override
            public void update(double elapsedTime) {
                parent.getRenderAttachment().rotateAroundUpAxis(elapsedTime * 10);
                parent.getRenderAttachment().move(new Vector3D(0, 0, elapsedTime));
            }
        });
	}
}
