/*
 * Author: Yusup Orazov and Keyik Annagulyyeva
 * Date: May 3rd, 2022
 * */

package com.fmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//conneciton factory class 
public class ConnectionFactory {
	private static ConnectionFactory instance;
	private static final String URL = "jdbc:mysql://cse.unl.edu/yorazov?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private static final String USERNAME = "yorazov";
	private static final String PASSWORD = "E7hAh164";

	// Constructor to prevent direct instantiation
	private ConnectionFactory() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// getter for instance
	public static ConnectionFactory getInstance() {
		if (instance == null) {
			instance = new ConnectionFactory();
		}
		return instance;
	}

	// Create and return a new database connection
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}
}
