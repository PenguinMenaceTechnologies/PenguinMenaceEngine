package net.pme.objects;

/**
 * Models a GameObject int the PM-Engine. All game objects require an ID.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public abstract class GameObject {
	/**
	 * The id of an object.
	 */
	public final long id;

	/**
	 * Create a new gameobject.
	 * 
	 * @param id
	 *            The id of the object.
	 */
	public GameObject(long id) {
		this.id = id;
	}
}
