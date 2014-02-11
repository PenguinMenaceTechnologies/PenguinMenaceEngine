package net.pme;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JOptionPane;

import net.pme.network.NetworkInitializer;
import net.pme.network.NetworkManager;
import net.pme.objects.HudObject;
import net.pme.objects.MovableObject;
import net.pme.objects.Particle;
import net.pme.objects.Player;
import net.pme.objects.RenderableObject;
import net.pme.objects.Shader;

/**
 * Creates the LWJGL object and invokes a game loop thread as well as a network
 * listener etc.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public final class Game {
	private List<MovableObject> movableObjects = new CopyOnWriteArrayList<MovableObject>();
	private List<RenderableObject> renderableObjects = new CopyOnWriteArrayList<RenderableObject>();
	private List<HudObject> hudObjects = new CopyOnWriteArrayList<HudObject>();
	private LinkedList<Particle> particleObjects = new LinkedList<Particle>();
	private Shader postprocessing = null;
	private Shader finalShader = null;
	private boolean isLoaded = false;
	private GameLoop gameLoop = null;
	private NetworkManager networkManager = null;

	/**
	 * Add a movable object to the movable objects.
	 * 
	 * Only works for objects that are not in renderable objects.
	 * 
	 * @param movableObject
	 *            The object that should be added.
	 */
	public void addMovable(final MovableObject movableObject) {
		if (!renderableObjects.contains(movableObject)) {
			movableObjects.add(movableObject);
		}
	}

	/**
	 * Add a renderable object to the renderable objects.
	 * 
	 * Removes objects from movable list, to reduce redundancy.
	 * 
	 * @param renderableObject
	 *            The object that should be added.
	 */
	public void addRenderable(final RenderableObject renderableObject) {
		if (movableObjects.contains(renderableObject)) {
			removeMovable(renderableObject);
		}
		if (!renderableObjects.contains(renderableObject)) {
			renderableObjects.add(renderableObject);
		}
	}

	/**
	 * Adds the specified hud object to the HUD.
	 * 
	 * @param hudObject
	 *            The specified hud object.
	 */
	public void addHud(final HudObject hudObject) {
		hudObjects.add(hudObject);
	}
	
	/**
	 * Set a shader to be used for postprocessing.
	 * @param shader The shader to use.
	 */
	public void setPostprocessingShader(Shader shader) {
		postprocessing = shader;
	}
	
	/**
	 * Set a shader to be used for final postprocessing (also applied on hud).
	 * @param shader The shader to use.
	 */
	public void setFinalShader(Shader shader) {
		finalShader = shader;
	}
	
	/**
	 * Get an instance of the currently used postprocessing shader.
	 * @return The currently used shader.
	 */
	public Shader getPostprocessingShader() {
		return postprocessing;
	}
	
	/**
	 * Get an instance of the currently used final postprocessing shader.
	 * @return The currently used shader.
	 */
	public Shader getFinalShader() {
		return finalShader;
	}

	/**
	 * Remove an object from movable objects list.
	 * 
	 * @param movableObject
	 *            The object that should be removed.
	 */
	public void removeMovable(final MovableObject movableObject) {
		movableObjects.remove(movableObject);
	}

	/**
	 * Remove an object from the renderable objects list.
	 * 
	 * @param renderableObject
	 *            The object that should be removed.
	 */
	public void removeRenderable(final RenderableObject renderableObject) {
		renderableObjects.remove(renderableObject);
	}

	/**
	 * Remove an object from the hud object list.
	 * 
	 * @param hudObject
	 *            The object that should be removed.
	 */
	public void removeHud(final HudObject hudObject) {
		hudObjects.remove(hudObject);
	}

	/**
	 * Clear the movable object list.
	 */
	public void clearMovable() {
		movableObjects.clear();
	}

	/**
	 * Clear the renderable object list.
	 */
	public void clearRenderable() {
		renderableObjects.clear();
	}

	/**
	 * Clear the hud object list.
	 */
	public void clearHud() {
		hudObjects.clear();
	}

	/**
	 * Clear the renderable, movable and hud object list.
	 */
	public void clearAll() {
		clearMovable();
		clearRenderable();
		clearHud();
	}

	/**
	 * Run the game with the given player.
	 * 
	 * @param player
	 *            The player instance. (Null creates a dummy-player placed at 0)
	 */
	public void runGame(final Player player) {
		if (!isLoaded) {
			loadGame();
		}
		if (gameLoop != null) {
			throw new IllegalStateException(
					"There cannot be 2 calls of run game at a time");
		}

		gameLoop = new GameLoop();

		// Start the gameLoop
		gameLoop.run(movableObjects, renderableObjects, particleObjects,
				hudObjects, player, this);

		gameLoop = null;
	}

	/**
	 * Stop the currently running gameloop.
	 */
	public void stopGame() {
		if (gameLoop == null) {
			throw new IllegalStateException("You must call runGame first.");
		}
		gameLoop.terminate();
	}

	/**
	 * Loads the game.
	 */
	public void loadGame() {
		try {
			NativeLoader.loadLibraries();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showConfirmDialog(null,
					"Cannot find/load native libraries!", "ERROR",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		GameInput.load();

		// TODO invoke network listener thread.

		isLoaded = true;
	}

	/**
	 * Unload the game.
	 */
	public void unload() {
		NativeLoader.unloadLibraries();
		GameSettings s = GameSettings.get();
		if (s != null) {
			s.saveAll();
		}
	}

	/**
	 * Initialize a network with the given parameters.
	 * @param NetworkInitializer The initializer.
	 * @throws IOException Connecting failed.
	 * @throws UnknownHostException Host is not known.
	 */
	public void initializeNetwork(NetworkInitializer networkInitializer) throws UnknownHostException, IOException {
		if (networkManager == null) {
			networkManager = new NetworkManager(networkInitializer);
		}
	}
	
	/**
	 * Get the instance of the currently running network manager.
	 * @return The active network manager.
	 */
	public NetworkManager getNetworkManager() {
		return networkManager;
	}
	
	/**
	 * Deinitialize the network.
	 */
	public void deinitializeNetwork() {
		if (networkManager != null) {
			networkManager.deinitialize();
		}
	}
}
