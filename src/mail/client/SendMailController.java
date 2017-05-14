package mail.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import utilities.AllProcessing;

/**
 * Servlet implementation class SendMailController
 */
@WebServlet("/SendMailController")
@MultipartConfig
public class SendMailController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendMailController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		request.setCharacterEncoding("UTF-8");
		
		HttpSession session = request.getSession();
		
		ClientUtilities clientUtilities = ClientUtilities.getInstance();
		
		String act = request.getParameter("act");
		if (act == null){
			act = "";
		}
		switch (act){
			case "NewMail":
				String[] receiveList = {""};
				String receive = request.getParameter("receipient");
				String subject = request.getParameter("subject");
				String content = request.getParameter("content");
				System.out.println(content);
				String userName = (String) session.getAttribute("userName");
				Part attachment = request.getPart("attachment");
				String fileName = attachment.getSubmittedFileName();
				InputStream fileContent = attachment.getInputStream();
				if (!"".equals(receive)){
					receiveList = receive.trim().split(";");
					String messageName = AllProcessing.sendMail(userName, receiveList, subject, content);
					boolean isSent = false;
					if (!"".equals(messageName)){
						if (!"".equals(fileName)){
							fileName = messageName + "." +  fileName.substring(fileName.lastIndexOf('.') + 1);
							for (String receiver : receiveList){
								isSent = clientUtilities.sendAttachment(fileName, fileContent, receiver.substring(0, receive.indexOf("@")));
								System.out.println(isSent);
							}
						}
					}
					if (isSent = true){
						String message = "Sent successfully!";
						request.setAttribute("message", message);
						try {
							request.setAttribute("listMessages", clientUtilities.getInbox((String) session.getAttribute("userName")));
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
						request.getRequestDispatcher("/WEB-INF/Inbox.jsp").include(request, response);
					} else {
						String message = "Sent fail. Go back and try again!";
						request.setAttribute("message", message);
						request.getRequestDispatcher("/WEB-INF/Notification.jsp").include(request, response);
						try {
							request.setAttribute("listMessages", clientUtilities.getInbox((String) session.getAttribute("userName")));
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
						request.getRequestDispatcher("/WEB-INF/Inbox.jsp").include(request, response);
					}
				}
				break;
			default:
				break;
		}
	}

}
