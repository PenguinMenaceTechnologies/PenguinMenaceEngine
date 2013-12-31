package net.pme.objects;

import net.pme.math.Vector3D;
import net.pme.model.Model;

/**
 * Models an object that's supposed to be synced over the network.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public abstract class NetworkObject extends RenderableObject implements
		Networkable {
	/**
	 * Create a new NetworkObject.
	 * 
	 * @param id
	 *            The id.
	 * @param position
	 *            The position.
	 * @param front
	 *            The direction the object is facing at.
	 * @param up
	 *            The direction where the objects up is.
	 * @param graphics
	 *            The model identifier.
	 */
	public NetworkObject(final long id, final Vector3D position, final Vector3D front,
			final Vector3D up, final Model graphics) {
		super(id, position, front, up, graphics);
	}
}
