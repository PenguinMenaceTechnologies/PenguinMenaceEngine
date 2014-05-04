package net.pme.core;

import net.pme.core.math.MathUtils;
import net.pme.core.math.Matrix;
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
     * The direction the object is facing at.
     */
    private Vector3D front;
    /**
     * The direction where the top of the object is.
     */
    private Vector3D up;

    /**
     * Create a new gameobject.
     *
     * @param id       The id of the object.
     * @param position The position.
     * @param front    The front vector.
     * @param up       The up vector.
     */
    public GameObject(final long id, final Vector3D position, final Vector3D front, final Vector3D up) {
        this.id = id;
        this.setPosition(position);
        this.setFront(front);
        this.setUp(up);
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

    /**
     * @return the front
     */
    public final Vector3D getFront() {
        return front;
    }

    /**
     * @param front the front to set
     */
    public final void setFront(final Vector3D front) {
        this.front = front.normalize();
        if (getRenderAttachment() != null) {
            getRenderAttachment().setNeedsUpdate(true);
        }
    }

    /**
     * @return the up
     */
    public final Vector3D getUp() {
        return up;
    }

    /**
     * @param up the up to set
     */
    public final void setUp(final Vector3D up) {
        this.up = up.normalize();
        if (getRenderAttachment() != null) {
            getRenderAttachment().setNeedsUpdate(true);
        }
    }

    /**
     * Rotate the object around it's front axis.
     *
     * @param degree The amount it should be rotated in degree.
     */
    public final void rotateAroundFrontAxis(final double degree) {
        double angle = MathUtils.DEG2RAD * degree;
        up = Vector3D.transformCoords(up, Matrix.rotationAxis(front, angle));
        up = Vector3D.normalize(up);
        if (getRenderAttachment() != null) {
            getRenderAttachment().setNeedsUpdate(true);
        }
    }

    /**
     * Rotate the object around it's up axis.
     *
     * @param degree The amount it should be rotated in degree.
     */
    public final void rotateAroundUpAxis(final double degree) {
        double angle = MathUtils.DEG2RAD * degree;
        front = Vector3D.transformCoords(front, Matrix.rotationAxis(up, angle));
        front = Vector3D.normalize(front);
        if (getRenderAttachment() != null) {
            getRenderAttachment().setNeedsUpdate(true);
        }
    }

    /**
     * Rotate the object around it's pitch axis.
     * <p/>
     * The pitch axis is orthogonal to the up and front axis.
     *
     * @param degree The amount it should be rotated in degree.
     */
    public final void rotateAroundPitchAxis(final double degree) {
        Vector3D pitch = Vector3D.crossProduct(front, up);
        double angle = MathUtils.DEG2RAD * degree;
        up = Vector3D.transformCoords(up, Matrix.rotationAxis(pitch, angle));
        up = Vector3D.normalize(up);
        front = Vector3D.transformCoords(front,
                Matrix.rotationAxis(pitch, angle));
        front = Vector3D.normalize(front);
        if (getRenderAttachment() != null) {
            getRenderAttachment().setNeedsUpdate(true);
        }
    }

    /**
     * Move relative to your own coordinate system.
     *
     * @param relative The vector defining the motion.
     */
    public final void move(final Vector3D relative) {
        Vector3D pitch = Vector3D.crossProduct(front, up);

        Matrix m = Matrix.axes(pitch, up, front);

        Vector3D absolute = Vector3D.transformCoords(relative, m);

        setPosition(Vector3D.add(getPosition(), absolute));
        if (getRenderAttachment() != null) {
            getRenderAttachment().setNeedsUpdate(true);
        }
    }
}
