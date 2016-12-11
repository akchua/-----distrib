package com.chua.distributions.utility;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 11, 2016
 */
public class StringHelper {
	
	private StringHelper() {
		
	}

	public static String center(String s, int length) {
		if(s == null || length <= s.length()) return s;
		
		String centered = "";
		int temp = (length - s.length()) / 2;
		for(int i = 0; i < temp; i++) centered += " ";
		centered += s;
		for(int i = 0; i < temp; i++) centered += " ";
		if((length - s.length()) % 2 == 1) centered += " ";
		
		return centered;
	}
}
