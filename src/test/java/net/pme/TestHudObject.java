/**
 *
 */
package net.pme;

import net.pme.graphics.HudObject;

import java.awt.image.BufferedImage;

/**
 * @author Michael
 */
public class TestHudObject extends HudObject {
    private BufferedImage bi;

    public TestHudObject(int x, int y, BufferedImage bi) {
        super(x, y);
        this.bi = bi;
    }

    /*
     * (non-Javadoc)
     *
     * @see net.pme.graphics.HudObject#update(double)
     */
    @Override
    public void move(double elapsedTime) {

    }

    /*
     * (non-Javadoc)
     *
     * @see net.pme.graphics.HudObject#offscreenRendering()
     */
    @Override
    public BufferedImage offscreenRendering() {
        return bi;
    }

}
