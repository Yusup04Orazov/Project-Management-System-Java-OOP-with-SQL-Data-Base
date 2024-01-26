/*
 * Author: Yusup Orazov and Keyik Annagulyyeva
 * Date: May 3rd, 2022
 * */
package com.fmt;

public class Address {
	private int addressId;
	private String street;
	private String city;
	private String state;
	private String zip;
	private String country;
	private String address;

	// constructor of address
	public Address(String street, String city, String state, String zip, String country) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
	}

	// constructor of address
	public Address(int addressId, String street, String city, String state, String zip, String country) {
		super();
		this.addressId = addressId;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
	}

	// constructor of address
	public Address(int addressId, String address) {
		this.addressId = addressId;
		this.address = address;
	}

	// getter methods
	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	public String getCountry() {
		return country;
	}

	public int getAddressId() {
		return addressId;
	}

	public String getAddress() {
		return address;
	}

	// toString method
	@Override
	public String toString() {
		return "\nStreet: " + street + " \nCity: " + city + " \nState: " + state + " \nZip: " + zip + " \nCountry: "
				+ country;
	}
}