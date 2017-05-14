package mail.server;

import java.io.*;
import java.net.*;
import java.util.*;



/**
 * @author Quang Ngo TP
 *
 */
public class POPServer extends Thread{
	public Vector connections;
	public Boolean stopRequested;
	protected ServerSocket socket;

	public POPServer() throws Exception {
		int port = 0;
		String portString = null;
		// Get port number from properties
		try {
			portString = Server.properties.getProperty("pop.port");
			port = Integer.parseInt(portString);
		} catch (NumberFormatException e) {
			throw new Exception("Invalid 'pop.port' ­ " + portString);
		}
		// Wait for connect from client on this port
		socket = new ServerSocket(port); 
		// Create vector to store connections
		connections = new Vector(10, 10);
	}

	public void removeConnection(POPConnection connection) {
		connections.remove(connection);
	}
	
	public void run() {
		System.out.println("POPServer is running...");
		stopRequested = new Boolean(false);
		while (true) {
			synchronized (stopRequested) {
				if (stopRequested.booleanValue())
					break;
			}
			// Accept request from client
			try {
				Socket s = socket.accept();
				// Processing
				POPConnection connection = new POPConnection(s, this);
				connections.addElement(connection);
				connection.setDaemon(true);
				connection.start();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
		// Close connection
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
