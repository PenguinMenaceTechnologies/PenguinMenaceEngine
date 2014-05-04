package net.pme.core;

import net.pme.core.math.Matrix;
import net.pme.core.math.Vector3D;
import net.pme.graphics.RenderAttachment;
import net.pme.model.Model;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;

/**
 * A player represents the person sitting in front of the computer playing the
 * game.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public abstract class Player extends GameObject {
    /**
     * The matrix buffer to avoid recalculations.
     */
    private DoubleBuffer matrixBuffer;

    /**
     * Create a new player.
     *
     * @param position The position.
     * @param front    The direction the player is facing to.
     * @param up       The direction where the players top is.
     * @param model    The graphics identifier.
     */
    public Player(final Vector3D position, final Vector3D front, final Vector3D up, final Model model) {
        super(1, position, front, up);
        setRenderAttachment(new RenderAttachment(this, model));
    }

    /**
     * Apply the camera on the scene, so that the scene can be rendered after a
     * call of this function.
     */
    public final void applyCamera() {
        if (getRenderAttachment() != null) {
            Matrix m = Matrix.camera(getPosition(),
                    Vector3D.crossProduct(getFront(), getUp()), getUp(), getFront());

            matrixBuffer = m.getValues(matrixBuffer);
            matrixBuffer.position(0);
            GL11.glMultMatrix(matrixBuffer);
        }
    }

    /**
     * Handles keyboard input.
     * <p/>
     * <i>The GameInput class is a huge help, when implementing this method.</i>
     *
     * @param eventKey      Determines the key that is pressed.
     * @param eventKeyState If the key is down.
     */
    public abstract void keyboardInputHandler(int eventKey,
                                              boolean eventKeyState);

    /**
     * Handles mouse input.
     * <p/>
     * <i>The GameInput class is a huge help, when implementing this method.</i>
     *
     * @param eventButton      Determines the mouse button that is pressed.
     * @param eventButtonState If the button is down.
     */
    public abstract void mouseInputHandler(int eventButton,
                                           boolean eventButtonState);

    /**
     * Handles mouse motion.
     *
     * @param dx The dx of the mouse.
     * @param dy The dy of the mouse.
     */
    public abstract void mouseMoveHandler(int dx, int dy);
}
