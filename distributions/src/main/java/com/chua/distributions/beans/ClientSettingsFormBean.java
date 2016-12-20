package com.chua.distributions.beans;

import com.chua.distributions.enums.VatType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 11, 2016
 */
public class ClientSettingsFormBean extends FormBean {

	private Float discount;
	
	private VatType vatType;

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}
	
	public VatType getVatType() {
		return vatType;
	}

	public void setVatType(VatType vatType) {
		this.vatType = vatType;
	}
}
