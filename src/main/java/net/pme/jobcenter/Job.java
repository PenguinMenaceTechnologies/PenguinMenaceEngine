package net.pme.jobcenter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

/**
 * A job to be executed.
 *
 * @author <a href="mailto:mail@michaelfuerst.de>Michael Fürst</a>
 * @version 1.0
 * @since 2014-05-05
 */
public abstract class Job implements Runnable {
    private static final List<Job> EMPTY_DEPENDENCY = new LinkedList<>();
    private static final int DEFAULT_CANCEL_TIMEOUT = 40;
    private int cancelTimeout = DEFAULT_CANCEL_TIMEOUT;
    private Scheduler scheduler;

    /**
     * Chreate a new job.
     * @param scheduler The scheduler to use.
     */
    public Job(final Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * Set the scheduler.
     * @param scheduler The scheduler that is used.
     */
    public final void setScheduler(final Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * Test if the job has pending dependencies in the queues.
     * @param queue The queue.
     * @return Whether the jobs has dependencies in the queue or not.
     */
    synchronized private boolean hasDependencies(final SynchronousQueue<Job> queue) {
        List<Job> jobs = getDependencies();
        for (Job j: jobs) {
            if (queue.contains(j)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the dependencies of this job.
     * Jobs this job depends on. When a job is scheduled it tests if any of it's dependencies is in the queue and if so it is put back to the end of the queue.
     *
     * After a job is put back to the queue for 20 times it is canceled.
     *
     * For best performance avoid dependencies completely.
     *
     * Dependencies that are not yet in the queue will be ignored!
     *
     * You should prefer triggering new Jobs rather than depending on others.
     *
     * @return A list of all jobs this job depends on.
     */
    public List<Job> getDependencies() {
        return EMPTY_DEPENDENCY;
    }

    @Override
    public final void run() {
        // Kill threads that have bugged dependencies.
        if (cancelTimeout-- < 0) {
            scheduler.deleteJob(this);
            return;
        }

        // Check the dependencies.
        for (Job d: getDependencies()) {
            if (scheduler.isPending(d)) {
                scheduler.addJob(this);
                return;
            }
        }

        // Execute the job.
        this.execute();

        cancelTimeout = DEFAULT_CANCEL_TIMEOUT;

        // Remove it from the pending list (mark it as processed)
        scheduler.deleteJob(this);
    }

    /**
     * Execute the job.
     */
    public abstract void execute();
}
