package net.pme.jobcenter;

import java.util.ArrayList;

/**
 * Bundles together jobs. Especially useful when jobs are small.
 * Used to reduce scheduler overhead.
 *
 * @author <a href="mailto:mail@michaelfuerst.de>Michael Fürst</a>
 * @version 1.0
 * @since 2014-05-05
 */
public class JobBundle extends Job {

    /**
     * A list of all attached jobs.
     */
    private ArrayList<Job> jobs = new ArrayList<>();

    /**
     * Chreate a new job bundle.
     *
     * @param scheduler The scheduler to use.
     */
    public JobBundle(final Scheduler scheduler) {
        super(scheduler);
    }

    /**
     * Add a job to the bundle.
     * @param job The job to add.
     */
    synchronized public final void addJob(final Job job) {
        jobs.add(job);
    }

    /**
     * Remove a job from the bundle.
     * @param job The job to remove.
     */
    synchronized public final void removeJob(final Job job) {
        jobs.remove(job);
    }

    @Override
    synchronized public final void execute() {
        for (Job j: jobs) {
            j.run();
        }
    }
}
