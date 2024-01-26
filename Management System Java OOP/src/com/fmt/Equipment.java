/*
 * Author: Yusup Orazov and Keyik Annagulyyeva
 * Date: May 3rd, 2022
 * */
package com.fmt;

public class Equipment extends Item {

	private String model;

	// constructor
	public Equipment(String code, String type, String name, String model) {
		super(code, type, name);
		this.model = model;
	}

	// getter methods for model
	public String getModel() {
		return model;
	}

	// getter methods for Tax
	@Override
	public double getTax() {
		return 0;
	}

	// getter methods for FInal Total
	@Override
	public double getFinalTotal() {
		return 0;
	}

	// getter methods for 'subtotal'
	@Override
	public double getIncompletePrice() {
		return 0;
	}

	@Override
	public String toString() {
		return "Equipment :\n\nModel: " + getModel() + "\nFinalTotal: " + getFinalTotal() + ",\nIncompletePrice()="
				+ getIncompletePrice() + "\nCode()=" + getCode() + "\nType: " + getType() + "\nName: " + getName()
				+ "\nItemId: " + getItemId();
	}

}
