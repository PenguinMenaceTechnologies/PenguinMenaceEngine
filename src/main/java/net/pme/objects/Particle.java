package net.pme.objects;

import net.pme.Game;
import net.pme.math.Vector3D;

/**
 * A particle can decay.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public abstract class Particle extends RenderableObject {
	private final double initialDecay;
	private double decay;
	private Game game;

	/**
	 * Create a new particle.
	 * 
	 * @param id
	 *            The id.
	 * @param position
	 *            The position.
	 * @param front
	 *            The front of the particle.
	 * @param up
	 *            The up of the particle.
	 * @param decay
	 *            The decay of the particle in seconds.
	 * @param graphics
	 *            The graphics identifier.
	 * @param game
	 *            The game instance in which the particle should live.
	 */
	public Particle(long id, Vector3D position, Vector3D front, Vector3D up,
			double decay, int graphics, Game game) {
		super(id, position, front, up, graphics);
		this.initialDecay = decay;
		this.decay = decay;
		this.game = game;
		// This is safe since addRenderable only adds the particle if it has not
		// been added before.
		game.addRenderable(this);
	}

	@Override
	public final void move(double elapsedTime) {
		decay -= elapsedTime;

		if (decay < 0) {
			game.removeRenderable(this);
		}

		lifeTimeBasedMove(elapsedTime, (decay / initialDecay) * 100);
	}

	/**
	 * A move operation based on the remaining lifetime.
	 * 
	 * @param elapsedTime
	 *            The common frametime in s.
	 * @param remainingLifeTime
	 *            The remaining lifetime in percent.
	 */
	public abstract void lifeTimeBasedMove(double elapsedTime,
			double remainingLifeTime);
}
