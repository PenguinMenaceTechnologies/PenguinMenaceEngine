package net.pme.jobcenter;

import net.pme.core.GameObject;

/**
 * @author <a href="mailto:mail@michaelfuerst.de>Michael Fürst</a>
 * @version 1.0
 * @since ${date}
 */
public final class MoveJob extends Job {
    private final GameObject object;
    private double elapsedTime;

    /**
     * Create a job to move a game object.
     * @param scheduler The scheduler to use.
     */
    public MoveJob(final Scheduler scheduler, final GameObject object) {
        super(scheduler);
        this.object = object;
    }

    /**
     * Setup the job for this frame. (Reusing the same job saves a lot of time.
     * @param elapsedTime The time elapsed since the last frame.
     * @param scheduler The scheduler to use this frame.
     */
    public final void setup(final double elapsedTime, final Scheduler scheduler) {
        this.elapsedTime = elapsedTime;
        setScheduler(scheduler);
    }

    @Override
    public final void execute() {
        if (object.getLoopableAttachment() != null) {
            object.getLoopableAttachment().update(elapsedTime);
        }
    }
}
