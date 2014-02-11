/**
 * 
 */
package net.pme.network;

import java.util.List;

/**
 * A Network initializer. It is recommendet to implement this one hardcoded and
 * use the same implementation on servers and clients, due to it's restrictions.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public interface NetworkInitializer {

	/**
	 * A list of all packets that can be transmitted by the game. Important:
	 * This list must be in the same order on all instances of the game.
	 * 
	 * @return The list of all packets.
	 */
	public List<RegisteredPacket> getRegisteredPackets();

	/**
	 * The hostname or null.
	 * 
	 * @return A string containing the hostname or null if you do not want to
	 *         connect to a server.
	 */
	public String getHost();

	/**
	 * The port.
	 * 
	 * @return A int containing the port.
	 */
	public int getPort();

	/**
	 * Returns if this is a server. This causes the networkmanager to launch a
	 * connection listener or not.
	 * 
	 * @return True if this is a server.
	 */
	public boolean isServer();
}
