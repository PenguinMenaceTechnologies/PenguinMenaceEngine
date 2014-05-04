package net.pme.graphics;

/**
 * An interface for offscreen rendering.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public interface OffscreenRenderer {

    /**
     * Get the width of the offscreen rendering.
     *
     * @return The width of the renderer.
     */
    public int getWidth();

    /**
     * Get the height of the offscreen rendering.
     *
     * @return The height of the renderer.
     */
    public int getHeight();

    /**
     * Setup rendering. (FBO is already setup)
     */
    public void preRender();

    /**
     * Called after rendering. Used to do something with your buffer.
     *
     * @param renderbuffer The renderbuffer. (Do not destroy this)
     * @param depthbuffer  The depthbuffer. (Do not destroy this)
     */
    public void postRender(int renderbuffer, int depthbuffer);
}
