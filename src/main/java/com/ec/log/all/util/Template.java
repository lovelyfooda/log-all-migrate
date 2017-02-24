package com.ec.log.all.util;

import java.util.HashSet;
import java.util.Set;

public final class Template {
	private static Set<Integer> contactSet;
	static {
		contactSet = new HashSet<>();
		contactSet.add(8);
		contactSet.add(9);
		contactSet.add(1);
		contactSet.add(11);
		contactSet.add(15);
		contactSet.add(2);
		contactSet.add(16);
		contactSet.add(18);
		contactSet.add(21);
		contactSet.add(32);
		contactSet.add(33);
		contactSet.add(43);
		contactSet.add(45);
		contactSet.add(47);
		contactSet.add(48);
		contactSet.add(49);
		contactSet.add(14);
		contactSet.add(17);
		contactSet.add(20);
		contactSet.add(30);
		contactSet.add(31);
		contactSet.add(42);
		contactSet.add(44);
		contactSet.add(46);
		contactSet.add(5);
	}
	public static boolean checkContactType(int style, int operateType) {
		if ( style == 1 ) {
			if ( contactSet.contains(operateType) ) {
				return true;
			}
		}
		return false;
	}
}
