/*
 * Author: Yusup Orazov and Keyik Annagulyyeva
 * Date: May 3rd, 2022
 * */

package com.fmt;

public class Purchase extends Equipment {

	private double price;

	// constructor
	public Purchase(String code, String type, String name, String model, double price) {
		super(code, type, name, model);
		this.price = price;
	}

	// getter methods
	public double getPrice() {
		return price;
	}

	public Purchase(Equipment item, double price) {
		super(item.getCode(), item.getType(), item.getName(), item.getModel());
		this.price = price;
	}

	public double getIncompletePrice() {
		return price;
	}

	public double getTax() {
		return 0;
	}

	public double getFinalTotal() {
		return (getIncompletePrice() + getTax());
	}

	// To String method
	@Override
	public String toString() {
		return "Purchase: " + " \nTotal:" + getFinalTotal() + "\nModel: " + getModel() + "\nCode: " + getCode()
				+ "\nName: " + getName();
	}
}