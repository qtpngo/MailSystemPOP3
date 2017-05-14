package mail.server;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author Quang Ngo TP
 *
 */
public class POPConnection  extends Thread{
	protected static final int ENTER_USER = 0;
	protected static final int ENTER_PASSWORD = 1;
	protected static final int TRANSACTION = 2;
	protected static final int UPDATE = 3;
	protected POPServer server;
	protected Socket socket;
	protected BufferedReader in;
	protected PrintStream out;
	protected boolean stopRequested;
	protected int state;
	protected String userName;
	protected User user;
	protected Vector messages = null;

	public POPConnection(Socket socket, POPServer server) {
		super();
		this.socket = socket;
		this.server = server;
	}
	
	protected void close() {
		try {
			socket.close();
			System.out.println("Close POP Connection!");
		} catch (Exception e) {
			System.err.println("Exception trying to close POPConnection socket!");
			e.printStackTrace(System.err);
		}
	}
	
	protected int countMessages() {
		if (messages == null)
			return 0;
		Enumeration enumeration = messages.elements();
		int count = 0;
		while (enumeration.hasMoreElements()) {
			Message message = (Message) enumeration.nextElement();
//			if (!message.isDeleted())
				count++;
		}
		return count;
	}
	
	protected Message getMessage(int number) {
		if (number <= 0 || number > messages.size())
			return null;
		Message message = (Message) messages.elementAt(number - 1);
		if (message.isDeleted())
			return null;
		return message;
	}

	protected Message getMessage(String messageNumber) {
		int number;
		try {
			number = Integer.parseInt(messageNumber);
		} catch (NumberFormatException e) {
			return null;
		}
		return getMessage(number);
	}

	protected long getMessagesSize() {
		if (messages == null)
			return 0;
		Enumeration enumeration = messages.elements();
		long size = 0;
		while (enumeration.hasMoreElements()) {
			Message message = (Message) enumeration.nextElement();
			if (!message.isDeleted())
				size += message.getSize();
		}
		return size;
	}
	
	protected void processDELE(StringTokenizer arguments) {
		if (!arguments.hasMoreTokens()) {
			out.println("­ERR must supply message number");
		} else {
			Message message = getMessage(arguments.nextToken());
			if (message == null) {
				out.println("­ERR no such message");
			}
			message.setDeleted(true);
			out.println("+OK");
		}
	}

	protected void processEnterPassword(String command, StringTokenizer arguments) {
		if (command.equalsIgnoreCase("QUIT")) {
			stopRequested = true;
			out.println("+OK Signing off");
		} else if (command.equalsIgnoreCase("PASS")) {
			if (!arguments.hasMoreTokens()) {
				out.println("­ERR must supply password");
				return;
			}
			String password = arguments.nextToken();
			System.out.println(password);
			if (arguments.hasMoreTokens()) {
				out.println("­ERR only one argument to PASS, your password");
				return;
			}
			user = Server.storage.login(userName, password);
			if (user == null) {
				out.println("­ERR invalid user or password");
				state = ENTER_USER;
			} else {
				messages = Server.storage.getMessages(user);
				if (messages == null){
					out.println("-ERR Mailbox is empty");
				}
				out.println("+OK mailbox open, " + countMessages() + " messages");
				state = TRANSACTION;
			}
		} else {
			out.println("­ERR Only use PASS or QUIT commands");
		}
	}

	protected void processEnterUser(String command, StringTokenizer arguments) {
		if (command.equalsIgnoreCase("QUIT")) {
			stopRequested = true;
			out.println("+OK Signing off");
			return;
		}
		if (command.equalsIgnoreCase("USER")) {
			if (!arguments.hasMoreTokens()) {
				out.println("­ERR must supply user name");
				return;
			}
			userName = arguments.nextToken();
			if (arguments.hasMoreTokens()) {
				out.println("­ERR only one argument to USER, the user name");
				return;
			}
			state = ENTER_PASSWORD;
			out.println("+OK use PASS command to send password");
			return;
		}
		out.println("­ERR Only use USER or QUIT commands");
	}

	protected void processLIST(StringTokenizer arguments) {
		if (!arguments.hasMoreTokens()) {
			out.println("+OK " + countMessages() + " " + getMessagesSize());
			for (int i = 1; i <= messages.size(); i++) {
				Message message = getMessage(i);
				if (message != null) {
					out.println(i + " " + message.getSize());
				}
			}
			out.println(".");
		} else {
			String messageNumber = arguments.nextToken();
			Message message = getMessage(messageNumber);
			if (message == null) {
				out.println("­ERR no such message");
			} else {
				out.println("+OK " + messageNumber + " " + message.getSize());
			}
			out.println(".");
		}
	}
	
	protected void processNOOP(StringTokenizer arguments) {
		out.println("+OK");
	}

	protected void processQUIT(StringTokenizer arguments) {
		Enumeration enumeration = messages.elements();
		while (enumeration.hasMoreElements()) {
			Message message = (Message) enumeration.nextElement();
			if (message.isDeleted()) {
				Server.storage.deleteMessage(message, user.getName());
			}
		}
		out.println("+OK Goodbye, " + user.getName());
		stopRequested = true;
	}

