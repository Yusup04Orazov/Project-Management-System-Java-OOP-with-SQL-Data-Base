/*
 * Author: Yusup Orazov and Keyik Annagulyyeva
 * Date: May 3rd, 2022
 * */

package com.fmt;

import java.util.List;

//this is the person class 
public class Person {
	private int personId;
	private String personCode;
	private String firstName;
	private String lastName;
	private Address address;

	private List<String> emails;
	private String email;

	// consturctors
	public Person(String personCode, String firstName, String lastName, Address personAddress, List<String> emails) {
		this.personCode = personCode;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = personAddress;
		this.emails = emails;
	}

	public Person(int personId, String personCode, String firstName, String lastName, Address address) {
		super();
		this.personId = personId;
		this.personCode = personCode;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
	}

	// getter methods
	public String getPersonCode() {
		return personCode;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Address getAddress() {
		return address;
	}

	public List<String> getEmails() {
		return emails;
	}

	// gets full name
	public String getFullname() {
		return (getLastName() + ", " + getFirstName());
	}

	public int getPersonId() {
		return personId;
	}

	public String getEmail() {
		return email;
	}

	// To String methods
	@Override
	public String toString() {
		return "\nPerson's Code: " + getPersonCode() + "\nPerson Full Name: " + getFullname() + "\nAddress of Person: "
				+ getAddress() + " \nEmails of Person: " + getEmails();
	}
}