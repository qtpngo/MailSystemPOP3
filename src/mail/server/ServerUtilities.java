package mail.server;

import java.awt.image.DataBufferUShort;
import java.io.*;
import java.net.*;
import java.util.*;
import utilities.*;

public class ServerUtilities extends Thread {
	private ObjectOutputStream objectOutputStream;
	// private ObjectInputStream objectInputStream;
	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;
	private Socket socket;
	private ServerSocket serverSocket;
	private String receive;

	public ServerUtilities() throws IOException {
		System.out.println("Server is started!");
		serverSocket = new ServerSocket(2602);
		receive = "";
	}

	@Override
	public void run() {
		try {
			socket = serverSocket.accept();
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (true) {
			try {
				receive = dataInputStream.readUTF();
				System.out.println(receive);
				String request = receive.split("#")[0];
				String userName = receive.split("#")[1];
				String time = receive.split("#")[2];
				String from = receive.split("#")[3];
				switch (request) {
				case "Inbox":
					System.out.println("Get inbox");
					ArrayList<MessageListAdapter> listInbox = Utilities.getInboxMessages(userName);
					objectOutputStream.writeObject(listInbox);
					break;
				case "Sent":
					System.out.println("Get sent");
					ArrayList<MessageListAdapter> listSent = Utilities.getAllSentMessages(userName);
					objectOutputStream.writeObject(listSent);
					break;
				case "Delete":
					System.out.println("Delete a message");
					ArrayList<MessageListAdapter> listInbox1 = Utilities.getInboxMessages(userName);
					for (MessageListAdapter m : listInbox1) {
						if (m.getTime().equals(time)) {
							dataOutputStream.writeBoolean(AllProcessing.deleteMessage(from, time, userName));
						}
					}
					break;
				case "Read":
					System.out.println("Message is read");
					ArrayList<MessageListAdapter> listInbox2 = Utilities.getInboxMessages(userName);
					for (MessageListAdapter m : listInbox2) {
						if (m.getTime().equals(time)) {
							dataOutputStream.writeUTF(AllProcessing.readMessage(from, time, userName) + "");
						}
					}
					break;
				case "Attachment":
					System.out.println("Saving attachment file...");
					int size = dataInputStream.readInt();
					byte[] buffer = new byte[size];
//					for (int i = 0; i < size; i++) {
//						buffer[i] = dataInputStream.readByte();
//					}
					dataInputStream.readFully(buffer, 0, size);
					dataOutputStream.writeBoolean(Utilities.saveFileAttachment(buffer, time, userName));
					break;
				case "CheckUser":
					dataOutputStream.writeBoolean(Utilities.checkUserName(userName));
					break;
				case "AddUser":
					dataOutputStream.writeBoolean(Utilities.addUser(userName, time));
					break;
				case "GetAttachment":
					System.out.println("Getting attachment file");
					File file = new File(userName);
					if (file.exists()){
						InputStream inputStream = new FileInputStream(file);
						byte[] buffer1 = new byte[inputStream.available()];
						inputStream.read(buffer1);
						dataOutputStream.writeInt(buffer1.length);
//						for (int i = 0; i < buffer1.length; i++){
//							dataOutputStream.writeByte(buffer1[i]);
//						}
						dataOutputStream.write(buffer1, 0, buffer1.length);
						inputStream.close();
					}
					break;
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// try {
		// new ServerUtilities();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
}
