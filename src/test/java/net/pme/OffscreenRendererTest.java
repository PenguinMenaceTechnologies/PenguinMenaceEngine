package net.pme;

import net.pme.offscreen.OffscreenRenderer;

/**
 * Created by michael on 10.03.14.
 */
public class OffscreenRendererTest implements OffscreenRenderer {
    /**
     * Get the width of the offscreen rendering.
     *
     * @return The width of the renderer.
     */
    @Override
    public int getWidth() {
        return 10;
    }

    /**
     * Get the height of the offscreen rendering.
     *
     * @return The height of the renderer.
     */
    @Override
    public int getHeight() {
        return 10;
    }

    /**
     * Setup rendering. (FBO is already setup)
     */
    @Override
    public void preRender() {

    }

    /**
     * Called after rendering. Used to do something with your buffer.
     *
     * @param renderbuffer The renderbuffer. (Do not destroy this)
     * @param depthbuffer  The depthbuffer. (Do not destroy this)
     */
    @Override
    public void postRender(int renderbuffer, int depthbuffer) {

    }
}
