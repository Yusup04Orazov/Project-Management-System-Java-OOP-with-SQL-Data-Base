/*
 * Author: Yusup Orazov and Keyik Annagulyyeva
 * Date: May 3rd, 2022
 * */

package com.fmt;

public class Service extends Item {
	private double hourlyRate;
	private double hoursBilled;

	// constructors for the Serivce
	public Service(String code, String name, String type, double hourlyRate, double hoursBilled) {
		super(code, name, type);
		this.hourlyRate = hourlyRate;
		this.hoursBilled = hoursBilled;
	}

	// constructors for the Serivce
	public Service(Service item, double hoursBilled) {
		this(item.code, item.name, item.type, item.hourlyRate, hoursBilled);
	}

	// constructors for the Serivce
	public Service(String code, String type, String name, double hourlyRate) {
		super(code, name, type);
		this.hourlyRate = hourlyRate;
	}

	// getter methods
	public double getHourlyRate() {
		return hourlyRate;
	}

	// getter methods
	public double getHoursBilled() {
		return hoursBilled;
	}

	// Round a value to the nearest cent
	private double roundToNearestCent(double value) {
		return Math.round(value * 100.0) / 100.0;
	}

	// Get the tax rate for the service
	public double getTaxRate() {
		return 0.0345;
	}

	// getter methods for tax
	public double getTax() {
		return roundToNearestCent(getIncompletePrice() * getTaxRate());
	}

	// getter methods for 'Sub-total'
	@Override
	public double getIncompletePrice() {
		return roundToNearestCent(hourlyRate * hoursBilled);
	}

	// getter methods for 'total'
	@Override
	public double getFinalTotal() {
		return roundToNearestCent(getIncompletePrice() + getTax());
	}

	@Override
	public String toString() {
		return "Service: \ncode: " + code + "\nname: " + name + "\nHourly Rate: " + getHourlyRate()
				+ "\nHours Billed: " + getHoursBilled();
	}

}