package net.pme;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.input.Mouse;

import net.pme.Game;
import net.pme.GameDisplay;
import net.pme.GameSettings;
import net.pme.ModelManager;
import net.pme.math.Vector3D;
import net.pme.objects.Player;
import net.pme.objects.RenderableObject;
import net.pme.objects.Shader;

/**
 * A simple test of the game engine.
 * 
 * @author Michael Fürst
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
	 */
	public static void main(final String[] args) {
		game = new Game();

		// Set the game settings first
		GameSettings.set(new TestSettings());

		game.loadGame();

		GameDisplay.create("PenguinMenaceEngine Test", 800, 600, false);
		GameDisplay.getDisplay().setFPS(1000000);

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
				up1));
		game.addRenderable(new TestCube2(3, new Vector3D(1, -2, -10), front2,
				up2));
		game.addRenderable(new TestCube3(4, new Vector3D(-2, 0, -10), front3,
				up3));
		
		game.addRenderable(new RenderableObject(5, new Vector3D(-4, 2.5, -10),
				frontA, upA, ModelManager.getSpecialCoords(Test.class
						.getResource("/assets/ships/Anachron.obj").getFile())));
		game.addRenderable(new RenderableObject(6, new Vector3D(4, 2.5, -10),
				frontB, upB, ModelManager.getSpecialCoords(Test.class
						.getResource("/assets/ships/Anachron.obj").getFile())));
		/*
		 * for (int j = 0; j < 100; j++) { for (int i = 1; i < 100; i++) {
		 * game.addRenderable(new RenderableObject(6, new Vector3D( 4 + 2 * i,
		 * 2.5 + 2 * j, -10), frontB, upB, ModelManager
		 * .getSpecialCoords(Test.class.getResource(
		 * "/assets/ships/Anachron.obj").getFile())));
		 * 
		 * } }
		 */

		try {
			BufferedImage bi = ImageIO.read(new File(Test.class.getResource(
					"/assets/ships/Anachron.jpg").getFile()));
			game.addHud(new TestHudObject(7, 250, 250, bi));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Mouse.setGrabbed(true);

		Vector3D playerFront = new Vector3D(0, 0, 1);
		Vector3D playerUp = new Vector3D(0, 1, 0);
		Player player = new TestPlayer(new Vector3D(0, 0, 0), playerFront,
				playerUp);

		game.runGame(player);

		GameDisplay.getDisplay().deinit();
	}
}