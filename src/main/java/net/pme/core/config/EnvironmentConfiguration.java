package net.pme.core.config;

import net.pme.core.GameObject;
import net.pme.core.math.Vector3d;
import net.pme.graphics.RenderAttachment;
import net.pme.model.Model;
import net.pme.model.ModelManager;

import java.io.File;
import java.io.IOException;

/**
 * An environment configuration.
 *
 * Like a map only that you have to generate game objects from this config.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public class EnvironmentConfiguration {
    private static int CURRENT_ID = 1000;
    private final FileConfiguration config;

    /**
     * Create an environment configuration using the defaut file.
     */
    public EnvironmentConfiguration() {
        this("config/environment.config");
    }

    /**
     * Create an environment configuration from a file.
     * @param path The file to parse.
     */
    public EnvironmentConfiguration(final String path) {
        File file = new File(path);
        config = FileConfiguration.parseFromFile(file);
    }

    /**
     * Create an object from the config by using its name.
     * @param name The name of the model to load.
     * @param modelManager The model manager to use for loading the graphics.
     * @param callee The callee to determine the search location for the model.
     * @return The game object with this model as a render attachment.
     * @throws IOException
     */
    public GameObject createObjectByName(final String name, final ModelManager modelManager, final Class callee) throws IOException {
        Model m = modelManager.get(config.getString(name+"_path"), callee);
        double x = config.getDouble(name + "_pos_x");
        double y = config.getDouble(name + "_pos_y");
        double z = config.getDouble(name + "_pos_z");
        Vector3d pos = new Vector3d(x,y,z);
        x = config.getDouble(name + "_front_x",0);
        y = config.getDouble(name + "_front_y",0);
        z = config.getDouble(name + "_front_z",1);
        Vector3d front = new Vector3d(x,y,z);
        x = config.getDouble(name + "_up_x",0);
        y = config.getDouble(name + "_up_y",1);
        z = config.getDouble(name + "_up_z",0);
        Vector3d up = new Vector3d(x,y,z);

        GameObject o = new GameObject(CURRENT_ID++, pos, front, up) {};
        o.setRenderAttachment(new RenderAttachment(o, m));
        return o;
    }

    /**
     * Create all models specified in the all_models tag in the config.
     * @param modelManager The model manager to use for loading the graphics.
     * @param callee The callee to determine the search location for the model.
     * @return An array containing all game objects.
     * @throws IOException
     */
    public final GameObject[] createAllObjects(final ModelManager modelManager, final Class callee) throws IOException {
        String models = config.getString("all_models");
        if (models == null || models.equals("")) {
            return new GameObject[0];
        }
        String[] modelList = models.split(",");
        GameObject[] result = new GameObject[modelList.length];
        for (int i = 0; i < modelList.length; i++) {
            result[i] = createObjectByName(modelList[i], modelManager, callee);
        }
        return result;
    }

    /**
     * Add an object to the map.
     * @param name The name to give the model.
     * @param o The object.
     * @return The name that was actually given after resolving conflicts.
     */
    synchronized public String addObjectToMap(String name, GameObject o) {
        String models = config.getString("all_models");
        if (models != null) {
            String[] modelList = models.split(",");
            for (String s : modelList) {
                if (name.equals(s)) {
                    return addObjectToMap(name + "1", o);
                }
            }
        }

        config.set(name+"_path", o.getRenderAttachment().getModel().getPath());
        config.set(name + "_pos_x", o.getPosition().getX());
        config.set(name + "_pos_y", o.getPosition().getY());
        config.set(name + "_pos_z", o.getPosition().getZ());
        config.set(name + "_front_x", o.getFront().getX());
        config.set(name + "_front_y", o.getFront().getY());
        config.set(name + "_front_z", o.getFront().getZ());
        config.set(name + "_up_x", o.getUp().getX());
        config.set(name + "_up_y", o.getUp().getY());
        config.set(name + "_up_z", o.getUp().getZ());
        if (models != null) {
            config.set("all_models", models + "," + name);
        } else {
            config.set("all_models", name);
        }

        return name;
    }

    /**
     * Save the current state.
     */
    public void save() {
        config.save();
    }
}
