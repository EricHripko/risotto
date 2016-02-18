package comp2541.bison.restaurant;


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
	
	//public abstract constructor
	public Database(String dbName){}
	
	//insertBooking() abstract method
	abstract int insertBooking(Booking booking);
}