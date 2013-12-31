package net.pme;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import net.pme.math.Vector3D;
import net.pme.objects.RenderableObject;

/**
 * A simple cube.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public class Ship extends RenderableObject {
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
	 * @throws IOException 
	 **/
	public Ship(long ID, Vector3D position, Vector3D front, Vector3D up) throws IOException {
		super(ID, position, front, up, ModelManager.get(Test.class.getResource(
				"/assets/ship.obj").getPath()));
//		super(ID, position, front, up, 0);
	}

	@Override
	public void move(double elapsedTime) {
		rotateAroundUpAxis(elapsedTime * 10);
		move(new Vector3D(0, 0, elapsedTime));
	}

	@Override
	protected void specialFX() {
		if (this.getGraphics() != null) {
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

}
