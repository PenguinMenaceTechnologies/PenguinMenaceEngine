package net.pme.objects;

/**
 * All objects that are focusable.
 * 
 * Main purpose is GameEngine internal focus handling and enemy tracking.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public interface Focusable {
	/**
	 * Defines what happens if the object gets focused.
	 */
	public void onFocus();

	/**
	 * Defines what happens when the focus is lost.
	 */
	public void onFocusLost();
}
