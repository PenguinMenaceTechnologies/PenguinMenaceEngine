package net.pme.config;

import java.util.HashMap;

public class MemoryConfiguration {
	protected HashMap<String, String> values;
	
	public MemoryConfiguration() {
		this.values = new HashMap<String, String>();
	}
	
	public String getString(String key) {
		return getString(key, null);
	}
	
	public String getString(String key, String def) {
		String res = values.get(key);
		
		if (res == null)
			res = def;
		
		return res;
	}
	
	public int getInteger(String key) {
		return getInteger(key, 0);
	}
	
	public int getInteger(String key, int def) {
		String res = values.get(key);
		if (res == null)
			return def;
		
		try {
			return Integer.parseInt(res);
		} catch (NumberFormatException e) {
			return def;
		}
	}
	
	public boolean getBoolean(String key, boolean def) {
		if (!isSet(key))
			return def;
		
		return getBoolean(key);
	}
	
	public boolean getBoolean(String key) {
		String res = values.get(key);
		
		return Boolean.parseBoolean(res);
	}
	
	public boolean isSet(String key) {
		return values.containsKey(key);
	}
	
	public void set(String key, Object o) {
		if (o == null)
			return;
		
		values.put(key, o.toString());
	}
	
	public void remove(String key) {
		values.remove(key);
	}

	public void setMap(MemoryConfiguration m) {
		values = m.values;
	}
}