package net.pme;

import net.pme.core.Player;
import net.pme.core.math.Vector3D;
import net.pme.graphics.OffscreenRenderer;
import net.pme.model.Model;
import net.pme.model.ModelManager;
import org.lwjgl.input.Mouse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A simple test of the game engine.
 *
 * @author Michael FÃ¼rst
 * @version 1.0
 */
public class Test {
    /**
     * The game for the test.
     */
    static Game game;

    /**
     * The main testing method.
     *
     * @param args The arguments for the program.
     * @throws IOException
     */
    public static void main(final String[] args) throws IOException {
        // Yay, our game!
        game = new Game();

        // Initialize the required modules.
        // Core is independant.
        game.initializeCore(new TestSettings());
        // Display depends on core.
        game.initializeGraphics("PenguinMenaceEngine Test", 800, 600, false, 10000);
        // ModelManager depends on display.
        game.initializeModelManager();
        // Network Manager is independant.
        game.initializeNetwork(new TestNetworkInitializer());

        ModelManager modelManager = game.getModelManager();

        Model model = modelManager.get(Test.class.getResource(
                "/assets/cube_small.obj").getPath());
        Model model2 = modelManager.get(Test.class.getResource(
                "/assets/cube_small.obj").getPath());
        if (model == model2) {
            System.out.println("Model loading is efficient!");
        }

        Vector3D frontA = new Vector3D(0, 0, -1);
        Vector3D upA = new Vector3D(0, 1, 0);

        Vector3D frontB = new Vector3D(0, 0, -1);
        Vector3D upB = new Vector3D(0, 1, 0);

        Vector3D front1 = new Vector3D(0, 0, -1);
        Vector3D up1 = new Vector3D(1, -1, 0);

        Vector3D front2 = new Vector3D(0, 0, -1);
        Vector3D up2 = new Vector3D(1, 1, 0);

        Vector3D front3 = new Vector3D(0, 0, -1);
        Vector3D up3 = new Vector3D(1, 1, 0);

        game.addGameObject(new Ship(2, new Vector3D(1, 2, -10), front1, up1,
                game.getModelManager().get(Test.class.getResource("/assets/cube_small.obj").getPath())));
        game.addGameObject(new Ship(3, new Vector3D(1, -2, -10), front2, up2,
                game.getModelManager().get(Test.class.getResource("/assets/ship.obj").getPath())));
        game.addGameObject(new TestCube3(4, new Vector3D(-2, 0, -10), front3,
                up3));

		/*game.addGameObject(new RenderAttachment(5, new Vector3D(-4, 2.5, -10),
                frontA, upA, model));
		game.addGameObject(new RenderAttachment(6, new Vector3D(4, 2.5, -10),
				frontB, upB, modelManager.get(Test.class.getResource(
						"/assets/cube_small.obj").getPath())));

		for (int j = 0; j < 10; j++) {
			for (int i = 1; i < 10; i++) {
				game.addGameObject(new RenderAttachment(6, new Vector3D(
						4 + 4 * i, 2.5 + 4 * j, -10), frontB, upB, model));

			}
		}*/

        try {
            BufferedImage bi = ImageIO.read(new File(Test.class.getResource(
                    "/assets/overlay.jpg").getFile()));
            game.addHud(new TestHudObject(64, 64, bi));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Mouse.setGrabbed(true);

        Vector3D playerFront = new Vector3D(0, 0, 1);
        Vector3D playerUp = new Vector3D(0, 1, 0);
        Player player = new TestPlayer(new Vector3D(0, 0, 0), playerFront,
                playerUp);
        game.getDisplay().setPostprocessingShader(new PostprocessingShader());
        game.getDisplay().setFinalShader(new PostprocessingShader2());

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    return;
                }
                OffscreenRenderer o = new OffscreenRendererTest();
                game.getDisplay().addOffscreenRenderer(o, game);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    return;
                }
                game.getDisplay().removeOffscreenRenderer(o);
            }
        }.start();

        // Run our game.
        game.runGame(player);

        // Deinitialize all modules.
        game.deinitializeNetwork();
        game.deinitializeModelManager();
        game.deinitializeGraphics();
        game.deinitializeCore();
    }
}
