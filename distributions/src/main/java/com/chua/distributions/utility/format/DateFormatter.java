package com.chua.distributions.utility.format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	6 Jan 2017
 */
public class DateFormatter {
	
	private static final DateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

	private static final DateFormat LONG_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	private static final DateFormat FILE_SAFE_DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss");
	
	private static final DateTimeFormatter SHORT_DATETIME_FORMAT = DateTimeFormat.forPattern("MM/dd/yyyy");
	
	private static final DateTimeFormatter LONG_DATETIME_FORMAT = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
	
	private static final DateTimeFormatter FILE_SAFE_DATETIME_FORMAT = DateTimeFormat.forPattern("MM-dd-yyyy-HH-mm-ss");
	
	private DateFormatter() {
		
	}
	
	public static String longFormat(Date toFormat) {
		return LONG_DATE_FORMAT.format(toFormat);
	}
	
	public static String longFormat(DateTime toFormat) {
		return toFormat.toString(LONG_DATETIME_FORMAT);
	}
	
	public static String shortFormat(Date toFormat) {
		return SHORT_DATE_FORMAT.format(toFormat);
	}
	
	public static String shortFormat(DateTime toFormat) {
		return toFormat.toString(SHORT_DATETIME_FORMAT);
	}
	
	public static String fileSafeFormat(Date toFormat) {
		return FILE_SAFE_DATE_FORMAT.format(toFormat);
	}
	
	public static String fileSafeFormat(DateTime toFormat) {
		return toFormat.toString(FILE_SAFE_DATETIME_FORMAT);
	}
}
