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
	private Vector3D position;

	/**
	 * Create a new movable object.
	 * 
	 * @param id
	 *            The id.
	 * @param position
	 *            The position.
	 */
	public MovableObject(final long id, final Vector3D position) {
		super(id);
		this.setPosition(position);
	}

	/**
	 * Move the movable object here.
	 * 
	 * @param elapsedTime
	 *            The time that the last frame took to calculate. (in seconds)
	 */
	public abstract void move(double elapsedTime);

	/**
	 * @return the position
	 */
	public final Vector3D getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public final void setPosition(final Vector3D position) {
		this.position = position;
	}
}
