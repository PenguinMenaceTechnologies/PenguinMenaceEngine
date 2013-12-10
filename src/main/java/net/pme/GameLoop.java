package net.pme;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.pme.objects.HudObject;
import net.pme.objects.MovableObject;
import net.pme.objects.Particle;
import net.pme.objects.Player;
import net.pme.objects.RenderableObject;

/**
 * This makes the gameengine pump.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
final class GameLoop {
	private static final double NANO_TO_SEC = 1E-9;
	private boolean running = true;

	/**
	 * Do nothing, only visible inside engine.
	 */
	GameLoop() {
	}

	/**
	 * This is the heart-beat of the engine.
	 * 
	 * It ticks once every 16.6 ms.
	 * 
	 * @param player
	 *            The player.
	 * @param renderableObjects
	 *            All renderable Objects.
	 * @param movableObjects
	 *            All movable Objects.
	 * @param particleObjects
	 *            All particles. (Note that the particle list is internally not
	 *            thread-safe.)
	 * @param hudObjects
	 * 			  All hud objects.
	 */
	void run(final List<MovableObject> movableObjects,
			final List<RenderableObject> renderableObjects,
			final LinkedList<Particle> particleObjects, final List<HudObject> hudObjects,
			final Player player) {
		List<Particle> localParticleObjects = new ArrayList<Particle>();

		double elapsedTime = 0;
		GameDisplay display = GameDisplay.getDisplay();
		while (running && !display.isCloseRequested()) {
			long timer = System.nanoTime();

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();

			// Flush particle buffer to local buffer.
			addAll(localParticleObjects, particleObjects);

			// Move all objects
			for (MovableObject o : movableObjects) {
				o.move(elapsedTime);
			}
			for (RenderableObject o : renderableObjects) {
				o.move(elapsedTime);
			}
			for (Particle o : particleObjects) {
				o.move(elapsedTime);
			}
			for (HudObject o : hudObjects) {
				o.move(elapsedTime);
			}

			if (player != null) {
				if (Keyboard.next()) {
					player.keyboardInputHandler(Keyboard.getEventKey(),
							Keyboard.getEventKeyState());
				}
				if (Mouse.next()) {
					player.mouseInputHandler(Mouse.getEventButton(),
							Mouse.getEventButtonState());
				}
				if (Mouse.isGrabbed()) {
					player.mouseMoveHandler(Mouse.getDX(), Mouse.getDY());
				}

				player.move(elapsedTime);

				player.applyCamera();
			}

			// Render all objects
			for (RenderableObject o : renderableObjects) {
				o.render();
			}
			for (Particle o : particleObjects) {
				o.render();
			}

			// Render Hud ontop of all
			GL11.glLoadIdentity();
			display.enterOrtho();
			for (HudObject o : hudObjects) {
				o.render();
			}
			display.leaveOrtho();

			GameDisplay.getDisplay().update();

			timer = System.nanoTime() - timer;
			elapsedTime = timer * NANO_TO_SEC;
		}
	}

	/**
	 * Add all objects from buffer to active list.
	 * 
	 * @param localParticleObjects
	 *            Local list.
	 * @param particleObjects
	 *            Buffer to copy into local list.
	 */
	private void addAll(final List<Particle> localParticleObjects,
			final LinkedList<Particle> particleObjects) {
		while (!particleObjects.isEmpty()) {
			localParticleObjects.add(particleObjects.removeFirst());
		}
	}

	/**
	 * Stop the game loop.
	 */
	void terminate() {
		running = false;
	}
}
