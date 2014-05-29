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
        this.front = (Vector3d) front.normalize();
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
        this.up = (Vector3d) up.normalize();
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
        up = Vector3d.transformCoords(up, Matrix.rotationAxis(front, angle));
        up = (Vector3d) Vector3d.normalize(up);
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
        front = Vector3d.transformCoords(front, Matrix.rotationAxis(up, angle));
        front = (Vector3d) Vector3d.normalize(front);
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
        Vector3d pitch = (Vector3d) Vector3d.crossProduct(front, up);
        double angle = MathUtils.DEG2RAD * degree;
        up = Vector3d.transformCoords(up, Matrix.rotationAxis(pitch, angle));
        up = (Vector3d) Vector3d.normalize(up);
        front = Vector3d.transformCoords(front,
                Matrix.rotationAxis(pitch, angle));
        front = (Vector3d) Vector3d.normalize(front);
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
        Vector3d pitch = (Vector3d) Vector3d.crossProduct(front, up);

        Matrix m = Matrix.axes(pitch, up, front);

        Vector3d absolute = Vector3d.transformCoords(relative, m);

        setPosition((Vector3d) Vector3d.add(getPosition(), absolute));
        if (getRenderAttachment() != null) {
            getRenderAttachment().setNeedsUpdate(true);
        }
    }

    public MoveJob getMoveJob() {
        return moveJob;
    }
}
