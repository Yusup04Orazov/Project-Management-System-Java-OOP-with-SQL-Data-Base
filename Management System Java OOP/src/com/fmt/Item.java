/*
 * Author: Yusup Orazov and Keyik Annagulyyeva
 * Date: May 3rd, 2022
 * */

package com.fmt;

import java.util.List;
import java.util.ArrayList;

//This class represents an item with its properties like code, type, name, price, and quantity
public abstract class Item {
	public int itemId;
	public String code;
	public String name;
	public String type;
	// constructors
	private List<Invoice> invoices = new ArrayList<>();

	public Item(String code, String name, String type, int itemId) {
		this.code = code;
		this.name = name;
		this.type = type;
		this.itemId = itemId;
	}

	public Item(String code, String type, String name) {
		this.code = code;
		this.type = type;
		this.name = name;
	}

	// getter methods for code
	public String getCode() {
		return code;
	}

	// getter methods for type
	public String getType() {
		return type;
	}

	// getter methods for name
	public String getName() {
		return name;
	}

	// getter methods for itemId
	public int getItemId() {
		return itemId;
	}

	// abstract values
	public abstract double getFinalTotal();

	public abstract double getTax();

	public abstract double getIncompletePrice();

	// list of invoices
	public List<Invoice> getInvoices() {
		return invoices;
	}
}