package net.pme;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.pme.offscreen.OffscreenRenderer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;

import net.pme.objects.HudObject;
import net.pme.objects.MovableObject;
import net.pme.objects.Particle;
import net.pme.objects.Player;
import net.pme.objects.RenderableObject;
import net.pme.objects.Shader;
import net.pme.objects.PostprocessingShader;
import static org.lwjgl.opengl.EXTFramebufferObject.*;

/**
 * This makes the gameengine pump.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
final class GameLoop {
	private static final double NANO_TO_SEC = 1E-9;
	private boolean running = true;

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
	private boolean rendering = false;

	/**
	 * Do nothing, only visible inside engine.
	 */
	GameLoop() {
	}

	public void initializeRendering(Game game) {
		GameDisplay display = game.getDisplay();
		if (display == null) {
			rendering = false;
			throw new IllegalStateException(
					"You must initialize the display module before enabling rendering");
		}
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

        for (OffscreenRendererWrapper tmp: offscreenRenderers) {
            tmp.init(game);
        }

		GL11.glViewport(0, 0, display.getWidth(), display.getHeight());

		defaultPostprocessing = new PostprocessingShader(0);
		defaultFinal = new PostprocessingShader(0);
		rendering = true;
	}

	public void deinitializeRendering() {
		if (rendering) {
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

            for (OffscreenRendererWrapper tmp: offscreenRenderers) {
                tmp.delete();
            }

			rendering = false;
		}
	}

    /**
     * Add an offscreen renderer.
     * @param renderer The renderer to add.
     * @param game The to which to add the renderer.
     */
    public void addOffscreenRenderer(OffscreenRenderer renderer, Game game) {
        OffscreenRendererWrapper wrapper = new OffscreenRendererWrapper(renderer);
        if (rendering) {
            wrapper.init(game);
        }
        offscreenRenderers.add(wrapper);
    }

    /**
     * Remove a offscreen renderer.
     * @param renderer The renderer to remove.
     */
    public void removeOffscreenRenderer(OffscreenRenderer renderer) {
        OffscreenRendererWrapper wrapper = new OffscreenRendererWrapper(renderer);
        offscreenRenderers.remove(wrapper);
    }

	/**
	 * This is the heart-beat of the engine.
	 * 
	 * It ticks once every 16.6 ms.
	 * 
	 * @param player
	 *            The player.
	 * @param renderableObjects
	 *            All renderable Objects.
	 * @param movableObjects
	 *            All movable Objects.
	 * @param particleObjects
	 *            All particles. (Note that the particle list is internally not
	 *            thread-safe.)
	 * @param hudObjects
	 *            All hud objects.
	 * @param game
	 */
	void run(final List<MovableObject> movableObjects,
			final List<RenderableObject> renderableObjects,
			final LinkedList<Particle> particleObjects,
			final List<HudObject> hudObjects, final Player player, Game game) {
		List<Particle> localParticleObjects = new ArrayList<Particle>();

		double elapsedTime = 0;

		GameDisplay display = game.getDisplay();

		if (display != null) {
			initializeRendering(game);
		}

		Shader postprocessingShader = null;
		Shader finalShader = null;

		while (running && (display == null || !display.isCloseRequested())) {
			long timer = System.nanoTime();

			// Flush particle buffer to local buffer.
			addAll(localParticleObjects, particleObjects);

			// Move all objects
			for (MovableObject o : movableObjects) {
				o.move(elapsedTime);
			}
			for (RenderableObject o : renderableObjects) {
				o.move(elapsedTime);
			}
			for (Particle o : particleObjects) {
				o.move(elapsedTime);
			}
			for (HudObject o : hudObjects) {
				o.move(elapsedTime);
			}

			if (player != null) {
				if (rendering && Keyboard.next()) {
					player.keyboardInputHandler(Keyboard.getEventKey(),
							Keyboard.getEventKeyState());
				}
				if (rendering && Mouse.next()) {
					player.mouseInputHandler(Mouse.getEventButton(),
							Mouse.getEventButtonState());
				}
				if (rendering && Mouse.isGrabbed()) {
					player.mouseMoveHandler(Mouse.getDX(), Mouse.getDY());
				}

				player.move(elapsedTime);
			}

			if (rendering) {
				display = game.getDisplay();
				postprocessingShader = game.getPostprocessingShader();
				finalShader = game.getFinalShader();
				display.sync();

				for(OffscreenRendererWrapper tmp: offscreenRenderers) {

					GL13.glActiveTexture(GL13.GL_TEXTURE0);
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
					
	                tmp.preRender();
	                
	                // Render all objects
					for (RenderableObject o : renderableObjects) {
						o.render();
					}
					for (Particle o : particleObjects) {
						o.render();
					}

                    GL11.glFlush();
	                tmp.postRender();
	            }

				if (postprocessing) {
					// unlink textures to avoid errors
					GL13.glActiveTexture(GL13.GL_TEXTURE0);
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
					glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo);
				}

				GL11.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT
						| GL11.GL_DEPTH_BUFFER_BIT);

				GL11.glLoadIdentity();

				if (player != null) {
					player.applyCamera();
				}

				// Render all objects
				for (RenderableObject o : renderableObjects) {
					o.render();
				}
				for (Particle o : particleObjects) {
					o.render();
				}

				if (postprocessing) {
					glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo2);

					GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
					GL11.glClear(GL11.GL_COLOR_BUFFER_BIT
							| GL11.GL_DEPTH_BUFFER_BIT);

					GL11.glLoadIdentity();

					display.enterOrtho();

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
					GL11.glVertex2i(display.getWidth(), 0);
					GL11.glTexCoord2f(1, 0);
					GL11.glVertex2i(display.getWidth(), display.getHeight());
					GL11.glTexCoord2f(0, 0);
					GL11.glVertex2i(0, display.getHeight());
					GL11.glEnd();

					if (postprocessingShader != null) {
						postprocessingShader.unbind();
					} else {
						defaultPostprocessing.unbind();
					}

					GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

					display.leaveOrtho();

				}

				if (hudObjects.size() > 0) {
					display.enterOrtho();

					// Render Hud ontop of all
					for (HudObject o : hudObjects) {
						o.render();
					}
					display.leaveOrtho();
				}

				if (postprocessing) {
					glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

					GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
					GL11.glClear(GL11.GL_COLOR_BUFFER_BIT
							| GL11.GL_DEPTH_BUFFER_BIT);

					GL11.glLoadIdentity();

					display.enterOrtho();

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
					GL11.glVertex2i(display.getWidth(), 0);
					GL11.glTexCoord2f(1, 0);
					GL11.glVertex2i(display.getWidth(), display.getHeight());
					GL11.glTexCoord2f(0, 0);
					GL11.glVertex2i(0, display.getHeight());
					GL11.glEnd();

					if (finalShader != null) {
						finalShader.unbind();
					} else {
						defaultFinal.unbind();
					}

					GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

					display.leaveOrtho();
				}

				GL11.glFlush();
				display.update(this, game);
			} else {
				// Sleep a bit when we are not rendering. To yield for other threads.
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			timer = System.nanoTime() - timer;
			elapsedTime = timer * NANO_TO_SEC;
		}

		if (rendering) {
			deinitializeRendering();
		}
	}

	/**
	 * Initialize the FBO.
	 * 
	 * @param fbo
	 *            The fbo to initialize
	 * @param texture
	 *            The texture to use as renderbuffer.
	 * @param depth
	 *            The depth buffer.
	 */
	private void initFBO(Game game, int fbo, int texture, int depth) {
		int width = game.getDisplay().getWidth();
		int height = game.getDisplay().getHeight();

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
				GL11.GL_LINEAR);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height,
				0, GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null);
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT,
				GL11.GL_TEXTURE_2D, texture, 0);

		glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depth);
		glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT,
				GL14.GL_DEPTH_COMPONENT24, width, height);
		glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,
				GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, depth);

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
	}

	/**
	 * Add all objects from buffer to active list.
	 * 
	 * @param localParticleObjects
	 *            Local list.
	 * @param particleObjects
	 *            Buffer to copy into local list.
	 */
	private void addAll(final List<Particle> localParticleObjects,
			final LinkedList<Particle> particleObjects) {
		while (!particleObjects.isEmpty()) {
			localParticleObjects.add(particleObjects.removeFirst());
		}
	}

	/**
	 * Stop the game loop.
	 */
	void terminate() {
		running = false;
	}

    class OffscreenRendererWrapper {
        private int f;
        private int t;
        private int d;
        private OffscreenRenderer renderer;


        /**
         * An offscreen renderer wrapper, to make it more comfortable to use.
         * @param renderer The renderer.
         */
        OffscreenRendererWrapper(OffscreenRenderer renderer) {
            this.renderer = renderer;
        }

        /**
         * Initialize the fbo.
         *
         * @param game The game for which to initialize.
         */
        void init(Game game) {
            f = glGenFramebuffersEXT();
            t = GL11.glGenTextures();
            d = GL11.glGenTextures();

            int width = renderer.getWidth();
            int height = renderer.getHeight();

            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, f);

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, t);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
                    GL11.GL_LINEAR);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height,
                    0, GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null);
            glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT,
                    GL11.GL_TEXTURE_2D, t, 0);

            glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, d);
            glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT,
                    GL14.GL_DEPTH_COMPONENT24, width, height);
            glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,
                    GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, d);

            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        }

        /**
         * Delete the fbo.
         */
        void delete() {
            if (d != 0)
            GL11.glDeleteTextures(d);
            if (t != 0)
            GL11.glDeleteTextures(t);
            if(f != 0)
            glDeleteFramebuffersEXT(f);

            f = 0;
            d = 0;
            t = 0;
        }

        /**
         * Use the fbo.
         * @return Weather it is usable or not.
         */
        private boolean useFBO() {
            if (f != 0 && t != 0 && d != 0) {
                glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, f);

                GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT
                        | GL11.GL_DEPTH_BUFFER_BIT);

                GL11.glLoadIdentity();
                return true;
            } else {
                return false;
            }
        }

        /**
         * Render offscreen automatically binding the fbo.
         */
        boolean preRender() {
            if(useFBO()) {
                GL11.glViewport(0, 0, renderer.getWidth(), renderer.getHeight());

                renderer.preRender();
                return true;
            }
			return false;
        }
        
        /**
         * Called after rendering.
         */
        void postRender() {
        	renderer.postRender(t, d);
            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        }

        @Override
        public void finalize() {
            delete();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof OffscreenRendererWrapper) {
                OffscreenRendererWrapper tmp = (OffscreenRendererWrapper) obj;

                // They are equal if they have the same offscreen renderer.
                if (tmp.renderer.equals(this.renderer)) {
                    return true;
                }
            }
            return false;
        }
    }
}
