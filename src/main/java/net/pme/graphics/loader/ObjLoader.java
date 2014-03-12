package net.pme.graphics.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;

import java.util.List;
import java.util.ArrayList;

import net.pme.graphics.Model;
import net.pme.graphics.data.VertexData;

/**
 * @author Johannes Schuck <jojoschuck@googlemail.com>
 * @version 0.1
 * @since 2014-03-12
 */
public class ObjLoader {

    public static Model loadObj(final String path) throws IOException {
        return loadObj(path, false);
    }

    public static Model loadObj(final String path, boolean flipZ) {
        FileInputStream fis;
        try {
            fis = new FileInputStream (path);
        } catch (FileNotFoundException e) {
            System.out.println("File not found:" + path);
            return null;
        }

        System.out.println("Warning: No full support for obj files yet!");

        BufferedReader reader = new BufferedReader(new InputStreamReader(fis), 4096);
        String line; String tokens[];

        List<Float> vertices = new ArrayList<Float>();

        try {
            while ((line = reader.readLine()) != null) {
                // Escape all whitespace characters. The \\s is equivalent to [ \\t\\n\\x0B\\f\\r]
                tokens = line.split("\\s+");
                if (tokens.length < 1)
                    break;

                char first = tokens[0].toLowerCase().charAt(0);

                if (first == '#') {
                    continue;
                } else if (first == 'v') {
                    // parse vertex data
                    if (tokens[0].length() == 1) {
                        if (tokens.length == 4) {
                            vertices.add(Float.parseFloat(tokens[1]));
                            vertices.add(Float.parseFloat(tokens[2]));
                            vertices.add(Float.parseFloat(tokens[3]));
                        } else if (tokens.length == 5) {

                        } else {

                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Invalid float value found!");
            return null;
        }

        return null;
    }
}
