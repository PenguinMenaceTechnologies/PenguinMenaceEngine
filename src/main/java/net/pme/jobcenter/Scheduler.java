package net.pme.jobcenter;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The scheduler manages the calculations of the jobs.
 *
 * @author <a href="mailto:mail@michaelfuerst.de>Michael Fürst</a>
 * @version 1.0
 * @since ${date}
 */
public class Scheduler {
    private ThreadPoolExecutor executor;
    private LinkedList<Job> jobQueue;
    private LinkedList<Job> jobQueueNextTick;

    /**
     * Create a scheduler.
     */
    public Scheduler() {
        jobQueue = new LinkedList<>();
        jobQueueNextTick = new LinkedList<>();
        int cores = Runtime.getRuntime().availableProcessors();
        executor = new ThreadPoolExecutor(cores, cores*4, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    /**
     * Tick the scheduler to the next tick.
     */
    synchronized void tick() {
        while (!jobQueueNextTick.isEmpty()) {
            addJob(jobQueueNextTick.poll());
        }
    }

    /**
     * Await the end of a tick.
     * Especially useful for calculations like rendering.
     */
    synchronized public void await() {
        try {
            while (!jobQueue.isEmpty()) {
                this.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a job for the next tick.
     * @param job The job to add.
     */
    synchronized public final void addJobForNextTick(final Job job) {
        jobQueueNextTick.add(job);
    }

    /**
     * Add a job to the scheduler.
     * @param job The job to add.
     */
    synchronized final void addJob(final Job job) {
        jobQueue.add(job);
        executor.execute(job);
    }

    /**
     * Delete a job from the scheduler.
     * @param job The job to delete.
     */
    synchronized final void deleteJob(final Job job) {
        jobQueue.remove(job);
        notifyAll();
    }

    /**
     * Test if a job is pending or being processed.
     * @param job The job to test.
     * @return Whether it is pending (or being processed) or not.
     */
    synchronized final boolean isPending(final Job job) {
        return jobQueue.contains(job);
    }
}
