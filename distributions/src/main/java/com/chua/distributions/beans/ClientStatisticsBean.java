package com.chua.distributions.beans;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Apr 21, 2018
 */
public class ClientStatisticsBean {

	private String clientName;
	
	private Float purchaseAmount;

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Float getPurchaseAmount() {
		return purchaseAmount;
	}

	public void setPurchaseAmount(Float purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}
}
