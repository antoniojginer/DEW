

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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

/**
 * Servlet implementation class log
 */
@WebServlet("/log")
public class log_pro extends HttpServlet {
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
		String dni = request.getRemoteUser();
		String pw = "123456";		
		try {
			if(request.isUserInRole("rolpro")) {
				JSONObject logJSON = new JSONObject();
				logJSON.put("dni", dni);
				logJSON.put("password", pw);
				String log = JSONObject.valueToString(logJSON);
				
				BasicCookieStore cookie = new BasicCookieStore();
				HttpClient cl = HttpClientBuilder.create().setDefaultCookieStore(cookie).build();
				HttpPost post = new HttpPost("http://localhost:9090/CentroEducativo/login");
				post.setHeader("Content-Type", "application/json; charset=utf-8");
				post.setEntity(new StringEntity(log, ContentType.APPLICATION_JSON));
				HttpResponse respPost = cl.execute(post);
				BufferedReader br1 = new BufferedReader(new InputStreamReader(respPost.getEntity().getContent()));
				String key = br1.readLine();		

				HttpSession sesion = request.getSession(true);
				sesion.setAttribute("dni", dni);
				sesion.setAttribute("rol", "pro");
				sesion.setAttribute("key", key);
				sesion.setAttribute("cliente", cl);
				sesion.setAttribute("cookie", cookie.getCookies().get(0));
				
				LocalDateTime hoy = LocalDateTime.now();
				String logs = getServletContext().getRealPath("logs.txt");
				File file = new File(logs);
				FileWriter f = new FileWriter(file, true);
				f.write(hoy.toString() + " - Alumno: " + dni + " - log_alu \n");
				f.close();
			
				PrintWriter w = response.getWriter();
				w.print("Exito");
				w.close();
			} else {
				PrintWriter w = response.getWriter();
				w.print("Fallo");
				w.close();
			}

		} catch(Exception e) {
			PrintWriter w = response.getWriter();
			w.print("Fallo");
			w.close();
		}
		
	}

}
