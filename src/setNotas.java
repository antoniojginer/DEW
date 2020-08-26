

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

/**
 * Servlet implementation class setNotas
 */
@WebServlet("/setNotas")
public class setNotas extends HttpServlet {
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
		
		if(sesion != null) {
			String alu = request.getParameter("alumno");
			String asig = request.getParameter("asig");
			String nota = request.getParameter("nota");
			String key = (String) sesion.getAttribute("key");
			String dni = request.getRemoteUser();
			
			if(request.isUserInRole("rolpro")) {
				LocalDateTime hoy = LocalDateTime.now();
				String logs = getServletContext().getRealPath("logs.txt");
				
				File file = new File(logs);
				FileWriter f = new FileWriter(file, true);
				f.write(hoy.toString() + " - Profesor: " + dni + " - setNotas \n");
				f.close();
				
				HttpPut put= new HttpPut("http://localhost:9090/CentroEducativo/alumnos/"+alu+"/asignatura/"+asig+"?key=" + key);
				put.setEntity(new StringEntity(nota, ContentType.APPLICATION_JSON));
				
				HttpResponse resp = ((HttpClient) sesion.getAttribute("cliente")).execute(put);
				
				JSONObject respuesta = new JSONObject();
				respuesta.put("code", resp.getStatusLine().getStatusCode());
				PrintWriter w = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				w.println(respuesta);
				w.flush();
			} else {
				response.setStatus(404);
			}			
			
		}
		
	}

}
