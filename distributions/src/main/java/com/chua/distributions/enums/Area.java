package com.chua.distributions.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 4, 2016
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Area {

	PAMPANGA("Pampanga", VatType.VAT),
	
	TESTAREA("TestArea", VatType.NON_VAT);
	
	private final String displayName;
	
	private final VatType vatType;
	
	private Area(final String displayName, final VatType vatType) {
		this.displayName = displayName;
		this.vatType = vatType;
	}
	
	public String getName() {
		return toString();
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public VatType getVatType() {
		return vatType;
	}
}
