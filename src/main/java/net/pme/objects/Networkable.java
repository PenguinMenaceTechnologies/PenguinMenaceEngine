package net.pme.objects;

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
	public void retrievePacket(Packet p);
}
