package mail.client;

import java.io.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Servlet implementation class DownloadFileAttachmentController
 */
@WebServlet("/DownloadFileAttachmentController")
public class DownloadFileAttachmentController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DownloadFileAttachmentController() {
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

		ClientUtilities clientUtilities = ClientUtilities.getInstance();

		String filePath = (String) request.getParameter("attachment");
		File downloadFile = new File(filePath);
		System.out.println(downloadFile.getAbsolutePath());
		byte[] file = clientUtilities.getAttachment(filePath);
		System.out.println("File size: " + file.length);
		// FileInputStream inStream = new FileInputStream(downloadFile);

		ServletContext context = getServletContext();

		String mimeType = context.getMimeType(filePath);
		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

//		response.setContentType(mimeType);
		response.setContentLength(file.length);

		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		response.setHeader(headerKey, headerValue);

		OutputStream outStream = response.getOutputStream();

		// byte[] buffer = new byte[4096];
		// int bytesRead = -1;
		//
		// while ((bytesRead = inStream.read(buffer)) != -1) {
		// outStream.write(buffer, 0, bytesRead);
		// }
		outStream.write(file);

		// inStream.close();
		outStream.close();
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
