package net.pme;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

	/**
	 * Do nothing, only visible inside engine.
	 */
	GameLoop() {
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
		GameDisplay display = GameDisplay.getDisplay();

		GL11.glEnable(GL11.GL_TEXTURE_2D);

		if (!GLContext.getCapabilities().GL_EXT_framebuffer_object) {
			postprocessing = false;
			System.err.println("FBO not supported. Postprocessing disabled.");
		}

		int fbo = 0;
		int texture = 0;
		int depth = 0;
		
		int fbo2 = 0;
		int texture2 = 0;
		int depth2 = 0;

		if (postprocessing) {
			fbo = glGenFramebuffersEXT();
			texture = GL11.glGenTextures();
			depth = GL11.glGenTextures();
			
			fbo2 = glGenFramebuffersEXT();
			texture2 = GL11.glGenTextures();
			depth2 = GL11.glGenTextures();

			initFBO(fbo, texture, depth);
			initFBO(fbo2, texture2, depth2);
		}

		GL11.glViewport(0, 0, display.getWidth(), display.getHeight());
		Shader postprocessingShader = null;
		Shader finalShader = null;
		Shader defaultPostprocessing = new PostprocessingShader(0);
		Shader defaultFinal = new PostprocessingShader(0);
		
		while (running && !display.isCloseRequested()) {
			postprocessingShader = game.getPostprocessingShader();
			finalShader = game.getFinalShader();
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

				player.move(elapsedTime);
			}

			display.sync();
			
			if (postprocessing) {
				// unlink textures to avoid errors
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
				glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, fbo);
			}

			GL11.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
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
					postprocessingShader.setupTexture("texture", texture, 0);
				} else {
					defaultPostprocessing.bind();
					defaultPostprocessing.setupTexture("texture", texture, 0);
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
			display.update();

			timer = System.nanoTime() - timer;
			elapsedTime = timer * NANO_TO_SEC;
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
	private void initFBO(int fbo, int texture, int depth) {
		int width = GameDisplay.getDisplay().getWidth();
		int height = GameDisplay.getDisplay().getHeight();

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
}
