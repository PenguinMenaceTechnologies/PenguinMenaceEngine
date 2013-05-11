package net.pme.objects;

import net.pme.math.Vector3D;

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
	 * @param ID
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
	public NetworkObject(long ID, Vector3D position, Vector3D front,
			Vector3D up, int graphics) {
		super(ID, position, front, up, graphics);
	}
}
