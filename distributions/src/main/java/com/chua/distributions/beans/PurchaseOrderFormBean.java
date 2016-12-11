package com.chua.distributions.beans;

import com.chua.distributions.enums.Warehouse;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 6, 2016
 */
public class PurchaseOrderFormBean extends FormBean {

	private Warehouse warehouse;
	
	private Long companyId;

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
}
