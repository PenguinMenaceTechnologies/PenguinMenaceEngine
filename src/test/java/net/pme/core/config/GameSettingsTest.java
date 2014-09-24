package net.pme.core.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.io.File;


/**
 * Tests for the gamesettings.
 * @author Michael Fürst
 * @version 1.0
 */
public class GameSettingsTest {

    private static String configPath = "config/test_settings.config";
    private static String keyPath = "config/test_keymapping.config";
    private static MemoryConfiguration x = new MemoryConfiguration();

    GameSettings gs;

    @Before
    public void before() {
        x.set("foo", "bar");
        gs = new GameSettings() {
            @Override
            protected MemoryConfiguration setDefaultKeyMapping() {
                MemoryConfiguration k = new MemoryConfiguration();
                k.set("w", 1);
                return k;
            }

            @Override
            protected MemoryConfiguration setDefaultSettings() {
                return x;
            }
        };
        gs.setSettingsPath(configPath);
        gs.setKeyMappingPath(keyPath);
    }

    @Test
    public void testSettings() {
        Assert.assertEquals("Not valid", "bar", gs.getSettings().getString("foo"));
    }

    @Test
    public void testKeyMap() {
        Assert.assertEquals("Not valid", 1, gs.getKeyMapping().getInteger("w"));
    }

    @After
    public void after() {
        File f = new File(configPath);
        if (f.exists()) {
            f.delete();
        }
        f = new File(keyPath);
        if (f.exists()) {
            f.delete();
        }
    }
}
