package mail.client;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import utilities.AllProcessing;
import utilities.Utilities;

/**
 * Servlet implementation class LoginController
 */
/**
 * @author Quang Ngo TP
 *
 */
@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginController() {
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

		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		
		HttpSession session = request.getSession();
		
		if (!Utilities.isLogined){
			if (AllProcessing.login(userName, password)){
				session.setAttribute("userName", userName);
				session.setAttribute("password", password);
				Utilities.isLogined = true;
				request.getRequestDispatcher("/WEB-INF/HomePage.jsp").forward(request, response);
			} else {
				String message =  "Unknown user/wrong password. Please log in again!";
				response.sendRedirect("default.jsp?message=" + message);
			}
		} else {
			request.getRequestDispatcher("/WEB-INF/HomePage.jsp").forward(request, response);
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
