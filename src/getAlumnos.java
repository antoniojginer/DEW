

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
 * Servlet implementation class getAlumnos
 */
@WebServlet("/getAlumnos")
public class getAlumnos extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sesion = request.getSession(false);	
		
		if(request.isUserInRole("rolpro")) {
			String dni = request.getRemoteUser();
			LocalDateTime hoy = LocalDateTime.now();
			String logs = getServletContext().getRealPath("logs.txt");
			
			File file = new File(logs);
			FileWriter f = new FileWriter(file, true);
			f.write(hoy.toString() + " - Profesor: " + dni + " - getAlumnos \n");
			f.close();
			
			String asig = request.getParameter("asig");		
			String key = (String) sesion.getAttribute("key");
			
			HttpGet getAsignaturasProfesor = new HttpGet("http://localhost:9090/CentroEducativo/asignaturas/"+asig+"/alumnos?key=" + key);
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
		} else {
			response.setStatus(404);
		}
		
		
		}
		
		
	}


