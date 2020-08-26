

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;

/**
 * Servlet implementation class getAsignaturas
 */
@WebServlet("/getAsignaturas")
public class getAsignaturas extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sesion = request.getSession(false);

		String dni = request.getRemoteUser();
		String key = (String) sesion.getAttribute("key");
		boolean alu = request.isUserInRole("rolalu");
		
		if(alu) {
			LocalDateTime hoy = LocalDateTime.now();
			String logs = getServletContext().getRealPath("logs.txt");
			File file = new File(logs);
			FileWriter f = new FileWriter(file, true);
			f.write(hoy.toString() + " - Alumno: " + dni + " - getAsignaturas \n");
			f.close();
			
			HttpGet getAsignaturasAlumnos = new HttpGet("http://localhost:9090/CentroEducativo/alumnos/"+dni+"/asignaturas?key=" + key);
			HttpResponse resp = ((HttpClient) sesion.getAttribute("cliente")).execute(getAsignaturasAlumnos);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
			String asigJSON = "";
			String line = "";
			
			while((line = br.readLine()) != null) {
				asigJSON += line;
			}
			
			JSONArray arrayJSON = new JSONArray(asigJSON);

			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.print(arrayJSON);
			out.flush();
			
		} else {
			LocalDateTime hoy = LocalDateTime.now();
			String logs = getServletContext().getRealPath("logs.txt");
			
			File file = new File(logs);
			FileWriter f = new FileWriter(file, true);
			f.write(hoy.toString() + " - Profesor: " + dni + " - getAsignaturas \n");
			f.close();
			
			HttpGet getAsignaturasProfesor = new HttpGet("http://localhost:9090/CentroEducativo/profesores/"+dni+"/asignaturas?key=" + key);
			HttpResponse resp = ((HttpClient) sesion.getAttribute("cliente")).execute(getAsignaturasProfesor);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
			String asigJSON = "";
			String line = "";
			
			while((line = br.readLine()) != null) {
				asigJSON += line;
			}
			
			JSONArray arrayJSON = new JSONArray(asigJSON);

			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.print(arrayJSON);
			out.flush();
			
		}
		
	}
	
}


