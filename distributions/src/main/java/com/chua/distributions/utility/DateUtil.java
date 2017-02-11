package com.chua.distributions.utility;

import java.util.Calendar;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	9 Feb 2017
 */
public class DateUtil {

	private static final String[] NAME_OF_MONTH = {"January", "February", "March", "April", "May", "June", "July",
	        "August", "September", "October", "November", "December"};
	
	public static String getNameOfMonth(Calendar calendar) {
		return NAME_OF_MONTH[calendar.get(Calendar.MONTH)];
	}
}