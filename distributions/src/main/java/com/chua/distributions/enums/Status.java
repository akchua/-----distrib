package com.chua.distributions.enums;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 6, 2016
 */
public enum Status {
	
	CANCELLED("Cancelled"),

	CREATING("Creating"),
	
	SUBMITTED("Submitted"),
	
	ACCEPTED("Accepted"),
	
	PAID("Paid");
	
	private final String name;
	
	private Status(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
