/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package org.vcell.sybil.util.iterators;

/*   IterOfThree  --- by Oliver Ruebenacker, UCHC --- August 2007 to July 2008
 *   An iterator with exactly three elements
 */

import java.util.NoSuchElementException;

public class IterOfThree<E> implements SmartIter<E> {

	private E element1, element2, element3;
	private int number;
	
	public IterOfThree(E newElement1, E newElement2, E newElement3) { 
		element1 = newElement1; 
		element2 = newElement2; 
		element3 = newElement3; 
	};

	public boolean hasNext() { return number < 3; };

	public E next() {
		switch(number) {
		case 0: number = 1; return element1;
		case 1: number = 2; return element2;
		case 2: number = 3; return element3;
		default: throw new NoSuchElementException();
		}
	}

	public int count() { return number; }
	public boolean isAtBoundary() { return number == 0 || number == 3; }
	public boolean isAtInternalBoundary() { return number == 0 || number == 3; }
	public int subCount() { return number; }
	public void remove() { throw new UnsupportedOperationException(); }

}
