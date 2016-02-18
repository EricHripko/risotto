package comp2541.bison.restaurant;

import org.json.JSONObject;

/**
 * 
 * @author michelecipriano
 *
 */
public class Booking {
	
	private String costumerName; /** Name of the costumer. */
	private String phoneNumber;	 /** Phone number of the costumer. */
	private String email;		 /** Email of the costumer. */
	private int partySize;		 /** Number of people of the party. */
	private long unixDate;		 /** Date of the booking. */
	
	/**
	 * 
	 * @param jsonBooking
	 */
	public Booking(JSONObject jsonBooking) {
		// Take elements from JSONObject and create a Booking object.
		costumerName = jsonBooking.getString("costumerName");
		phoneNumber = jsonBooking.getString("phoneNumber");
		email = jsonBooking.getString("email");
		partySize = jsonBooking.getInt("partySize");
		unixDate = jsonBooking.getLong("date");
		
		// TODO: All the inserted data MUST be correct.
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCostumerName() {
		return costumerName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getPartySize() {
		return partySize;
	}
	
	public long getUnixDate() {
		return unixDate;
	}

}
