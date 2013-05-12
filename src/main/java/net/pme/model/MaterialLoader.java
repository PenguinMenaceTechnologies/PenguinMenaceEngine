/**
 * 
 */
package net.pme.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import net.pme.utils.FileFormatException;

import org.lwjgl.util.vector.Vector3f;

/**
 * A helper class to load materials defined in a mtl file.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
class MaterialLoader {
	/**
	 * Create a material from a file.
	 * 
	 * @param f
	 *            The file.
	 * @return
	 * @throws IOException
	 */
	static void loadMaterials(File f, HashMap<String, Material> map)
			throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line;
		Material mtl = null;
		int lineNumber = 0;
		while ((line = reader.readLine()) != null) {
			try {
				// parse the mtl file
				if (line.startsWith("#")) {
					// Ignore comments
				} else if (line.startsWith("newmtl ")) {
					String[] splitline = line.split(" ");
					if (mtl != null) {
						map.put(mtl.name, mtl);
					}
					mtl = new Material(splitline[1]);
				} else if (line.startsWith("Ka ")) {
					String[] splitline = line.split(" ");
					float r = Float.parseFloat(splitline[1]);
					float g = Float.parseFloat(splitline[2]);
					float b = Float.parseFloat(splitline[3]);
					mtl.ambient = new Vector3f(r, g, b);
				} else if (line.startsWith("Kd ")) {
					String[] splitline = line.split(" ");
					float r = Float.parseFloat(splitline[1]);
					float g = Float.parseFloat(splitline[2]);
					float b = Float.parseFloat(splitline[3]);
					mtl.diffuse = new Vector3f(r, g, b);
				} else if (line.startsWith("Ks ")) {
					String[] splitline = line.split(" ");
					float r = Float.parseFloat(splitline[1]);
					float g = Float.parseFloat(splitline[2]);
					float b = Float.parseFloat(splitline[3]);
					mtl.specular = new Vector3f(r, g, b);
				} else if (line.startsWith("Ns ")) {
					String[] splitline = line.split(" ");
					mtl.specularCoefficient = Float.parseFloat(splitline[1]);
				} else if (line.startsWith("d ") || line.startsWith("Tr ")) {
					String[] splitline = line.split(" ");
					mtl.transparency = Float.parseFloat(splitline[1]);
				} else if (line.startsWith("illum ")) {
					String[] splitline = line.split(" ");
					mtl.illuminationModel = Integer.parseInt(splitline[1]);
				} else if (line.startsWith("map_Ka ")) {
					String[] splitline = line.split(" ");
					mtl.ambientMap = new Texture(f.getParent() + "/"
							+ splitline[1]);
				} else if (line.startsWith("map_Kd ")) {
					String[] splitline = line.split(" ");
					mtl.diffuseMap = new Texture(f.getParent() + "/"
							+ splitline[1]);
				} else if (line.startsWith("map_Ks ")) {
					String[] splitline = line.split(" ");
					mtl.specularMap = new Texture(f.getParent() + "/"
							+ splitline[1]);
				} else if (line.startsWith("map_Ns ")) {
					String[] splitline = line.split(" ");
					mtl.specularHighlightMap = new Texture(f.getParent() + "/"
							+ splitline[1]);
				} else if (line.startsWith("map_bump ")
						|| line.startsWith("bump ")) {
					String[] splitline = line.split(" ");
					mtl.bumpMap = new Texture(f.getParent() + "/"
							+ splitline[1]);
				} else if (line.startsWith("disp ")) {
					String[] splitline = line.split(" ");
					mtl.displacementMap = new Texture(f.getParent() + "/"
							+ splitline[1]);
				} else if (line.startsWith("decal ")) {
					String[] splitline = line.split(" ");
					mtl.decalMap = new Texture(f.getParent() + "/"
							+ splitline[1]);
				}
			} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
				reader.close();
				throw new FileFormatException("Invalid mtl file format.",
						f.getAbsolutePath(), lineNumber);
			}
			lineNumber++;
		}
		if (mtl != null) {
			map.put(mtl.name, mtl);
		}
		reader.close();
	}
}
