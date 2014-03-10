package net.pme.core;

import net.pme.core.math.Vector3D;
import net.pme.gameloop.LoopableAttachment;
import net.pme.graphics.RenderAttachment;
import net.pme.physics.PhysicsAttachment;

/**
 * Models a GameObject int the PM-Engine. All game objects require an ID.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public abstract class GameObject {
    private RenderAttachment renderAttachment;
    private LoopableAttachment loopableAttachment;
    private PhysicsAttachment physicsAttachment;

    /**
     * The position of the object.
     */
    private Vector3D position;

	/**
	 * The id of an object.
	 */
	private final long id;

	/**
	 * @return the id
	 */
	public final long getId() {
		return id;
	}

	/**
	 * Create a new gameobject.
	 * 
	 * @param id
	 *            The id of the object.
     * @param position
     *            The position.
	 */
	public GameObject(final long id, final Vector3D position) {
		this.id = id;
        this.setPosition(position);
	}


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

    public void setRenderAttachment(RenderAttachment renderAttachment) {
        this.renderAttachment = renderAttachment;
    }

    public RenderAttachment getRenderAttachment() {
        return renderAttachment;
    }

    public void setLoopableAttachment(LoopableAttachment loopableAttachment) {
        this.loopableAttachment = loopableAttachment;
    }

    public LoopableAttachment getLoopableAttachment() {
        return loopableAttachment;
    }

    public void setPhysicsAttachment(PhysicsAttachment physicsAttachment) {
        this.physicsAttachment = physicsAttachment;
    }

    public PhysicsAttachment getPhysicsAttachment() {
        return physicsAttachment;
    }
}
