package net.pme.core.utils;

/**
 * Check on which operating system the game runs.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public final class OSValidator {
    /**
     * Utility class.
     */
    private OSValidator() {

    }

    /**
     * Detect a windows.
     *
     * @return True, iff on windows.
     */
    public static boolean isWindows() {

        String os = System.getProperty("os.name").toLowerCase();
        // windows
        return (os.indexOf("win") >= 0);

    }

    /**
     * Detect a mac.
     *
     * @return True, iff on mac.
     */
    public static boolean isMac() {

        String os = System.getProperty("os.name").toLowerCase();
        // Mac
        return (os.indexOf("mac") >= 0);

    }

    /**
     * Detect a unix.
     *
     * @return True, iff on unix.
     */
    public static boolean isUnix() {

        String os = System.getProperty("os.name").toLowerCase();
        // linux or unix
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);

    }
}
