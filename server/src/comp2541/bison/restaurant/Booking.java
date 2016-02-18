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
	
	private int referenceNumber; /** Reference number of the booking. */
	private String customerName; /** Name of the customer. */
	private String phoneNumber;	 /** Phone number of the costumer. */
	private String email;		 /** Email of the costumer. */
	private int partySize;		 /** Number of people of the party. */
	private long unixDate;		 /** Date of the booking. */
	
	/**
	 * Constructor from JSON object.
	 * 
	 * @param jsonBooking A JSON object containing mandatory information for the booking.
	 */
	public Booking(JSONObject jsonBooking) {
		// Take elements from JSONObject and create a Booking object.
		customerName = jsonBooking.getString("customerName");
		phoneNumber = jsonBooking.getString("phoneNumber");
		email = jsonBooking.getString("emailAddress");
		partySize = jsonBooking.getInt("partySize");
		unixDate = jsonBooking.getLong("date");
		
		// TODO: All the inserted data MUST be correct.
	}
	
	public int getReferenceNumber() {
		return referenceNumber;
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

	public long getUnixDate() {
		return unixDate;
	}

	public void setReferenceNumber(int pReferenceNumber) {
		referenceNumber = pReferenceNumber;
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

	public void setDate(long pUnixDate) {
		unixDate = pUnixDate;
	}
	
	/**
	 * Converts the Booking object to a JSONObject, useful for sending through HTTP.
	 * 
	 * @return A JSON object corresponding to the Booking object.
	 */
	public JSONObject getJSONObject() {
		JSONObject jsonBooking = new JSONObject();
		
		jsonBooking.put("referenceNumber", referenceNumber);
		jsonBooking.put("costomerName", customerName);
		jsonBooking.put("phoneNumber", phoneNumber);
		jsonBooking.put("emailAddress", email);
		jsonBooking.put("partySize", partySize);
		jsonBooking.put("date", unixDate);
		
		return jsonBooking;
	}
}
