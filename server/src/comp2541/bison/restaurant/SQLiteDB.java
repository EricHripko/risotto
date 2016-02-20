package comp2541.bison.restaurant;

import java.sql.*;

public class SQLiteDB extends Database {

	/*
	 * String dbName
	 */
	private String dbName = null;


	//public constructor
	public SQLiteDB(String dbName) {
		super(dbName);

		//fields initialisation
		this.dbName = dbName;
		
		//Call setUp()
		setUp();
	}

	/**
	 * 
	 * connect to existing database, otherwise it creates one with the specified name.
	 */
	private void setUp() {
		
		//Declare DB's objects
		Connection conn = null;
		Statement stmt = null;

		try {
			//Connect to database
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			conn.setAutoCommit(false);

			//Create new table in the database
			stmt = conn.createStatement();
			String query = "CREATE TABLE IF NOT EXISTS Booking " +
						   "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
						   " customerName TEXT NOT NULL,"+
					       " phoneNumber TEXT NOT NULL,"+ 
					       " email TEXT NOT NULL,"+
					       " partySize INT NOT NULL,"+  
					       " dateTime NUMERIC NOT NULL"+
					       	");";
			//Execute query
			stmt.executeUpdate(query);
			System.out.println("Table created successfully");
			
			//Free resources + commit
			stmt.close();
			conn.commit();
			conn.close();
			
		} 
		catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		
		//Console message
		System.out.println("Set up completed");
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
	public int insertBooking(Booking booking) {
		
		//reference variable
		int ref = -1;
		
		//Create DB's object
		Connection conn = null;
		Statement stmt = null;

		//Database booking query
		try {
			//Connect to database
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			//conn.setAutoCommit(false);
			System.out.println(dbName + " opened");

			//Insert new row in the database
			stmt = conn.createStatement();
			
			String query1 = "INSERT INTO Booking(customerName, phoneNumber, email, partySize, dateTime) " +
	                       "VALUES('"                          + 
	                       booking.getCustomerName()  + "', '" + 
	                       booking.getPhoneNumber()  + "', '" + 
	                       booking.getEmail()         + "', "  + 
	                       booking.getPartySize()     + ", "   + 
	                       booking.getUnixDate()      + ");";    
	                     
			
			//Execute query for insert booking
			stmt.executeUpdate(query1);
			
			//Get ID as reference for server
			String query2 = "SELECT ID FROM Booking;";
			
			//Execute query to retrieve the ID
			ResultSet rs = stmt.executeQuery(query2);
			while(rs.next()) {
				ref = rs.getInt("ID");
			}
			System.out.println("Reference: " + ref);
			
			//Free resources + commit
			stmt.close();
			//conn.commit();
			conn.close();
		} 
		catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}

		//Console message
		System.out.println("Booking completed");
		
		//return reference to server
		return ref;
	}
}