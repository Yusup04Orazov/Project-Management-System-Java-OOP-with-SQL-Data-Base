/*
 * Author: Yusup Orazov and Keyik Annagulyyeva
 * Date: May 3rd, 2022
 * */

package com.fmt;

import java.time.LocalDate;

public class Lease extends Equipment {
	// values
	private double fee;
	private LocalDate startDate;
	private LocalDate endDate;

	// constructors
	public Lease(Equipment e, String code, String type, String name, String model, double fee, LocalDate startDate,
			LocalDate endDate) {
		super(e.getCode(), e.getType(), e.getName(), e.getModel());
		this.fee = fee;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	// constructor
	public Lease(Equipment e, double fee, LocalDate startDate, LocalDate endDate) {
		super(e.getCode(), e.getType(), e.getName(), e.getModel());
		this.fee = fee;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	// getter methods
	public double getFee() {
		return fee;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	// does tax
	public double getTax() {
		double tax = 0.0;
		if (getIncompletePrice() < 10000) {
			tax = 0;
		} else if (getIncompletePrice() < 100000) {
			tax = 500;
		} else {
			tax = 1500;
		}
		return tax;
	}

	// rounding method
	private double roundToNearestCent(double value) {
		return Math.round(value * 100.0) / 100.0;
	}

	public long getDays() {
		LocalDate currentDate = startDate;
		long days = 1;

		while (currentDate.isBefore(endDate)) {
			currentDate = currentDate.plusDays(1);
			days++;
		}

		return days;
	}
	//this method is to get the price with no tax
	public double getIncompletePrice() {
		double subtotal = ((double) getDays() / 30.0) * getFee();
		return roundToNearestCent(subtotal);
	}
	//this is to get the price with tax
	public double getFinalTotal() {
		return roundToNearestCent(getTax() + getIncompletePrice());
	}

	@Override
	public String toString() {
		return "Lease :\nFee: " + getFee() + "\n StartDate: " + getStartDate() + "\nEndDate: " + getEndDate()
				+ "\nTax: " + getTax() + "\nDays: " + getDays() + "\nIncompletePrice: " + getIncompletePrice()
				+ "\nFinalTotal: " + getFinalTotal() + "\nModel: " + getModel() + "\nCode: " + getCode() + "\nType: "
				+ getType() + "\nName: " + getName();
	}

}