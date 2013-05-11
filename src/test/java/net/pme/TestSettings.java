package net.pme;

import java.util.HashMap;

import net.pme.GameSettings;

/**
 * The settings for the test.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public class TestSettings extends GameSettings {

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.pme.GameSettings#setDefaultKeyMapping()
	 */
	@Override
	protected HashMap<String, Integer> setDefaultKeyMapping() {
		return new HashMap<String, Integer>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.pme.GameSettings#setDefaultLibraryPath()
	 */
	@Override
	protected String setDefaultLibraryPath() {
		return null;
	}

}
