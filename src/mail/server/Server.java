package mail.server;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * @author Quang Ngo TP
 *
 */
public class Server {
	public static Properties properties;
	public static SMTPServer smtpServer;
	public static EmailStorage storage;
	public static POPServer popServer;
	public static ServerUtilities serverUtilities;
	public static JFrame serverGUIFrame;
	public static String serverPath = System.getProperty("user.dir");
	
	public static String getAddress(){
		// Get server address
		String address = properties.getProperty("server.address");
		if (address == null){
			address = "UNKNOWN[server.address]";
		} return address;
	}
	
	protected static boolean getProperties(){
		String fileName = "email.properties";
		File file = new File(fileName);
		// Check existing of properties file: email.properties
		if (!file.exists()){
			fileName = file.getAbsolutePath();
			System.out.println("Specified properties file '" + fileName + "' does not exist!");
			return false;
		}
		// Read properties
		properties = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			properties.load(in);
			in.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		System.getProperties().put("line.seperator", "\r\n");
		return true;
	}
	
	public static void initServerGUI(){
		serverGUIFrame = new JFrame("Server");
		serverGUIFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverGUIFrame.setBounds(500, 200, 400, 284);
		serverGUIFrame.getContentPane().setLayout(null);
		
		JLabel lblMailServer = new JLabel("ngoquang.com Mail Server (c) 2016");
		lblMailServer.setHorizontalAlignment(SwingConstants.CENTER);
		lblMailServer.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblMailServer.setBounds(10, 11, 364, 14);
		serverGUIFrame.getContentPane().add(lblMailServer);
		
		JButton btnStartServer = new JButton("Start server");
		btnStartServer.setBounds(137, 36, 109, 23);
		serverGUIFrame.getContentPane().add(btnStartServer);
		
		JLabel lblInformation = new JLabel("Information");
		lblInformation.setBounds(23, 71, 75, 14);
		serverGUIFrame.getContentPane().add(lblInformation);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(10, 96, 364, 138);
		serverGUIFrame.getContentPane().add(textArea);
		textArea.setEditable(false);
		
		btnStartServer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isErrorInInitServer = false;
				if (!setupStorage()){
					textArea.append("Could not setup storage!");
					isErrorInInitServer = true;
				}
				if (!startSMTPServer()){
					textArea.append("Could not start SMTP server!");
					isErrorInInitServer = true;
				}
				if (!startPOPServer()){
					textArea.append("Could not start POP server!");
					isErrorInInitServer = true;
				}
				if (!startServerUtilities()){
					textArea.append("Could not start Utilities server!");
					isErrorInInitServer = true;
				}
				if (!isErrorInInitServer){
					textArea.append("Server started!\nServer address: "
							+ properties.getProperty("server.address") + "\nSMTP server at port: "
							+ properties.getProperty("smtp.port") + "\nPOP server at port: "
							+ properties.getProperty("pop.port"));
				}
			}
		});
		
		serverGUIFrame.setResizable(false);
		serverGUIFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
		if (!getProperties()){
			System.err.println("Could not get properties!");
			return;
		}
		// Start server
		initServerGUI();
	}

	private static boolean startPOPServer() {
		try {
			popServer = new POPServer();
			popServer.setDaemon(false);
			popServer.start();
		} catch (Exception e){
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	private static boolean startSMTPServer() {
		try {
			smtpServer = new SMTPServer();
			smtpServer.setDaemon(false);
			smtpServer.start();
		} catch (Exception e){
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	private static boolean setupStorage() {
		storage = EmailStorage.getInstance();
		if (storage != null){
			return true;
		}
		return false;
	}
	private static boolean startServerUtilities() {
		try {
			serverUtilities = new ServerUtilities();
			serverUtilities.setDaemon(false);
			serverUtilities.start();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
