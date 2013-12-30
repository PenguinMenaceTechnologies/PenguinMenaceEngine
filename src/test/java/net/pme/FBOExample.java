package net.pme;
/********************************************************
*														*
*    Overlords Tutorial - 01							*
*     http://www.flashbang.se							*
*             2006                  					*
*                                   					*
*    ported by kappaOne 								*
*    from http://www.flashbang.se/archives/48			*                           
*********************************************************/

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.glu.GLU;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.EXTFramebufferObject.*;
 
public class FBOExample {
 
	float angle;
	
	int colorTextureID;
	int framebufferID;
	int depthRenderBufferID;
 
	/** time at last frame */
	long lastFrame;
 
	/** frames per second */
	int fps;
	/** last fps time */
	long lastFPS;
	
	/** is VSync Enabled */
	boolean vsyncEnabled;
 
	public void start() {
		try {
			Display.setDisplayMode(new DisplayMode(512, 512));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
 
		initGL(); // init OpenGL
		getDelta(); // call once before loop to initialise lastFrame
		lastFPS = getTime(); // call before loop to initialise fps timer
 
		while (!Display.isCloseRequested()) {
			int delta = getDelta();
 
			update(delta);
			renderGL();
 
			Display.update();
			Display.sync(120); // cap fps to 60fps
		}
 
		Display.destroy();
	}
 
	public void update(int delta) {
		// rotate box
		angle += 0.15f * delta;
 
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		        if (Keyboard.getEventKey() == Keyboard.KEY_F) {
		        	setDisplayMode(800, 600, !Display.isFullscreen());
		        }
		        else if (Keyboard.getEventKey() == Keyboard.KEY_V) {
		        	vsyncEnabled = !vsyncEnabled;
		        	Display.setVSyncEnabled(vsyncEnabled);
		        }
		    }
		}
 
