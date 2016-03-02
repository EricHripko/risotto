package comp2541.bison.restaurant;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.JSONArray;
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
			
			BufferedReader requestBodyBR = request.getReader(); // Reader for the body of the HTTP message
			StringBuilder sb = new StringBuilder();				// Auxiliary object to tranform body to JSON
			String line;										// String used to read from BufferedReader
			
			// Reading the body:
			while ((line = requestBodyBR.readLine()) != null) {
				sb.append(line);
			}
			
			// StringBuilder to JSONObject:
			JSONObject jsonBody = new JSONObject(sb.toString());
			
			if (request.getRequestURI().equals("/bookings")) {
				
				// Booking requested, built from JSONObject:
				Booking booking = new Booking(jsonBody);
				
				// Put booking into the database and get the reference number,
				// then upload the object Booking with the received data:
				try {
					
					// Get all the available tables and check if the request can be satisfied:
					ArrayList<Table> availableTables = restaurantDB.getAvailableTables(booking.getUnixStart(), booking.getUnixEnd());
					boolean tableFound = false;
					
					for (Table table : availableTables) {
						// TODO: This searches only for a perfect number, it should be changed to be more versatile.
						if (table.getSize() == booking.getPartySize()) {
							// If the table is found then the request could be satisfied:
							tableFound = true;
							
							// Update table ID
							booking.getTable().setId(table.getId());
							
							int referenceNumber = restaurantDB.insertBooking(booking);
							booking.setReferenceNumber(referenceNumber);
							
							// Send OK and referenceNumber to the client.
							response.setStatus(HttpServletResponse.SC_OK);
							response.getWriter().println(booking.getJSONObject().toString());
							
							break;
						}
					}
					
					// If after the search the table hasn't been found then the request couldn't be satisfied:
					if (tableFound == false) {
						// Send JSON error message to the client
						JSONObject jsonError = new JSONObject();
						jsonError.put("errorMessage", "There are no tables available at the requested time.");
						
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						response.getWriter().println(jsonError.toString());
					}
					
				} catch (Exception e) {
					// Send JSON error message to the client
					JSONObject jsonError = new JSONObject();
					jsonError.put("errorMessage", "The request cannot be satisfied");
					
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println(jsonError.toString());
					
					e.printStackTrace();
				}
				
			} else if (request.getRequestURI().equals("/orders")) {
				
				try {
					JSONArray jsonArrayOrder = jsonBody.getJSONArray("orders");   // Orders in the request.
					ArrayList<Order> unsatisfiedOrders = new ArrayList<Order>();  // Unsatisfied orders to send back
					boolean requestFullySatisfied = true; // Are all the requests satisfied?
					
					// Insert all the orders into the database:
					for (Object objectOrder : jsonArrayOrder) {
						// jsonOrder must be a JSONObject as specified in the Wiki,
						// otherwise the structure of the JSON message is not correct.
						JSONObject jsonOrder = (JSONObject) objectOrder;
						Order order = new Order(jsonOrder);
						
						try {
							restaurantDB.insertOrder(order);
						} catch (Exception e) {
							// TODO: Add unsatisfied orders (the object order) to an array and send them
							// back to the client to let them know that something wrong occurred.
							unsatisfiedOrders.add(order);
							requestFullySatisfied = false;
						}
						
					}
					
					if (requestFullySatisfied) {
						// Send OK to the client.
						response.setStatus(HttpServletResponse.SC_OK);
						response.getWriter().println("");
					} else {
						// Send JSON error message to the client with unsatisifed orders.
						JSONObject jsonError = new JSONObject();
						JSONArray jsonUnsatisfiedOrders = new JSONArray();
						
						// Construct a JSONArray including unsatisfied orders:
						for (Order order : unsatisfiedOrders) {
							jsonUnsatisfiedOrders.put(order.getJSONObject());
						}
						
						jsonError.put("errorMessage", "The request cannot be satisfied");
						jsonError.put("orders", jsonUnsatisfiedOrders);
						
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						response.getWriter().println(jsonError.toString());
					}
					
					
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
		} else if (request.getMethod().equalsIgnoreCase("GET")) {
			
			if (request.getRequestURI().equals("/bookings")) {
				// Overview of bookings request
				
				String query = request.getQueryString();
				
				// Starting and ending time of the request:
				int indexOfQuestionMark = query.indexOf("?");
				int indexOfAmpersend = query.indexOf("&");
				String startingTimeStr = query.substring(indexOfQuestionMark+1, indexOfAmpersend);
				String endingTimeStr = query.substring(indexOfAmpersend+1);
				long startingTime = Long.parseLong(startingTimeStr.substring(startingTimeStr.indexOf("=") + 1));
				long endingTime = Long.parseLong(endingTimeStr.substring(endingTimeStr.indexOf("=") + 1));
				
				try {
					// Get all the bookings from time to time:
					ArrayList<Booking> bookings = restaurantDB.getBookings(startingTime, endingTime);
					
					// Build the JSON message:
					JSONObject jsonResponse = new JSONObject();
					JSONArray jsonBookingsArray = new JSONArray();
					for (Booking b : bookings) {
						jsonBookingsArray.put(b.getJSONObject());
					}
					jsonResponse.put("bookings", jsonBookingsArray);
					
					// Send OK and list of bookings:
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().println(jsonResponse.toString());
					
				} catch (Exception e) {
					e.printStackTrace();
					
					JSONObject jsonError = new JSONObject();
					jsonError.put("errorMessage", "The request cannot be satisfied");
					
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println(jsonError.toString());
				}
				
			}
		} else if (request.getRequestURI().equals("/menu")) {
			try {
				// Build a menu object from an ArrayList<Meal>:
				Menu menu = new Menu("", restaurantDB.getMeals());
				
				// Send OK and menu:
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println(menu.getJSONObject());
				
			} catch (Exception e) {
				e.printStackTrace();
				
				JSONObject jsonError = new JSONObject();
				jsonError.put("errorMessage", "The request cannot be satisfied");
				
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println(jsonError.toString());
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