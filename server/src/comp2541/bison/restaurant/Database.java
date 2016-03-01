package comp2541.bison.restaurant;

import java.util.ArrayList;

/**
 * Database abstract class that 
 * defines the general usage of
 * databAse using JDBC framework
 * independently of what database
 * engine is used.
 * 
 * 
 * @author Ilyass Taouil
 *
 */

public abstract class Database {
	
	/**
	 * ArrayList of type Booking
	 */
	ArrayList<Booking> bookings = new ArrayList<>();
	
	/**
	 * ArrayList of type Table
	 */
	ArrayList<Table> tables = new ArrayList<>();
	
	/**
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
	abstract int insertBooking(Booking booking) throws Exception;
	
	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * @return ArrayList<Booking>
	 * @throws Exception
	 */
	abstract ArrayList<Booking> getBookings(long startTime, long endTime) throws Exception;
	
	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * @return ArrayList<Table>
	 * @throws Exception
	 */
	abstract ArrayList<Table> getAvailableTables(long startTime, long endTime) throws Exception;
}