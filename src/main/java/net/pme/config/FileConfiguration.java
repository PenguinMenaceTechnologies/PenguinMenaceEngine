package net.pme.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public final class FileConfiguration extends MemoryConfiguration {
	private File file;
	
	public FileConfiguration() {
		this(null);
	}
	
	public FileConfiguration(File file) {
		this.file = file;
	}
	
	public static FileConfiguration parseFromFile(File file) {
		FileConfiguration configuration = new FileConfiguration(file);
		
		if (file == null || !file.exists())
			return configuration;
		
		try {			
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String line = reader.readLine();
			while (line != null) {
				String key, value;
				int i = line.indexOf("=");
				
				if (i > 0) {
					key = line.substring(0, i);
					value = line.substring(i + 1);
					
					configuration.set(key, value);
				}

				line = reader.readLine();
			}
			
			reader.close();
		} catch (IOException e) {
			//Well Crap!
		}
		
		return configuration;
	}
	
	public boolean save() {
		return save(file);
	}
	
	public boolean save(File file) {
		if (file == null)
			return false;

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
			
			Set<String> keySet = values.keySet();
			for (String key : keySet) {
				String value = values.get(key);
				
				writer.write(key);
				writer.write('=');
				writer.write(value);
				writer.newLine();
				writer.flush();
			}
			
			writer.close();
		} catch (IOException e) {
			return false;			
		}
				
		return true;
	}
}