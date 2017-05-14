package mail.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import mail.server.Server;
import utilities.MessageListAdapter;

public class ClientUtilities {
	private static ClientUtilities clientUtilities = new ClientUtilities();
	private static ObjectInputStream objectInputStream;
	private static DataOutputStream dataOutputStream;
	private static DataInputStream dataInputStream;
	private static Socket socket;

	public static ClientUtilities getInstance() {
		return clientUtilities;
	}

	private ClientUtilities() {
		try {
			socket = new Socket("localhost", 2602);
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			dataInputStream = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<MessageListAdapter> getInbox(String userName) throws IOException, ClassNotFoundException {
		dataOutputStream.writeUTF("Inbox#" + userName + "#a#a");
		return (ArrayList<MessageListAdapter>) objectInputStream.readObject();
	}

	public ArrayList<MessageListAdapter> getSent(String userName) throws IOException, ClassNotFoundException {
		dataOutputStream.writeUTF("Sent#" + userName + "#b#b");
		return (ArrayList<MessageListAdapter>) objectInputStream.readObject();
	}

	public boolean deleteMessage(String from, String time, String userName) throws IOException {
		dataOutputStream.writeUTF("Delete#" + userName + "#" + time + "#" + from);
		return dataInputStream.readBoolean();
	}

	public String readMessage(String from, String time, String userName) throws IOException {
		dataOutputStream.writeUTF("Read#" + userName + "#" + time + "#" + from);
		return (String) dataInputStream.readUTF();
	}

	public boolean sendAttachment(String fileName, InputStream inputStream, String userName) throws IOException {
		dataOutputStream.writeUTF("Attachment#" + userName + "#" + fileName + "#c");
		byte[] buffer = new byte[inputStream.available()];
		inputStream.read(buffer);
		dataOutputStream.writeInt(buffer.length);
//		for (int i = 0; i < buffer.length; i++){
//			dataOutputStream.writeByte(buffer[i]);
//		}
		dataOutputStream.write(buffer, 0, buffer.length);
		return dataInputStream.readBoolean();
	}
	
	public boolean addUser(String userName, String password) throws IOException{
		dataOutputStream.writeUTF("AddUser#" + userName + "#" + password + "#c");
		return dataInputStream.readBoolean();
	}

	public byte[] getAttachment(String filePath) throws IOException {
		dataOutputStream.writeUTF("GetAttachment#" + filePath + "#c#c");
		int size = dataInputStream.readInt();
		byte[] buffer = new byte[size];
//		for (int i = 0; i < size; i++){
//			buffer[i] = dataInputStream.readByte();
//		}
		dataInputStream.readFully(buffer, 0, size);
		return buffer;
	}

	public boolean checkUserName(String userName) throws IOException {
		dataOutputStream.writeUTF("CheckUser#" + userName + "#d#d");
		return dataInputStream.readBoolean();
	}
}
