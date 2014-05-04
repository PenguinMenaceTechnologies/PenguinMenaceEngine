/**
 *
 */
package net.pme.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * A registered package.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public abstract class RegisteredPacket {
    private ArrayList<Networkable> listeners = new ArrayList<Networkable>();

    /**
     * Create a packet from the buffered reader.
     *
     * @param inputStream The input stream.
     * @param socket
     * @return The packet that was read from the stream.
     */
    public abstract Packet create(DataInputStream inputStream, Socket socket)
            throws IOException;

    /**
     * Create a packet of the given type. With the correct id set.
     *
     * @return The Packet to create.
     */
    public abstract Packet create();

    /**
     * Get the id of the packet.
     *
     * @return The id of the packet.
     */
    public abstract long getId();

    /**
     * Add a listener to this packet.
     *
     * @param n The networkable to add.
     */
    public void addListener(Networkable n) {
        listeners.add(n);
    }

    /**
     * Remove a listener from this packet.
     *
     * @param n The networkable to remove.
     */
    public void removeListener(Networkable n) {
        listeners.remove(n);
    }

    /**
     * Clear the list of listeners.
     */
    public void clearListeners() {
        listeners.clear();
    }

    /**
     * Retrieve a packet.
     *
     * @param p The packet to retrieve.
     */
    void retrievePacket(Packet p) {
        for (Networkable n : listeners) {
            n.retrievePacket(p);
        }
    }

}
