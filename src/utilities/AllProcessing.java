package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import mail.server.*;

public class AllProcessing {
	public static Socket smtpSocket, popSocket;
	public static BufferedReader inputSmtp, inputPop;
	public static PrintStream outputSmtp, outputPop;
	public static ArrayList<Integer> listDelete = new ArrayList<>();

	public static boolean initSocket() {
		try {
			smtpSocket = new Socket("localhost", 25);
			popSocket = new Socket("localhost", 110);
			initStream();
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void initStream() {
		try {
			inputSmtp = new BufferedReader(new InputStreamReader(smtpSocket.getInputStream(), "UTF-8"));
			inputPop = new BufferedReader(new InputStreamReader(popSocket.getInputStream(), "UTF-8"));
			outputSmtp = new PrintStream(smtpSocket.getOutputStream(), true, "UTF-8");
			outputPop = new PrintStream(popSocket.getOutputStream(), true, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static boolean login(String userName, String password) throws IOException {
		outputPop.println("user " + userName);
		System.out.println("user " + userName);
		System.out.println(inputPop.readLine());
		outputPop.println("pass " + password);
		System.out.println("pass " + password);
		String result = inputPop.readLine();
		System.out.println(result);
		return result.contains("OK");
	}

	public static boolean checkIsDeleted(int id) {
		for (Integer isDeleted : listDelete) {
			if (id == isDeleted) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<MessageListAdapter> getListMessageInbox(String userName) {
		ArrayList<MessageListAdapter> listMessages = new ArrayList<MessageListAdapter>();
		System.out.println("stat");
		outputPop.println("stat");
		int numOfMessages;
		try {
			numOfMessages = Integer.parseInt(inputPop.readLine().split(" ")[1]);
			System.out.println("Num of messages: " + numOfMessages);
			for (int i = numOfMessages; i >= 1; i--) {
				outputPop.println("retr " + i);
				System.out.println(inputPop.readLine());
				String from = inputPop.readLine().trim().substring(6);
				String time = inputPop.readLine().trim().substring(6);
				String subject = inputPop.readLine().trim().substring(9);
				String content = "";
				do {
					String _tempContent = inputPop.readLine();
					if (".".equals(_tempContent.trim())) {
						break;
					}
					content += _tempContent + "\n";
				} while (true);
				if (content.contains("QMS is deleted")) {
					continue;
				}
				String attachmentPath = "";
				String folderInboxPath = Utilities.dirPath + "ngoquang.com\\" + userName;
				System.out.println("Folder inbox: " + folderInboxPath);
				File folderInbox = new File(folderInboxPath);
				FilenameFilter attachmentFilter = new FilenameFilter() {

					@Override
					public boolean accept(File dir, String name) {
						if (name.contains("attachment")) {
							return true;
						}
						return false;
					}
				};
				File[] listFile = folderInbox.listFiles(attachmentFilter);
				for (File file : listFile) {
					if (file.getName().contains(time)) {
						attachmentPath = file.getAbsolutePath();
					}
				}
				MessageListAdapter messageListAdapter = new MessageListAdapter(from, time, subject, content,
						attachmentPath);
				listMessages.add(messageListAdapter);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listMessages;
	}

	public static String sendMail(String userName, String[] receiveList, String subject, String content) {
		try {
			System.out.println("Send email processing...");
			outputSmtp.println("mail from:<" + userName + "@ngoquang.com>");
			System.out.println("mail from:<" + userName + "@ngoquang.com>");
			System.out.println("Reply from mail from: " + inputSmtp.readLine());
			for (String receiveUser : receiveList) {
				System.out.println("rcpt to:<" + receiveUser.trim() + ">");
				outputSmtp.println("rcpt to:<" + receiveUser.trim() + ">");
				String in = inputSmtp.readLine();
				System.out.println("Reply from RCPT: " + in);
				if (in.contains("550")) {
					return "";
				}
			}
			System.out.println("data");
			outputSmtp.println("data");
			System.out.println(inputSmtp.readLine());
			System.out.println("Subject: " + subject);
			outputSmtp.println("Subject: " + subject);
			System.out.println(inputSmtp.readLine());
			System.out.println(content);
			String[] listContent = content.split("\\n");
			System.out.println(listContent.length);
			for (String _str : listContent){
				System.out.println(_str.trim());
				outputSmtp.println(_str.trim());
				System.out.println(inputSmtp.readLine());
			}
			outputSmtp.flush();
			outputSmtp.println(".");
			String messageName = inputSmtp.readLine();
			System.out.println("Message name: " + messageName);
			return "attachment_" + messageName.split(" ")[2].substring(1, messageName.split(" ")[2].length() - 5);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return "";
	}

	public static boolean deleteMessage(String from, String time, String userName) throws IOException {
		/*
		 * System.out.println("dele " + id); outputPop.println("dele " + id);
		 * String result = inputPop.readLine(); System.out.println(result); if
		 * (result.contains("OK")) { listDelete.add(new Integer(id)); return
		 * true; } else { return false; }
		 */
		return Utilities
				.addDeleteToFile(Utilities.dirPath + "ngoquang.com\\" + userName + "\\" + time + "_" + from + ".qms");
	}

	public static void main(String[] args) {
	}

	public static boolean readMessage(String from, String time, String userName) {
		return Utilities
				.addReadToFile(Utilities.dirPath + "ngoquang.com\\" + userName + "\\" + time + "_" + from + ".qms");
	}
}
