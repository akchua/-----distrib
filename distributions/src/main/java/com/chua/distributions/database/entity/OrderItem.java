package com.chua.distributions.database.entity;

import java.text.DecimalFormat;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.chua.distributions.database.entity.base.BaseObject;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 7, 2016
 */
@MappedSuperclass
public class OrderItem extends BaseObject {

	private static final long serialVersionUID = -9019953782155267102L;

	private Long productId;
	
	private Integer quantity;
	
	private String productCode;
	
	private String displayName;
	
	private Integer packaging;
	
	private Float unitPrice;
	
	private Float discount;

	@Basic
	@Column(name = "product_id")
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@Basic
	@Column(name = "quantity")
	public Integer getQuantity() {
		return quantity;
	}
	
	@Transient
	public Integer getPackageQuantity() {
		return quantity / packaging;
	}
	
	public void setPackageQuantity(Integer packageQuantity) {
		this.quantity = (packageQuantity * packaging) + getPieceQuantity();
	}
	
	@Transient
	public Integer getPieceQuantity() {
		return quantity % packaging;
	}
	
	public void setPieceQuantity(Integer pieceQuantity) {
		this.quantity = (getPackageQuantity() * packaging) + pieceQuantity;
	}
	
	@Transient
	public String getFormattedQuantity() {
		String formattedQuantity = "";
		
		int packageCount = quantity / packaging;
		int pieceCount = quantity % packaging;
		if(packageCount > 0) formattedQuantity += packageCount + "";
		if(pieceCount > 0) formattedQuantity += " & " + pieceCount + "/" + packaging;
		if(formattedQuantity.equals("")) formattedQuantity += "0";
		
		return formattedQuantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Basic
	@Column(name = "product_code")
	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	@Basic
	@Column(name = "display_name")
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Basic
	@Column(name = "packaging")
	public Integer getPackaging() {
		return packaging;
	}

	public void setPackaging(Integer packaging) {
		this.packaging = packaging;
	}

	@Basic
	@Column(name = "unit_price")
	public Float getUnitPrice() {
		return unitPrice;
	}
	
	@Transient
	public Float getPackageUnitPrice() {
		return unitPrice * packaging;
	}
	
	@Transient
	public String getFormattedPackageUnitPrice() {
		DecimalFormat df = new DecimalFormat("#,##0.00");
		return "Php " + df.format(getPackageUnitPrice());
	}
	
	@Transient
	public Float getGrossPrice() {
		return unitPrice * quantity;
	}
	
	@Transient
	public String getFormattedGrossPrice() {
		DecimalFormat df = new DecimalFormat("#,##0.00");
		return "Php " + df.format(getGrossPrice());
	}

	public void setUnitPrice(Float unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Basic
	@Column(name = "discount")
	public Float getDiscount() {
		return discount;
	}
	
	@Transient
	public Float getDiscountAmount() {
		return (getGrossPrice() * discount) / 100.0f;
	}
	
	@Transient
	public String getFormattedDiscountAmount() {
		DecimalFormat df = new DecimalFormat("#,##0.00");
		return "Php " + df.format(getDiscountAmount()) + " (" + discount + "%)";
	}
	
	@Transient
	public Float getNetPrice() {
		return getGrossPrice() - getDiscountAmount();
	}
	
	@Transient
	public String getFormattedNetPrice() {
		DecimalFormat df = new DecimalFormat("#,##0.00");
		return "Php " + df.format(getNetPrice());
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}
}
