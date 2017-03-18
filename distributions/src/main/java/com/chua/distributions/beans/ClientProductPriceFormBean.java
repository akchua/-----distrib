package com.chua.distributions.beans;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 18, 2017
 */
public class ClientProductPriceFormBean extends FormBean {

	private Long clientId;
	
	private Long productId;
	
	private Float packageSellingPrice;

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Float getPackageSellingPrice() {
		return packageSellingPrice;
	}

	public void setPackageSellingPrice(Float packageSellingPrice) {
		this.packageSellingPrice = packageSellingPrice;
	}
}
