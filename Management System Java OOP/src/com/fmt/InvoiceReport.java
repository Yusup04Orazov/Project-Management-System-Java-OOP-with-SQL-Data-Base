/*
 * Author: Yusup Orazov and Keyik Annagulyyeva
 * Date: May 3rd, 2022
 * */

package com.fmt;

import java.util.Comparator;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

public class InvoiceReport {

	// Generate a report with a given title, using a specified comparator for sorting invoices
	public static void generateReport(String title, Comparator<Invoice> comparator, Map<Integer, Invoice> invoiceMap) {
		// Create a SortedList to store the sorted invoices
		SortedList<Invoice> sortedInvoices = new SortedList<>(comparator);

		// Add invoices to the SortedList
		for (Invoice invoice : invoiceMap.values()) {
			sortedInvoices.add(invoice);
		}

		// Print the report header
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.printf("| %-71s|%n", title);
		System.out.println("+-------------------------------------------------------------------------+");
		System.out.println(
				String.format("%-10s %-10s %-20s %-20s %-10s", "Sale", "Store", "Customer", "Salesperson", "Total"));

		// Print the sorted invoices
		for (Invoice inv : sortedInvoices) {
			System.out.println(inv.getSalesSummary());
		}

		System.out.println("+-------------------------------------------------------------------------+");
	}

	public static void main(String[] args) {
		// Initialize logging configuration and set log level
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.INFO);

		// Load data from the database
		Map<Integer, Person> mapPerson = DatabaseLoader.personConnectToDatabase();
		Map<Integer, Store> mapStores = DatabaseLoader.storeConnectToDatabase(mapPerson);
		Map<Integer, Item> mapItems = DatabaseLoader.itemConnectToDatabase();
		Map<Integer, Invoice> invoiceMap = DatabaseLoader.invoiceConnectToDatabase(mapStores, mapPerson);
		@SuppressWarnings("unused")
		Map<Integer, Invoice> mapInvoiceItems = DatabaseLoader.invoiceItemsConnectToDatabase(mapItems, invoiceMap);

		// Generate reports using different comparators
		generateReport("Sales by Customer", InvoiceComparator.customerNameComparator(), invoiceMap);
		generateReport("Sales by Total", InvoiceComparator.totalValueComparator(), invoiceMap);
		generateReport("Sales by Store", InvoiceComparator.storeAndSalespersonComparator(), invoiceMap);
	}
}