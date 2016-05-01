package comp2541.bison.restaurant.database;

import java.util.ArrayList;

import comp2541.bison.restaurant.data.Booking;
import comp2541.bison.restaurant.data.Meal;
import comp2541.bison.restaurant.data.Order;
import comp2541.bison.restaurant.data.Table;

/**
 * Database abstract class that 
 * defines the general usage of the
 * database.
 * 
 * @author Ilyass Taouil
 */

public abstract class Database {
		
	/**
	 * Constructor
	 * 
	 * @param dbName
	 */
	public Database(String dbName){}
	
	/**
	 * 
	 * @param booking
	 * @return referenceID
	 * @throws Exception
	 */
	public abstract int insertBooking(Booking booking) throws Exception;
	
	/**
	 * 
	 * @param order
	 * @throws Exception
	 */
	public abstract void insertOrder(Order order) throws Exception;
	
	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * @return ArrayList<Booking>
	 * @throws Exception
	 */
	public abstract ArrayList<Booking> getBookings(long startTime, long endTime) throws Exception;
	
	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * @return ArrayList<Table>
	 * @throws Exception
	 */
	public abstract ArrayList<Table> getAvailableTables(long startTime, long endTime) throws Exception;
	
	/**
	 * 
	 * @return ArrayList<Meal>
	 * @throws Exception
	 */
	public abstract ArrayList<Meal> getMeals() throws Exception;

	/**
	 * 
	 * @param booking
	 * @return ArrayList<Meal>
	 * @throws Exception
	 */
	public abstract ArrayList<Meal> getOrderedMeals(Booking booking) throws Exception;

	/**
	 * 
	 * @return ArrayList<Tables>
	 * @throws Exception
	 */
	public abstract ArrayList<Table> getTables() throws Exception;
	
	/**
	 * 
	 * @param booking
	 */
	public abstract void removeAllOrders(Booking booking) throws Exception;

}