	protected void processRETR(StringTokenizer arguments) {
		if (!arguments.hasMoreTokens()) {
			out.println("­ERR message number required, RETR 1");
		} else {

			String messageNumber = arguments.nextToken();
			Message message = getMessage(messageNumber);
			if (message == null) {
				out.println("­ERR no such message");
				return;
			}
			out.println("+OK " + message.getSize() + " octets");
			StringBuffer buffer = Server.storage.getMessageData(message, user.getName());
			BufferedReader reader = new BufferedReader(new StringReader(buffer.toString()));
			boolean done = false;
			try {
				while (reader.ready() && (!done)) {
					String line = reader.readLine();
					if (line == null)
						break;
					if (line.length() >= 1) {
						if (line.substring(0, 1).equals("."))
							line = "." + line;
					}
					out.println(line);
				}
			} catch (IOException e) {
				System.err.println("POPConnection.processRETR()");
				e.printStackTrace(System.err);
			}
			try {
				reader.close();
			} catch (IOException e) {
				System.err.println("POPConnection.processRETR() ­ reader.close()");
				e.printStackTrace(System.err);
			}
			out.println(".");
		}
	}

	protected void processRSET(StringTokenizer arguments) {
		Enumeration enumeration = messages.elements();
		while (enumeration.hasMoreElements()) {
			Message message = (Message) enumeration.nextElement();
			if (message.isDeleted())
				message.setDeleted(false);
		}
		out.println("+OK");
	}

	protected void processSTAT(StringTokenizer arguments) {
		out.println("+OK " + countMessages() + " " + getMessagesSize());
	}

	protected void processTOP(StringTokenizer arguments) {
		if (!arguments.hasMoreTokens()) {
			out.println("­ERR syntax: TOP <msg> <lines>");
			return;
		}
		String messageNumber = arguments.nextToken();

		if (!arguments.hasMoreTokens()) {
			out.println("­ERR syntax: TOP <msg> <lines>");
			return;
		}

		int lines = 0;
		try {
			lines = Integer.parseInt(arguments.nextToken());
		} catch (NumberFormatException e) {
			out.println("­ERR bad number of lines");
			return;
		}
		Message message = getMessage(messageNumber);
		if (message == null) {
			out.println("­ERR no such message");
			return;
		}
		out.println("+OK " + message.getSize() + " octets");

		StringBuffer buffer = Server.storage.getMessageData(message, user.getName());
		BufferedReader reader = new BufferedReader(new StringReader(buffer.toString()));
		boolean done = false;
		boolean inBody = false;
		int count = 0;
		try {
			while (reader.ready() && (!done) && (count <= lines)) {
				String line = reader.readLine();
				if (line == null)
					break;

				if (line.length() >= 1) {
					if (line.substring(0, 1).equals("."))
						line = "." + line;
				} else {
					inBody = true;
				}
				if (inBody) {
					count++;
				}
				out.println(line);
			}
		} catch (IOException e) {
			System.err.println("POPConnection.processTOP()");
			e.printStackTrace(System.err);
		}
		try {
			reader.close();
		} catch (IOException e) {
			System.err.println("POPConnection.processTOP() ­ reader.close()");
			e.printStackTrace(System.err);
		}
		out.println(".");
	}

	protected void processTransaction(String command, StringTokenizer arguments) {
		if (command.equalsIgnoreCase("STAT")) {
			processSTAT(arguments);
		} else if (command.equalsIgnoreCase("LIST")) {
			processLIST(arguments);
		} else if (command.equalsIgnoreCase("RETR")) {
			processRETR(arguments);
		} else if (command.equalsIgnoreCase("DELE")) {
			processDELE(arguments);
		} else if (command.equalsIgnoreCase("NOOP")) {
			processTOP(arguments);
		} else if (command.equalsIgnoreCase("TOP")) {
			processTOP(arguments);
		} else if (command.equalsIgnoreCase("UIDL")) {
			processUIDL(arguments);
		} else if (command.equalsIgnoreCase("RSET")) {
			processRSET(arguments);
		} else if (command.equalsIgnoreCase("QUIT")) {
			processQUIT(arguments);
		} else {
			out.println("­ERR Unknown command " + command);
		}
	}

	protected void processUIDL(StringTokenizer arguments) {
		if (!arguments.hasMoreTokens()) {
			out.println("+OK " + countMessages() + " " + getMessagesSize());
			for (int i = 1; i <= messages.size(); i++) {
				Message message = getMessage(i);
				if (message != null) {
					out.println(i + " " + message.getMessageId());
				}
			}
			out.println(".");
		} else {
			String messageNumber = arguments.nextToken();
			Message message = getMessage(messageNumber);
			if (message == null) {
				out.println("­ERR no such message");
			} else {
				out.println("+OK " + messageNumber + " " + message.getMessageId());
			}
		}
	}
	
	protected void resetState(StringTokenizer arguments){
		if (arguments.toString().equalsIgnoreCase("RESET_STATE")){
			state = ENTER_USER;
		}
	}

	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace(System.err);
			close();
			return;
		}
		if (server == null) {
			System.err.println("SERVER NOT SET!!!");
			return;
		}
		stopRequested = false;
		state = ENTER_USER;
		out.println("+OK POP3 " + Server.getAddress());
		while (!stopRequested) {
			try {

				String line = in.readLine();
				if (line == null)
					break;
				StringTokenizer tokenizer = new StringTokenizer(line);
				String command = "";
				if (tokenizer.hasMoreTokens())
					command = tokenizer.nextToken();
				if (command.equalsIgnoreCase("RESET_STATE")){
					state = ENTER_USER;
					continue;
				}
				switch (state) {
				case ENTER_USER:
					processEnterUser(command, tokenizer);
					break;
				case ENTER_PASSWORD:
					processEnterPassword(command, tokenizer);
					break;
				case TRANSACTION:
					processTransaction(command, tokenizer);
					break;
				default:
					out.println("­ERR invalid state! ");
					System.err.println("Invalid State in POPConnection.run()");
					stopRequested = true;
					break;
				}
			} catch (Exception e) {
				e.printStackTrace(System.err);
				stopRequested = true;
			}
		}
		close();
		server.removeConnection(this);
	}
}
