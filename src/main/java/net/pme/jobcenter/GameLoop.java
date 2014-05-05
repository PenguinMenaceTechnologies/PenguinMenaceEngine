package net.pme.jobcenter;

import net.pme.Game;
import net.pme.core.GameObject;
import net.pme.core.Player;
import net.pme.graphics.Graphics;
import net.pme.graphics.HudObject;
import net.pme.graphics.Particle;

import java.util.LinkedList;
import java.util.List;

/**
 * This makes the gameengine pump.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public final class GameLoop {
    private static final double NANO_TO_SEC = 1E-9;
    private boolean running = true;

    /**
     * Do nothing, only visible inside engine.
     */
    public GameLoop() {
    }

    /**
     * This is the heart-beat of the engine.
     * <p/>
     * It ticks once every 16.6 ms.
     *
     * @param player     The player.
     * @param objects    All objects.
     * @param hudObjects All hud objects.
     * @param game
     */
    public void run(final List<GameObject> objects,
                    final List<HudObject> hudObjects, final Player player, Game game) {
        //List<Particle> localParticleObjects = new ArrayList<Particle>();

        double elapsedTime = 0;

        final Scheduler scheduler = new Scheduler();

        Graphics display = game.getDisplay();

        while (running && (display == null || !display.isCloseRequested())) {
            long timer = System.nanoTime();

            // Flush particle buffer to local buffer.
            //addAll(localParticleObjects, particleObjects);

            // Wait for all jobs to wait in this frame.
            scheduler.await();

            final double elapsedTimeFixed = elapsedTime;
            scheduler.addJob(new Job(scheduler) {
                @Override
                public void execute() {
                    // Move all objects
                    for (GameObject o : objects) {
                        o.getMoveJob().setup(elapsedTimeFixed, scheduler);
                        scheduler.addJobForNextTick(o.getMoveJob());
                    }
                    scheduler.addJobForNextTick(new Job(scheduler) {
                        @Override
                        public void execute() {
                            for (HudObject h: hudObjects) {
                                h.move(elapsedTimeFixed);
                            }
                        }
                    });

                }
            });

            display = game.getDisplay();

            if (player != null) {
                if (display != null) {
                    display.handleInputs(player);
                }
            }

            if (display != null) {
                display.render(objects, hudObjects, player, game);
            } else {
                // Sleep a bit when we are not rendering. To yield for other threads.
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            scheduler.await();
            scheduler.tick();

            timer = System.nanoTime() - timer;
            elapsedTime = timer * NANO_TO_SEC;
        }
    }

    /**
     * Add all objects from buffer to active list.
     *
     * @param localParticleObjects Local list.
     * @param particleObjects      Buffer to copy into local list.
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
    public void terminate() {
        running = false;
    }
}
