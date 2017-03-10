package com.chua.distributions.beans;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Mar 2017
 */
public class ClientCompanyPriceFormBean extends FormBean {

	private Long companyId;
	
	private Long clientId;
	
	private Float discount;
	
	private Float markup;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public Float getMarkup() {
		return markup;
	}

	public void setMarkup(Float markup) {
		this.markup = markup;
	}
}
