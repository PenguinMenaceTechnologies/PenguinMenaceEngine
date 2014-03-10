package net.pme.gameloop;

import net.pme.core.math.Vector3D;
import net.pme.core.GameObject;

/**
 * Models a movable object.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public interface LoopableAttachment {

	/**
	 * Move the movable object here.
	 * 
	 * @param elapsedTime
	 *            The time that the last frame took to calculate. (in seconds)
	 */
	void update(double elapsedTime);
}
