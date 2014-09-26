package net.pme;

import net.pme.core.Player;
import net.pme.core.math.Vector3d;
import net.pme.jobcenter.LoopableAttachment;

import org.lwjgl.input.Keyboard;

/**
 * A basic player implementation
 *
 * @author Michael Fürst
 * @version 1.0
 */
class DefaultPlayer extends Player {
    private double xAxis = 0;
    private double yAxis = 0;
    private double zAxis = 0;
    private double rotate = 0;
    private Game game;

    /**
     * A default player.
     */
    public DefaultPlayer(Game game) {
        super(new Vector3d(0,0,0), new Vector3d(0,0,1), new Vector3d(0,1,0), null);

        this.game = game;

        setLoopableAttachment(new LoopableAttachment() {
            @Override
            public void update(double elapsedTime) {
                move(new Vector3d(xAxis * elapsedTime * 3.0, yAxis * elapsedTime * 3.0,
                        zAxis * elapsedTime * 6.0));
                rotateAroundFrontAxis(elapsedTime * rotate * 50.0);
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see net.pme.core.Player#keyboardInputHandler(int, boolean)
     */
    @Override
    public void keyboardInputHandler(int eventKey, boolean eventKeyState) {
        if (eventKey == Keyboard.KEY_ESCAPE) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("This should never happen.");
                    }
                    System.exit(0);
                }
            }.start();
            game.stopGame();
        }
        if (eventKey == Keyboard.KEY_E) {
            if (eventKeyState) {
                rotate++;
            } else {
                rotate--;
            }
        }
        if (eventKey == Keyboard.KEY_Q) {
            if (eventKeyState) {
                rotate--;
            } else {
                rotate++;
            }
        }
        if (eventKey == Keyboard.KEY_W) {
            if (eventKeyState) {
                zAxis--;
            } else {
                zAxis++;
            }
        }
        if (eventKey == Keyboard.KEY_S) {
            if (eventKeyState) {
                zAxis++;
            } else {
                zAxis--;
            }
        }
        if (eventKey == Keyboard.KEY_A) {
            if (eventKeyState) {
                xAxis--;
            } else {
                xAxis++;
            }
        }
        if (eventKey == Keyboard.KEY_D) {
            if (eventKeyState) {
                xAxis++;
            } else {
                xAxis--;
            }
        }
        if (eventKey == Keyboard.KEY_R) {
            if (eventKeyState) {
                yAxis++;
            } else {
                yAxis--;
            }
        }
        if (eventKey == Keyboard.KEY_F) {
            if (eventKeyState) {
                yAxis--;
            } else {
                yAxis++;
            }
        }
        xAxis = Math.signum(xAxis);
        yAxis = Math.signum(yAxis);
        zAxis = Math.signum(zAxis);
        rotate = Math.signum(rotate);
    }

    /*
     * (non-Javadoc)
     *
     * @see net.pme.core.Player#mouseInputHandler(int, boolean)
     */
    @Override
    public void mouseInputHandler(int eventButton, boolean eventButtonState) {
    }

    /*
     * (non-Javadoc)
     *
     * @see net.pme.core.Player#mouseMoveHandler(int, int)
     */
    @Override
    public void mouseMoveHandler(int dx, int dy) {
        rotateAroundPitchAxis(-dy * 0.2);
        rotateAroundUpAxis(dx * 0.2);
    }

}
