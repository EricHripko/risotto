package comp2541.bison.restaurant;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

import org.apache.log4j.*;

import com.sun.xml.internal.txw2.IllegalAnnotationException;

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
	 * Connection object
	 */
	private Connection conn = null;
	
	/**
	 * Statement object
	 */
	private Statement stmt = null;
	
	/**
	 * PreparedStatement object
	 */
	private PreparedStatement pstmt = null;
	
	/**
	 * Result Set object
	 */
	private ResultSet rs = null;
	

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
		executeScript("dropScriptDB.sh");

		//Call setUp()
		setUp();
		
		//Import data into tables
		executeScript("importScriptDB.sh");
	}

	/**
	 * setUp()
	 * 
	 * connect to existing database, otherwise it creates one with the specified name.
	 */
	private void setUp() throws Exception {

		//Connect to database
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		conn.setAutoCommit(false);

		//Create needed tables in the database
		stmt = conn.createStatement();
		
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

		//Execute queries
		stmt.executeUpdate(table);
		stmt.executeUpdate(booking);
		stmt.executeUpdate(meal);
		stmt.executeUpdate(order);
		
		//Log info (table created successfully)
		log.info("Tables created successfully");
		
		//Free resources + commit
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

		//reference variable
		int ref = -1;

		//Connect to database
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		conn.setAutoCommit(false);
		log.info(dbName + " opened");

		//Initialize prepared statement execution for insertion into the DB (Booking)
		String insert = "INSERT INTO Booking(customerName, phoneNumber, email, partySize, unixStart, unixEnd, tableID)" +
				        "VALUES(?, ?, ?, ?, ?, ?, ?);";
		pstmt = conn.prepareStatement(insert);

		//insert into database the Booking entry
		pstmt.setString(1, booking.getCustomerName());
		pstmt.setString(2, booking.getPhoneNumber());
		pstmt.setString(3, booking.getEmail());
		pstmt.setInt(4, booking.getPartySize());
		pstmt.setLong(5, booking.getUnixStart());
		pstmt.setLong(6, booking.getUnixEnd());
		pstmt.setInt(7, booking.getTable().getId());
		
		//Execute insert into the DB
		pstmt.executeUpdate();

		//Log info (Booking insert success)
		log.info("booking inserted");

		//Execute query to retrieve ID from Booking table
		stmt = conn.createStatement();
		String idQuery = "SELECT MAX(ID) FROM Booking;";
		rs = stmt.executeQuery(idQuery);

		//Retrieve max ID from ResultSet
		if(rs.next()) {
			ref = rs.getInt(1);
		}
		
		//Free resources + commit
		pstmt.close();
		conn.commit();
		conn.close();

		//Log info (Database closed)
		log.info("Database closed");

		//return reference to server
		return ref;
	}
	
	@Override
	void insertOrder(Order order) throws Exception {
		
		//variable used for checking
		boolean check = false;

		//Connect to database
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		conn.setAutoCommit(false);
		System.out.println(dbName + " opened");
		
		//Statements string preparation
		String mealID 	 = "SELECT ID FROM Meal" +
						   " WHERE ID = ?;";
		
		String bookingID = "SELECT ID FROM Booking " +
						   " WHERE ID = ?;";
		
		String insert = "INSERT INTO RestaurantOrder(mealID, bookingID)" +
						"VALUES(?, ?);";
		
		//Prepared statement initialization
		pstmt = conn.prepareStatement(mealID);
		pstmt = conn.prepareStatement(bookingID);
		pstmt = conn.prepareStatement(insert);
		
		//Insert into database the Order entry
		pstmt.setInt(1, order.getMealId());
		pstmt.setInt(2, order.getBookingId());

		//Execute mealID query
		rs = pstmt.executeQuery(mealID);
		if(rs.next()) {
			check = true;
		}
		
		//Execute bookingID query
		rs = pstmt.executeQuery(bookingID);
		if(rs.next()) {
			check = true;
		}
		
		//Execute insert query given condition
		if(check == true) {
			pstmt.setInt(3, order.getMealId());
			pstmt.setInt(4, order.getBookingId());
			pstmt.executeUpdate(insert);
		}
		else 
			throw new IllegalAnnotationException("mealID and bookingID not valid");
		
		

		//Log info (Booking insert success)
		log.info("order inserted");

		//Free resources + commit
		pstmt.close();
		conn.commit();
		conn.close();

		//Log info (Database closed)
		log.info("Database closed");
	}

	@Override
	ArrayList<Booking> getBookings(long startTime, long endTime) throws Exception {
		
		//Connect to database
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		conn.setAutoCommit(false);

		//Initialize prepared statement execution to retrieve bookings
		stmt = conn.createStatement();
		String retrieve = "SELECT * FROM Booking"+
						  " WHERE unixStart >= "	+	startTime	+	" OR unixStart <= "	+	endTime	+
						  " OR unixEnd >= "		+	startTime	+	" OR unixEnd <= "	+	endTime	+ 
						  ";";
		
		//Retrieve booking objects
		rs = stmt.executeQuery(retrieve);
		
		//Clear tables from previous data
		tables.clear();

		//Retrieve booking objects from ResultSet
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
			
			//Create Booking instance 
			Booking booking = new Booking(referenceNumber, 
					                      customerName, 
					                      phoneNumber, 
					                      email, 
					                      partySize, 
					                      unixStart, 
					                      unixEnd,
					                      tableID);
			bookings.add(booking);
		}
		
		//Log info
		log.info("Booking objects retrieved from database");

		//Free resources + commit
		stmt.close();
		conn.commit();
		conn.close();

		//return reference to server
		return bookings;
	}

	@Override
	ArrayList<Table> getAvailableTables(long startTime, long endTime) throws Exception {

		//Connect to database
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		conn.setAutoCommit(false);

		//Initialize prepared statement execution to retrieve bookings
		stmt = conn.createStatement();
		String retrieve = "SELECT * FROM RestaurantTable"														+
						  " except"																				+
						  " SELECT RestaurantTable.* FROM"														+
						  " RestaurantTable INNER JOIN Booking" 												+
						  " ON RestaurantTable.ID = Booking.tableID" 											+
						  " WHERE Booking.unixStart >= " + startTime + " AND Booking.unixStart <= " + endTime 	+
						  " OR Booking.unixEnd >= "+ startTime + " AND Booking.unixEnd <= " + endTime 			+
						  " OR Booking.unixStart < " + startTime + " AND Booking.unixEnd > " + endTime; 

		//Retrieve booking objects
		rs = stmt.executeQuery(retrieve);
		
		//Clear tables from previous data
		tables.clear();

		//Retrieve booking objects from ResultSet
		while(rs.next()) {
			//Retrieve data
			int referenceNumber = rs.getInt("ID");
			String description = rs.getString("description");
			int size = rs.getInt("size");

			//Create Booking instance 
			Table table = new Table(referenceNumber, description, size);
			tables.add(table);
		}

		//Log info
		log.info("ResturantTable objects retrieved from database");

		//Free resources + commit
		stmt.close();
		conn.commit();
		conn.close();

		//return reference to server
		return tables;
	}

	@Override
	ArrayList<Meal> getMeals() throws Exception {
		
		//Connect to database
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		conn.setAutoCommit(false);

		//Initialize prepared statement execution to retrieve bookings
		stmt = conn.createStatement();
		String retrieve = "SELECT * FROM Meal";

		//Retrieve booking objects
		rs = stmt.executeQuery(retrieve);

		//Clear tables from previous data
		meals.clear();

		//Retrieve booking objects from ResultSet
		while(rs.next()) {
			//Retrieve data
			int referenceNumber = rs.getInt("ID");
			String name = rs.getString("name");
			String description = rs.getString("description");
			int price = rs.getInt("price");
			String category = rs.getString("category");

			//Create Booking instance 
			Meal meal = new Meal(referenceNumber, name, description, price, category);
			meals.add(meal);
		}

		//Log info
		log.info("Meal objects retrieved from database");

		//Free resources + commit
		stmt.close();
		conn.commit();
		conn.close();

		//return reference to server
		return meals;
	}
	
	/**
	 * Method that runs shell
	 * script for testing 
	 * purposes
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