package net.pme.graphics;

import net.pme.Game;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;

import static org.lwjgl.opengl.EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_RENDERBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindRenderbufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glDeleteFramebuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glFramebufferRenderbufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glFramebufferTexture2DEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glGenFramebuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glRenderbufferStorageEXT;

/**
 * Created by michael on 10.03.14.
 */
public class OffscreenRendererWrapper {
        private int f;
        private int t;
        private int d;
        private OffscreenRenderer renderer;
        private boolean disabled = false;


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
            if ((f != 0) || (t != 0) ||(d != 0)) {
                return;
            }
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

            //glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, d);
            //glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT,
            //        GL14.GL_DEPTH_COMPONENT24, width, height);
            //glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,
            //        GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, d);

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, d);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_DEPTH_TEXTURE_MODE, GL11.GL_INTENSITY);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_MODE, GL14.GL_COMPARE_R_TO_TEXTURE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_COMPARE_FUNC, GL11.GL_LEQUAL);

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height,
                    0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (java.nio.ByteBuffer) null);

            glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL11.GL_TEXTURE_2D, d, 0);

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
        private boolean useFBO(Game game) {
            if (f == 0 && t == 0 && d == 0) {
                init(game);
            }

            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

            glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, f);

            GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glViewport(0, 0, renderer.getWidth(), renderer.getHeight());

            GL11.glLoadIdentity();
            return true;
        }

        /**
         * Render offscreen automatically binding the fbo.
         */
        boolean preRender(Game game) {
            if (disabled) {
                delete();
                return false;
            }
            if(useFBO(game)) {
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
            if (obj instanceof OffscreenRenderer) {
                OffscreenRenderer r = (OffscreenRenderer) obj;
                return r.equals(this.renderer);
            }
            if (obj instanceof OffscreenRendererWrapper) {
                OffscreenRendererWrapper tmp = (OffscreenRendererWrapper) obj;

                // They are equal if they have the same offscreen renderer.
                return tmp.renderer.equals(this.renderer);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return renderer.hashCode();
        }

        /**
         * Disable the offscreen renderer.
         */
        void disable() {
            disabled = true;
        }

}
