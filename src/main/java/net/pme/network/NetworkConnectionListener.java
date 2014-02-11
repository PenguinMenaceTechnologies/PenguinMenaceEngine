/**
 * 
 */
package net.pme.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

/**
 * A connection listener. This listenes for incoming connections.
 * 
 * @author Michael Fürst
 * @version 1.0
 */
public class NetworkConnectionListener extends Thread {
	/**
	 * The networkmanager where to add the sockets to.
	 */
	private NetworkManager networkManager;
	/**
	 * The port on which we run.
	 */
	private int port;
	/**
	 * The server socket we use, for interrupting.
	 */
	private ServerSocket serverSocket;

	/**
	 * Initialize the connection listener.
	 * 
	 * @param networkManager
	 *            The network manager where to add new connections.
	 * @param port
	 *            The port on which to listen.
	 */
	public void initialize(NetworkManager networkManager, int port) {
		this.networkManager = networkManager;
		this.port = port;
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while (!this.isInterrupted()) {
			try {
				networkManager.addSocket(serverSocket.accept());
			} catch (SocketException e) {
			    // We cannot connect to that socket, so wayne.
				System.out.println("(NetworkConnectionListener) Socket closed.");
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void interrupt() {
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.interrupt();
	}
}
