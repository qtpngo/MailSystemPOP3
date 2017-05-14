package mail.client;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utilities.AllProcessing;
import utilities.MessageListAdapter;
import utilities.Utilities;

/**
 * Servlet implementation class OthersController
 */
@WebServlet("/OthersController")
public class OthersController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OthersController() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		request.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession();

		ClientUtilities clientUtilities = ClientUtilities.getInstance();

		String act = (String) request.getParameter("act");
		if (act == null) {
			act = "";
		}

		switch (act) {
		case "DetailInbox":
			String time = (String) request.getParameter("time");
			ArrayList<MessageListAdapter> listMessage = null;
			try {
				listMessage = clientUtilities.getInbox((String) session.getAttribute("userName"));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			for (MessageListAdapter ms : listMessage) {
				if (time.equals(ms.getTime())) {
					System.out.println(
							clientUtilities.readMessage(ms.getFrom(), time, (String) session.getAttribute("userName")));
					request.setAttribute("messageListAdapter", ms);
				}
			}
			request.getRequestDispatcher("/WEB-INF/Detail.jsp").include(request, response);
			break;
		case "DetailSent":
			String time1 = (String) request.getParameter("time");
			ArrayList<MessageListAdapter> listMessage1 = null;
			try {
				listMessage1 = clientUtilities.getSent((String) session.getAttribute("userName"));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			for (MessageListAdapter ms : listMessage1) {
				if (time1.equals(ms.getTime())) {
					request.setAttribute("messageListAdapter", ms);
				}
			}
			request.getRequestDispatcher("/WEB-INF/DetailSent.jsp").include(request, response);
			break;
		case "Delete":
			String time2 = (String) request.getParameter("time");
			String from = (String) request.getParameter("from");
			String subject1 = (String) request.getParameter("subject");
			String message = "";
			if (clientUtilities.deleteMessage(from, time2, (String) session.getAttribute("userName"))) {
				message = "Message " + subject1 + "... : Delete successfully!";
			} else {
				message = "Message " + subject1 + "...: Delete failed!";
			}
			request.setAttribute("message", message);
			ArrayList<MessageListAdapter> listMessages = new ArrayList<>();
			try {
				listMessages = clientUtilities.getInbox((String) session.getAttribute("userName"));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("listMessages", listMessages);
			request.getRequestDispatcher("/WEB-INF/Inbox.jsp").include(request, response);
			break;
		case "Reply":
			String receipient = (String) request.getParameter("receipient");
			String subject = (String) request.getParameter("subject");
			request.setAttribute("receipient", receipient);
			request.setAttribute("subject", subject);
			request.getRequestDispatcher("/WEB-INF/NewMail.jsp").include(request, response);
			break;
		case "Register":
			request.getRequestDispatcher("/WEB-INF/Register.jsp").forward(request, response);
			break;
		case "SignOut":
			session.removeAttribute("userName");
			session.removeAttribute("password");
			AllProcessing.listDelete.clear();
			Utilities.isLogined = false;
			AllProcessing.outputPop.println("RESET_STATE");
			request.getRequestDispatcher("default.jsp").include(request, response);
			break;
		default:
			break;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
