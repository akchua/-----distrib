package com.chua.distributions.beans;

import com.chua.distributions.database.entity.ClientOrderItem;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Mar 2017
 */
public class PartialClientOrderItemBean extends PartialEntityBean<ClientOrderItem> {

	private Integer packageQuantity;
	
	private Integer pieceQuantity;
	
	private Integer packaging;
	
	private String productCode;
	
	private String displayName;
	
	private String formattedPackageUnitPrice;
	
	private String formattedDiscountAmount;
	
	private String formattedNetPrice;
	
	public PartialClientOrderItemBean(ClientOrderItem clientOrderItem) {
		super(clientOrderItem);
		setPackageQuantity(clientOrderItem.getPackageQuantity());
		setPieceQuantity(clientOrderItem.getPieceQuantity());
		setPackaging(clientOrderItem.getPackaging());
		setProductCode(clientOrderItem.getProductCode());
		setDisplayName(clientOrderItem.getDisplayName());
		setFormattedPackageUnitPrice(clientOrderItem.getFormattedPackageUnitPrice());
		setFormattedDiscountAmount(clientOrderItem.getFormattedDiscountAmount());
		setFormattedNetPrice(clientOrderItem.getFormattedNetPrice());
	}

	public Integer getPackageQuantity() {
		return packageQuantity;
	}

	public void setPackageQuantity(Integer packageQuantity) {
		this.packageQuantity = packageQuantity;
	}

	public Integer getPieceQuantity() {
		return pieceQuantity;
	}

	public void setPieceQuantity(Integer pieceQuantity) {
		this.pieceQuantity = pieceQuantity;
	}

	public Integer getPackaging() {
		return packaging;
	}

	public void setPackaging(Integer packaging) {
		this.packaging = packaging;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getFormattedPackageUnitPrice() {
		return formattedPackageUnitPrice;
	}

	public void setFormattedPackageUnitPrice(String formattedPackageUnitPrice) {
		this.formattedPackageUnitPrice = formattedPackageUnitPrice;
	}

	public String getFormattedDiscountAmount() {
		return formattedDiscountAmount;
	}

	public void setFormattedDiscountAmount(String formattedDiscountAmount) {
		this.formattedDiscountAmount = formattedDiscountAmount;
	}

	public String getFormattedNetPrice() {
		return formattedNetPrice;
	}

	public void setFormattedNetPrice(String formattedNetPrice) {
		this.formattedNetPrice = formattedNetPrice;
	}
}
