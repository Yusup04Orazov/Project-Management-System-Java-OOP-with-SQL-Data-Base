/*
 * Author: Yusup Orazov and Keyik Annagulyyeva
 * Date: May 3rd, 2022
 * */

package com.fmt;

public class Product extends Item {
	private double unitPrice;
	private String unit;
	private double quantity;

	// constructors
	public Product(String code, String name, String type, String unit, double unitPrice) {
		super(code, name, type);
		this.unitPrice = unitPrice;
	}

	// constructors
	public Product(Product p, double quantity) {
		super(p.getCode(), p.getType(), p.getName());
		this.unit = p.unit;
		this.unitPrice = p.unitPrice;
		this.quantity = quantity;
	}

	// getter methods
	public String getUnit() {
		return unit;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public double getQuantity() {
		return quantity;
	}

	// tax related
	public double getTaxRate() {
		return 0.0715;
	}

	// tax related
	public double getTax() {
		return roundToNearestCent(getIncompletePrice() * getTaxRate());
	}

	@Override
	public double getIncompletePrice() {
		return roundToNearestCent(quantity * unitPrice);
	}

	@Override
	public double getFinalTotal() {
		return roundToNearestCent(getIncompletePrice() + getTax());
	}

	// rounding method
	private double roundToNearestCent(double value) {
		return Math.round(value * 100.0) / 100.0;
	}

	@Override
	public String toString() {
		return "Product :\n\nUnitPrice: " + getUnitPrice() + "\nQuantity: " + getQuantity() + "\nPrice; "
				+ getIncompletePrice() + "\nPrice with Tax: " + getFinalTotal() + "\nCode: " + getCode() + "\ngetType"
				+ getType() + "\nName: " + getName() + "\nItemId: " + getItemId();
	}

}