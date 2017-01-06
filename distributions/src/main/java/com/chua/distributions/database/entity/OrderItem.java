package com.chua.distributions.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.chua.distributions.database.entity.base.BaseObject;
import com.chua.distributions.utility.format.CurrencyFormatter;
import com.chua.distributions.utility.format.QuantityFormatter;

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
		return QuantityFormatter.format(quantity, packaging);
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
		return CurrencyFormatter.pesoFormat(getPackageUnitPrice());
	}
	
	@Transient
	public Float getGrossPrice() {
		return unitPrice * quantity;
	}
	
	@Transient
	public String getFormattedGrossPrice() {
		return CurrencyFormatter.pesoFormat(getGrossPrice());
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
		return CurrencyFormatter.pesoFormat(getDiscountAmount()) + " (" + discount + "%)";
	}
	
	@Transient
	public Float getNetPrice() {
		return getGrossPrice() - getDiscountAmount();
	}
	
	@Transient
	public String getFormattedNetPrice() {
		return CurrencyFormatter.pesoFormat(getNetPrice());
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}
}
