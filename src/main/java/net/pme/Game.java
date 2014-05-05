package net.pme;

import net.pme.core.GameObject;
import net.pme.core.Player;
import net.pme.core.config.GameSettings;
import net.pme.jobcenter.GameLoop;
import net.pme.graphics.Graphics;
import net.pme.graphics.HudObject;
import net.pme.model.ModelManager;
import net.pme.network.Network;
import net.pme.network.NetworkInitializer;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Creates the LWJGL object and invokes a game loop thread as well as a network
 * listener etc.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public final class Game {
    private List<GameObject> gameObjects = new CopyOnWriteArrayList<>();
    private List<HudObject> hudObjects = new CopyOnWriteArrayList<HudObject>();

    private boolean isLoaded = false;
    private GameLoop gameLoop = null;
    private Network network = null;
    private Graphics graphics = null;
    private ModelManager modelManager;
    private GameSettings settings;

    /**
     * Add a gameObject to the game.
     *
     * @param gameObject The object that should be added.
     */
    public void addGameObject(final GameObject gameObject) {
        if (!gameObjects.contains(gameObject)) {
            gameObjects.add(gameObject);
        }
    }

    /**
     * Adds the specified hud object to the HUD.
     *
     * @param hudObject The specified hud object.
     */
    public void addHud(final HudObject hudObject) {
        hudObjects.add(hudObject);
    }

    /**
     * Remove an object from the game objecst.
     *
     * @param gameObject The object that should be removed.
     */
    public void removeGameObject(final GameObject gameObject) {
        gameObjects.remove(gameObject);
    }

    /**
     * Remove an object from the hud object list.
     *
     * @param hudObject The object that should be removed.
     */
    public void removeHud(final HudObject hudObject) {
        hudObjects.remove(hudObject);
    }

    /**
     * Clear the movable object list.
     */
    public void clearGameObjects() {
        gameObjects.clear();
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
        clearGameObjects();
        clearHud();
    }

    /**
     * Run the game with the given player.
     *
     * @param player The player instance. (Null creates a dummy-player placed at 0)
     */
    public void runGame(final Player player) {
        if (!isLoaded) {
            throw new IllegalStateException(
                    "You have to initialize the core module to run a game!");
        }
        if (gameLoop != null) {
            throw new IllegalStateException(
                    "There cannot be 2 calls of run game at a time");
        }

        gameLoop = new GameLoop();


        addGameObject(player);

        // Start the gameLoop
        gameLoop.run(gameObjects, hudObjects, player, this);

        gameLoop = null;
    }

    /**
     * Stop the currently running jobcenter.
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
    public void initializeCore(GameSettings settings) {
        setSettings(settings);
        try {
            NativeLoader.loadLibraries();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null,
                    "Cannot find/load native libraries!", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Cause the settings to really generate the settings and the mapping.
        settings.getSettings();
        settings.getKeyMapping();

        isLoaded = true;
    }

    /**
     * Unload the game.
     */
    public void deinitializeCore() {
        NativeLoader.unloadLibraries();
        if (settings != null) {
            settings.saveAll();
        }
    }

    /**
     * Initialize a network with the given parameters.
     *
     * @param networkInitializer The initializer.
     * @throws IOException          Connecting failed.
     * @throws UnknownHostException Host is not known.
     */
    public void initializeNetwork(NetworkInitializer networkInitializer)
            throws UnknownHostException, IOException {
        if (network == null) {
            network = new Network(networkInitializer);
        }
    }

    /**
     * Get the instance of the currently running network manager.
     *
     * @return The active network manager.
     */
    public Network getNetwork() {
        return network;
    }

    /**
     * Deinitialize the network.
     */
    public void deinitializeNetwork() {
        if (network != null) {
            network.deinitialize();
        }
    }

    /**
     * Initialize the graphics module.
     *
     * @param title      The title of the window to create.
     * @param width      The width of the window. (This is the default for window mode
     *                   fullscreen will be set automatically)
     * @param height     The height of the window. (This is the default for window mode
     *                   fullscreen will be set automatically)
     * @param fullscreen Weather to start in fullscreen or not.
     * @return A game graphics.
     */
    public Graphics initializeGraphics(String title, int width, int height,
                                       boolean fullscreen, int fpscap) {
        if (graphics != null || Display.isCreated()) {
            throw new IllegalStateException(
                    "You can only initialize one display module at a time.");
        }
        if (!isLoaded) {
            throw new IllegalStateException(
                    "Display module is dependant on the core module. Initialize it before initializing this.");
        }
        graphics = Graphics.create(this, title, width, height, fullscreen);
        graphics.setFPS(fpscap);

        return graphics;
    }

    /**
     * Deinitialize the graphics module.
     */
    public void deinitializeGraphics() {
        if (graphics != null) {
            graphics.deinit();
            graphics = null;
        }
    }

    /**
     * Get the game display.
     *
     * @return The current display or null if the display module is not
     * initialized.
     */
    public Graphics getDisplay() {
        return graphics;
    }

    /**
     * Initialize the model manager.
     *
     * @return An initialized ModelManager.
     */
    public ModelManager initializeModelManager() {
        if (modelManager != null) {
            throw new IllegalStateException(
                    "You can only initialize one modelmanager module at a time.");
        }
        if (graphics == null) {
            throw new IllegalStateException(
                    "ModelManager is dependant on the display module. Initialize it before initializing this.");
        }
        modelManager = new ModelManager();
        return modelManager;
    }

    /**
     * Get the model manager.
     *
     * @return
     */
    public ModelManager getModelManager() {
        return modelManager;
    }

    /**
     * Deinitialize the mode manager.
     */
    public void deinitializeModelManager() {
        if (modelManager != null) {
            modelManager.clear();
            modelManager = null;
        }
    }

    /**
     * Get the settings.
     *
     * @return The current settings.
     */
    public GameSettings getSettings() {
        return settings;
    }

    /**
     * Set the game settings.
     *
     * @param settings The settings to set as current.
     */
    public void setSettings(GameSettings settings) {
        this.settings = settings;
    }
}
