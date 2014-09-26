package net.pme.core;

import net.pme.core.math.MathUtils;
import net.pme.core.math.Matrix;
import net.pme.core.math.Vector3d;
import net.pme.jobcenter.LoopableAttachment;
import net.pme.graphics.RenderAttachment;
import net.pme.jobcenter.MoveJob;
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
    private Vector3d position;
    /**
     * The direction the object is facing at.
     */
    private Vector3d front;
    /**
     * The direction where the top of the object is.
     */
    private Vector3d up;
    private MoveJob moveJob = new MoveJob(null, this);

    /**
     * Create a new gameobject.
     *
     * @param id       The id of the object.
     * @param position The position.
     * @param front    The front vector.
     * @param up       The up vector.
     */
    public GameObject(final long id, final Vector3d position, final Vector3d front, final Vector3d up) {
        this.id = id;
        this.setPosition(position.clone());
        this.setFront(front.clone().normalize());
        this.setUp(up.clone().normalize());
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
    public final Vector3d getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public final void setPosition(final Vector3d position) {
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
    public final Vector3d getFront() {
        return front;
    }

    /**
     * @param front the front to set
     */
    public final void setFront(final Vector3d front) {
        this.front = front.normalize().clone();
        if (getRenderAttachment() != null) {
            getRenderAttachment().setNeedsUpdate(true);
        }
    }

    /**
     * @return the up
     */
    public final Vector3d getUp() {
        return up;
    }

    /**
     * @param up the up to set
     */
    public final void setUp(final Vector3d up) {
        this.up = up.normalize().clone();
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
        up.transformCoords(Matrix.rotationAxis(front, angle));
        up.normalize();
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
        front.transformCoords(Matrix.rotationAxis(up, angle));
        front.normalize();
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
        Vector3d pitch = front.clone().crossProduct(up);
        double angle = MathUtils.DEG2RAD * degree;
        up.transformCoords( Matrix.rotationAxis(pitch, angle));
        up.normalize();
        front.transformCoords(Matrix.rotationAxis(pitch, angle));
        front.normalize();
        if (getRenderAttachment() != null) {
            getRenderAttachment().setNeedsUpdate(true);
        }
    }

    /**
     * Move relative to your own coordinate system.
     *
     * @param relative The vector defining the motion.
     */
    public final void move(final Vector3d relative) {
        Vector3d pitch = front.clone().crossProduct(up);

        Matrix m = Matrix.axes(pitch, up, front);

        Vector3d absolute = relative.clone().transformCoords(m);

        getPosition().add(absolute);
        if (getRenderAttachment() != null) {
            getRenderAttachment().setNeedsUpdate(true);
        }
    }

    public MoveJob getMoveJob() {
        return moveJob;
    }
}
