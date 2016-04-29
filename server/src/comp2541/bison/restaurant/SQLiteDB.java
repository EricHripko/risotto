package comp2541.bison.restaurant;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import org.apache.log4j.*;

/**
 * 
 * The class defines the
 * methods required from 
 * the above layer (i.e.
 * Server and Client).
 * 
 * @author Ilyass Taouil
 */

public class SQLiteDB extends Database {
	
	/**
	 * Logger file 
	 */
	static Logger log = Logger.getLogger(SQLiteDB.class.getName());
	
	/**
	 * String dbName
	 */
	private String dbName = null;
	
	/**
	 * SQLiteDB Constructor
	 * 
	 * @param dbName
	 * @throws Exception
	 */
	public SQLiteDB(String dbName) throws Exception {
		super(dbName);

		//fields initialization
		this.dbName = dbName;
		
		//Drop tables via shell script
		//executeScript("dropScriptDB.sh");

		//Call setUp()
		setUp();
		
		//Import data into tables
		//executeScript("importScriptDB.sh");
	}

	/**
	 * setUp()
	 * 
	 * Creates new DB if needed. The method creates the needed tables as well.
	 */
	private void setUp() throws Exception {
		
		//Load SQLite driver
		Class.forName("org.sqlite.JDBC");
		
		//Queries
		String table = "CREATE TABLE IF NOT EXISTS RestaurantTable "	+
		     	       "(ID INTEGER PRIMARY KEY NOT NULL,"				+
	                   " description TEXT NOT NULL,"					+
	                   " size INTEGER NOT NULL"							+ 
	                   ");";
		
		String booking = "CREATE TABLE IF NOT EXISTS Booking "					+
					     "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"				+
				         " customerName TEXT NOT NULL,"							+
				         " phoneNumber TEXT NOT NULL,"							+ 
				         " email TEXT NOT NULL,"								+
				         " partySize INTEGER NOT NULL,"							+  
				         " unixStart NUMERIC NOT NULL,"							+
				         " unixEnd   NUMERIC NOT NULL,"							+
				         " tableID INTEGER,"									+
				         " FOREIGN KEY(tableID) REFERENCES RestaurantTable(ID)"	+
				         ");";
		
		String meal = "CREATE TABLE IF NOT EXISTS Meal "	+
			     	  "(ID INTEGER PRIMARY KEY NOT NULL,"	+
			     	  " name TEXT NOT NULL,"				+
			     	  " description TEXT NOT NULL,"			+ 
			     	  " price INTEGER NOT NULL,"			+  
			     	  " category TEXT NOT NULL"				+
			     	  ");";
		
		String order = "CREATE TABLE IF NOT EXISTS RestaurantOrder "	+
			     	   "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"			+
			     	   " mealID INTEGER NOT NULL,"						+
			     	   " bookingID INTEGER NOT NULL,"					+ 
			     	   " FOREIGN KEY(mealID) REFERENCES Meal(ID),"		+
			     	   " FOREIGN KEY(bookingID) REFERENCES Booking(ID)"	+
			     	   ");";
		
		//Try with resources
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			 Statement stmt = conn.createStatement()) 
		{
			//Queries execution
			stmt.executeUpdate(table);
			stmt.executeUpdate(booking);
			stmt.executeUpdate(meal);
			stmt.executeUpdate(order);
		}
		
