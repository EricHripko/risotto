package comp2541.bison.restaurant.handlers;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import comp2541.bison.restaurant.data.Booking;
import comp2541.bison.restaurant.data.Table;

/**
 * Handler for creating a booking.
 * @author Michele Cipriano
 */
public class PostBookingsHandler extends BaseHandler {
	@Override
	public void run() {
		// Put booking into the database and get the reference number,
		// then upload the object Booking with the received data
		try {
			// Booking requested, built from JSONObject
			Booking booking = new Booking(getBody());

			// Get all the available tables and check if the request can be satisfied
			ArrayList<Table> availableTables = database.getAvailableTables(booking.getUnixStart(), booking.getUnixEnd());
			boolean tableFound = false;

			for (Table table : availableTables) {
				if (table.getSize() >= booking.getPartySize()) {
					// If the table is found then the request could be satisfied:
					tableFound = true;

					// Update table ID
					booking.setTable(table);

					int referenceNumber = database.insertBooking(booking);
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
			try {
				response.getWriter().println(jsonError.toString());
			} catch (IOException iex) {
			}

			e.printStackTrace();
		}
	}	
}
