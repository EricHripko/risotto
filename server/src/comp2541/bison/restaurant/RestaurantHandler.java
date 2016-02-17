package comp2541.bison.restaurant;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.JSONObject;

/**
 * 
 * @author michelecipriano
 *
 */
public class RestaurantHandler extends AbstractHandler {

	/**
	 * 
	 * @param dbString
	 */
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
		
		// TODO Handle requests and db (Issue #6)
		
		// TODO Add proper logging to the server (Issue #10)
		//System.out.printf("%s %s", request.getMethod(), request.getRequestURI());
		//System.out.println("Body:");
		
		if (request.getMethod().equalsIgnoreCase("GET")) {
			System.out.println("Requested URI: " + request.getRequestURI());
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println("");
		} else if (request.getMethod().equalsIgnoreCase("POST")) {
			if (request.getRequestURI() == "/bookings") {
				BufferedReader requestBodyBR = request.getReader(); // Reader for the body of the HTTP message
				StringBuilder sb = new StringBuilder();				// Auxiliary object to tranform body to JSON
				String line;										// String used to read from BufferedReader
				
				// Reading the body:
				while ((line = requestBodyBR.readLine()) != null) {
					sb.append(line);
				}
				
				// StringBuilder to JSONObject:
				JSONObject jsonBody = new JSONObject(sb.toString());
				
				// TODO Take elements from JSON object and put them to db.
			} else {
				// TODO Handle other requests.
			}
		} else {
			// Dump the Request
			
			// TODO Why do you need to read a line?
			//try (BufferedReader in = request.getReader()) {
			//	System.out.println(in.readLine());
			//}
			
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println("");
		}
		
		// The request has been handled correctly.
		baseRequest.setHandled(true);
	}

}
