package net.pme.graphics;

import net.pme.Game;
import net.pme.core.math.Vector3d;

/**
 * A particle can decay.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public abstract class Particle {
    private static final double PERCENT = 100.0;
    private final double initialDecay;
    private double decay;
    private Game game;

    /**
     * Create a new particle.
     *
     * @param id       The id.
     * @param position The position.
     * @param front    The front of the particle.
     * @param up       The up of the particle.
     * @param decay    The decay of the particle in seconds.
     * @param graphics The graphics identifier.
     * @param game     The game instance in which the particle should live.
     */
    public Particle(final long id, final Vector3d position, final Vector3d front, final Vector3d up,
                    final double decay, final int graphics, final Game game) {
        //super(id, position, front, up, graphics);
        this.initialDecay = decay;
        this.decay = decay;
        this.game = game;
        // This is safe since addRenderable only adds the particle if it has not
        // been added before.
        //game.addGameObject(this);
    }

    public final void move(final double elapsedTime) {
        decay -= elapsedTime;

        if (decay < 0) {
            //game.removeGameObject(this);
        }

        lifeTimeBasedMove(elapsedTime, (decay / initialDecay) * PERCENT);
    }

    /**
     * A update operation based on the remaining lifetime.
     *
     * @param elapsedTime       The common frametime in s.
     * @param remainingLifeTime The remaining lifetime in percent.
     */
    public abstract void lifeTimeBasedMove(double elapsedTime,
                                           double remainingLifeTime);
}
