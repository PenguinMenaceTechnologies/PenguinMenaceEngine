package net.pme.gameloop;

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
     * @param elapsedTime The time that the last frame took to calculate. (in seconds)
     */
    void update(double elapsedTime);
}
