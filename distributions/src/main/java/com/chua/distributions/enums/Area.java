package com.chua.distributions.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 4, 2016
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Area {

	PAMPANGA("Pampanga", "");
	
	private final String displayName;
	
	private final String warehouseAddress;
	
	private Area(final String displayName, final String warehouseAddress) {
		this.displayName = displayName;
		this.warehouseAddress = warehouseAddress;
	}
	
	public String getName() {
		return toString();
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getWarehouseAddress() {
		return warehouseAddress;
	}
}
