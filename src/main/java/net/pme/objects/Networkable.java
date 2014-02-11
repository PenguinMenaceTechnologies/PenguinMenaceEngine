package net.pme.objects;

import net.pme.network.Packet;

/**
 * All classes that are capable of retrieving packets via network.
 * 
 * This interface does not differ between UDP and TCP.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public interface Networkable {
	/**
	 * Retrieve a packet.
	 * 
	 * @param p
	 *            The packet to retrieve.
	 */
	void retrievePacket(Packet p);
}
