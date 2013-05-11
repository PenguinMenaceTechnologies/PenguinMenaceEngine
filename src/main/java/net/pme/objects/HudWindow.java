package net.pme.objects;

/**
 * Models a window in the HUD.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public abstract class HudWindow extends HudObject implements Focusable {
	/**
	 * Create a new window in the hud.
	 * 
	 * @param id
	 *            The id.
	 * @param x
	 *            The x position.
	 * @param y
	 *            The y position.
	 */
	public HudWindow(long id, int x, int y) {
		super(id, x, y);
	}

	/**
	 * Called when the window is dragged.
	 * 
	 * @param dx
	 *            The x distance to the last position.
	 * @param dy
	 *            The y distance to the last position.
	 */
	public abstract void onDrag(int dx, int dy);

	/**
	 * Called when there is a click on the window.
	 * 
	 * @param x
	 *            The x click offset relative to the left top corner of this
	 *            window.
	 * @param y
	 *            The y click offset relative to the left top corner of this
	 *            window.
	 */
	public abstract void onClick(int x, int y);
}
