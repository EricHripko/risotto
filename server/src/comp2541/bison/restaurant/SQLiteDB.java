package comp2541.bison.restaurant;

import java.sql.*;

public class SQLiteDB extends Database {

	/*
	 * String dbName
	 */
	private String dbName = null;

	/*
	 * Connection object
	 */
	private Connection conn = null;

	/*
	 * Statement object
	 */
	private Statement stmt = null;

	//public constructor
	public SQLiteDB(String dbName) {
		super(dbName);

		//fields initialization
		this.dbName = dbName;
	}

	/**
	 * 
	 * connect to existing database, otherwise it creates one with the specified name.
	 */
	public void setUp() {

		try {
			//Connect to database
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);

			//Create new table in the database
			stmt = conn.createStatement();
			String query = "CREATE TABLE IF NOT EXISTS Booking " +
						   "(customerName TEXT     NOT NULL,"    +
					       " phoneNumber  TEXT     NOT NULL, "   + 
					       " email        TEXT     NOT NULL,"    +
					       " partySize    INT      NOT NULL,"    +  
					       " date         TEXT     NOT NULL,"    +
					       " time         TEXT     NOT NULL,"    +
					       " primary key(customerName,"          +
					       				"phoneNumber, "	         +
					       				"date,"                  +
					       				"time)"					 +
					       	");";
			//Execute query
			stmt.executeUpdate(query);
			
			//Free resources
			stmt.close();
			conn.close();
		} 
		catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		
		//Console message
		System.out.println("Set up completed/updated");
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
	void insertBooking(String customerName, String phoneNumber, String email, int partySize, String date, String time) {

		//Call setUp() method for database update
		setUp();
		
		try {
			//Connect to database
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);

			//Insert new row in the database
			stmt = conn.createStatement();
			String query = "INSERT INTO Booking (customerName, phoneNumber, email, partySize, date, time) " +
	                       "VALUES ('"+ customerName + "', '" + phoneNumber + "', '" + email + "', '" + partySize + "', '" + date +"', '" + time + "');";        
			
			//Execute query
			stmt.executeUpdate(query);

			//Free resources
			stmt.close();
			conn.close();
		} 
		catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}

		//Console message
		System.out.println("Booking completed");
	}
}

