package com.chua.distributions.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 6, 2016
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Status {
	
	CANCELLED("Cancelled", Integer.valueOf("10")),

	CREATING("Creating", Integer.valueOf("1")),
	
	SUBMITTED("Submitted", Integer.valueOf("2")),
	
	ACCEPTED("Accepted", Integer.valueOf("3")),
	
	PAID("Paid", Integer.valueOf("4"));
	
	private final String displayName;
	
	private final Integer intValue;
	
	public String getName() {
		return toString();
	}
	
	private Status(final String displayName, final Integer intValue) {
		this.displayName = displayName;
		this.intValue = intValue;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public Integer getIntValue() {
		return intValue;
	}
}
