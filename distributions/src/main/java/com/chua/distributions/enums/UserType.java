package com.chua.distributions.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 13, 2016
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserType {

	ADMINISTRATOR("Administrator", Integer.valueOf(1)),
	
	MANAGER("Manager", Integer.valueOf(2)),
	
	SECRETARY("Secretary", Integer.valueOf(3)),
	
	SUPERVISOR("Supervisor", Integer.valueOf(5)),
	
	CLIENT("Client", Integer.valueOf(10));
	
	private final String displayName;
	
	private final Integer authority;
	
	UserType(final String displayName, final Integer authority) {
		this.displayName = displayName;
		this.authority = authority;
	}
	
	public String getName() {
		return toString();
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public Integer getAuthority() {
		return authority;
	}
}
