package com.chua.distributions.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 13, 2016
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserType {

	ADMINISTRATOR("Administrator"),
	
	MANAGER("Manager"),
	
	CLIENT("Client");
	
	private final String displayName;
	
	UserType(final String displayName) {
		this.displayName = displayName;
	}
	
	public String getName() {
		return toString();
	}
	
	public String getDisplayName() {
		return displayName;
	}
}
