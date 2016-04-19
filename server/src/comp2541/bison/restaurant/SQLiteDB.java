package comp2541.bison.restaurant;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import org.apache.log4j.*;


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
	 * SQLiteDB public constructor
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
	 * connect to existing database, otherwise it creates one with the specified name.
	 */
	private void setUp() throws Exception {

		//Variable declaration
		Connection conn = null;
		Statement stmt = null;
		
		//Connection set up.
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		conn.setAutoCommit(false);

		//Create connection to database.
		stmt = conn.createStatement();
		
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

		//Queries execution
		stmt.executeUpdate(table);
		stmt.executeUpdate(booking);
		stmt.executeUpdate(meal);
		stmt.executeUpdate(order);
		
		//Log info
		log.info("Tables created successfully");
		
		//Close connections
		stmt.close();
		conn.commit();
		conn.close();
	}

	/**
	 *
	 * @param customerName
	 * @param phoneNumber
	 * @param email
	 * @param partySize
	 * @param date
	 * @param time
	 * 
	 * overrides the Database class method and adds a new row to the Booking table.
	 */
	@Override
	public int insertBooking(Booking booking) throws Exception {

		//Variable declaration
		int ref = -1;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		//Connection to db
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		conn.setAutoCommit(false);

		//Prepared statement initialisation
		String insert = "INSERT INTO Booking(customerName, phoneNumber, email, partySize, unixStart, unixEnd, tableID)" +
				        "VALUES(?, ?, ?, ?, ?, ?, ?);";
		pstmt = conn.prepareStatement(insert);

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
		stmt = conn.createStatement();
		String idQuery = "SELECT MAX(ID) FROM Booking;";
		rs = stmt.executeQuery(idQuery);

		//Retrieve max ID from ResultSet
		if(rs.next()) {
			ref = rs.getInt(1);
		}
		
		//Close connections
		rs.close();
		stmt.close();
		pstmt.close();
		conn.commit();
		conn.close();

		//return reference to server
		return ref;
	}
	
	@Override
	void insertOrder(Order order) throws Exception {
		
		boolean checkMeal = false;
		boolean checkBooking = false;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;

		//Connection setup
		Connection conn = null;
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		conn.setAutoCommit(false);
		
		//Queries
		String mealID 	 = "SELECT ID FROM Meal " +
						   "WHERE ID = ?;";
		
		String bookingID = "SELECT ID FROM Booking " +
						   "WHERE ID = ?;";
		
		String insert 	 = "INSERT INTO RestaurantOrder(mealID, bookingID)" +
						   "VALUES(?, ?);";
		
		//Prepared statement initialisation
		pstmt1 = conn.prepareStatement(mealID);
		pstmt2 = conn.prepareStatement(bookingID);
		pstmt = conn.prepareStatement(insert);
		
		//Prepared Statement insertion
		pstmt1.setInt(1, order.getMealId());
		pstmt2.setInt(1, order.getBookingId());
		pstmt.setInt(1, order.getMealId());
		pstmt.setInt(2, order.getBookingId());

		
		/* Prepared Statements execution */
		rs1 = pstmt1.executeQuery();
		if(rs1.next()) 
			checkMeal = true;
		else
			checkMeal = false;
		
		rs2 = pstmt2.executeQuery();
		if(rs2.next()) 
			checkBooking = true;
		else
			checkBooking = false;
		
		if((checkMeal & checkBooking) == true) {
			pstmt.executeUpdate();
		}
		else {
			pstmt1.close();
			pstmt2.close();
			pstmt.close();
			conn.close();
			throw new IllegalArgumentException("mealID and/or bookingID invalid");
		}
			
		//Log info
		log.info("order inserted");

		//Close connections
		rs1.close();
		rs2.close();
		pstmt.close();
		pstmt1.close();
		pstmt2.close();
		conn.commit();
		conn.close();
	}

	@Override
	ArrayList<Booking> getBookings(long startTime, long endTime) throws Exception {
		
		//Variable declaration
		ArrayList<Booking> bookings = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		//Connection set up.
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		conn.setAutoCommit(false);

		//Create connection to database
		stmt = conn.createStatement();
		
		//Query
		String retrieve = "SELECT Booking.*, RestaurantTable.description, RestaurantTable.size FROM"																+
						  " Booking INNER JOIN RestaurantTable" 										+
						  " ON Booking.tableID = RestaurantTable.ID"	 								+
						  " WHERE unixStart >= "	+	startTime	+	" AND unixStart < "	+	endTime	+
						  " OR unixEnd > "		+	startTime	+	" AND unixEnd <= "	+	endTime	+
						  " OR unixStart <= "		+	startTime	+	" AND unixEnd >= "	+	endTime	+
						  ";";
		
		//Execute Query
		rs = stmt.executeQuery(retrieve);
		
		//Retrieve booking objects
		while(rs.next()) {
			
			//Retrieve data
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

		//Close connections.
		rs.close();
		stmt.close();
		conn.commit();
		conn.close();

		return bookings;
	}

	@Override
	ArrayList<Table> getAvailableTables(long startTime, long endTime) throws Exception {
		
		//Variable declaration
		ArrayList<Table> availableTables = new ArrayList<>();
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		
		//Connection set up.
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		conn.setAutoCommit(false);

		//Connect to database
		stmt = conn.createStatement();
		
		//Query
		String retrieve = "SELECT * FROM RestaurantTable"														+
						  " except"																				+
						  " SELECT RestaurantTable.* FROM"														+
						  " RestaurantTable INNER JOIN Booking" 												+
						  " ON RestaurantTable.ID = Booking.tableID" 											+
						  " WHERE Booking.unixStart >= " + startTime + " AND Booking.unixStart < " + endTime 	+
						  " OR Booking.unixEnd > "+ startTime + " AND Booking.unixEnd <= " + endTime 			+
						  " OR Booking.unixStart < " + startTime + " AND Booking.unixEnd > " + endTime; 

		//Execute Query
		rs = stmt.executeQuery(retrieve);
		
		//Retrieve table objects
		while(rs.next()) {
			
			int referenceNumber = rs.getInt("ID");
			String description = rs.getString("description");
			int size = rs.getInt("size");

			//Create Table object 
			Table table = new Table(referenceNumber, description, size);
			availableTables.add(table);
		}

		//Log info
		log.info("ResturantTable objects retrieved from database");

		//Close connections
		rs.close();
		stmt.close();
		conn.commit();
		conn.close();

		return availableTables;
	}

	@Override
	ArrayList<Meal> getMeals() throws Exception {
		
		//List of type Meal
		ArrayList<Meal> meals = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		//Connection set up.
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		conn.setAutoCommit(false);

		//Connect to database
		stmt = conn.createStatement();
		
		//Query
		String retrieve = "SELECT * FROM Meal";

		//Execute Query
		rs = stmt.executeQuery(retrieve);

		//Retrieve meal objects
		while(rs.next()) {
			
			int referenceNumber = rs.getInt("ID");
			String name = rs.getString("name");
			String description = rs.getString("description");
			int price = rs.getInt("price");
			String category = rs.getString("category");

			//Create Meal instance 
			Meal meal = new Meal(referenceNumber, name, description, price, category);
			meals.add(meal);
		}

		//Log info
		log.info("Meal objects retrieved from database");

		//Close connections
		rs.close();
		stmt.close();
		conn.commit();
		conn.close();

		return meals;
	}
	
	@Override
	ArrayList<Meal> getOrderedMeals(Booking booking) throws Exception {
		
		//Variable declaration
		ArrayList<Meal> orderedMeals = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		//Connection set up.
		
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		conn.setAutoCommit(false);

		//Query
		String orders = "SELECT Meal.* FROM" 									+
						" RestaurantOrder JOIN Meal" 							+
						" ON RestaurantOrder.mealID = Meal.ID JOIN" 			+
						" Booking ON RestaurantOrder.bookingID = Booking.ID"	+
						" WHERE (RestaurantOrder.bookingID = ?);";
		pstmt = conn.prepareStatement(orders);
		
		//Prepared Statement insertion
		pstmt.setInt(1, booking.getReferenceNumber());
		
		//Execute query
		rs = pstmt.executeQuery();
		
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

		//Close connections
		rs.close();
		pstmt.close();
		conn.commit();
		conn.close();

		return orderedMeals;
	}

	@Override
	ArrayList<Table> getTables() throws Exception {
		
		//Variable declaration
		ArrayList<Table> tables = new ArrayList<>();
		ResultSet rs = null;
		Connection conn = null;
		Statement stmt = null;
		
		//Connection to database
		
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		conn.setAutoCommit(false);
		stmt = conn.createStatement();
		
		//Query
		String retrieve = "SELECT * FROM RestaurantTable";

		//Execute Query
		rs = stmt.executeQuery(retrieve);

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

		//Close connections
		rs.close();
		stmt.close();
		conn.commit();
		conn.close();

		return tables;
	}
	
	@Override
	void removeAllOrders(Booking booking) throws Exception {

		//Variable declaration
		Connection conn = null;
		Statement stmt = null;
		
		//Variable declaration
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		conn.setAutoCommit(false);
		stmt = conn.createStatement();
		
		//Query
		String query = "DELETE FROM RestaurantOrder " 										+
					   "WHERE RestaurantOrder.bookingID = " + booking.getReferenceNumber()	+
					   ";";
		
		//Execute query
		stmt.executeUpdate(query);
		
		//Log info (table created successfully)
		log.info("Tables created successfully");

		//Close connections
		stmt.close();
		conn.commit();
		conn.close();
	}
	
	/**
	 * Method that runs shell
	 * script for testing 
	 * purposes.
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