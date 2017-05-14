package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.swing.filechooser.FileNameExtensionFilter;

import mail.server.Server;


public class Utilities {
	public static boolean isSocketInited = false;
	public static boolean isLogined = false;
	public static String dirPath = Server.serverPath + "\\";
	public static InputStream inputStream;
	public static BufferedReader bufferedReader;
	public static BufferedWriter bufferedWriter;

	public static String getTimeNow() {
		String date = "";
		DateFormat dateFormat = new SimpleDateFormat("HH-mm-ss_dd-MM-yyyy");
		Date today = new Date();
		date = dateFormat.format(today);
		return date;
	}

	public static boolean checkUser(String userName, String password) {
		try {
			FileReader fileReader = new FileReader("User.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			do {
				String account = bufferedReader.readLine();
				if (account == null) {
					break;
				}
				if (account.substring(0, account.indexOf(":")).equals(userName)
						&& account.substring(account.indexOf(":") + 1).equals(password)) {
					bufferedReader.close();
					return true;
				}
			} while (true);
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean checkUserName(String userName) {
		try {
			File file = new File(Utilities.dirPath + "User.txt");
			if (file.exists()){
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				do {
					String account = bufferedReader.readLine();
					if (account == null) {
						break;
					}
					System.out.println(account);
					if (account.substring(0, account.indexOf(":")).equals(userName)) {
						bufferedReader.close();
						return true;
					}
				} while (true);
				bufferedReader.close();
				fileReader.close();
			} else {
				return false;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean addUser(String userName, String password) throws IOException {
		File file = new File(Utilities.dirPath + "User.txt");
		if (file.exists()) {
			System.out.println(file.getAbsolutePath());
			FileWriter fileWriter = new FileWriter(file, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write("\n" + userName + ":" + password);
			bufferedWriter.close();
			fileWriter.close();
			File folder = new File(Utilities.dirPath + "ngoquang.com\\" + userName);
			return folder.mkdir();
		} else {
			file.createNewFile();
			System.out.println(file.getAbsolutePath());
			FileWriter fileWriter = new FileWriter(file, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write("\n" + userName + ":" + password);
			bufferedWriter.close();
			fileWriter.close();
			File folder = new File(Utilities.dirPath + "ngoquang.com\\" + userName);
			return folder.mkdir();
		}
	}

	public static String createFile() {
		String currentDir = System.getProperty("user.dir");
		System.out.println(currentDir);
		String dirName = currentDir + "\\quang.com\\hieutran";
		File f = new File(dirName);
		f.mkdirs();
		System.out.println(f.getAbsolutePath());
		String fileName = dirName + "\\" + getTimeNow() + ".qmsg";
		System.out.println(fileName);
		File file = new File(fileName);
		try {
			if (!file.exists()) {
				if (file.createNewFile()) {
					StringBuffer strBf = new StringBuffer();
					strBf.append("Ngô Quang\n");
					strBf.append("Ngô Quang\n");
					String string = strBf.toString();
					FileWriter fileWriter = new FileWriter(file);
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					bufferedWriter.write(string);
					bufferedWriter.close();
					return "true";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static boolean saveFileAttachment(byte[] buffer, String fileName, String userName)
			throws IOException {
		File target = new File(Utilities.dirPath + "ngoquang.com\\" + userName + "\\" + fileName);
		if (target.exists()) {
			return false;
		} else {
			target.createNewFile();
			System.out.println(target);
		}
		OutputStream outputStream = new FileOutputStream(target);
		outputStream.write(buffer);
		outputStream.close();
		System.out.println("Finished save a file");
		return true;
	}

	public static ArrayList<MessageListAdapter> getAllSentMessages(String userName) {
		ArrayList<MessageListAdapter> listMessageSent = new ArrayList<>();
		File allUserDirs = new File(Utilities.dirPath + "ngoquang.com");
		String[] listDirs = allUserDirs.list();
		FilenameFilter filenameFilterMessage = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.contains(userName + "@ngoquang.com") && name.contains("qms")) {
					return true;
				}
				return false;
			}
		};
		FilenameFilter filenameFilterAttachment = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.contains(userName + "@ngoquang.com") && !name.contains("qms")) {
					return true;
				}
				return false;
			}
		};
		for (String dir : listDirs) {
			File dirOfOneUser = new File(Utilities.dirPath + "ngoquang.com\\" + dir);
			File[] allMessageInOneDir = dirOfOneUser.listFiles(filenameFilterMessage);
			File[] allAttachmentInOneDir = dirOfOneUser.listFiles(filenameFilterAttachment);
			for (File message : allMessageInOneDir) {
				try {
					inputStream = new FileInputStream(message);
					bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
					bufferedReader.readLine();
					String from = dir + "@ngoquang.com";
					String time = bufferedReader.readLine().substring(6);
					String subject = bufferedReader.readLine().substring(9);
					String content = "";
					do {
						String _temp = bufferedReader.readLine();
						if (_temp == null) {
							break;
						}
						if (_temp.contains("QMS is deleted") || _temp.contains("QMS is read")) {
							continue;
						}
						content += _temp + "\n";
					} while (true);
					String attachmentPath = "";
					for (File attachment : allAttachmentInOneDir) {
						if (attachment.getName().contains(time)) {
							attachmentPath = attachment.getAbsolutePath();
						}
					}
					listMessageSent.add(new MessageListAdapter(from, time, subject, content, attachmentPath));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		try {
			if (bufferedReader != null){
				bufferedReader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Collections.sort(listMessageSent, new Comparator<MessageListAdapter>() {
			@Override
			public int compare(MessageListAdapter o1, MessageListAdapter o2) {
				return o1.getTime().substring(14).compareTo(o2.getTime().substring(14));
			}
		});
		Collections.sort(listMessageSent, new Comparator<MessageListAdapter>() {
			@Override
			public int compare(MessageListAdapter o1, MessageListAdapter o2) {
				return o1.getTime().substring(8, 11).compareTo(o2.getTime().substring(8, 11));
			}
		});
		Collections.sort(listMessageSent, new Comparator<MessageListAdapter>() {
			@Override
			public int compare(MessageListAdapter o1, MessageListAdapter o2) {
				return o1.getTime().substring(11, 14).compareTo(o2.getTime().substring(11, 14));
			}
		});
		Collections.reverse(listMessageSent);
		return listMessageSent;
	}

	public static ArrayList<MessageListAdapter> getInboxMessages(String userName) {
		ArrayList<MessageListAdapter> listMessageInbox = new ArrayList<>();
		File folderInbox = new File(Utilities.dirPath + "ngoquang.com\\" + userName);
		System.out.println(folderInbox.getAbsolutePath());
		FilenameFilter filenameFilterMessage = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (name.contains("qms")) {
					return true;
				}
				return false;
			}
		};
		FilenameFilter filenameFilterAttachment = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				if (!name.contains("qms")) {
					return true;
				}
				return false;
			}
		};
		File[] allMessagesInbox = folderInbox.listFiles(filenameFilterMessage);
		File[] allAttachment = folderInbox.listFiles(filenameFilterAttachment);
		for (File message : allMessagesInbox) {
			try {
				inputStream = new FileInputStream(message);
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
				String from = bufferedReader.readLine().substring(6);
				String time = bufferedReader.readLine().substring(6);
				String subject = bufferedReader.readLine().substring(9);
				String content = "";
				do {
					String _temp = bufferedReader.readLine();
					if (_temp == null) {
						break;
					}
					content += _temp + "\n";
				} while (true);
				String attachmentPath = "";
				for (File attachment : allAttachment) {
					if (attachment.getName().contains(time)) {
						attachmentPath = attachment.getAbsolutePath();
					}
				}
				listMessageInbox.add(new MessageListAdapter(from, time, subject, content, attachmentPath));
			} catch (Exception e) {
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
		Collections.sort(listMessageInbox, new Comparator<MessageListAdapter>() {
			@Override
			public int compare(MessageListAdapter o1, MessageListAdapter o2) {
				return o1.getTime().substring(14).compareTo(o2.getTime().substring(14));
			}
		});
		Collections.sort(listMessageInbox, new Comparator<MessageListAdapter>() {
			@Override
			public int compare(MessageListAdapter o1, MessageListAdapter o2) {
				return o1.getTime().substring(8, 11).compareTo(o2.getTime().substring(8, 11));
			}
		});
		Collections.sort(listMessageInbox, new Comparator<MessageListAdapter>() {
			@Override
			public int compare(MessageListAdapter o1, MessageListAdapter o2) {
				return o1.getTime().substring(11, 14).compareTo(o2.getTime().substring(11, 14));
			}
		});
		Collections.reverse(listMessageInbox);
		return listMessageInbox;
	}

	public static boolean addDeleteToFile(String filePath) {
		File file = new File(filePath);
		FileWriter fileWriter;
		FileReader fileReader;
		try {
			if (file.exists()) {
				fileReader = new FileReader(file);
				bufferedReader = new BufferedReader(fileReader);
				String _str = "";
				boolean isDeleted = false;
				while (true) {
					_str = bufferedReader.readLine();
					if (_str == null) {
						break;
					}
					if (_str.contains("QMS is deleted")) {
						isDeleted = true;
					}
				}
				if (!isDeleted) {
					fileWriter = new FileWriter(file.getAbsolutePath(), true);
					bufferedWriter = new BufferedWriter(fileWriter);
					System.out.println(bufferedWriter.toString());
					bufferedWriter.write("QMS is deleted");
					bufferedWriter.write("\r\n");
					bufferedWriter.close();
					fileWriter.close();
				}
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean addReadToFile(String filePath) {
		File file = new File(filePath);
		FileWriter fileWriter;
		FileReader fileReader;
		try {
			if (file.exists()) {
				fileReader = new FileReader(file);
				bufferedReader = new BufferedReader(fileReader);
				String _str = "";
				boolean isRead = false;
				while (true) {
					_str = bufferedReader.readLine();
					if (_str == null) {
						break;
					}
					if (_str.contains("QMS is read")) {
						isRead = true;
					}
				}
				if (!isRead) {
					fileWriter = new FileWriter(file.getAbsolutePath(), true);
					bufferedWriter = new BufferedWriter(fileWriter);
					System.out.println(bufferedWriter.toString());
					bufferedWriter.write("QMS is read");
					bufferedWriter.write("\r\n");
					bufferedWriter.close();
					fileWriter.close();
				}
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		
	}
}
