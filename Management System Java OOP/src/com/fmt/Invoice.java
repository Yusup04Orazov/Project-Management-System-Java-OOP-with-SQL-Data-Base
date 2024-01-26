/*
 * Author: Yusup Orazov and Keyik Annagulyyeva
 * Date: May 3rd, 2022
 * */

package com.fmt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Invoice {
	// values
	private int invoiceId;
	private String invoiceCode;
	private String itemCode;
	private int itemId;
	private Store storeId;
	private Person customer;
	private Person salesPerson;
	private LocalDate invoiceDate;
	private List<Item> invoiceItems = new ArrayList<>();

	// constructors for invoice
	public Invoice(String invoiceCode, Store storeId, Person customer, Person salesPerson, LocalDate invoiceDate,
			int invoiceId, int itemId) {
		this.invoiceCode = invoiceCode;
		this.storeId = storeId;
		this.customer = customer;
		this.salesPerson = salesPerson;
		this.invoiceDate = invoiceDate;
		this.invoiceId = invoiceId;
		this.itemId = itemId;
	}

	// constructors for invoice
	public Invoice(Invoice i, String invoiceCode, String itemCode, List<Item> invoiceItems, int invoiceId, int itemId) {
		this.invoiceCode = invoiceCode;
		this.storeId = i.storeId;
		this.customer = i.customer;
		this.salesPerson = i.salesPerson;
		this.invoiceDate = i.invoiceDate;
		this.itemCode = itemCode;
		this.invoiceItems = invoiceItems;
		this.invoiceId = invoiceId;
		this.itemId = itemId;
	}

	// constructors for invoice
	public Invoice(int invoiceId, String invoiceCode, Store store, Person customer, Person salesPerson,
			LocalDate invoiceDate) {
		this.invoiceId = invoiceId;
		this.invoiceCode = invoiceCode;
		this.storeId = store;
		this.customer = customer;
		this.salesPerson = salesPerson;
		this.invoiceDate = invoiceDate;
	}

	// constructors for invoice
	public Invoice(String invoiceCode, Store storeId, Person customerId, Person salespersonId) {
		this.getInvoiceCode();
		this.getStoreId();
	}

	// constructors for invoice
	public Invoice(int invoiceId, String invoiceCode, Store storeId, Person customerId, Person salespersonId) {
		this.getInvoiceId();
		this.getInvoiceCode();
		this.getStoreId();
	}

	// constructors for invoice
	public Invoice(int invoiceId, Store storeId, int customerId, double tax) {
		this.invoiceId = invoiceId;
		this.storeId = storeId;
	}

	// list for invoiceItem
	public List<Item> getInvoiceItems() {
		return invoiceItems;
	}

	// adding items
	public void addItem(Item item) {
		this.invoiceItems.add(item);
	}

	// getting invocie code
	public String getInvoiceCode() {
		return invoiceCode;
	}

	// getting Store Id
	public Store getStoreId() {
		return storeId;
	}

	// getting Customer
	public Person getCustomer() {
		return customer;
	}

	// getting Salesperson
	public Person getSalesPerson() {
		return salesPerson;
	}

	// getter for invoice date
	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}

	// getter for Item Codd
	public String getItemCode() {
		return itemCode;
	}

	// getter for Items Id
	public int getItemId() {
		return itemId;
	}

	// getter for invoice Id
	public int getInvoiceId() {
		return invoiceId;
	}

	// getting 'subtotal'
	public double getIncompletePrice() {
		double subtotal = 0.0;
		for (Item invoiceItem : this.invoiceItems) {
			subtotal += invoiceItem.getIncompletePrice();
		}
		return Math.round(100 * (subtotal)) / 100.00;
	}

	// calculating tax
	public double getTax() {
		double taxes = 0.0;
		for (Item invoiceItem : this.invoiceItems) {
			taxes += invoiceItem.getTax();
		}
		return Math.round(100 * (taxes)) / 100.00;
	}

	// calculating total
	public double calculateFinal() {
		return Math.round(100 * (getTax() + getIncompletePrice())) / 100.0;
	}

	// getting sales summary
	public String getSalesSummary() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%-10s %-10s %-20s %-20s $%10.2f", this.invoiceCode, this.storeId.getStoreCode(),
				this.customer.getLastName() + ", " + this.customer.getFirstName(),
				this.salesPerson.getLastName() + ", " + this.salesPerson.getFirstName(), this.calculateFinal()));
		return sb.toString();
	}

	// invoice Report method
	public String InvoiceReport() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("\n\nInvoice\n\t#%s\nStore\t%s\nDate\t%s\nCustomer: %s \n\nSalesPerson: %s\n",
				getInvoiceCode(), getStoreId().getStoreCode(), getInvoiceDate(), getCustomer(), getSalesPerson()));
		sb.append("Item\t\t\t\t\t\t\t\t Total\n");
		sb.append("-+-+-+-+-+-+-+-+-+-+-+-+-+-+\t\t-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n");
		for (Item i : this.invoiceItems) {
			sb.append(String.format("%s\n\n", i.toString()));
		}

		sb.append("-=-=-=-=-=-=-=-=-=-\t\t\t\t\t\t\t-=-=-=-=-=-=-=-=-=-\n");
		sb.append(String.format("\t\t                     Subtotal $ %5.2f\n", this.getIncompletePrice()));
		sb.append(String.format("\t\t                     Tax $ %5.2f\n", this.getTax()));
		sb.append(String.format("\t\t                     Grand Total $ %5.2f\n", this.calculateFinal()));
		return sb.toString();
	}
}