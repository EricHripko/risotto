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
	}

	/**
	 * 
	 * connect to existing database, otherwise it creates one with the specified name.
	 */
	public void setUp() {

		try {
			//Connect to database
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);

			//Create new table in the database
			Statement stmt = conn.createStatement();
			String query = "CREATE TABLE IF NOT EXISTS Booking " +
						   "(ID			  INT      NOT NULL,"    +
						   " customerName TEXT     NOT NULL,"    +
					       " phoneNumber  TEXT     NOT NULL, "   + 
					       " email        TEXT     NOT NULL,"    +
					       " partySize    INT      NOT NULL,"    +  
					       " dateTime     NUMERIC  NOT NULL,"    +
					       " primary key(ID)"         			 +
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
	void insertBooking(int ID, String customerName, String phoneNumber, String email, int partySize, long dateTime) {
		
		try {
			//Connect to database
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);

			//Insert new row in the database
			Statement stmt = conn.createStatement();
			String query = "INSERT INTO Booking (ID, customerName, phoneNumber, email, partySize, dateTime) " +
	                       "VALUES ('"+ ID + ", '"+ customerName + "', '" + phoneNumber + "', '" + email + "', '" + partySize + "', '" + dateTime + "');";        
			
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