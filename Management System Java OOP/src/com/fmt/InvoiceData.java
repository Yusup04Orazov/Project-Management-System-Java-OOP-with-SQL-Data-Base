package com.fmt;

/*
 * Author: Yusup Orazov and Keyik Annagulyyeva
 * Date: April 27th, 2022
 * */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class InvoiceData {

	private static final Logger LOGGER = LogManager.getLogger(InvoiceData.class);

	/**
	 * Removes all records from all tables in the database.
	 */
	@SuppressWarnings("resource")
	public static void truncateTable(String tableName) {
		ConnectionFactory connectionFactoryToDatabase = ConnectionFactory.getInstance();
		Connection connectionToDataBase = null;
		PreparedStatement preparedStatement = null;
		try {
			connectionToDataBase = connectionFactoryToDatabase.getConnection();
			preparedStatement = connectionToDataBase.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
			preparedStatement.execute();
			preparedStatement = connectionToDataBase.prepareStatement("DELETE FROM " + tableName);
			preparedStatement.execute();
			preparedStatement = connectionToDataBase.prepareStatement("SET FOREIGN_KEY_CHECKS=1");
			preparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (connectionToDataBase != null) {
					connectionToDataBase.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void clearDatabase() {
		String[] clearingDatabase = { "Email", "InvoiceItem", "Invoice", "Store", "Person", "Item", "Address", "State",
				"Country" };
		for (String dataBaseData : clearingDatabase) {
			truncateTable(dataBaseData);
		}
	}

	// this method is to insert state into state table
	private static int getStateId(String state) throws SQLException {
		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
			// Get the stateId using the state name
			String stateQuery = "SELECT stateId FROM State WHERE stateName = ?";
			PreparedStatement stateStmt = conn.prepareStatement(stateQuery);
			stateStmt.setString(1, state);
			ResultSet stateResult = stateStmt.executeQuery();
			int stateId = -1;
			if (stateResult.next()) {
				stateId = stateResult.getInt("stateId");
			} else {
				// Here I am inserting new state into the State table
				String insertQuery = "INSERT INTO State (stateName) VALUES (?)";
				PreparedStatement insertStmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
				insertStmt.setString(1, state);
				insertStmt.executeUpdate();
				ResultSet keys = insertStmt.getGeneratedKeys();
				if (keys.next()) {
					stateId = keys.getInt(1);
				}
				keys.close();
				insertStmt.close();
			}
			stateResult.close();
			stateStmt.close();

			return stateId;
		} catch (SQLException e) {
			LOGGER.error("Error retrieving state from the database: " + e.getMessage(), e);
			throw e;
		}
	}


	// this method is to insert country into the country table
	private static int getCountryId(String country) throws SQLException {
		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
			// Here I am getting the countryId using the country name
			String countryQuery = "SELECT countryId FROM Country WHERE countryName = ?";
			PreparedStatement countryStmt = conn.prepareStatement(countryQuery);
			countryStmt.setString(1, country);
			ResultSet countryResult = countryStmt.executeQuery();
			int countryId = -1;
			if (countryResult.next()) {
				countryId = countryResult.getInt("countryId");
			} else {
				// Here I am inserting new country into the Country table
				String insertQuery = "INSERT INTO Country (countryName) VALUES (?)";
				PreparedStatement insertStmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
				insertStmt.setString(1, country);
				insertStmt.executeUpdate();
				ResultSet keys = insertStmt.getGeneratedKeys();
				if (keys.next()) {
					countryId = keys.getInt(1);
				}
				keys.close();
				insertStmt.close();
			}
			countryResult.close();
			countryStmt.close();

			return countryId;
		} catch (SQLException e) {
			LOGGER.error("Error retrieving country from the database: " + e.getMessage(), e);
			throw e;
		}
	}

	// this method is to insert address into the address table
	private static int getAddressId(String street, String city, String state, String zip, String country)
	        throws SQLException {
	    int stateId = getStateId(state);
	    int countryId = getCountryId(country);

	    // Using try-with-resources to automatically close the connection
	    try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
	        // Here I am defining the SQL query to check if the address already exists
	        String sql = "SELECT addressId FROM Address WHERE street = ? AND city = ? AND zipCode = ? AND stateId = ? AND countryId = ?";
	        PreparedStatement stmt = conn.prepareStatement(sql);

	        // Here I am setting the parameters for the prepared statement
	        stmt.setString(1, street);
	        stmt.setString(2, city);
	        stmt.setString(3, zip);
	        stmt.setInt(4, stateId);
	        stmt.setInt(5, countryId);

	        // Here I am executing the SQL statement to check if the address already exists
	        ResultSet rs = stmt.executeQuery();

	        // If the address already exists, return the existing address ID
	        if (rs.next()) {
	            int addressId = rs.getInt("addressId");
	            LOGGER.info("Address already exists with ID: " + addressId);
	            rs.close();
	            stmt.close();
	            return addressId;
	        }

	        // If the address does not exist, insert the new address and return the newly
	        // generated address ID
	        sql = "INSERT INTO Address(street, city, zipCode, stateId, countryId) VALUES (?, ?, ?, ?, ?)";
	        stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

	        // Here I am setting the parameters for the prepared statement
	        stmt.setString(1, street);
	        stmt.setString(2, city);
	        stmt.setString(3, zip);
	        stmt.setInt(4, stateId);
	        stmt.setInt(5, countryId);

	        // Here I am executing the SQL statement to insert the new address
	        stmt.executeUpdate();

	        // Here I am getting the auto-generated key for the new address record
	        rs = stmt.getGeneratedKeys();
	        rs.next();
	        int addressId = rs.getInt(1);

	        LOGGER.info("Successfully added Address with ID: " + addressId);

	        rs.close();
	        stmt.close();
	        return addressId;
	    } catch (SQLException e) {
	        LOGGER.error("Error retrieving state from the database: " + e.getMessage(), e);
	        throw e;
	    }
	}


	// this is to get person Id by person's code.
	public static int getPersonIdByCode(String personCode) throws SQLException {
		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
			String sql = "SELECT personId FROM Person WHERE personCode = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, personCode);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("personId");
			} else {
				throw new SQLException("Person with code " + personCode + " couldn't be found");
			}
		} catch (SQLException e) {
			LOGGER.error("Error retrieving person ID: " + e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */

	public static void addPerson(String personCode, String firstName, String lastName, String street, String city,
			String state, String zip, String country) {
		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
			// Here I am checking if the person already exists in the database using their person code
			String checkSql = "SELECT personCode FROM Person WHERE personCode = ?";
			try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
				checkStmt.setString(1, personCode);
				try (ResultSet resultSet = checkStmt.executeQuery()) {
					if (resultSet.next()) {
						LOGGER.warn("Person with code " + personCode + " already exists in the database.");
						return;
					}
				} catch (SQLException e) {
					LOGGER.error("Error executing SELECT statement: " + e.getMessage(), e);
					return;
				}
			} catch (SQLException e) {
				LOGGER.error("Error creating prepared statement: " + e.getMessage(), e);
				return;
			}

			// Get the addressId using the address details
			int addressId = getAddressId(street, city, state, zip, country);

			// Here I am defining the SQL query to insert the new person
			String sql = "INSERT INTO Person(personCode, firstName, lastName, addressId) VALUES (?, ?, ?, ?)";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {

				// Here I am setting the parameters for the prepared statement
				stmt.setString(1, personCode);
				stmt.setString(2, firstName);
				stmt.setString(3, lastName);
				stmt.setInt(4, addressId);

				// Here I am executing the SQL statement to insert the new person
				stmt.executeUpdate();

				LOGGER.info("Succesfully added Person ");
			} catch (SQLException e) {
				LOGGER.error("Error adding person to the database: " + e.getMessage(), e);
			}
		} catch (SQLException e) {
			LOGGER.error("Error getting connection: " + e.getMessage(), e);
		}
	}

	/**
	 * This method Adds an email record corresponding person record corresponding to
	 * the provided <code>personCode</code>
	 *
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {
		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
			// Here I am checking if email already exists for the given personCode
			String sql = "SELECT e.emailId FROM Email e " + "INNER JOIN Person p ON e.personId = p.personId "
					+ "WHERE p.personCode = ? AND e.userName = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, personCode);
				pstmt.setString(2, email);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					LOGGER.info("Email already exists for the given person");
					return;
				}
			} catch (SQLException e) {
				LOGGER.error("Error checking email in the database: " + e.getMessage(), e);
				return;
			}

			// Email doesn't already exist, so insert it
			sql = "INSERT INTO Email (userName, personId) "
					+ "SELECT ?, p.personId FROM Person p WHERE p.personCode = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, email);
				pstmt.setString(2, personCode);
				pstmt.executeUpdate();
				LOGGER.info("Added Email ");
			} catch (SQLException e) {
				LOGGER.error("Error adding email to the database: " + e.getMessage(), e);
			}
		} catch (SQLException e) {
			LOGGER.error("Error getting connection: " + e.getMessage(), e);
		}
	}

	// This method is to get the Store ID by Store Code
	public static int getStoreIdByCode(String storeCode) {
		int storeId = 0;
		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
			String sql = "SELECT storeId FROM Store WHERE storeCode = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, storeCode);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				storeId = rs.getInt("storeId");
			}
		} catch (SQLException e) {
			LOGGER.error("Error closing database connection: " + e.getMessage(), e);
		}
		return storeId;
	}

	// this method is to get the store code by store Id
	public static String getStoreCodeById(int storeId) {
		String storeCode = null;
		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
			String sql = "SELECT storeCode FROM Store WHERE storeId = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, storeId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				storeCode = rs.getString("storeCode");
			}
		} catch (SQLException e) {
			LOGGER.error("Error closing database connection: " + e.getMessage(), e);
		}
		return storeCode;
	}

	/**
	 * This method Adds a store record to the database managed by the person
	 * identified by the given code.
	 *
	 * @param storeCode
	 * @param managerCode
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */

	public static void addStore(String storeCode, String managerCode, String street, String city, String state,
			String zip, String country) {
		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
			// Here I am checking if the store already exists in the database using its store code
			String checkSql = "SELECT storeCode FROM Store WHERE storeCode = ?";
			try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
				checkStmt.setString(1, storeCode);
				try (ResultSet resultSet = checkStmt.executeQuery()) {
					if (resultSet.next()) {
						LOGGER.warn("Store with code " + storeCode + " already exists in the database.");
						return;
					}
				} catch (SQLException e) {
					LOGGER.error("Error executing SELECT statement: " + e.getMessage(), e);
					return;
				}
			} catch (SQLException e) {
				LOGGER.error("Error creating prepared statement: " + e.getMessage(), e);
				return;
			}

			// Here I am getting the managerId and addressId using the person and address
			// details
			int managerId = getPersonIdByCode(managerCode);
			int addressId = getAddressId(street, city, state, zip, country);

			// Here I am defining the SQL query to insert the new store
			String sql = "INSERT INTO Store(storeCode, managerId, addressId) VALUES (?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);

			// Here I am setting the parameters for the prepared statement
			stmt.setString(1, storeCode);
			stmt.setInt(2, managerId);
			stmt.setInt(3, addressId);

			// Here I am executing the SQL statement to insert the new store
			stmt.executeUpdate();

			LOGGER.info("Successfully added Store ");
		} catch (SQLException e) {
			LOGGER.error("Error closing database connection: " + e.getMessage(), e);
		}
	}

	/**
	 * This method Adds a product record to the database with the given
	 * <code>code</code>, <code>name</code> and <code>unit</code> and
	 * <code>pricePerUnit</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param unit
	 * @param pricePerUnit
	 */

	public static void addProduct(String code, String name, String unit, double pricePerUnit) {
		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
			//  Here I am checking if the product already exists
			PreparedStatement pstmtCheck = conn
					.prepareStatement("SELECT itemCode FROM Item WHERE itemCode = ? AND typeItem = 'P'");
			pstmtCheck.setString(1, code);
			ResultSet rs = pstmtCheck.executeQuery();
			if (rs.next()) {
				LOGGER.error("Product with code: " + code + " already exists in the database");
				return;
			}

			// If the product doesn't exist, insert the new product record
			PreparedStatement pstmt = conn.prepareStatement(
					"INSERT INTO Item (itemCode, nameItem, unit, unitPrice, typeItem) VALUES (?, ?, ?, ?, 'P')");
			pstmt.setString(1, code);
			pstmt.setString(2, name);
			pstmt.setString(3, unit);
			pstmt.setDouble(4, pricePerUnit);
			pstmt.executeUpdate();

			// Here I am logging the successful insertion of a new product
			LOGGER.info("Succesfully added product with code: " + code);
		} catch (SQLException e) {
			// Here I am logging the error and print stack trace
			LOGGER.error("Error adding product to the database: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * This method Adds an equipment record to the database with the given
	 * <code>code</code>, <code>name</code> and <code>modelNumber</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param modelNumber
	 */

	public static void addEquipment(String code, String name, String modelNumber) {
		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
			//  Here I am checking if the equipment already exists
			PreparedStatement pstmtCheck = conn
					.prepareStatement("SELECT itemCode FROM Item WHERE itemCode = ? AND typeItem = 'E'");
			pstmtCheck.setString(1, code);
			ResultSet rs = pstmtCheck.executeQuery();
			if (rs.next()) {
				LOGGER.error("Equipment with code: " + code + " already exists in the database");
				return;
			}

			// If the equipment doesn't exist, insert the new equipment record
			PreparedStatement pstmt = conn
					.prepareStatement("INSERT INTO Item (itemCode, nameItem, model, typeItem) VALUES (?, ?, ?, 'E')");
			pstmt.setString(1, code);
			pstmt.setString(2, name);
			pstmt.setString(3, modelNumber);
			pstmt.executeUpdate();

			LOGGER.info("Successfully added equipment");
		} catch (SQLException e) {
			LOGGER.error("Error adding equipment to the database: " + e.getMessage());
		}
	}

	/**
	 * This method Adds a service record to the database with the given
	 * <code>code</code>, <code>name</code> and <code>costPerHour</code>.
	 *
	 * @param itemCode
	 * @param name
	 * @param modelNumber
	 */

	public static void addService(String code, String name, double costPerHour) {
		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
			//  Here I am checking if the service already exists
			PreparedStatement pstmtCheck = conn
					.prepareStatement("SELECT itemCode FROM Item WHERE itemCode = ? AND typeItem = 'S'");
			pstmtCheck.setString(1, code);
			ResultSet rs = pstmtCheck.executeQuery();
			if (rs.next()) {
				LOGGER.error("Service with code: " + code + " already exists in the database");
				return;
			}

			// If the service doesn't exist, insert the new service record
			PreparedStatement pstmt = conn.prepareStatement(
					"INSERT INTO Item (itemCode, nameItem, hourlyRate, typeItem) VALUES (?, ?, ?, 'S')");
			pstmt.setString(1, code);
			pstmt.setString(2, name);
			pstmt.setDouble(3, costPerHour);
			pstmt.executeUpdate();

			LOGGER.info("Successfully added Service with code: " + code);
		} catch (SQLException e) {
			LOGGER.error("Error adding service with code: " + code, e);
		}
	}

	// this method is to get invoice's Id by its code
	public static int getInvoiceIdByCode(String invoiceCode) {
		int invoiceId = 0;
		try (Connection conn = ConnectionFactory.getInstance().getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT invoiceId FROM Invoice WHERE invoiceCode = ?")) {
			stmt.setString(1, invoiceCode);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					invoiceId = rs.getInt("invoiceId");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return invoiceId;
	}

	// this method is to get Item's Id by its code
	public static int getItemIdByCode(String itemCode) {
		int itemId = 0;
		try (Connection conn = ConnectionFactory.getInstance().getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT itemId FROM Item WHERE itemCode = ?")) {
			stmt.setString(1, itemCode);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					itemId = rs.getInt("itemId");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return itemId;
	}

	/**
	 * This method Adds an invoice record to the database with the given data.
	 *
	 * @param invoiceCode
	 * @param storeCode
	 * @param customerCode
	 * @param salesPersonCode
	 * @param invoiceDate
	 */
	// make it get StoreCode by StoreID instead

	public static void addInvoice(String invoiceCode, String storeCode, String customerCode, String salesPersonCode,
			String invoiceDate) {
		try (Connection conn = ConnectionFactory.getInstance().getConnection();
				PreparedStatement pstmtCheck = conn
						.prepareStatement("SELECT invoiceCode FROM Invoice WHERE invoiceCode = ?");
				PreparedStatement pstmtInsert = conn.prepareStatement(
						"INSERT INTO Invoice (invoiceCode, storeId, customerId, salesPersonId, invoiceDate) VALUES (?, ?, ?, ?, ?)")) {

			// Check if the invoice already exists
			pstmtCheck.setString(1, invoiceCode);
			ResultSet rs = pstmtCheck.executeQuery();
			if (rs.next()) {
				LOGGER.error("Invoice with code: " + invoiceCode + " already exists in the database");
				return;
			}

			// Here I am getting the storeId using the storeCode
			int storeId = getStoreIdByCode(storeCode);

			// Here I am setting the parameters for the prepared statement to insert the new
			// invoice
			pstmtInsert.setString(1, invoiceCode);
			pstmtInsert.setInt(2, storeId);
			pstmtInsert.setInt(3, getPersonIdByCode(customerCode));
			pstmtInsert.setInt(4, getPersonIdByCode(salesPersonCode));
			pstmtInsert.setString(5, invoiceDate);

			// Here I am executing the SQL statement to insert the new invoice
			pstmtInsert.executeUpdate();

			LOGGER.info("Successfully added Invoice");
		} catch (SQLException e) {
			LOGGER.error("Error adding invoice with code: " + invoiceCode, e);
		}
	}

	/**
	 * This method Adds a particular product (identified by <code>itemCode</code>)
	 * to a particular invoice (identified by <code>invoiceCode</code>) with the
	 * specified quantity.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param quantity
	 */

	public static void addProductToInvoice(String invoiceCode, String itemCode, int quantity) {
		try (Connection conn = ConnectionFactory.getInstance().getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("INSERT INTO InvoiceItem (invoiceId, itemId, quantity) VALUES (?, ?, ?)")) {
			// Here I am getting the invoiceId using the invoiceCode
			int invoiceId = getInvoiceIdByCode(invoiceCode);

			// Here I am getting the itemId using the itemCode
			int itemId = getItemIdByCode(itemCode);

			// Check if the invoice item already exists
			PreparedStatement pstmtCheck = conn
					.prepareStatement("SELECT * FROM InvoiceItem WHERE invoiceId = ? AND itemId = ?");
			pstmtCheck.setInt(1, invoiceId);
			pstmtCheck.setInt(2, itemId);
			ResultSet rs = pstmtCheck.executeQuery();
			if (rs.next()) {
				LOGGER.error("Invoice item already exists in the database");
				return;
			}

			// Here I am setting the parameters for the prepared statement
			stmt.setInt(1, invoiceId);
			stmt.setInt(2, itemId);
			stmt.setInt(3, quantity);

			// Here I am executing the SQL statement to insert the new invoice item
			stmt.executeUpdate();

			LOGGER.info("Successfully added products ");
		} catch (SQLException e) {
			LOGGER.error("Error adding product to invoice: " + e.getMessage());
		}
	}

	/**
	 * This method Adds a particular equipment <i>purchase</i> (identified by
	 * <code>itemCode</code>) to a particular invoice (identified by
	 * <code>invoiceCode</code>) at the given <code>purchasePrice</code>.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param purchasePrice
	 */

	public static void addEquipmentToInvoice(String invoiceCode, String itemCode, double purchasePrice) {
		try (Connection conn = ConnectionFactory.getInstance().getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO InvoiceItem (invoiceId, itemId, price, typeEquipment) VALUES (?, ?, ?, ?)")) {
			int invoiceId = getInvoiceIdByCode(invoiceCode);
			if (invoiceId == 0) {
				LOGGER.error("Invoice with code " + invoiceCode + " does not exist.");
				return;
			}

			int itemId = getItemIdByCode(itemCode);
			if (itemId == 0) {
				LOGGER.error("Item with code " + itemCode + " does not exist.");
				return;
			}

			// Check if the invoice item already exists in the database
			String checkQuery = "SELECT COUNT(*) FROM InvoiceItem WHERE invoiceId = ? AND itemId = ?";
			PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
			checkStmt.setInt(1, invoiceId);
			checkStmt.setInt(2, itemId);
			ResultSet rs = checkStmt.executeQuery();
			rs.next();
			int count = rs.getInt(1);

			if (count > 0) {
				LOGGER.error("The specified invoice item already exists in the database.");
				return;
			}

			stmt.setInt(1, invoiceId);
			stmt.setInt(2, itemId);
			stmt.setDouble(3, purchasePrice);
			stmt.setString(4, "P");
			stmt.executeUpdate();

			LOGGER.info("Successfully added equipment purchase to the invoice");
		} catch (SQLException e) {
			LOGGER.error("Error adding equipment purchase to invoice: " + e.getMessage());
		}
	}

	/**
	 * This method Adds a particular equipment <i>lease</i> (identified by
	 * <code>itemCode</code>) to a particular invoice (identified by
	 * <code>invoiceCode</code>) with the given 30-day <code>periodFee</code> and
	 * <code>beginDate/endDate</code>.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param amount
	 */

	public static void addEquipmentToInvoice(String invoiceCode, String itemCode, double periodFee, String beginDate,
			String endDate) {
		try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
			//  Here I am checking if invoice exists
			int invoiceId = getInvoiceIdByCode(invoiceCode);
			if (invoiceId == 0) {
				System.out.println("Invoice with code " + invoiceCode + " does not exist.");
				return;
			}

			//  Here I am checking if item exists
			int itemId = getItemIdByCode(itemCode);
			if (itemId == 0) {
				System.out.println("Item with code " + itemCode + " does not exist.");
				return;
			}

			//  Here I am checking if invoice item already exists
			String checkQuery = "SELECT COUNT(*) FROM InvoiceItem WHERE invoiceId = ? AND itemId = ?";
			try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
				checkStmt.setInt(1, invoiceId);
				checkStmt.setInt(2, itemId);
				ResultSet rs = checkStmt.executeQuery();
				rs.next();
				int count = rs.getInt(1);

				if (count > 0) {
					System.out.println("The specified invoice item already exists in the database.");
					return;
				}
			}

			// Insert the invoice item
			String insertQuery = "INSERT INTO InvoiceItem (invoiceId, itemId, price, startLease, endLease, typeEquipment) VALUES (?, ?, ?, ?, ?, ?)";
			try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
				stmt.setInt(1, invoiceId);
				stmt.setInt(2, itemId);
				stmt.setDouble(3, periodFee);
				stmt.setString(4, beginDate);
				stmt.setString(5, endDate);
				stmt.setString(6, "L");
				stmt.executeUpdate();
				System.out.println("Successfully added equipment lease to the invoice");
			}

		} catch (SQLException e) {
			System.out.println("Error adding equipment lease to invoice: " + e.getMessage());
		}
	}

	/**
	 * This method Adds a particular service (identified by <code>itemCode</code>)
	 * to a particular invoice (identified by <code>invoiceCode</code>) with the
	 * specified number of hours.
	 *
	 * @param invoiceCode
	 * @param itemCode
	 * @param billedHours
	 */
	public static void addServiceToInvoice(String invoiceCode, String itemCode, double billedHours) {
		try (Connection conn = ConnectionFactory.getInstance().getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"INSERT INTO InvoiceItem (invoiceId, itemId, hoursBilled) VALUES (?, ?, ?)")) {

			int invoiceId = getInvoiceIdByCode(invoiceCode);
			if (invoiceId == 0) {
				System.out.println("Invoice with code " + invoiceCode + " does not exist.");
				return;
			}

			int itemId = getItemIdByCode(itemCode);
			if (itemId == 0) {
				System.out.println("Item with code " + itemCode + " does not exist.");
				return;
			}

			//  Here I am checking if the invoice item already exists in the database
			String checkQuery = "SELECT COUNT(*) FROM InvoiceItem WHERE invoiceId = ? AND itemId = ?";
			PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
			checkStmt.setInt(1, invoiceId);
			checkStmt.setInt(2, itemId);
			ResultSet rs = checkStmt.executeQuery();
			rs.next();
			int count = rs.getInt(1);

			if (count > 0) {
				LOGGER.error("The specified invoice item already exists in the database.");
				return;
			}

			stmt.setInt(1, invoiceId);
			stmt.setInt(2, itemId);
			stmt.setDouble(3, billedHours);
			stmt.executeUpdate();

			LOGGER.info("Successfully added service to the invoice");
		} catch (SQLException e) {
			System.out.println("Error adding service to invoice: " + e.getMessage());
		}
	}

}