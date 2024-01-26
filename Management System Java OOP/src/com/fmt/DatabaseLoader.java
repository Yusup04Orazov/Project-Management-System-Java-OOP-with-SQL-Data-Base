/*
 * Author: Yusup Orazov and Keyik Annagulyyeva
 * Date: May 3rd, 2022
 * */

package com.fmt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.HashMap;

// DatabaseLoader Class
public class DatabaseLoader {

	private static final Logger LOGGER = LogManager.getLogger(DatabaseLoader.class);

	// creating list for email
	public static List<String> getEmailById(int personId) {

		List<String> listOfEmail = new ArrayList<>();

		LOGGER.debug("Getting email by id: " + personId);

		java.sql.Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getInstance().getConnection();

			String sql = "SELECT userName FROM Email WHERE personId = ?;";

			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, personId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String email = rs.getString("userName");
				listOfEmail.add(email);
			}
			// closing
			stmt.close();
			rs.close();
			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return listOfEmail;
	}

	// maps person retreived from database
	public static Map<Integer, Person> personConnectToDatabase() {
		LOGGER.debug("Connecting to database and retrieving persons");

		Map<Integer, Person> mapPerson = new HashMap<>();
		java.sql.Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Person p = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = ConnectionFactory.getInstance().getConnection();
			String sql = "SELECT p.personId, p.personCode, p.firstName, p.lastName, a.street, a.city, a.zipCode, s.stateName, c.countryName "
					+ "FROM Person p " + "JOIN Address a ON p.addressId = a.addressId "
					+ "JOIN State s ON a.stateId = s.stateId " + "JOIN Country c ON a.countryId = c.countryId;";

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int personId = rs.getInt("p.personId");
				String personCode = rs.getString("p.personCode");
				String firstName = rs.getString("p.firstName");
				String lastName = rs.getString("p.lastName");
				String street = rs.getString("a.street");
				String city = rs.getString("a.city");
				String state = rs.getString("s.stateName");
				String zipCode = rs.getString("a.zipCode");
				String country = rs.getString("c.countryName");
				Address address = new Address(street, city, state, zipCode, country);

				List<String> email = DatabaseLoader.getEmailById(rs.getInt("p.personId"));
				p = new Person(personCode, firstName, lastName, address, email);
				mapPerson.put(personId, p);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapPerson;
	}

	// maps store retreived from the database
	public static Map<Integer, Store> storeConnectToDatabase(Map<Integer, Person> persons) {
		LOGGER.debug("Connecting to database and retrieving stores");

		Map<Integer, Store> mapStore = new HashMap<>();
		java.sql.Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getInstance().getConnection();

			String query = "SELECT a.storeId, a.storeCode, a.managerId, "
					+ "b.street, b.city, c.stateName, b.zipCode, d.countryName FROM Store a "
					+ "JOIN Address b ON b.addressId = a. addressId " + "JOIN State c ON c.stateId = b.stateId "
					+ "JOIN Country d on d.countryId = b.countryId;";
			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int storeId = rs.getInt("a.storeId");
				String storeCode = rs.getString("a.storeCode");
				Person managerInfo = persons.get(rs.getInt("a.managerId"));
				String street = rs.getString("b.street");
				String city = rs.getString("b.city");
				String state = rs.getString("c.stateName");
				String zipCode = rs.getString("b.zipCode");
				String country = rs.getString("d.countryName");
				Address address = new Address(street, city, state, zipCode, country);
				// adding it to store
				Store s = new Store(storeCode, managerInfo, address);
				mapStore.put(storeId, s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return mapStore;
	}

	// mapping invoice retreived from database
	public static Map<Integer, Invoice> invoiceConnectToDatabase(Map<Integer, Store> mapStore,
			Map<Integer, Person> mapPerson) {
		LOGGER.debug("Connecting to database and retrieving invoices");

		Map<Integer, Invoice> mapInvoice = new HashMap<>();
		java.sql.Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			stmt = conn.createStatement();
			String sql = "SELECT invoiceId, invoiceCode, storeId, customerId, salesPersonId, invoiceDate FROM Invoice;";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int invoiceId = rs.getInt("invoiceId");
				String invoiceCode = rs.getString("invoiceCode");
				Store storeInfo = mapStore.get(rs.getInt("storeId"));
				Person customerInfo = mapPerson.get(rs.getInt("customerId"));
				Person salesPersonInfo = mapPerson.get(rs.getInt("salesPersonId"));
				LocalDate invoiceDate = LocalDate.parse(rs.getString("invoiceDate"));

				Invoice i = new Invoice(invoiceId, invoiceCode, storeInfo, customerInfo, salesPersonInfo, invoiceDate);
				storeInfo.addInvoice(i);
				mapInvoice.put(invoiceId, i);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return mapInvoice;
	}

	// mapping item retreived from database
	public static Map<Integer, Item> itemConnectToDatabase() {
		LOGGER.debug("Connecting to database and retrieving items");

		Map<Integer, Item> mapItem = new HashMap<>();
		Item i = null;
		java.sql.Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			String query = "SELECT itemId, itemCode, nameItem, typeItem, model, unit, unitPrice, hourlyRate"
					+ " FROM Item";
			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int itemId = rs.getInt("itemId");
				String itemCode = rs.getString("itemCode");
				String nameItem = rs.getString("nameItem");
				String typeItem = rs.getString("typeItem");
				if (typeItem.equals("E")) {
					String model = rs.getString("model");
					i = new Equipment(itemCode, typeItem, nameItem, model);
				} else if (typeItem.equals("P")) {
					String unit = rs.getString("unit");
					double unitPrice = rs.getDouble("unitPrice");
					i = new Product(itemCode, typeItem, nameItem, unit, unitPrice);
				} else if (typeItem.equals("S")) {
					double hourlyRate = rs.getDouble("hourlyRate");
					i = new Service(itemCode, typeItem, nameItem, hourlyRate);
				}
				mapItem.put(itemId, i);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return mapItem;
	}

	// Invoice Item method that gets invoice item data from database
	public static Map<Integer, Invoice> invoiceItemsConnectToDatabase(Map<Integer, Item> items,
			Map<Integer, Invoice> invoices) {
		LOGGER.debug("Connecting to database and retrieving invoice items");

		java.sql.Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = ConnectionFactory.getInstance().getConnection();
			String sql = "SELECT invoiceId, itemId, typeEquipment, "
					+ "price, startLease, endLease, quantity, hoursBilled FROM InvoiceItem;";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int invoiceId = rs.getInt("invoiceId");
				Item item = items.get(rs.getInt("itemId"));
				Invoice invoice = invoices.get(rs.getInt("invoiceId"));
				String typeEquipment = rs.getString("typeEquipment");

				if (item instanceof Equipment) {
					if (typeEquipment.equals("P")) {
						Double price = rs.getDouble("price");
						invoice.addItem(new Purchase((Equipment) item, price));
					} else if (typeEquipment.equals("L")) {
						Double fee = rs.getDouble("price");
						;
						LocalDate startDate = LocalDate.parse(rs.getString("startLease"));
						LocalDate endDate = LocalDate.parse(rs.getString("endLease"));
						invoice.addItem(new Lease((Equipment) item, fee, startDate, endDate));
					}

				} else if (item instanceof Product) {
					Double quantity = rs.getDouble("quantity");
					invoice.addItem(new Product((Product) item, quantity));
				}

				else if (item instanceof Service) {
					Double hoursBilled = rs.getDouble("hoursBilled");
					invoice.addItem(new Service((Service) item, hoursBilled));
				}
				invoices.put(invoiceId, invoice);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}

				if (stmt != null) {
					stmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return invoices;
	}
}