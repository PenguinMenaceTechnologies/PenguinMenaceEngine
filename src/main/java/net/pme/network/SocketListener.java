/**
 * 
 */
package net.pme.network;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * Listens on a socket for packets.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
class SocketListener extends Thread {
	/**
	 * The socket to work with.
	 */
	private Socket socket;
	/**
	 * The networkmanager.
	 */
	private NetworkManager networkManager;

	/**
	 * Create a new SocketListener.
	 * 
	 * @param networkManager
	 *            The networkmanager with which to cooperate.
	 * @param socket
	 *            The socket of our connection.
	 */
	SocketListener(NetworkManager networkManager, Socket socket) {
		this.networkManager = networkManager;
		this.socket = socket;
	}

	@Override
	public void run() {
		DataInputStream is = null;
		try {
			is = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		while (!this.isInterrupted()) {
			try {
				int id = is.readInt();
				networkManager.retrievePacket(socket, id, is);
			} catch(EOFException e) {
				// We just got closed our socket.
				break;
			} catch(SocketException e) {
				// We just got closed our socket.
				System.out.println("(SocketListener) Socket closed.");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
		try {
			is.close();
			networkManager.removeSocket(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
