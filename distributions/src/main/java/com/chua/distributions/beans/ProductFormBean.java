package com.chua.distributions.beans;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 5, 2016
 */
public class ProductFormBean extends FormBean {

	private Long companyId;
	
	private Long categoryId;
	
	private String productCode;
	
	private Long packagingId;
	
	private String name;
	
	private String size;
	
	private Integer packaging;
	
	private Boolean allowRetail;
	
	private String description;
	
	private Float packageGrossPrice;
	
	private Float discount;
	
	private Float packageNetPrice;
	
	private Float packageSellingPrice;
	
	private Float percentProfit;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	public Long getPackagingId() {
		return packagingId;
	}

	public void setPackagingId(Long packagingId) {
		this.packagingId = packagingId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Integer getPackaging() {
		return packaging;
	}

	public void setPackaging(Integer packaging) {
		this.packaging = packaging;
	}

	public Boolean getAllowRetail() {
		return allowRetail;
	}

	public void setAllowRetail(Boolean allowRetail) {
		this.allowRetail = allowRetail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getPackageGrossPrice() {
		return packageGrossPrice;
	}

	public void setPackageGrossPrice(Float packageGrossPrice) {
		this.packageGrossPrice = packageGrossPrice;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public Float getPackageNetPrice() {
		return packageNetPrice;
	}

	public void setPackageNetPrice(Float packageNetPrice) {
		this.packageNetPrice = packageNetPrice;
	}

	public Float getPackageSellingPrice() {
		return packageSellingPrice;
	}

	public void setPackageSellingPrice(Float packageSellingPrice) {
		this.packageSellingPrice = packageSellingPrice;
	}

	public Float getPercentProfit() {
		return percentProfit;
	}

	public void setPercentProfit(Float percentProfit) {
		this.percentProfit = percentProfit;
	}
}
