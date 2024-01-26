/*
 * Author: Yusup Orazov and Keyik Annagulyyeva
 * Date: May 3rd, 2022
 * */

package com.fmt;

import java.util.ArrayList;
import java.util.List;

public class Store implements Comparable<Store> {
	private int storeId;
	private String storeCode;
	private Person managerId;
	private Address address;
	private List<Invoice> invoices;

	// constructor for Store
	public Store(int storeId, String storeCode, Person managerId, Address address, List<Invoice> invoices) {
		super();
		this.invoices = new ArrayList<>();
		this.storeId = storeId;
		this.storeCode = storeCode;
		this.managerId = managerId;
		this.address = address;
		this.invoices = invoices;
	}

	// constructor for Store
	public Store(String storeCode, Person managerId, Address address) {
		this.storeCode = storeCode;
		this.managerId = managerId;
		this.address = address;
		this.invoices = new ArrayList<>();
	}

	// constructor for Store
	public Store(String storeCode, String storeId, Person managerId) {
		this.getStoreId();
		this.getStoreCode();
		this.getManagerId();

	}

	// add invoice
	public void addInvoice(Invoice invoice) {
		this.invoices.add(invoice);
	}

	// getter method for store code
	public String getStoreCode() {
		return storeCode;
	}

	// getter method for Manager Id
	public Person getManagerId() {
		return managerId;
	}

	// getter method for address
	public Address getAddress() {
		return address;
	}

	// getter method for Store Id
	public int getStoreId() {
		return storeId;
	}

	// List for invoice
	public List<Invoice> getInvoices() {
		return invoices;
	}

	// getter method for 'Total'
	public int getFinalRevenue() {
	    return invoices.size();
	}

	// getter method for calculatingFinal
	public double getCalculateFinal() {
		double getFinal = 0;
		for (int i = 0; i < invoices.size(); i++) {
			getFinal += invoices.get(i).calculateFinal();
		}
		return getFinal;
	}

	// getter for salesSummary
	public String getSalesSummary() {
	    return String.format("%-15s %-30s %-15s %s", this.storeCode, this.managerId.getFullname(),
	            this.getFinalRevenue(), this.getCalculateFinal());
	}

	// Comparing so I list store from decending revenue order
	@Override
	public int compareTo(Store o) {
		int resultant = (int) (o.getFinalRevenue() - this.getFinalRevenue());
		return resultant;
	}
}