package net.pme.graphics.loader;

import net.pme.graphics.Model;
import net.pme.graphics.data.VertexData;

import java.io.*;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;

/**
 * @author Johannes Schuck <jojoschuck@googlemail.com>
 * @version 0.1
 * @since 2014-03-12
 */
public class ObjLoader {
    public static ArrayList<VertexData> models;

    public static Model loadObj(final String path) throws IOException {
        return loadObj(path, false);
    }

    public static Model loadObj(final String path, boolean flipZ) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            System.out.println("File not found:" + path);
            return null;
        }

        System.out.println("Warning: No full support for obj files yet!");

        BufferedReader reader = new BufferedReader(new InputStreamReader(fis), 4096);
        String line;
        String tokens[];

        models = new ArrayList<VertexData>();
        int currentObject = 0;

        // TODO: actually check this constructor args

        models.add(new VertexData(true, false, true));

        try {
            int currentLine = 0;
            while ((line = reader.readLine()) != null) {
                // Escape all whitespace characters. The \\s is equivalent to [ \\t\\n\\x0B\\f\\r]
                tokens = line.split("\\s+");
                if (tokens.length < 1)
                    break;

                if (tokens[0].length() == 0)
                    continue;

                char first = tokens[0].toLowerCase().charAt(0);
                // ignore groups for now
                if (first == '#' || first == 'g')
                    continue;

                if (first == 'v' && tokens.length > 2) {
                    float x = Float.parseFloat(tokens[1]);
                    float y = Float.parseFloat(tokens[2]);

                    if (tokens[0].length() > 1 && tokens[0].charAt(1) == 't') {
                        models.get(currentObject).addTexCoord(x, y);
                    }

                    if (tokens.length < 4) {
                        throw new InvalidPropertiesFormatException("Vertex Data must have 3 or 4 values.");
                    }

                    float z = Float.parseFloat(tokens[3]);

                    if (tokens[0].length() == 1) {
                        if (tokens.length == 4) {
                            models.get(currentObject).addVertex(x, y, z);
                        } else if (tokens.length == 5) {
                            models.get(currentObject).addVertex(x, y, z, Float.parseFloat(tokens[4]));
                        }
                    } else if (tokens[0].charAt(1) == 'n') {
                        if (!flipZ) {
                            models.get(currentObject).addNormal(x, y, z);
                        } else {
                            models.get(currentObject).addNormal(x, y, -z);
                        }
                    }
                } else if (first == 'f') {
                    int faceVertices = tokens.length - 1;
                    if (faceVertices < 3) {
                        throw new InvalidPropertiesFormatException("Faces must at least have 3 vertices.");
                    }
                    int face[] = new int[faceVertices * 3];
                    String[] currentFace;
                    for (int i = 0; i < faceVertices; i++) {
                        currentFace = tokens[i + 1].split("/");
                        face[i * 3] = Integer.parseInt(currentFace[0]);
                        face[i * 3 + 1] = Integer.parseInt(currentFace[1]);
                        face[i * 3 + 2] = Integer.parseInt(currentFace[2]);
                    }
                    models.get(currentObject).addFace(face);
                } else if (first == 'o') {
                    if (tokens.length > 1) {
                        models.add(new VertexData(true, false, true));
                        currentObject++;
                    }
                } else if (tokens[0].equals("mtllib")) {
                    loadMtl(tokens[1]);
                } /*else if (tokens[0].equals("usemtl")) {
                    if (tokens.length == 1)
                        activeGroup.materialName = "default";
                    else
                        activeGroup.materialName = tokens[1];
                }*/
                currentLine++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Invalid float value(s) found!");
            return null;
        }

        return null;
    }

    private static void loadMtl(String path) {

    }
}
