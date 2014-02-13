package net.pme;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import org.lwjgl.input.Mouse;

import net.pme.Game;
import net.pme.ModelManager;
import net.pme.math.Vector3D;
import net.pme.objects.Player;
import net.pme.objects.RenderableObject;

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
	 * @param args
	 *            The arguments for the program.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(final String[] args) throws UnknownHostException, IOException {
		// Yay, our game!
		game = new Game();
		
		// Initialize the required modules.
		// Core is independant.
		game.initializeCore(new TestSettings());
		// Display depends on core.
		game.initializeDisplay("PenguinMenaceEngine Test", 800, 600, false, 10000);
		// ModelManager depends on display.
		game.initializeModelManager();
		// Network Manager is independant.
		game.initializeNetwork(new TestNetworkInitializer());
		
		ModelManager modelManager = game.getModelManager();

		int model = modelManager.get(Test.class.getResource(
				"/assets/cube_small.obj").getPath());
		int model2 = modelManager.get(Test.class.getResource(
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

		game.addRenderable(new TestCube1(2, new Vector3D(1, 2, -10), front1,
				up1, modelManager));
		game.addRenderable(new Ship(3, new Vector3D(1, -2, -10), front2, up2));
		game.addRenderable(new TestCube3(4, new Vector3D(-2, 0, -10), front3,
				up3));

		game.addRenderable(new RenderableObject(5, new Vector3D(-4, 2.5, -10),
				frontA, upA, model));
		game.addRenderable(new RenderableObject(6, new Vector3D(4, 2.5, -10),
				frontB, upB, modelManager.get(Test.class.getResource(
						"/assets/cube_small.obj").getPath())));

		for (int j = 0; j < 10; j++) {
			for (int i = 1; i < 10; i++) {
				game.addRenderable(new RenderableObject(6, new Vector3D(
						4 + 4 * i, 2.5 + 4 * j, -10), frontB, upB, model));

			}
		}

		try {
			BufferedImage bi = ImageIO.read(new File(Test.class.getResource(
					"/assets/overlay.jpg").getFile()));
			game.addHud(new TestHudObject(7, 64, 64, bi));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Mouse.setGrabbed(true);

		Vector3D playerFront = new Vector3D(0, 0, 1);
		Vector3D playerUp = new Vector3D(0, 1, 0);
		Player player = new TestPlayer(new Vector3D(0, 0, 0), playerFront,
				playerUp);
		game.setPostprocessingShader(new PostprocessingShader(8));
		game.setFinalShader(new PostprocessingShader2(9));

		// Run our game.
		game.runGame(player);

		// Deinitialize all modules.
		game.deinitializeNetwork();
		game.deinitializeModelManager();
		game.deinitializeDisplay();
		game.unload();
	}
}
