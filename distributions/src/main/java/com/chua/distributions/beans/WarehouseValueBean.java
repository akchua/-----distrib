package com.chua.distributions.beans;

import com.chua.distributions.enums.Warehouse;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	17 Feb 2017
 */
public class WarehouseValueBean {

	private Warehouse warehouse;
	
	private String formattedPurchaseValue;

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public String getFormattedPurchaseValue() {
		return formattedPurchaseValue;
	}

	public void setFormattedPurchaseValue(String formattedPurchaseValue) {
		this.formattedPurchaseValue = formattedPurchaseValue;
	}
}
