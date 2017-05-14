package mail.server;

import java.io.*;
import java.net.*;
import java.util.*;



/**
 * @author Quang Ngo TP
 *
 */
public class SMTPServer extends Thread{
	private ServerSocket socket = null;
	private Vector connections;
	public Boolean stopRequested;
	
	public SMTPServer() throws Exception{
		int port = 0;
		String portString = null;
		// Get port number form properties file
		try {
			portString = Server.properties.getProperty("smtp.port");
			port = Integer.parseInt(portString);
		} catch (NumberFormatException e){
			throw new Exception("Invalid 'smpt.port' - " + portString);
		}
		// Open socket to wait for connection from client
		socket = new ServerSocket(port);
		// Vector stores connections from clients
		connections =  new Vector(10, 10);
	}
	
	public void removeConnection(SMTPConnection connection){
		connections.remove(connection);
	}
	
	public void run(){
		System.out.println("SMTPServer is running...");
		stopRequested = new Boolean(false);
		while (true){
			synchronized(stopRequested){
				if (stopRequested.booleanValue()){
					break;
				}
				// Accept connection
				try {
					Socket s = socket.accept();
					// Analyze SMTP commands from client and receive message
					SMTPConnection connection = new SMTPConnection(s, this);
					connections.addElement(connection);
					connection.start();
				} catch (IOException e) {
					System.out.println(e.getMessage());
					break;
				}
			}
			// Close connection
			try {
				socket.close();
			} catch (IOException e){
				System.out.println(e.getMessage());
			}
		}
	}
}
