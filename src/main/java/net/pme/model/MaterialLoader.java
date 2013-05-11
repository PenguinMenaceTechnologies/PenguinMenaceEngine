/**
 * 
 */
package net.pme.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * A helper class to load materials defined in a mtl file.
 * @author Michael Fürst
 * @version 1.0
 */
class MaterialLoader {
	/**
	 * Create a material from a file.
	 * 
	 * @param f The file.
	 * @return 
	 * @throws IOException
	 */
	static void loadMaterials(File f, HashMap<String,Material> map) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line;
		while ((line = reader.readLine()) != null) {
			// parse the mtl file
			if (line.startsWith("#")) {
				// Ignore comments
			} else if (line.startsWith("mtllib ")) {
				String[] splitline = line.split(" ");
				String mtl = splitline[1];
			}
		}
		reader.close();
	}
}
