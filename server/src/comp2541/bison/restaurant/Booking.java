package comp2541.bison.restaurant;

import org.json.JSONObject;

/**
 * 
 * @author michelecipriano
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
	 * 
	 * @param jsonBooking
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
	
	/**
	 * 
	 * @return
	 */
	public int getReferenceNumber() {
		return referenceNumber;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCustomerName() {
		return customerName;
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
	
	/**
	 * 
	 * @return
	 */
	public long getUnixDate() {
		return unixDate;
	}

	/**
	 * 
	 * @param pReferenceNumber
	 */
	public void setReferenceNumber(int pReferenceNumber) {
		referenceNumber = pReferenceNumber;
	}
	
	/**
	 * 
	 * @param pCustomerName
	 */
	public void setCustomerName(String pCustomerName) {
		customerName = pCustomerName;
	}
	
	/**
	 * 
	 * @param pPhoneNumber
	 */
	public void setPhoneNumber(String pPhoneNumber) {
		phoneNumber = pPhoneNumber;
	}
	
	/**
	 * 
	 * @param pEmail
	 */
	public void setEmail(String pEmail) {
		email = pEmail;
	}
	
	/**
	 * 
	 * @param pPartySize
	 */
	public void setPartySize(int pPartySize) {
		partySize = pPartySize;
	}
	
	/**
	 * 
	 * @param pUnixDate
	 */
	public void setDate(long pUnixDate) {
		unixDate = pUnixDate;
	}
	
	/**
	 * 
	 * @return
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
