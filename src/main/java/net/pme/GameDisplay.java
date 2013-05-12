package net.pme;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

/**
 * The Display.
 * 
 * @author Michael Fürst
 */
public class GameDisplay {
	private int displayWidth;
	private int displayHeight;
	private int fpsLimit = 120;
	private static GameDisplay instance = null;

	/**
	 * Creates a display of the given size.
	 * 
	 * @param title
	 *            The Window title
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 * @param fullscreen
	 *            Request fullscreen.
	 */
	private void init(String title, int width, int height, boolean fullscreen) {
		if (fullscreen) {
			try {
				Display.setFullscreen(fullscreen);
			} catch (LWJGLException ex) {
			}
			displayWidth = Display.getDesktopDisplayMode().getWidth();
			displayHeight = Display.getDesktopDisplayMode().getHeight();
		} else {
			try {
				displayWidth = width;
				displayHeight = height;
				Display.setDisplayMode(new DisplayMode(displayWidth,
						displayHeight));
			} catch (LWJGLException ex) {
			}
		}
		Display.setTitle(title);
		Display.setVSyncEnabled(false);
		try {
			Display.create();
		} catch (LWJGLException ex) {
		}
		try {
			Keyboard.create();
			Mouse.create();
			Mouse.setGrabbed(false);
		} catch (LWJGLException ex) {
			Logger.getLogger(GameDisplay.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		initGL();
		resizeGL();
	}

	/**
	 * Set VSyncEnabled true. (Cannot be unset.)
	 */
	public void setVSync() {
		Display.setVSyncEnabled(true);
	}

	/**
	 * Limit the fps. (Only works when vsync disabled.)
	 * 
	 * @param fps
	 */
	public void setFPS(int fps) {
		fpsLimit = fps;
	}

	/**
	 * Destroy the window.
	 */
	public void deinit() {
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}

	/**
	 * Return if the game wants to quit.
	 * 
	 * @return if the user wants to close the window.
	 */
	boolean isCloseRequested() {
		return Display.isCloseRequested();
	}

	/**
	 * Updates the frame.
	 */
	void update() {
		Display.update();
		Display.sync(fpsLimit);
	}

	/**
	 * Initialize GL for the window.
	 */
	private void initGL() {
		// run through some based OpenGL capability settings. Textures
		// enabled, back face culling enabled, depth texting is on,
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_TEXTURE_2D);

		// define the properties for the perspective of the scene
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(45.0f, ((float) displayWidth) / ((float) displayHeight),
				0.1f, 2000.0f);
		glMatrixMode(GL_MODELVIEW);
		glShadeModel(GL_SMOOTH);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	/**
	 * Resize event of GL.
	 */
	private void resizeGL() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(45.0f, ((float) displayWidth) / ((float) displayHeight),
				0.1f, 2000.0f);
		glMatrixMode(GL_MODELVIEW);
	}

	/**
	 * Enter orthographical drawing mode. Required for 2d hud drawing. Taken
	 * from org.netdawn.asteroids
	 */
	void enterOrtho() {
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glMatrixMode(GL_MODELVIEW);
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadIdentity();
		glOrtho(0, displayWidth, displayHeight, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	/**
	 * Leave orthographical drawing mode. Required for 2d hud drawing. Taken
	 * from org.netdawn.asteroids
	 */
	void leaveOrtho() {
		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
	}

	/**
	 * Creates a display of the given size.
	 * 
	 * @param title
	 *            The Window title
	 * @param width
	 *            The width.
	 * @param height
	 *            The height.
	 * @param fullscreen
	 *            Request fullscreen.
	 * @return An instance of the display.
	 */
	public static GameDisplay create(String title, int width, int height,
			boolean fullscreen) {
		instance = new GameDisplay();
		instance.init(title, width, height, fullscreen);
		return instance;
	}

	/**
	 * Get the instance of the display.
	 * 
	 * @return The instance of the display.
	 */
	public static GameDisplay getDisplay() {
		return instance;
	}

	/**
	 * The current width of the display.
	 * 
	 * @return The current width of the display.
	 */
	public int getWidth() {
		return displayWidth;
	}

	/**
	 * The current width of the display.
	 * 
	 * @return The current width of the display.
	 */
	public int getHeight() {
		return displayHeight;
	}
}
