package com.chua.distributions.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 9, 2016
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Warehouse {

	MAIN("Main", "Temp Address"),
	
	TEST("Test", "Test Address");
	
	private String displayName;
	
	private String address;
	
	private Warehouse(final String displayName, final String address) {
		this.displayName = displayName;
		this.address = address;
	}
	
	public String getName() {
		return toString();
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getAddress() {
		return address;
	}
}
