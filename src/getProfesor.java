

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
import org.json.JSONObject;

/**
 * Servlet implementation class getProfesor
 */
@WebServlet("/getProfesor")
public class getProfesor extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sesion = request.getSession(false);		
		String dni = request.getRemoteUser();			
		String key = (String) sesion.getAttribute("key");			
			
		if(request.isUserInRole("rolpro")) {
			LocalDateTime hoy = LocalDateTime.now();
			String logs = getServletContext().getRealPath("logs.txt");				
			File file = new File(logs);
			FileWriter f = new FileWriter(file, true);
			f.write(hoy.toString() + " - Profesor: " + dni + " - getProfesor \n");
			f.close();
			
			HttpGet get = new HttpGet("http://localhost:9090/CentroEducativo/profesores/"+dni+"?key=" + key);
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
			out.close();
		} else {
			response.setStatus(404);
		}		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
