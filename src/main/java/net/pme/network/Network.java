/**
 * 
 */
package net.pme.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * A Network Manager.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public class Network {
	/**
	 * A hash map of all registered packets.
	 */
	private HashMap<Long, RegisteredPacket> registeredPackets = new HashMap<Long, RegisteredPacket>();
	/**
	 * A list of all connections.
	 */
	private LinkedList<Socket> connections = new LinkedList<Socket>();
	/**
	 * A List of all socket listeners.
	 */
	private LinkedList<SocketListener> socketListeners = new LinkedList<SocketListener>();
	/**
	 * A connection listener if we are in server mode otherwise null.
	 */
	private NetworkConnectionListener connectionListener = null;

	/**
	 * Create a new Network manager with the given initializer.
	 * 
	 * @throws IOException
	 *             If initialization does not work.
	 * @throws UnknownHostException
	 *             If the host is unknown.
	 */
	public Network(NetworkInitializer networkInitializer)
			throws UnknownHostException, IOException {
		List<RegisteredPacket> tmp = networkInitializer.getRegisteredPackets();
		for (RegisteredPacket rp : tmp) {
			registeredPackets.put(rp.getId(), rp);
		}
		if (networkInitializer.isServer()) {
			connectionListener = new NetworkConnectionListener();
			connectionListener.initialize(this, networkInitializer.getPort());
			connectionListener.start();
		}
		if (networkInitializer.getHost() != null) {
			Socket s = new Socket(networkInitializer.getHost(),
					networkInitializer.getPort());
			SocketListener sl = new SocketListener(this, s);
			socketListeners.add(sl);
			connections.add(s);
			sl.start();
		}
	}

	/**
	 * Deinitialize the network manager.
	 */
	public void deinitialize() {
		if (connectionListener != null) {
			connectionListener.interrupt();
		}
		while(!connections.isEmpty()) {
			try {
				removeSocket(connections.peek());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Send a packet to all connections.
	 * 
	 * @param p
	 *            The packet to send to all.
	 * @throws IOException
	 *             When writing the package does not work.
	 */
	public void sendAll(Packet p) throws IOException {
		for (Socket s : connections) {
			send(s, p);
		}
	}

	/**
	 * Send a packet to a specific socket.
	 * 
	 * @param s
	 *            The socket to send the packet to.
	 * @param p
	 *            The packet to send.
	 * @throws IOException
	 *             When writing the package does not work.
	 */
	public void send(Socket s, Packet p) throws IOException {
		DataOutputStream os = new DataOutputStream(s.getOutputStream());
		os.writeLong(p.getId());
		p.send(os);
	}

	/**
	 * Add a socket to the network manager.
	 * 
	 * @param s
	 *            The socket to add.
	 */
	void addSocket(Socket s) {
		if (connections.contains(s))
			return;
		SocketListener sl = new SocketListener(this, s);
		socketListeners.add(sl);
		connections.add(s);
		sl.start();
	}

	/**
	 * Remove a socket from the network manager.
	 * 
	 * @param s
	 *            The socket to remove.
	 * @throws IOException
	 *             When the socket cannot be closed for some reason.
	 */
	void removeSocket(Socket s) throws IOException {
		if (s == null) return;
		connections.remove(s);
		if (!s.isClosed()) {
			s.close();
		}
	}

	/**
	 * Retrieve a packet with the given id.
	 * 
	 * @param id
	 *            The id of the packet.
	 * @param is
	 *            The input stream.
	 * @throws IOException
	 *             When reading the packet does not work.
	 */
	void retrievePacket(Socket socket, long id, DataInputStream is)
			throws IOException {
		if (registeredPackets.containsKey(id)) {
			throw new IOException("Invalid packet id");
		}
		RegisteredPacket rp = registeredPackets.get(id);
		rp.retrievePacket(rp.create(is, socket));
	}
}
