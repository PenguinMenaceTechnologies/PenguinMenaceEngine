package net.pme;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * The Display.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public final class GameDisplay {
	private static final float FOVY = 45.0f;
	private static final float Z_NEAR = 0.001f;
	private static final float Z_FAR = 2000.0f;
	private static final int INITIAL_FPS_LIMIT = 120;
	private int displayWidth;
	private int displayHeight;
	private int fpsLimit = INITIAL_FPS_LIMIT;
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
	private void init(final String title, final int width, final int height,
			final boolean fullscreen) {
		if (fullscreen) {
			try {
				Display.setFullscreen(fullscreen);
			} catch (LWJGLException ex) {
				ex.printStackTrace();
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
				ex.printStackTrace();
			}
		}
		Display.setTitle(title);
		Display.setVSyncEnabled(false);
		try {
			Display.create();
		} catch (LWJGLException ex) {
			ex.printStackTrace();
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
	 *            The framerate to set.
	 */
	public void setFPS(final int fps) {
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
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		// define the properties for the perspective of the scene
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(FOVY, ((float) displayWidth)
				/ ((float) displayHeight), Z_NEAR, Z_FAR);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	/**
	 * Resize event of GL.
	 */
	private void resizeGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(FOVY, ((float) displayWidth)
				/ ((float) displayHeight), Z_NEAR, Z_FAR);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	/**
	 * Enter orthographical drawing mode. Required for 2d hud drawing. Taken
	 * from org.netdawn.asteroids
	 */
	void enterOrtho() {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glOrtho(0, displayWidth, displayHeight, 0, 0, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	/**
	 * Leave orthographical drawing mode. Required for 2d hud drawing. Taken
	 * from org.netdawn.asteroids
	 */
	void leaveOrtho() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
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
	public static GameDisplay create(final String title, final int width, final int height,
			final boolean fullscreen) {
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
