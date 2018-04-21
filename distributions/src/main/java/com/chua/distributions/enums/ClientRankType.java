package com.chua.distributions.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Apr 20, 2018
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ClientRankType {

	TOP_DELIVERED("Highest Delivered Amount");
	
	private final String displayName;
	
	ClientRankType(final String displayName) {
		this.displayName = displayName;
	}
	
	public String getName() {
		return toString();
	}
	
	public String getDisplayName() {
		return displayName;
	}
}
