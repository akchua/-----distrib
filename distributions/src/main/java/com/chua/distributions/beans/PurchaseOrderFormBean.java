package com.chua.distributions.beans;

import com.chua.distributions.enums.Area;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 6, 2016
 */
public class PurchaseOrderFormBean extends FormBean {

	private Area area;
	
	private Long companyId;

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
}
