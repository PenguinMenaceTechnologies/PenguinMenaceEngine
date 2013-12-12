package net.pme;

import net.pme.math.Vector3D;
import net.pme.objects.RenderableObject;

/**
 * A simple cube.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public class TestCube1 extends RenderableObject {
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
	 */
	public TestCube1(long id, Vector3D position, Vector3D front, Vector3D up) {
		super(id, position, front, up, ModelManager.getSpecialCoords(Test.class.getResource(
				"/assets/cube_small.obj").getPath()));
	}

	@Override
	public void move(double elapsedTime) {
		rotateAroundFrontAxis(elapsedTime * 10);
	}
}
