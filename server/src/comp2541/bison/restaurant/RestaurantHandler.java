package comp2541.bison.restaurant;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 * 
 * @author michelecipriano
 *
 */
public class RestaurantHandler extends AbstractHandler {

	public RestaurantHandler(String dbString) {
		// TODO Connect to database
	}
	
	/**
	 * 
	 */
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		// Below is necessary to pass CORS check
		String origin = request.getHeader("Origin");
		response.addHeader("Access-Control-Allow-Origin", origin);
		response.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
		response.addHeader("Access-Control-Allow-Headers", "Accept, Content-type");
		response.addHeader("Access-Control-Max-Age", "1728000");
		
		if(request.getMethod().equalsIgnoreCase("options")) {
			response.setStatus(200);
			try (PrintWriter out = response.getWriter()) {
				out.write("");
			}
			return;
		}
		
		//TODO Handler code
		
		// Dump the request
		System.out.printf("%s %s", request.getMethod(), request.getRequestURI());
		System.out.println("Body:");
		try(BufferedReader in = request.getReader()) {
			System.out.println(in.readLine());
		}
		// Send dummy response
		response.setStatus(200);
		try(PrintWriter out = response.getWriter()) {
			out.write("");
		}
	}

}
