package com.chua.distributions.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Jul 4, 2017
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ClientSalesReportType {

	STATUS_BASED("Status-based"),
	
	DELIVERY("Delivery"),
	
	PAYMENTS("Payments");
	
	private final String displayName;
	
	private ClientSalesReportType(final String displayName) {
		this.displayName = displayName;
	}
	
	public String getName() {
		return toString();
	}

	public String getDisplayName() {
		return displayName;
	}
}
