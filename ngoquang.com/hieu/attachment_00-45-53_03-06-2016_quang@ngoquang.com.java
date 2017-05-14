import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


/**
 * @author NguyenBaAnh
 *
 */
public class Client {
	private static Socket socket;
	private static Scanner scanner;
	private static String input, output;
	StringBuffer screenBuffer;
	String command;
	JFrame frame;
	JPanel topPanel, middlePanel, bottomPanel, panel;
	JTextArea mainScreen;
	JTextField commandLine;
	public Client() {
		scanner = new Scanner(System.in);
		System.out.print("Nhập địa chỉ Server: ");
		String serverAddress = scanner.nextLine();
		System.out.print("Nhập cổng: ");
		int serverPort = scanner.nextInt();
		initializeGUI(serverAddress, serverPort);
		addListeners(serverAddress, serverPort);
	}
	
	public static void main(String[] args) throws IOException {
		new Client();
		//socket = new Socket("localhost", 8977);
		//socket = new Socket("192.168.1.99", 8977);
		
	}
	
	 void initializeGUI(String serverAddress, int port) {
		//frame = new JFrame("ĐA CSNM - GVHD: Huỳnh Công Pháp - SVTH: Nguyễn Bá Anh - 12T2");
		frame = new JFrame("ĐA CSNM - Client Monitor - SVTH: Nguyễn Bá Anh - 12T2");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(300, 150, 600, 400);
		frame.setResizable(false);
		
		topPanel = new JPanel();
		topPanel.setLayout(new GridLayout());
		topPanel.setBorder(new TitledBorder(new EtchedBorder(), "Server"));
		JLabel serverPortLabel = new JLabel("<html><font color='red'>" + serverAddress + ":" + port + "</font></html>", SwingConstants.CENTER);
		topPanel.add(serverPortLabel, BorderLayout.NORTH);
		
		middlePanel = new JPanel();
		middlePanel.setLayout(new GridLayout());
		middlePanel.setBorder(new TitledBorder(new EtchedBorder(), "Cửa sổ lệnh"));
		
		screenBuffer = new StringBuffer("Chào! Nhập lệnh để điều khiển phía Server.\nNhập help để xem hướng dẫn");
		mainScreen = new JTextArea(screenBuffer.toString());
		mainScreen.setEditable(false);
		mainScreen.setWrapStyleWord(false);
		mainScreen.setFont(new Font("Consolas", Font.BOLD, 14));
		mainScreen.setForeground(Color.RED);
		ScrollPane scrollPanel = new ScrollPane();
		scrollPanel.add(mainScreen);
		middlePanel.add(scrollPanel, BorderLayout.CENTER);
		
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout());
		bottomPanel.setBorder(new TitledBorder(new EtchedBorder(), "Nhập lệnh và tỉa Enter"));
		commandLine = new JTextField();
		bottomPanel.add(commandLine, BorderLayout.SOUTH);
		
		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(middlePanel, BorderLayout.CENTER);
		frame.add(bottomPanel, BorderLayout.SOUTH);
		frame.setVisible(true);		
		
	}
	
	void addListeners(String serverAddress, int port) {
		commandLine.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				command = commandLine.getText();
				screenBuffer.append("\n" + command);
				commandLine.setText("");
				if ("cls".equals(command)) {
					screenBuffer.delete(0, screenBuffer.length());
				} else {
					DataInputStream dis;
					DataOutputStream dos;
					try {
						socket = new Socket(serverAddress, port);
						dis = new DataInputStream(socket.getInputStream());
						dos = new DataOutputStream(socket.getOutputStream());
						
						System.out.print("Nhập một chuỗi: ");
						scanner = new Scanner(System.in);
						input = scanner.nextLine();
						dos.writeUTF(input);
						output = dis.readUTF();
						System.out.print("Chuỗi chuyển thành chữ HOA và thường: \n" + output);
					} catch (IOException e1) { }
					// 2. Kiểm tra xem Server phản hồi về có kết quả không.
					// 3. Đưa ra thông báo thích hợp
				}
				
				mainScreen.setText("");
				mainScreen.setText(screenBuffer.toString());
			}
		});
	}
	
}
