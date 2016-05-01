package comp2541.bison.restaurant.data;

import org.json.JSONObject;

/**
 * Class managing orders of a booking (a table in the short period of time).
 * 
 * @author Michele Cipriano
 *
 */
public class Order {
	
	private int mealId;
	private int bookingId;
	
	/**
	 * Useful constructor when you want to refer to an order
	 * without knowing the full datas of the meal and the
	 * booking.
	 * 
	 * @param pMealId Id of the meal.
	 * @param pBookingId Id of the booking.
	 */
	public Order(int pMealId, int pBookingId) {
		mealId = pMealId;
		bookingId = pBookingId;
	}
	
	/**
	 * The object must include a "meal" and a "booking".
	 * 
	 * @param jsonObject A JSONObject object.
	 */
	public Order(JSONObject jsonObject) {
		mealId = jsonObject.getInt("meal");
		bookingId = jsonObject.getInt("booking");
	}

	/**
	 * @return the mealId
	 */
	public int getMealId() {
		return mealId;
	}
	
	/**
	 * 
	 * @return A JSONObject corresponding to the order.
	 */
	public JSONObject getJSONObject() {
		JSONObject jsonOrder = new JSONObject();
		
		jsonOrder.put("meal", mealId);
		jsonOrder.put("booking", bookingId);
		
		return jsonOrder;
	}

	/**
	 * @param mealId the mealId to set
	 */
	public void setMealId(int mealId) {
		this.mealId = mealId;
	}

	/**
	 * @return the bookingId
	 */
	public int getBookingId() {
		return bookingId;
	}

	/**
	 * @param bookingId the bookingId to set
	 */
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}
	
}
