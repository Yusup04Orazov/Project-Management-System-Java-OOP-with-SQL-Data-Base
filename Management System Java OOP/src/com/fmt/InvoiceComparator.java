/*
 * Author: Yusup Orazov and Keyik Annagulyyeva
 * Date: May 3rd, 2022
 * */

package com.fmt;

import java.util.Comparator;

public class InvoiceComparator {

	// Comparator for sorting invoices based on customer's last and first name
	
	public static Comparator<Invoice> customerNameComparator() {
	    return (Invoice o1, Invoice o2) -> {
	        int lastName = o1.getCustomer().getLastName().compareTo(o2.getCustomer().getLastName());
	        if (lastName == 0) {
	            int firstName = o1.getCustomer().getFirstName().compareTo(o2.getCustomer().getFirstName());
	            if (firstName == 0) {
	                return o1.getInvoiceCode().compareTo(o2.getInvoiceCode());
	            }
	            return firstName;
	        }
	        return lastName;
	    };
	}

	// This is my comparator for sorting invoices based on their total value (highest to lowest)
	public static Comparator<Invoice> totalValueComparator() {
		return (Invoice o1, Invoice o2) -> Double.compare(o2.calculateFinal(), o1.calculateFinal());
	}

	// This is my comparator for sorting invoices first by store, then by salesperson's last and first name
	public static Comparator<Invoice> storeAndSalespersonComparator() {
		return (Invoice o1, Invoice o2) -> {
			int store = o1.getStoreId().getStoreCode().compareTo(o2.getStoreId().getStoreCode());
			if (store == 0) {
				int lastName = o1.getSalesPerson().getLastName().compareTo(o2.getSalesPerson().getLastName());
				if (lastName == 0) {
					return o1.getSalesPerson().getFirstName().compareTo(o2.getSalesPerson().getFirstName());
				}
				return lastName;
			}
			return store;
		};
	}
}