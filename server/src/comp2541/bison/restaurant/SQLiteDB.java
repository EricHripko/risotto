package comp2541.bison.restaurant;

import java.sql.*;
import java.util.ArrayList;

import org.apache.log4j.*;
public class SQLiteDB extends Database {

	/*
	 * String dbName
	 */
	private String dbName = null;
	static Logger log = Logger.getLogger(SQLiteDB.class.getName());

	//public constructor
	public SQLiteDB(String dbName) throws Exception {
		super(dbName);

		//fields initialization
		this.dbName = dbName;

		//Call setUp()
		setUp();
	}

	/**
	 * 
	 * connect to existing database, otherwise it creates one with the specified name.
	 */
	private void setUp() throws Exception {

		//Declare DB's objects
		Connection conn = null;
		Statement stmt = null;

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

		//Execute queries
		stmt.executeUpdate(table);
		stmt.executeUpdate(booking);
		
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

		//Create DB's object
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		//Connect to database
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		conn.setAutoCommit(false);
		System.out.println(dbName + " opened");

		//Initialize prepared statement execution for insertion into the DB
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
		
		System.out.println("Reference: " + ref);

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
	ArrayList<Booking> getBookings(long startTime, long endTime) throws Exception {
		
		//Create DB's object
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		//Connect to database
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		conn.setAutoCommit(false);

		//Initialize prepared statement execution to retrieve bookings
		stmt = conn.createStatement();
		String retrieve = "SELECT * FROM Booking "+
						  "WHERE unixStart >= "	+	startTime	+	" OR unixStart <= "	+	endTime	+
						  "OR unixEnd >= "		+	startTime	+	" OR unixEnd <= "	+	endTime	+ 
						  ";";
		
		//Retrieve booking objects
		rs = stmt.executeQuery(retrieve);

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

		//Create DB's object
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		//Connect to database
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		conn.setAutoCommit(false);

		//Initialize prepared statement execution to retrieve bookings
		stmt = conn.createStatement();
		String retrieve = "SELECT * FROM RestaurantTable"														+
						  " except"													+
						  " SELECT RestaurantTable.* FROM"											+
						  " RestaurantTable INNER JOIN Booking" +
						  " ON RestaurantTable.ID = Booking.tableID" +
						  " WHERE Booking.unixStart >= " + startTime + " AND Booking.unixStart <= " + endTime 	+
						  " OR Booking.unixEnd >= "+ startTime + " AND Booking.unixEnd <= " + endTime +
						  " OR Booking.unixStart < " + startTime + " AND Booking.unixEnd > " + endTime; 

		//Retrieve booking objects
		rs = stmt.executeQuery(retrieve);

		//Retrieve booking objects from ResultSet
		while(rs.next()) {
			//Retrieve data
			int referenceNumber = rs.getInt("ID");
			String description = rs.getString("description");
			int size = rs.getInt("size");
			
			System.out.println("ID: " + referenceNumber);
			System.out.println("Desc: " + description);
			System.out.println("Size: " + size + "\n");

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
}