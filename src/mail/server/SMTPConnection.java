package mail.server;

import java.net.*;
import java.util.*;
import java.io.*;

/**
 * @author Quang Ngo TP
 *
 */
public class SMTPConnection extends Thread{
	protected SMTPServer server;
	protected Socket socket;
	protected Vector recipients;
	public BufferedReader in;
	public PrintStream out;
	public String returnPath;
	protected Vector users;
	protected boolean stopRequested;
	

	public SMTPConnection(Socket socket, SMTPServer smtpServer) {
		super();
		this.socket = socket;
		this.server = smtpServer;
	}
	
	// Close connection, finish receiving mail process from client
	public void close() {
		try {
			socket.close();
			System.out.println("Close SMTP Connection!");
			Server.serverGUIFrame.dispose();
		} catch (Exception e) {
			System.err.println("Exception trying to close SMTPConnection socket!");
			e.printStackTrace(System.err);
		}
	}
	
	// Process SMTP DATA command
	public void processDATA() throws IOException {
		String line;
		StringBuffer data = new StringBuffer();
		line = in.readLine();
		while (!line.equals(".")) {
			if (line.startsWith(".."))
				line = line.substring(1);
			// Add receiving string to string buffer
			data.append(line + "\n");
			out.println("OK");
			line = in.readLine();
		}
		// Save message to file
		System.out.println("Saving message!");
		System.out.println(data.toString());
		String messageId = Server.storage.saveMessage(users, returnPath, data);
		System.out.println(messageId);
		// Notify to client that message is already stored
		out.println("250 Message '" + messageId + "' accepted for delivery");
		System.out.println("250 Message '" + messageId + "' accepted for delivery");
	}
	
	public void processEHLOCommand(StringTokenizer arguments) {
		processHELOCommand(arguments);
	}
	
	public void processHELOCommand(StringTokenizer arguments) {
		if (!arguments.hasMoreTokens()) {
			out.println("501 HELO requires domain address");
			return;
		}
		out.println("250 " + Server.getAddress() + " Hello");
	}
	
	public boolean processMAIL() throws IOException {
		String line = "";
		while (true) {
			line = in.readLine();
//			System.out.println("RECEIVED: " + line);
			if (line.length() < 4) {
//				System.out.println("Loi cho nay: " + line);
				out.println("500 Command Unknown '" + line + "'");
				continue;
			}
			StringTokenizer tokenizer = new StringTokenizer(line);
			String command = tokenizer.nextToken();
			if (command.equalsIgnoreCase("HELO")) {
				processHELOCommand(tokenizer);
				continue;
			}
			if (command.equalsIgnoreCase("EHLO")) {
				processEHLOCommand(tokenizer);
				continue;
			}
			if (command.equalsIgnoreCase("VRFY")) {
				processVRFYCommand(tokenizer);
				continue;
			}
			if (command.equalsIgnoreCase("QUIT")) {
				processQUITCommand(tokenizer);
				return false;
			}
			if (command.equalsIgnoreCase("RCPT") || command.equalsIgnoreCase("DATA")) {
				out.println("503 Bad sequence of commands ­ specify MAIL first");
				continue;
			}
			if (command.equalsIgnoreCase("MAIL")) {
				if (processMAILCommand(tokenizer)) {
					out.println("250 OK");
					return true;
				}
			}
//			System.out.println("Khong phai, loi cho nay: " + line);
			out.println("500 Command Unknown '" + line + "'");
		}
	}

	public boolean processMAILCommand(StringTokenizer arguments) {
		if (!arguments.hasMoreTokens()) {
			out.println("503 Syntax: MAIL FROM:<user>");
			return false;
		}
		returnPath = arguments.nextToken();
		if (returnPath.length() < 5) {
			out.println("503 Syntax: MAIL FROM:<user>");
			return false;
		}
		if (!returnPath.substring(0, 5).equalsIgnoreCase("FROM:")) {
			out.println("503 Syntax: MAIL FROM:<user>");
			return false;
		}
		return true;
	}

	protected void processQUITCommand(StringTokenizer arguments) {
		out.println("221 " + Server.getAddress() + " closing connection");
		stopRequested = true;
	}
	
	public boolean processRCPT() throws IOException {
		users = new Vector(2, 2);
		String line = "";
		while (true) {
			line = in.readLine();
			System.out.println(line);
			if (line.length() < 4) {
				out.println("500 Command Unknown '" + line + "'");
				continue;
			}
			StringTokenizer tokenizer = new StringTokenizer(line);
			String command = tokenizer.nextToken();
			if (command.equalsIgnoreCase("EHLO")) {
				processEHLOCommand(tokenizer);
				continue;
			}
			if (command.equalsIgnoreCase("VRFY")) {
				processVRFYCommand(tokenizer);
				continue;
			}
			if (command.equalsIgnoreCase("RSET")) {
				out.println("250 Reset state");
				return false;
			}
			if (command.equalsIgnoreCase("QUIT")) {
				processQUITCommand(tokenizer);
				return false;
			}
			if (command.equalsIgnoreCase("MAIL")) {
				out.println("503 Sender already specified");
				continue;
			}
			if (command.equalsIgnoreCase("DATA")) {
				if (users.size() == 0) {
					out.println("503 Bad sequence of commands ­ specify RCPT first");
					continue;
				}
				out.println("354 Enter mail, ending with '.' on a line by itself");
				return true;
			}
			if (command.equalsIgnoreCase("RCPT")) {
				processRCPTCommand(tokenizer);
				continue;
			}
			out.println("500 Command Unknown '" + line + "'");
		}
	}

	public void processRCPTCommand(StringTokenizer arguments) {
		if (!arguments.hasMoreTokens()) {
			out.println("501 Syntax: RCPT TO:<address>");
			return;
		}
		String arg = arguments.nextToken();
		if (!arg.substring(0, 3).equalsIgnoreCase("TO:")) {
			out.println("501 Syntax: RCPT TO:<address>");
			return;
		}
		String address;
		try {
			address = arguments.nextToken();
		} catch (Exception e) {
			address = arg.substring(3);
		}
		
		if (address.substring(0, 1).equals("<")
				&& address.substring((address.length() - 1), address.length()).equals(">")) {
			address = address.substring(1, address.length() - 1);
		}
		User user = Server.storage.getUser(address);
		if (user == null) {
			out.println("550 User " + address + " is not known");
			return;
		}
		users.addElement(user);
		out.println("250 Recipient " + address + " ok");
	}

	public void processVRFYCommand(StringTokenizer arguments) {
		out.println("252 VRFY command is not implemented");
	}

	public void run() {
		System.out.println("SMTPConnection is running...");
		try {
//			System.out.println("1");
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace(System.err);
			close();
			return;
		}
		stopRequested = false;
		out.println("220 quang.com SMTPServer ready#" + Server.serverPath);
		while (!stopRequested) {
			try {
				// Start mail process
				if (processMAIL()) {
					// Start get recipients process
					if (processRCPT())
						// Start receive message data process
						processDATA();
				}
			} catch (IOException e) {
				e.printStackTrace(System.err);
				stopRequested = true;
			}
		}
		// Close socket
		close();
		// Remove connection from server when it finishes its mission
		server.removeConnection(this);
	}

}
