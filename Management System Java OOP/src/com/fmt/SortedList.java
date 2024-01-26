/*
 * Author: Yusup Orazov and Keyik Annagulyyeva
 * Date: May 3rd, 2022
 * */

package com.fmt;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

//Implementation of a sorted list (using comparator)

public class SortedList<T> implements Iterable<T> {
	private T[] info;
	private int measure;
	private Comparator<T> comparator;

// Constructor: initializes a sorted list with a given comparator

	@SuppressWarnings("unchecked")

	public SortedList(Comparator<T> comparator) {
		this.info = (T[]) new Object[10];
		this.measure = 0;
		this.comparator = comparator;
	}

// Add method: inserts an element to the list while maintaining the order
	public void add(T element) {
		if (measure == info.length) {
			info = Arrays.copyOf(info, measure * 2);
		}
		int i = 0;
		while (i < measure && comparator.compare(info[i], element) < 0) {
			i++;
		}

// Shift elements to the right of the index to make room for the new element
		System.arraycopy(info, i, info, i + 1, measure - i);
		info[i] = element;
		measure++;
	}

// Remove method: removes an element from the list if it exists, maintaining the order
	public boolean remove(T item) {
		for (int i = 0; i < measure; i++) {
			if (info[i].equals(item)) {

// Shift elements to the left of the index to fill the gap created by the removed element
				System.arraycopy(info, i + 1, info, i, measure - i - 1);
				measure--;
				return true;
			}
		}
		return false;
	}

	public int size() {
		return measure;
	}

// Get method: retrieves the element at a given indexs

	public T get(int index) {
		if (index < 0 || index >= measure) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}
		return info[index];

	}

// Iterator method: returns an iterator to traverse the list

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			int current = 0;

			@Override
			public boolean hasNext() {

				return current < measure;
			}

			@Override
			public T next() {
				return info[current++];
			}
		};
	}
}