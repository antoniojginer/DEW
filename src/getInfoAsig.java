

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
import org.json.JSONObject;

/**
 * Servlet implementation class getInfoAsig
 */
@WebServlet("/getInfoAsig")
public class getInfoAsig extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			HttpSession sesion = request.getSession(false);
			String asig = request.getParameter("asig");
			String key = (String) sesion.getAttribute("key");
			String dni = request.getRemoteUser();
			
			if(request.isUserInRole("rolalu")) {
				LocalDateTime hoy = LocalDateTime.now();
				String logs = getServletContext().getRealPath("logs.txt");
				File file = new File(logs);
				FileWriter f = new FileWriter(file, true);
				f.write(hoy.toString() + " - Alumno: " + dni + " - getInfoAsig (GET)\n");
				f.close();
				
				HttpGet get = new HttpGet("http://localhost:9090/CentroEducativo/asignaturas/"+asig+"/profesores?key=" + key);
				HttpResponse resp = ((HttpClient) sesion.getAttribute("cliente")).execute(get);
				
				BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
				String infoJSON = "";
				String line = "";
				
				while((line = br.readLine()) != null) {
					infoJSON += line;
				}
				
				JSONArray info = new JSONArray(infoJSON);
				
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				out.print(info);
				out.flush();
			} else {
				response.setStatus(404);
			}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sesion = request.getSession(false);		
		String asig = request.getParameter("asig");
		String key = (String) sesion.getAttribute("key");
		String dni = request.getRemoteUser();
		LocalDateTime hoy = LocalDateTime.now();
		String logs = getServletContext().getRealPath("logs.txt");		
		File file = new File(logs);
		FileWriter f = new FileWriter(file, true);
		
		if(request.isUserInRole("rolalu")) f.write(hoy.toString() + " - Alumno: " + dni + " - getInfoAsig (POST)\n");
		if(request.isUserInRole("rolpro")) f.write(hoy.toString() + " - Profesor: " + dni + " - getInfoAsig (POST)\n");
		
		f.close();
		
		HttpGet get = new HttpGet("http://localhost:9090/CentroEducativo/asignaturas/"+asig+"?key=" + key);
		HttpResponse resp = ((HttpClient) sesion.getAttribute("cliente")).execute(get);		
		BufferedReader br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
		String infoJSON = "";
		String line = "";
		
		while((line = br.readLine()) != null) {
			infoJSON += line;
		}
		
		JSONObject info =new JSONObject(infoJSON);		
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print(info);
		out.flush();
	}
			
			
		 
		
	}