		//Log info
		log.info("Tables created successfully");
	}

	/**
	 *
	 * @param booking (Booking object)
	 * 
	 * Inserts booking elements in the DB.
	 */
	@Override
	public int insertBooking(Booking booking) throws Exception {
		
		int ref = -1;
		
		//Load SQLite driver
		Class.forName("org.sqlite.JDBC");
		

		//Queries
		String idQuery = "SELECT MAX(ID) FROM Booking;";
		
		String insert = "INSERT INTO Booking(customerName, phoneNumber, email, partySize, unixStart, unixEnd, tableID)" +
				        "VALUES(?, ?, ?, ?, ?, ?, ?);";
		
		//Try with resources
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			 PreparedStatement pstmt = conn.prepareStatement(insert);
			 Statement stmt = conn.createStatement())
		{
			//Prepared Statement insertion
			pstmt.setString(1, booking.getCustomerName());
			pstmt.setString(2, booking.getPhoneNumber());
			pstmt.setString(3, booking.getEmail());
			pstmt.setInt(4, booking.getPartySize());
			pstmt.setLong(5, booking.getUnixStart());
			pstmt.setLong(6, booking.getUnixEnd());
			pstmt.setInt(7, booking.getTable().getId());
			
			//Prepared Statement execution
			pstmt.executeUpdate();

			//Log info
			log.info("booking inserted");

			//ID booking retrieval
			ResultSet rs = stmt.executeQuery(idQuery);

			//Retrieve max ID from ResultSet
			if(rs.next()) {
				ref = rs.getInt(1);
			}

			//return reference to server
			return ref;
		}
	}
	
	/**
	 * 
	 * @param order (Order object)
	 * 
	 * Inserts new order in the DB.
	 */
	@Override
	void insertOrder(Order order) throws Exception {
		
		boolean checkMeal = false;
		boolean checkBooking = false;
		
		//Load SQLite driver
		Class.forName("org.sqlite.JDBC");
		
		//Queries
		String insert = "INSERT INTO RestaurantOrder(mealID, bookingID)" +
				   		"VALUES(?, ?);";
		
		String mealID = "SELECT ID FROM Meal " +
						"WHERE ID = ?;";
		
		String bookingID = "SELECT ID FROM Booking " +
						   "WHERE ID = ?;";
		
		//Try with resources
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			 PreparedStatement pstmt  = conn.prepareStatement(insert);
			 PreparedStatement pstmt1 = conn.prepareStatement(mealID);
			 PreparedStatement pstmt2 = conn.prepareStatement(bookingID))
		{
			//Prepared Statement insertion
			pstmt.setInt(1, order.getMealId());
			pstmt.setInt(2, order.getBookingId());
			pstmt1.setInt(1, order.getMealId());
			pstmt2.setInt(1, order.getBookingId());
			
			//Queries execution
			ResultSet rs1 = pstmt1.executeQuery();
			if(rs1.next()) 
				checkMeal = true;
			else
				checkMeal = false;
			
			ResultSet rs2 = pstmt2.executeQuery();
			if(rs2.next()) 
				checkBooking = true;
			else
				checkBooking = false;
			
			if((checkMeal & checkBooking) == true) {
				pstmt.executeUpdate();
			}
			else {
				throw new IllegalArgumentException("mealID and/or bookingID invalid");
			}
				
			//Log info
			log.info("order inserted");
		}
	}

	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * 
	 * Returns all bookings happening between startTime and endTime.
	 * 
	 */
	@Override
	ArrayList<Booking> getBookings(long startTime, long endTime) throws Exception {
		
		ArrayList<Booking> bookings = new ArrayList<>();
		
		//Load SQLite Driver
		Class.forName("org.sqlite.JDBC");
		
		//Query
		String retrieve = "SELECT Booking.*, RestaurantTable.description, RestaurantTable.size FROM"																+
						  " Booking INNER JOIN RestaurantTable" 										+
						  " ON Booking.tableID = RestaurantTable.ID"	 								+
						  " WHERE unixStart >= "	+	startTime	+	" AND unixStart < "	+	endTime	+
						  " OR unixEnd > "		+	startTime	+	" AND unixEnd <= "	+	endTime	+
						  " OR unixStart <= "		+	startTime	+	" AND unixEnd >= "	+	endTime	+
						  ";";
		
		//Try with resources
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			 Statement stmt = conn.createStatement())
		{
			//Query execution
			ResultSet rs = stmt.executeQuery(retrieve);
			
			//Retrieve booking objects
			while(rs.next()) {
				
				int referenceNumber = rs.getInt("ID");
				String customerName = rs.getString("customerName");
				String phoneNumber = rs.getString("phoneNumber");
				String email = rs.getString("email");
				int partySize = rs.getInt("partySize");
				long unixStart = rs.getLong("unixStart");
				long unixEnd = rs.getLong("unixEnd");
				int tableID = rs.getInt("tableID");
				String description = rs.getString("description");
				int size = rs.getInt("size");

				
				//Create table object
				Table table = new Table(tableID, description, size);
				
				//Create Booking object 
				Booking booking = new Booking(referenceNumber, 
						                      customerName, 
						                      phoneNumber, 
						                      email, 
						                      partySize, 
						                      unixStart, 
						                      unixEnd,
						                      table);
				bookings.add(booking);
			}
			
			//Log info
			log.info("Booking objects retrieved from database");
			
			return bookings;
		}
	}

	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * 
	 * Returns all available tables between startTime and endTime
	 */
	@Override
	ArrayList<Table> getAvailableTables(long startTime, long endTime) throws Exception {
		
		ArrayList<Table> availableTables = new ArrayList<>();
		
		//Load SQLite driver
		Class.forName("org.sqlite.JDBC");
		
		//Query
		String retrieve = "SELECT * FROM RestaurantTable"														+
						  " except"																				+
						  " SELECT RestaurantTable.* FROM"														+
						  " RestaurantTable INNER JOIN Booking" 												+
						  " ON RestaurantTable.ID = Booking.tableID" 											+
						  " WHERE Booking.unixStart >= " + startTime + " AND Booking.unixStart < " + endTime 	+
						  " OR Booking.unixEnd > "+ startTime + " AND Booking.unixEnd <= " + endTime 			+
						  " OR Booking.unixStart < " + startTime + " AND Booking.unixEnd > " + endTime;

		//Try with resources
		try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			 Statement stmt = conn.createStatement())
		{
			//Query execution
			ResultSet rs = stmt.executeQuery(retrieve);

			//Retrieve table objects
			while(rs.next()) {

				int referenceNumber = rs.getInt("ID");
				String description = rs.getString("description");
				int size = rs.getInt("size");

				//Create Table object for each table available
				Table table = new Table(referenceNumber, description, size);
				availableTables.add(table);
			}
		}

		//Log info
		log.info("ResturantTable objects retrieved from database");

		return availableTables;
	}
	
	/**
	 * Returns all meals in stored in the DB.
	 */
	@Override
	ArrayList<Meal> getMeals() throws Exception {
		
		ArrayList<Meal> meals = new ArrayList<>();
		
		//Load SQLite driver
		Class.forName("org.sqlite.JDBC");
		
		//Query
		String retrieve = "SELECT * FROM Meal";
		
		//Try with resources
		try(Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			Statement stmt = conn.createStatement())
		{
			//Query execution
			ResultSet rs = stmt.executeQuery(retrieve);

			//Retrieve meal objects
			while(rs.next()) {

				int referenceNumber = rs.getInt("ID");
				String name = rs.getString("name");
				String description = rs.getString("description");
				int price = rs.getInt("price");
				String category = rs.getString("category");

				//Create Meal instance for each meal 
				Meal meal = new Meal(referenceNumber, name, description, price, category);
				meals.add(meal);
			}
		}
		
		//Log info
		log.info("Meal objects retrieved from database");

		return meals;
	}
	
	/**
	 * 
	 * @param booking (Booking object)
	 * 
	 * Returns 
	 */
	@Override
	ArrayList<Meal> getOrderedMeals(Booking booking) throws Exception {
		
		ArrayList<Meal> orderedMeals = new ArrayList<>();
		
		//Load SQLite driver
		Class.forName("org.sqlite.JDBC");
		
		//Query
		String orders = "SELECT Meal.* FROM" 									+
						" RestaurantOrder JOIN Meal" 							+
						" ON RestaurantOrder.mealID = Meal.ID JOIN" 			+
						" Booking ON RestaurantOrder.bookingID = Booking.ID"	+
						" WHERE (RestaurantOrder.bookingID = ?);";
		
		//Try with resources
		try(Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			PreparedStatement pstmt = conn.prepareStatement(orders))
		{
			//Prepared Statement insertion
			pstmt.setInt(1, booking.getReferenceNumber());

			//Query execution
			ResultSet rs = pstmt.executeQuery();

			//Retrieve OrderedMeal objects
			while(rs.next()) {

				int referenceNumber = rs.getInt("ID");
				String name = rs.getString("name");
				String description = rs.getString("description");
				int price = rs.getInt("price");
				String category = rs.getString("category");

				//Create Meal instance 
				Meal meal = new Meal(referenceNumber, name, description, price, category);
				orderedMeals.add(meal);
			}
			
			//Log info
			log.info("Meal objects retrieved from database");

			return orderedMeals;
		}
	}

	/**
	 * Returns all tables in the DB restaurantTable.
	 */
	@Override
	ArrayList<Table> getTables() throws Exception {
		
		ArrayList<Table> tables = new ArrayList<>();
				
		//Load SQLite driver
		Class.forName("org.sqlite.JDBC");
		
		//Query
		String retrieve = "SELECT * FROM RestaurantTable";

		//Try with resources
		try(Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			Statement stmt = conn.createStatement())
		{
			//Execute Query
			ResultSet rs = stmt.executeQuery(retrieve);

			//Retrieve table objects
			while(rs.next()) {

				int referenceNumber = rs.getInt("ID");
				String description = rs.getString("description");
				int size = rs.getInt("size");

				//Create Table instance 
				Table table = new Table(referenceNumber, description, size);
				tables.add(table);
			}

			//Log info
			log.info("Table objects retrieved from RestaurantTable");

			return tables;
		}
	}
	
	/**
	 * Removes all orders in the RestaurantOrder DB table.
	 */
	@Override
	void removeAllOrders(Booking booking) throws Exception {

		//Load SQLite driver
		Class.forName("org.sqlite.JDBC");
		
		//Query
		String query = "DELETE FROM RestaurantOrder " 										+
					   "WHERE RestaurantOrder.bookingID = " + booking.getReferenceNumber()	+
					   ";";
		
		//Try with resources
		try(Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			Statement stmt = conn.createStatement())
		{
			//Execute query
			stmt.executeUpdate(query);
			
			//Log info (table created successfully)
			log.info("Tables created successfully");
		}
	}
	
	/**
	 * 
	 * @param script (fileName)
	 * 
	 * Executes shell script.
	 */
	public void executeScript(String script) throws Exception {
		
		//Get file path for execution
		File file = new File(script);
		String path = file.getAbsolutePath();

		//Process builder object instantiation
		ProcessBuilder pb = new ProcessBuilder(path);
		//Process execution
		Process p = pb.start();     
		p.waitFor();           
		
		//Console message
		System.out.println(script + " executed successfully");
	}
}