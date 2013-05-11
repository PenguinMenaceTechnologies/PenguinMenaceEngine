package net.pme.objects;

import net.pme.math.Vector3D;

/**
 * Models a movable object.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public abstract class MovableObject extends GameObject {
	/**
	 * The position of the object.
	 */
	protected Vector3D position;

	/**
	 * Create a new movable object.
	 * 
	 * @param id
	 *            The id.
	 * @param position
	 *            The position.
	 */
	public MovableObject(long id, Vector3D position) {
		super(id);
		this.position = position;
	}

	/**
	 * Move the movable object here.
	 * 
	 * @param elapsedTime
	 *            The time that the last frame took to calculate. (in seconds)
	 */
	public abstract void move(double elapsedTime);
}
