package net.pme;

import net.pme.core.config.GameSettings;
import net.pme.core.config.MemoryConfiguration;

/**
 * The settings for the test.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public class TestSettings extends GameSettings {

    @Override
    protected MemoryConfiguration setDefaultKeyMapping() {
        return new MemoryConfiguration();
    }

    @Override
    protected MemoryConfiguration setDefaultSettings() {
        MemoryConfiguration res = new MemoryConfiguration();
        res.set("test", "a test config");
        res.set("test2", "foobar");
        return res;
    }

}
