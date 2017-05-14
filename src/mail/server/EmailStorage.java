package mail.server;

import java.io.*;
import java.util.*;

import sun.nio.cs.UnicodeEncoder;
import utilities.Utilities;

public class EmailStorage {
	private static EmailStorage storage = new EmailStorage();
	public static final String serverName = "ngoquang.com";
	FileReader fileReader;
	InputStream inputStream;

	private EmailStorage() {
		File file = new File(serverName);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public static EmailStorage getInstance() {
		return storage;
	}

	protected String saveMessage(Vector users, String returnPath, StringBuffer data) {
		Iterator userIterator = users.iterator();
		returnPath = returnPath.substring(returnPath.indexOf('<') + 1, returnPath.indexOf('>'));
		BufferedWriter bufferedWriter;
		String timeNow = Utilities.getTimeNow();
		String fileName = timeNow + "_" + returnPath + ".qms";
		while (userIterator.hasNext()) {
			User user = (User) userIterator.next();
			String folderName = System.getProperty("user.dir") + "\\" + serverName + "\\" + user.getName();
			File file = new File(folderName + "\\" + fileName);
			try {
				if (!file.exists()) {
					file.createNewFile();
					bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
					String header = "From: " + returnPath + "\n";
					String time = "Time: " + timeNow + "\n";
					String content = data.toString();
					bufferedWriter.write(header);
					bufferedWriter.write(time);
					bufferedWriter.write(content);
					bufferedWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileName;
	}

	protected User getUser(String address) {
		int index = address.indexOf('@');
		try {
			fileReader = new FileReader("User.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			do {
				String account = bufferedReader.readLine();
				if (account == null) {
					break;
				}
				String username = account.substring(0, account.indexOf(":"));
				String password = account.substring(account.indexOf(":") + 1);
				if (username.equals(address.substring(0, index))) {
					bufferedReader.close();
					return new User(username, password);
				}
			} while (true);
		} catch (FileNotFoundException e) {
			System.out.println("File not found:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Cannot file user.");
		}
		return null;
	}

	protected User login(String userName, String password) {
		try {
			fileReader = new FileReader(Server.serverPath + "\\User.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			do {
				String account = bufferedReader.readLine();
				if (account == null) {
					break;
				}
				String user = account.substring(0, account.indexOf(":"));
				String pass = account.substring(account.indexOf(":") + 1);
				if (user.equals(userName) && pass.equals(password)) {
					bufferedReader.close();
					return new User(userName, password);
				}
			} while (true);
		} catch (FileNotFoundException e) {
			System.out.println("File not found:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("Cannot file user.");
		}
		return null;
	}

	protected Vector getMessages(User user) {
		String folderName = serverName + "/" + user.getName();
		File dir = new File(folderName);
		File[] listMessages = dir.listFiles();
		Vector messageList = new Vector();
		int messageId = 1;
		BufferedReader bufferedReader = null;
		for (File message : listMessages) {
			try {
//				fileReader = new FileReader(message);
				if (!message.getName().contains("qms")){
					continue;
				}
				inputStream = new FileInputStream(message);
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				String messageContent = "";
				String messageHeader = "";
				String _str = "";
				while(true){
					_str = bufferedReader.readLine();
					if (_str == null){
						break;
					}
					if (_str.contains("Header")){
						messageHeader = _str;
					}
					messageContent += _str;
				}
//				bufferedReader.close();
				messageList.addElement(new Message(messageId++, message.getName(), messageHeader, messageContent));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			if (bufferedReader != null){
				bufferedReader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return messageList;
	}

	protected StringBuffer getMessageData(Message message, String userName) {
		String fileName = serverName + "\\" + userName + "\\" + message.getMessageName();
		StringBuffer stringBuffer = new StringBuffer();
		try {
			inputStream = new FileInputStream(fileName);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			do {
				try {
					String msgContent = bufferedReader.readLine();
					if (msgContent == null){
						break;
					}
					stringBuffer.append(msgContent + "\n");
				} catch (Exception e){
					System.out.println(e.getMessage());
				}
			} while (true);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuffer;
		/*
		File file = new File(fileName);
		if (file.exists()) {
			StringBuffer strBuffer = new StringBuffer();
			do {
				try {
					fileReader = new FileReader(file);
					BufferedReader bufferedReader = new BufferedReader(fileReader);
					String messageContent = bufferedReader.readLine();
					if (messageContent == null) {
						break;
					}
					strBuffer.append(messageContent + "\n");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} while (true);
			return strBuffer;
		}
		return null;
		*/
	}

	protected void deleteMessage(Message message, String userName) {
		String fileName = serverName + "/" + userName + "/" + message.getMessageName() + ".qms";
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
			return;
		}
		return;
	}
}
