package com.chua.distributions.enums;

import com.chua.distributions.constants.BusinessConstants;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 11, 2016
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum VatType {

	VAT("VAT (" + BusinessConstants.REGULAR_VAT + "%)", 0.0f),
	
	NON_VAT("Non-VAT (0%)", BusinessConstants.REGULAR_VAT);
	
	private final String displayName;
	
	private final Float lessVat;
	
	private VatType(final String displayName, final Float lessVat) {
		this.displayName = displayName;
		this.lessVat = lessVat;
	}
	
	public String getName() {
		return toString();
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public Float getLessVat() {
		return lessVat;
	}
}
