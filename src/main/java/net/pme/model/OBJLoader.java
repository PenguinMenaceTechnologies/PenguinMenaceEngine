package net.pme.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.lwjgl.util.vector.Vector3f;

/**
 * Reads an obj out of a given File.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
class OBJLoader {
	/**
	 * Load a model from a file.
	 * 
	 * @param f
	 *            The file where the model is stored in.
	 * @return The model.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	static Model loadModel(File f) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		Model m = new Model();
		String line;
		while ((line = reader.readLine()) != null) {
			// parse the obj file
			if (line.startsWith("#")) {
				// Ignore comments
			} else if (line.startsWith("mtllib ")) {
				String[] splitline = line.split(" ");
				String mtl = splitline[1];
				MaterialLoader.loadMaterials(new File(f.getParent()+"/"+mtl), m.mtllibs);
			} else if (line.startsWith("usemtl ")) {
				String[] splitline = line.split(" ");
				String mtl = splitline[1];
				m.mtls.put(m.faces.size(),mtl);
			} else if (line.startsWith("s ")) {
				String[] splitline = line.split(" ");
				String smoothing = splitline[1];
				m.smoothing.put(m.faces.size(),smoothing);
			} else if (line.startsWith("v ")) {
				String[] splitline = line.split(" ");
				float x = Float.parseFloat(splitline[1]);
				float y = Float.parseFloat(splitline[2]);
				float z = Float.parseFloat(splitline[3]);
				m.vertices.add(new Vector3f(x, y, z));
			} else if (line.startsWith("vn ")) {
				String[] splitline = line.split(" ");
				float x = Float.parseFloat(splitline[1]);
				float y = Float.parseFloat(splitline[2]);
				float z = Float.parseFloat(splitline[3]);
				m.normals.add(new Vector3f(x, y, z));
			} else if (line.startsWith("vt ")) {
				String[] splitline = line.split(" ");
				float x = Float.parseFloat(splitline[1]);
				float y = Float.parseFloat(splitline[2]);
				float z = 0;
				if (splitline.length >= 4) {
					z = Float.parseFloat(splitline[3]);
				}
				m.textureCoords.add(new Vector3f(x, y, z));
			} else if (line.startsWith("vp ")) {
				String[] splitline = line.split(" ");
				float x = Float.parseFloat(splitline[1]);
				float y = 0;
				if (splitline.length >= 3) {
					y = Float.parseFloat(splitline[2]);
				}
				float z = 0;
				if (splitline.length >= 4) {
					z = Float.parseFloat(splitline[3]);
				}
				m.spaceVertices.add(new Vector3f(x, y, z));
			} else if (line.startsWith("f ")) {
				Vector3f vertexIndicies = new Vector3f(Float.parseFloat(line
						.split(" ")[1].split("/")[0]), Float.parseFloat(line
						.split(" ")[2].split("/")[0]), Float.parseFloat(line
						.split(" ")[3].split("/")[0]));
				Vector3f textureIndicies = new Vector3f(Float.parseFloat(line
						.split(" ")[1].split("/")[1]), Float.parseFloat(line
						.split(" ")[2].split("/")[1]), Float.parseFloat(line
						.split(" ")[3].split("/")[1]));
				Vector3f normalIndicies = new Vector3f(Float.parseFloat(line
						.split(" ")[1].split("/")[2]), Float.parseFloat(line
						.split(" ")[2].split("/")[2]), Float.parseFloat(line
						.split(" ")[3].split("/")[2]));
				m.faces.add(new Face(vertexIndicies, textureIndicies, normalIndicies));
			}
		}
		reader.close();
		return m;
	}
}