		updateFPS(); // update FPS Counter
	}
 
	public void initGL() {
		
		glViewport (0, 0, 512, 512);								// Reset The Current Viewport
		glMatrixMode (GL_PROJECTION);								// Select The Projection Matrix
		glLoadIdentity ();											// Reset The Projection Matrix
		GLU.gluPerspective (45.0f, 512f/512f, 1.0f, 100.0f);		// Calculate The Aspect Ratio Of The Window	
		glMatrixMode (GL_MODELVIEW);								// Select The Modelview Matrix
		glLoadIdentity ();											// Reset The Modelview Matrix
		
		// Start Of User Initialization
		angle		= 0.0f;											// Set Starting Angle To Zero

		glClearColor (0.0f, 0.0f, 0.0f, 0.5f);						// Black Background
		glClearDepth (1.0f);										// Depth Buffer Setup
		glDepthFunc (GL_LEQUAL);									// The Type Of Depth Testing (Less Or Equal)
		glEnable (GL_DEPTH_TEST);									// Enable Depth Testing
		glShadeModel (GL_SMOOTH);									// Select Smooth Shading
		glHint (GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);			// Set Perspective Calculations To Most Accurate
		
		

		// check if GL_EXT_framebuffer_object can be use on this system
		if (!GLContext.getCapabilities().GL_EXT_framebuffer_object) {
			System.out.println("FBO not supported!!!");
			System.exit(0);
		}
		else {
			
			System.out.println("FBO is supported!!!");
			
			// init our fbo
	
			framebufferID = glGenFramebuffersEXT();											// create a new framebuffer
			colorTextureID = glGenTextures();												// and a new texture used as a color buffer
			depthRenderBufferID = glGenRenderbuffersEXT();									// And finally a new depthbuffer
	
			glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID); 						// switch to the new framebuffer
	
			// initialize color texture
			glBindTexture(GL_TEXTURE_2D, colorTextureID);									// Bind the colorbuffer texture
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);				// make it linear filterd
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 512, 512, 0,GL_RGBA, GL_INT, (java.nio.ByteBuffer) null);	// Create the texture data
			glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_COLOR_ATTACHMENT0_EXT,GL_TEXTURE_2D, colorTextureID, 0); // attach it to the framebuffer
	
	
			// initialize depth renderbuffer
			glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthRenderBufferID);				// bind the depth renderbuffer
			glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, 512, 512);	// get the data space for it
			glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,GL_DEPTH_ATTACHMENT_EXT,GL_RENDERBUFFER_EXT, depthRenderBufferID); // bind it to the renderbuffer
	
			glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);									// Swithch back to normal framebuffer rendering
			
		}
		
	}
 
	public void renderGL() {
		
		// FBO render pass

		glViewport (0, 0, 512, 512);									// set The Current Viewport to the fbo size

		glBindTexture(GL_TEXTURE_2D, 0);								// unlink textures because if we dont it all is gonna fail
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);		// switch to rendering on our FBO

		glClearColor (1.0f, 0.0f, 0.0f, 0.5f);
		glClear (GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);			// Clear Screen And Depth Buffer on the fbo to red
		glLoadIdentity ();												// Reset The Modelview Matrix
		glTranslatef (0.0f, 0.0f, -6.0f);								// Translate 6 Units Into The Screen and then rotate
		glRotatef(angle,0.0f,1.0f,0.0f);
		glRotatef(angle,1.0f,0.0f,0.0f);
		glRotatef(angle,0.0f,0.0f,1.0f);

		glColor3f(1,1,0);												// set color to yellow
		drawBox();														// draw the box


		// Normal render pass, draw cube with texture

		glEnable(GL_TEXTURE_2D);										// enable texturing
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);					// switch to rendering on the framebuffer

		glClearColor (0.0f, 1.0f, 0.0f, 0.5f);
		glClear (GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);			// Clear Screen And Depth Buffer on the framebuffer to black

		glBindTexture(GL_TEXTURE_2D, colorTextureID);					// bind our FBO texture


		glViewport (0, 0, 512, 512);									// set The Current Viewport


		glLoadIdentity ();												// Reset The Modelview Matrix
		glTranslatef (0.0f, 0.0f, -6.0f);								// Translate 6 Units Into The Screen and then rotate
		glRotatef(angle,0.0f,1.0f,0.0f);
		glRotatef(angle,1.0f,0.0f,0.0f);
		glRotatef(angle,0.0f,0.0f,1.0f);
		glColor3f(1,1,1);												// set the color to white
		drawBox();														// draw the box

		glDisable(GL_TEXTURE_2D);
		glFlush ();
		
		
	}
	
	public void drawBox() { 
		// this func just draws a perfectly normal box with some texture coordinates
		glBegin(GL_QUADS);
			// Front Face
			glTexCoord2f(0.0f, 0.0f); glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Left Of The Texture and Quad
			glTexCoord2f(1.0f, 0.0f); glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Right Of The Texture and Quad
			glTexCoord2f(1.0f, 1.0f); glVertex3f( 1.0f,  1.0f,  1.0f);	// Top Right Of The Texture and Quad
			glTexCoord2f(0.0f, 1.0f); glVertex3f(-1.0f,  1.0f,  1.0f);	// Top Left Of The Texture and Quad
			// Back Face
			glTexCoord2f(1.0f, 0.0f); glVertex3f(-1.0f, -1.0f, -1.0f);	// Bottom Right Of The Texture and Quad
			glTexCoord2f(1.0f, 1.0f); glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Right Of The Texture and Quad
			glTexCoord2f(0.0f, 1.0f); glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Left Of The Texture and Quad
			glTexCoord2f(0.0f, 0.0f); glVertex3f( 1.0f, -1.0f, -1.0f);	// Bottom Left Of The Texture and Quad
			// Top Face
			glTexCoord2f(0.0f, 1.0f); glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Left Of The Texture and Quad
			glTexCoord2f(0.0f, 0.0f); glVertex3f(-1.0f,  1.0f,  1.0f);	// Bottom Left Of The Texture and Quad
			glTexCoord2f(1.0f, 0.0f); glVertex3f( 1.0f,  1.0f,  1.0f);	// Bottom Right Of The Texture and Quad
			glTexCoord2f(1.0f, 1.0f); glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Right Of The Texture and Quad
			// Bottom Face
			glTexCoord2f(1.0f, 1.0f); glVertex3f(-1.0f, -1.0f, -1.0f);	// Top Right Of The Texture and Quad
			glTexCoord2f(0.0f, 1.0f); glVertex3f( 1.0f, -1.0f, -1.0f);	// Top Left Of The Texture and Quad
			glTexCoord2f(0.0f, 0.0f); glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Left Of The Texture and Quad
			glTexCoord2f(1.0f, 0.0f); glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Right Of The Texture and Quad
			// Right face
			glTexCoord2f(1.0f, 0.0f); glVertex3f( 1.0f, -1.0f, -1.0f);	// Bottom Right Of The Texture and Quad
			glTexCoord2f(1.0f, 1.0f); glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Right Of The Texture and Quad
			glTexCoord2f(0.0f, 1.0f); glVertex3f( 1.0f,  1.0f,  1.0f);	// Top Left Of The Texture and Quad
			glTexCoord2f(0.0f, 0.0f); glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Left Of The Texture and Quad
			// Left Face
			glTexCoord2f(0.0f, 0.0f); glVertex3f(-1.0f, -1.0f, -1.0f);	// Bottom Left Of The Texture and Quad
			glTexCoord2f(1.0f, 0.0f); glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Right Of The Texture and Quad
			glTexCoord2f(1.0f, 1.0f); glVertex3f(-1.0f,  1.0f,  1.0f);	// Top Right Of The Texture and Quad
			glTexCoord2f(0.0f, 1.0f); glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Left Of The Texture and Quad
		glEnd();
	}
	
	/**
	 * Set the display mode to be used 
	 * 
	 * @param width The width of the display required
	 * @param height The height of the display required
	 * @param fullscreen True if we want fullscreen mode
	 */
	public void setDisplayMode(int width, int height, boolean fullscreen) {

		// return if requested DisplayMode is already set
                if ((Display.getDisplayMode().getWidth() == width) && 
			(Display.getDisplayMode().getHeight() == height) && 
			(Display.isFullscreen() == fullscreen)) {
			return;
		}
		
		try {
			DisplayMode targetDisplayMode = null;
			
			if (fullscreen) {
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;
				
				for (int i=0;i<modes.length;i++) {
					DisplayMode current = modes[i];
					
					if ((current.getWidth() == width) && (current.getHeight() == height)) {
						if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
							if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}

						// if we've found a match for bpp and frequence against the 
						// original display mode then it's probably best to go for this one
						// since it's most likely compatible with the monitor
						if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
						    (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
							targetDisplayMode = current;
							break;
						}
					}
				}
			} else {
				targetDisplayMode = new DisplayMode(width,height);
			}
			
			if (targetDisplayMode == null) {
				System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
				return;
			}

			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);
			
		} catch (LWJGLException e) {
			System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
		}
	}
	
	/** 
	 * Calculate how many milliseconds have passed 
	 * since last frame.
	 * 
	 * @return milliseconds passed since last frame 
	 */
	public int getDelta() {
	    long time = getTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame = time;
 
	    return delta;
	}
 
	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
 
	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
 
	public static void main(String[] argv) {
		NativeLoader.loadLibraries();
		FBOExample fboExample = new FBOExample();
		fboExample.start();
		NativeLoader.unloadLibraries();
	}
}