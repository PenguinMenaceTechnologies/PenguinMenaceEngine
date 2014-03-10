package net.pme.network;

import java.io.DataOutputStream;
import java.net.Socket;

import net.pme.core.GameObject;

/**
 * A network packet.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public class Packet {
	private Socket sender;

	/**
	 * A new packet.
	 * 
	 * Used for incoming packets.
	 *
	 * @param sender
	 *            The sender socket of this packet.
	 */
	Packet(Socket sender) {
		this.sender = sender;
	}

	/**
	 * A new packet.
	 * 
	 * Used for outgoing packets.
	 * 
	 * @param id
	 *            The id.
	 */
	Packet(final long id) {
		this(null);
	}

	/**
	 * Get the sender of this packet.
	 * 
	 * @return The sender of this packet.
	 */
	public final Socket getSender() {
		return sender;
	}

	/**
	 * Send a packet on the given DataOutputStream.
	 * 
	 * @param os
	 *            The DataOutputStream to write onto.
	 */
	public void send(DataOutputStream os) {

	}

    public int getId() {
        return 0;
    }
}
