package net.pme.graphics;

import net.pme.Game;
import net.pme.core.GameObject;
import net.pme.core.Player;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.lwjgl.opengl.EXTFramebufferObject.*;

/**
 * The Display.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public final class Graphics {
    private static final float FOVY = 45.0f;
    private static final float Z_NEAR = 0.001f;
    private static final float Z_FAR = 2000.0f;
    private static final int INITIAL_FPS_LIMIT = 120;
    private int fpsLimit = INITIAL_FPS_LIMIT;
    private static Graphics instance = null;
    private int displayWidth;
    private int displayHeight;
    private boolean postprocessing = true;

    private ArrayList<OffscreenRendererWrapper> offscreenRenderers = new ArrayList<OffscreenRendererWrapper>();
    private int fbo = 0;
    private int texture = 0;
    private int depth = 0;
    private int fbo2 = 0;
    private int texture2 = 0;
    private int depth2 = 0;
    private Shader defaultPostprocessing = null;
    private Shader defaultFinal = null;
    private Game game;
    private Shader postprocessingShader = null;
    private Shader finalShader = null;

    /**
     * Private constructor for singleton.
     */
    private Graphics(Game game) {
        this.game = game;
    }

    /**
     * Creates a display of the given size.
     *
     * @param title      The Window title
     * @param width      The width.
     * @param height     The height.
     * @param fullscreen Request fullscreen.
     * @return An instance of the display.
     */
    public static Graphics create(final Game game, final String title, final int width, final int height,
                                  final boolean fullscreen) {
        if (instance != null) {
            throw new IllegalStateException("You cannot create two displays.");
        }
        instance = new Graphics(game);
        instance.init(title, width, height, fullscreen);
        return instance;
    }

    private synchronized void initializeRendering() {
        if (!GLContext.getCapabilities().GL_EXT_framebuffer_object) {
            postprocessing = false;
            System.err.println("FBO not supported. Postprocessing disabled.");
        }

        if (postprocessing) {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            fbo = glGenFramebuffersEXT();
            texture = GL11.glGenTextures();
            depth = GL11.glGenTextures();

            fbo2 = glGenFramebuffersEXT();
            texture2 = GL11.glGenTextures();
            depth2 = GL11.glGenTextures();

            initFBO(game, fbo, texture, depth);
            initFBO(game, fbo2, texture2, depth2);
        }

        GL11.glViewport(0, 0, getWidth(), getHeight());

        defaultPostprocessing = new PostprocessingShader();
        defaultFinal = new PostprocessingShader();
    }

    private synchronized void deinitializeRendering() {
        defaultFinal.delete();
        defaultPostprocessing.delete();

        defaultFinal = null;
        defaultPostprocessing = null;

        GL11.glDeleteTextures(texture);
        GL11.glDeleteTextures(texture2);

        GL11.glDeleteTextures(depth);
        GL11.glDeleteTextures(depth2);

        glDeleteFramebuffersEXT(fbo);
        glDeleteFramebuffersEXT(fbo2);

        for (OffscreenRendererWrapper o : offscreenRenderers) {
            o.delete();
        }
    }

    /**
     * Add an offscreen renderer.
     *
     * @param renderer The renderer to add.
     * @param game     The to which to add the renderer.
     */
    public synchronized void addOffscreenRenderer(OffscreenRenderer renderer, Game game) {
        OffscreenRendererWrapper wrapper = new OffscreenRendererWrapper(renderer);
        if (offscreenRenderers.contains(wrapper)) return;
        offscreenRenderers.add(wrapper);
    }

    /**
     * Remove a offscreen renderer.
     *
     * @param renderer The renderer to remove.
     */
    public synchronized void removeOffscreenRenderer(OffscreenRenderer renderer) {
        for (OffscreenRendererWrapper o : offscreenRenderers) {
            if (o.equals(renderer)) {
                o.disable();
                return;
            }
        }
    }

    /**
     * Get an instance of the currently used postprocessing shader.
     *
     * @return The currently used shader.
     */
    public Shader getPostprocessingShader() {
        return postprocessingShader;
    }

    /**
     * Set a shader to be used for postprocessing.
     *
     * @param shader The shader to use.
     */
    public void setPostprocessingShader(Shader shader) {
        postprocessingShader = shader;
    }

    /**
     * Get an instance of the currently used final postprocessing shader.
     *
     * @return The currently used shader.
     */
    public Shader getFinalShader() {
        return finalShader;
    }

    /**
     * Set a shader to be used for final postprocessing (also applied on hud).
     *
     * @param shader The shader to use.
     */
    public void setFinalShader(Shader shader) {
        finalShader = shader;
    }

    /**
     * Creates a display of the given size.
     *
     * @param title      The Window title
     * @param width      The width.
     * @param height     The height.
     * @param fullscreen Request fullscreen.
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
        Display.setResizable(true);
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
            Logger.getLogger(Graphics.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        initGL();
        resizeGL();

        // Initialize the gameinput helper interface.
        GameInput.load(game.getSettings());

        initializeRendering();
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
     * @param fps The framerate to set.
     */
    public void setFPS(final int fps) {
        fpsLimit = fps;
    }

    /**
     * Destroy the window.
     */
    public void deinit() {
        deinitializeRendering();
        Display.destroy();
        Keyboard.destroy();
        Mouse.destroy();
    }

    /**
     * Return if the game wants to quit.
     *
     * @return if the user wants to close the window.
     */
    public boolean isCloseRequested() {
        return Display.isCloseRequested();
    }

    /**
     * Updates the frame.
     */
    void update(Game game) {
        Display.update();
        if (Display.wasResized()) {
            resizeGL();
            deinitializeRendering();
            initializeRendering();
        }
    }

    /**
     * Sync with the screen.
     */
    void sync() {
        Display.sync(fpsLimit);
    }

    /**
     * Initialize GL for the window.
     */
    private void initGL() {
        // define the properties for the perspective of the scene
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(FOVY, ((float) displayWidth)
                / ((float) displayHeight), Z_NEAR, Z_FAR);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glClearDepth(1.0f);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glShadeModel(GL11.GL_SMOOTH);

        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
    }

    /**
     * Resize event of GL.
     */
    private void resizeGL() {
        displayWidth = Display.getWidth();
        displayHeight = Display.getHeight();

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
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
    }

    /**
     * Leave orthographical drawing mode. Required for 2d hud drawing. Taken
     * from org.netdawn.asteroids
     */
    void leaveOrtho() {
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
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

    public void render(final List<GameObject> objects,
                       final List<HudObject> hudObjects, final Player player, Game game) {

        sync();

        if (postprocessing) {
            synchronized (this) {
                for (OffscreenRendererWrapper tmp : offscreenRenderers) {
                    if (tmp.preRender(game)) {
                        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT
                                | GL11.GL_DEPTH_BUFFER_BIT);

                        // Render all objects
                        for (GameObject o : objects) {
                            if (o.getRenderAttachment() != null) {
                                o.getRenderAttachment().render();
                            }
                        }

                        //GL11.glFlush();
                        tmp.postRender();
                    }
                }
            }

            // You cannot be sure that the renderers are implemented not changing this.
            initGL();
            resizeGL();

            GL11.glViewport(0, 0, getWidth(), getHeight());

            // unlink textures to avoid errors
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo);
        }

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT
                | GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glLoadIdentity();

        if (player != null) {
            player.applyCamera();
        }

        // Render all objects
        for (GameObject o : objects) {
            if (o.getRenderAttachment() != null) {
                o.getRenderAttachment().render();
            }
        }

        if (postprocessing) {
            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo2);

            GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT
                    | GL11.GL_DEPTH_BUFFER_BIT);

            GL11.glLoadIdentity();

            enterOrtho();

            if (postprocessingShader != null) {
                postprocessingShader.bind();
                postprocessingShader
                        .setupTexture("texture", texture, 0);
            } else {
                defaultPostprocessing.bind();
                defaultPostprocessing.setupTexture("texture", texture,
                        0);
            }

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2i(0, 0);
            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2i(getWidth(), 0);
            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2i(getWidth(), getHeight());
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2i(0, getHeight());
            GL11.glEnd();

            if (postprocessingShader != null) {
                postprocessingShader.unbind();
            } else {
                defaultPostprocessing.unbind();
            }

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

            leaveOrtho();

        }

        if (hudObjects.size() > 0) {
            enterOrtho();

            // Render Hud ontop of all
            for (HudObject o : hudObjects) {
                o.render();
            }
            leaveOrtho();
        }

        if (postprocessing) {
            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

            GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT
                    | GL11.GL_DEPTH_BUFFER_BIT);

            GL11.glLoadIdentity();

            enterOrtho();

            if (finalShader != null) {
                finalShader.bind();
                finalShader.setupTexture("texture", texture2, 0);
            } else {
                defaultFinal.bind();
                defaultFinal.setupTexture("texture", texture2, 0);
            }

            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2i(0, 0);
            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2i(getWidth(), 0);
            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2i(getWidth(), getHeight());
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2i(0, getHeight());
            GL11.glEnd();

            if (finalShader != null) {
                finalShader.unbind();
            } else {
                defaultFinal.unbind();
            }

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

            leaveOrtho();
        }

        GL11.glFlush();
        update(game);
    }

    /**
     * Initialize the FBO.
     *
     * @param fbo     The fbo to initialize
     * @param texture The texture to use as renderbuffer.
     * @param depth   The depth buffer.
     */
    private void initFBO(Game game, int fbo, int texture, int depth) {
        int width = getWidth();
        int height = getHeight();

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height,
                0, GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null);
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT,
                GL11.GL_TEXTURE_2D, texture, 0);

        /*glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depth);
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT,
                GL30.GL_DEPTH24_STENCIL8, width, height);
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,
                GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, depth);*/

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depth);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_DEPTH_TEXTURE_MODE, GL11.GL_INTENSITY);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_MODE, GL14.GL_COMPARE_R_TO_TEXTURE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_FUNC, GL11.GL_LEQUAL);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height,
                0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (java.nio.ByteBuffer) null);

        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL11.GL_TEXTURE_2D, depth, 0);

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }

    public void handleInputs(Player player) {
        if (Keyboard.next()) {
            player.keyboardInputHandler(Keyboard.getEventKey(),
                    Keyboard.getEventKeyState());
        }
        if (Mouse.next()) {
            player.mouseInputHandler(Mouse.getEventButton(),
                    Mouse.getEventButtonState());
        }
        if (Mouse.isGrabbed()) {
            player.mouseMoveHandler(Mouse.getDX(), Mouse.getDY());
        }
    }
}
