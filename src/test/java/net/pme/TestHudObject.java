/**
 * 
 */
package net.pme;

import java.awt.image.BufferedImage;

import net.pme.objects.HudObject;

/**
 * @author Michael
 *
 */
public class TestHudObject extends HudObject {
	private BufferedImage bi;
	
	public TestHudObject (long id, int x, int y, BufferedImage bi) {
		super(id, x, y);
		this.bi = bi;
	}

	/* (non-Javadoc)
	 * @see net.pme.objects.HudObject#move(double)
	 */
	@Override
	public void move(double elapsedTime) {
		
	}

	/* (non-Javadoc)
	 * @see net.pme.objects.HudObject#offscreenRendering()
	 */
	@Override
	public BufferedImage offscreenRendering() {
		return bi;
	}

}
