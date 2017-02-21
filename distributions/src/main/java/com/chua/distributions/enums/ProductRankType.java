package com.chua.distributions.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	21 Feb 2017
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProductRankType {

	MONTHLY_BY_NET_SALES("Ranked Monthly By Net Sales");
	
	private final String displayName;
	
	private ProductRankType(final String displayName) {
		this.displayName = displayName;
	}
	
	public String getName() {
		return toString();
	}
	
	public String getDisplayName() {
		return displayName;
	}
}
