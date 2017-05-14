package mail.client;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utilities.Utilities;

/**
 * Servlet implementation class RegisterController
 */
@WebServlet("/RegisterController")
public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		request.setCharacterEncoding("UTF-8");
		
		HttpSession session = request.getSession();
		ClientUtilities clientUtilities = ClientUtilities.getInstance();
		
		String userName = (String) request.getParameter("userName");
		String password = (String) request.getParameter("password");
		String rePassword = (String) request.getParameter("rePassword");
		String message = "";
		if ("".equals(userName) || "".equals(password) || "".equals(rePassword)){
			message = "Please fill all user name, password and re-password!";
			request.setAttribute("message", message);
			request.getRequestDispatcher("/WEB-INF/Register.jsp").include(request, response);
		} else if (!password.equals(rePassword)){
			message = "Passwords are not match!";
			request.setAttribute("message", message);
			request.getRequestDispatcher("/WEB-INF/Register.jsp").include(request, response);
		} else if (clientUtilities.checkUserName(userName)){
			message = "User name is exist. Please choose another!";
			request.setAttribute("message", message);
			request.getRequestDispatcher("/WEB-INF/Register.jsp").include(request, response);
		} else {
			if (clientUtilities.addUser(userName, password)){
				message = "Please login to use our service!";
				session.setAttribute("userName1", userName);
				session.setAttribute("password1", password);
				response.sendRedirect("default.jsp?message=" + message);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
