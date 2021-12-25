package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// import java.sql.ResultSetMetaData;

public class DatabaseConnector {
	private Connection conn;
	final private String databaseLocation;
	final private String username;
	final private String password;

	/**
	 * Database connection info stored here so we can quickly change it for systems as required
	 */
	public DatabaseConnector() {
		conn = null;
		databaseLocation = "jdbc:mysql://localhost:3306/TheatreDB";
		username = "root";
		password = "Pa$$word123";
	}

	public void connect() {
		try {
			conn = DriverManager.getConnection(databaseLocation, username, password);
		} catch (SQLException e) {
			System.out.println("Connection failed.");
			e.printStackTrace();
			return;
		}

		if (conn != null) {
			System.out.println("Connection established.");
		} else {
			System.out.println("Connection null still.");
		}
	}

	/*
	 * 4. Prepare a query statement to run - DONE :) 5. Execute query - DONE
	 */

	public ResultSet runQuery(String sql) {
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(sql,
					ResultSet.TYPE_SCROLL_SENSITIVE, // allows us to move forward and back in the ResultSet
                    ResultSet.CONCUR_UPDATABLE);
			pst.execute();
			
			ResultSet results = pst.getResultSet();
			if (results != null) {
				int rowcount = 0;
				if (results.last()) {
					rowcount = results.getRow();
					results.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first
											// element
				}
				System.out.println(sql + "\n Success.  Result set has " + rowcount + " rows");
			} else {
				System.out.println(sql + "\n Success.  No results returned");
			}
			return results;
		} catch (SQLException e) {
			System.out.println(sql + "\n failed to run.");
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Commented out as this will be handled by the front end and interface controller
	 */
	// 6. Process Results

	// public void printResult(ResultSet rs) {
	// 	ResultSetMetaData rsmd;
	// 	rsmd = null;
	// 	try {
	// 		rsmd = rs.getMetaData();
	// 	} catch (SQLException e) {
	// 		e.printStackTrace();
	// 		System.exit(1);
	// 	}

	// 	try {
	// 		// while there is another row
	// 		while (rs.next()) {
	// 			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
	// 				System.out.print(rsmd.getColumnLabel(i));
	// 				System.out.print(" | ");
	// 			}

	// 			System.out.println();

	// 			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
	// 				System.out.print(rs.getString(i));
	// 				System.out.print(" | ");
	// 			}
				
	// 			System.out.println();
	// 		}
	// 	} catch (SQLException e) {
	// 		e.printStackTrace();
	// 	}
	// }

	public void close() {
		try {
			conn.close();
			System.out.println("Connection closed.");
		} catch (SQLException e) {
			System.out.println("Connection not closed.");
			e.printStackTrace();
		}
	}

}