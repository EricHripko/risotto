package comp2541.bison.restaurant;

import org.json.JSONObject;

/**
 * The Booking class manages the Booking objects
 * used in the requests in the HTTP communication.
 * 
 * @author Michele Cipriano
 *
 */
public class Booking {
	
	private int bookingID; /** Reference number of the booking. (ID in the database) */
	private String customerName; /** Name of the customer. */
	private String phoneNumber;	 /** Phone number of the costumer. */
	private String email;		 /** Email of the costumer. */
	private int partySize;		 /** Number of people of the party. */
	private long unixStart;		 /** Date of start of the booking. */
	private long unixEnd;		 /** Date of end of the booking. */
	private Table table;		 /** Table assigned to the booking. */
	
	/**
	 * Constructor from JSON object.
	 * 
	 * @param jsonBooking A JSON object containing mandatory information for the booking.
	 */
	public Booking(JSONObject jsonBooking) {
		
		// Take elements from JSONObject and create a Booking object.
		bookingID = jsonBooking.optInt("referenceNumber");
		customerName = jsonBooking.getString("customerName");
		phoneNumber = jsonBooking.getString("phoneNumber");
		email = jsonBooking.getString("emailAddress");
		partySize = jsonBooking.getInt("partySize");
		unixStart = jsonBooking.getLong("date");
		
		if (jsonBooking.isNull("endingDate")) {
			unixEnd = unixStart + 3600*2; // +2hours by default
		} else {
			unixEnd = jsonBooking.getLong("endingDate");
		}
		
		table = new Table(jsonBooking.getJSONObject("table").getInt("id"),
						  jsonBooking.getJSONObject("table").getString("description"),
						  jsonBooking.getJSONObject("table").getInt("size"));
		
		// TODO: All the inserted data MUST be correct.
	}
	
	public Booking(int bookingID, 
				   String customerName, 
				   String phoneNumber, 
				   String email, 
				   int partySize, 
				   long unixStart, 
				   long unixEnd,
				   Table table) {
		
		// States initialization
		this.bookingID = bookingID;
		this.customerName = customerName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.partySize = partySize;
		this.unixStart = unixStart;
		this.unixEnd = unixEnd;
		this.table = table;
	}
	
	public Booking(int bookingID) {
		this.bookingID = bookingID;
	}

	public int getReferenceNumber() {
		return bookingID;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public String getEmail() {
		return email;
	}

	public int getPartySize() {
		return partySize;
	}

	public long getUnixStart() {
		return unixStart;
	}
	
	public long getUnixEnd() {
		return unixEnd;
	}
	
	public Table getTable() {
		return table;
	}
	
	public void setReferenceNumber(int pReferenceNumber) {
		bookingID = pReferenceNumber;
	}

	public void setCustomerName(String pCustomerName) {
		customerName = pCustomerName;
	}
	
	public void setPhoneNumber(String pPhoneNumber) {
		phoneNumber = pPhoneNumber;
	}
	
	public void setEmail(String pEmail) {
		email = pEmail;
	}
	
	public void setPartySize(int pPartySize) {
		partySize = pPartySize;
	}

	public void setUnixStart(long pUnixStart) {
		unixStart = pUnixStart;
	}
	
	public void setUnixEnd(long pUnixEnd) {
		unixEnd = pUnixEnd;
	}
	
	public void setTable(Table pTable) {
		table = pTable;
	}
	
	/**
	 * Converts the Booking object to a JSONObject, useful for sending through HTTP.
	 * 
	 * @return A JSON object corresponding to the Booking object.
	 */
	public JSONObject getJSONObject() {
		JSONObject jsonBooking = new JSONObject();
		
		jsonBooking.put("referenceNumber", bookingID);
		jsonBooking.put("customerName", customerName);
		jsonBooking.put("phoneNumber", phoneNumber);
		jsonBooking.put("emailAddress", email);
		jsonBooking.put("partySize", partySize);
		jsonBooking.put("date", unixStart);
		jsonBooking.put("endingDate", unixEnd);
		jsonBooking.put("table", table.getJSONObject());
		
		return jsonBooking;
	}
}
