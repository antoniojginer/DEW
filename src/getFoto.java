

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class getFoto
 */
@WebServlet("/getFoto")
public class getFoto extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		String dni = "";
		LocalDateTime hoy = LocalDateTime.now();
		String logs = getServletContext().getRealPath("logs.txt");
		File file = new File(logs);
		FileWriter f = new FileWriter(file, true);
		if(request.isUserInRole("rolalu")) {			
			dni = request.getRemoteUser();
			f.write(hoy.toString() + " - Alumno: " + dni + " - getFoto \n");
		}
		if(request.isUserInRole("rolpro")){
			dni = request.getParameter("dni");
			f.write(hoy.toString() + " - Profesor: " + dni + " - getFoto \n");
		}
		f.close();
		response.setContentType("text/plain");
		String carpeta = getServletContext().getRealPath("fotos");
		String ruta = carpeta + "/"+dni+".pngb64"; 
		response.setCharacterEncoding("UTF-8");
		BufferedReader origen = new BufferedReader(new FileReader(ruta));
		
		response.setContentType("text/plain");
		PrintWriter w = response.getWriter();
		w.print("{\"dni\": \""+dni+"\", \"img\": \"");
		String linea = origen.readLine(); w.print(linea);
		while ((linea = origen.readLine()) != null) {w.print("\n" + linea);}
		w.print("\"}");
		w.close(); origen.close();
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
