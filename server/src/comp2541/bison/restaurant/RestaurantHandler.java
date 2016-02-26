package comp2541.bison.restaurant;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.JSONObject;

/**
 * This class handles all the requests from the client
 * respecting the Wiki document (See "Client-Server Communication).
 * 
 * @author Michele Cipriano
 *
 */
public class RestaurantHandler extends AbstractHandler {
	static Logger log = Logger.getLogger(RestaurantHandler.class.getName());

	private Database restaurantDB; /** The database where are stored the data.
	
	/**
	 * Constructor taking the name of the database.
	 * 
	 * @param dbString Name of the database to use.
	 */
	public RestaurantHandler(String dbString) {
		try {
			restaurantDB = new SQLiteDB(dbString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("Cannot open the database.");
			System.exit(0);
		}
	}
	
	/**
	 * Handles all the requests from the clients.
	 * 
	 * @param target The target of the request - either a URI or a name.
	 * @param baseRequest The original unwrapped request object.
	 * @param request The request either as the Request object or a wrapper of that request.
	 * @param response The response as the Response object or a wrapper of that request.
	 * @throws IOException If unable to handle the request or response processing.
	 * @throws ServletException If unable to handle the request or response due to underlying servlet issue.
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
		
		if (request.getMethod().equalsIgnoreCase("options")) {
			System.out.println("Requested URI: " + request.getRequestURI());
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println("");
		} else if (request.getMethod().equalsIgnoreCase("POST")) {
			if (request.getRequestURI().equals("/bookings")) {
				BufferedReader requestBodyBR = request.getReader(); // Reader for the body of the HTTP message
				StringBuilder sb = new StringBuilder();				// Auxiliary object to tranform body to JSON
				String line;										// String used to read from BufferedReader
				
				// Reading the body:
				while ((line = requestBodyBR.readLine()) != null) {
					sb.append(line);
				}
				
				// StringBuilder to JSONObject:
				JSONObject jsonBody = new JSONObject(sb.toString());
				
				// Booking requested, built from JSONObject:
				Booking booking = new Booking(jsonBody);
				
				// Put booking into the database and get the reference number,
				// then upload the object Booking with the received data:
				try {
					int referenceNumber = restaurantDB.insertBooking(booking);
					
					booking.setReferenceNumber(referenceNumber);
					
					// Send OK and referenceNumber to the client.
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().println(booking.getJSONObject().toString());
					
				} catch (Exception e) {
					// Send JSON error message to the client
					JSONObject jsonError = new JSONObject();
					jsonError.put("errorMessage", "The request cannot be satisfied");
					
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println(jsonError.toString());
					
					e.printStackTrace();
				}
				
				
			} else {
				// TODO Handle other POST requests.
			}
		} else {
			// Dump the Request
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println("");
		}
		
		// The request has been handled correctly.
		baseRequest.setHandled(true);
		log.info("Request Handled successfully");
	}

}