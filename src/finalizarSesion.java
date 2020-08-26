

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 * Servlet implementation class finalizarSesion
 */
@WebServlet("/finalizarSesion")
public class finalizarSesion extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sesion = request.getSession(false);
		String dni = request.getRemoteUser();
		LocalDateTime hoy = LocalDateTime.now();
		String logs = getServletContext().getRealPath("logs.txt");
		File file = new File(logs);
		FileWriter f = new FileWriter(file, true);
		if(request.isUserInRole("rolalu")) {
			f.write(hoy.toString() + " - Alumno: " + dni + " - finalizarSesion \n");
		}
		if(request.isUserInRole("rolpro")) {
			f.write(hoy.toString() + " - Profesor: " + dni + " - finalizarSesion \n");
		}
		
		f.close();
		
		Cookie[] cookies = request.getCookies(); 
		if (cookies != null) 
			for (int i = 0; i < cookies.length; i++) { 
				cookies[i].setValue(""); 
				cookies[i].setPath("/"); 
				cookies[i].setMaxAge(0); 
				response.addCookie(cookies[i]); 
			}
		sesion.invalidate();
		
		JSONObject info = new JSONObject();
		info.put("code", "200");
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print(info);
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
