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

		//fields initialisation
		this.dbName = dbName;

		//Call setUp()
		setUp();
		
		getBookings(0, 1456664);
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

		//Create new table in the database
		stmt = conn.createStatement();
		String booking = "CREATE TABLE IF NOT EXISTS Booking "   +
					     "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"  +
				         " customerName TEXT NOT NULL,"            +
				         " phoneNumber TEXT NOT NULL,"             + 
				         " email TEXT NOT NULL,"                   +
				         " partySize INT NOT NULL,"                +  
				         " unixDate NUMERIC NOT NULL"              +
				         ");";

		//Execute query
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
		String insert = "INSERT INTO Booking(customerName, phoneNumber, email, partySize, unixDate)" +
				        "VALUES(?, ?, ?, ?, ?);";
		pstmt = conn.prepareStatement(insert);

		//insert into database the Booking entry
		pstmt.setString(1, booking.getCustomerName());
		pstmt.setString(2, booking.getPhoneNumber());
		pstmt.setString(3, booking.getEmail());
		pstmt.setInt(4, booking.getPartySize());
		pstmt.setLong(5, booking.getUnixDate());
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
		System.out.println(dbName + " opened");

		//Initialize prepared statement execution to retrieve bookings
		stmt = conn.createStatement();
		String retrieve = "SELECT ID, customerName, phoneNumber, email, partySize, unixDate FROM Booking " +
						  "WHERE unixDate >= " + startTime + " AND unixDate <= " + endTime + ";";
		
		//Retrieve booking objects
		rs = stmt.executeQuery(retrieve);

		//Retrieve max ID from ResultSet
		while(rs.next()) {
			//Retrieve data
			int id = rs.getInt("ID");
			String customerName = rs.getString("customerName");
			String phoneNumber = rs.getString("phoneNumber");
			String email = rs.getString("email");
			int partySize = rs.getInt("partySize");
			long unixDate = rs.getLong("unixDate");
			
			//Create Booking instance 
			Booking booking = new Booking(id, customerName, phoneNumber, email, partySize, unixDate);
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
}