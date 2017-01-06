package com.chua.distributions.utility.format;

import java.text.DecimalFormat;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	6 Jan 2017
 */
public class CurrencyFormatter {

	public static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##0.00");
	
	private CurrencyFormatter() {
		
	}
	
	public static String pesoFormat(Float amount) {
		return "Php " + CURRENCY_FORMAT.format(amount);
	}
	
	public static String pesoFormat(Double amount) {
		return "Php " + CURRENCY_FORMAT.format(amount);
	}
	
	public static String pesoFormat(String amount) {
		return "Php " + CURRENCY_FORMAT.format(amount);
	}
}
