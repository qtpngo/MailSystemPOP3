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
 * Servlet implementation class HomePageController
 */
@WebServlet("/HomePageController")
public class HomePageController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HomePageController() {
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

		String act = request.getParameter("act");
		if (act == null) {
			act = "";
		}
		switch (act) {
		case "Inbox":
			ArrayList<MessageListAdapter> listMessages = new ArrayList<>();
			try {
				listMessages = clientUtilities.getInbox((String) session.getAttribute("userName"));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			request.setAttribute("listMessages", listMessages);
			request.getRequestDispatcher("/WEB-INF/Inbox.jsp").include(request, response);
			break;
		case "NewMail":
			request.getRequestDispatcher("/WEB-INF/NewMail.jsp").include(request, response);
			break;
		case "Sent":
			ArrayList<MessageListAdapter> listMessagesSent = new ArrayList<>();
			try {
				listMessagesSent = clientUtilities.getSent((String) session.getAttribute("userName"));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			request.setAttribute("listMessages", listMessagesSent);
			request.getRequestDispatcher("/WEB-INF/Sent.jsp").include(request, response);
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
