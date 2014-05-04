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
    /**
     * The id of an object.
     */
    private final long id;
    private RenderAttachment renderAttachment;
    private LoopableAttachment loopableAttachment;
    private PhysicsAttachment physicsAttachment;
    /**
     * The position of the object.
     */
    private Vector3D position;

    /**
     * Create a new gameobject.
     *
     * @param id       The id of the object.
     * @param position The position.
     */
    public GameObject(final long id, final Vector3D position) {
        this.id = id;
        this.setPosition(position);
    }

    /**
     * @return the id
     */
    public final long getId() {
        return id;
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

    public RenderAttachment getRenderAttachment() {
        return renderAttachment;
    }

    public void setRenderAttachment(RenderAttachment renderAttachment) {
        this.renderAttachment = renderAttachment;
    }

    public LoopableAttachment getLoopableAttachment() {
        return loopableAttachment;
    }

    public void setLoopableAttachment(LoopableAttachment loopableAttachment) {
        this.loopableAttachment = loopableAttachment;
    }

    public PhysicsAttachment getPhysicsAttachment() {
        return physicsAttachment;
    }

    public void setPhysicsAttachment(PhysicsAttachment physicsAttachment) {
        this.physicsAttachment = physicsAttachment;
    }
}
