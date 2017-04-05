package com.chua.distributions.beans;

import com.chua.distributions.database.entity.Product;
import com.chua.distributions.utility.format.CurrencyFormatter;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Feb 2017
 */
public class PartialProductBean extends PartialEntityBean<Product> {

	private String productCode;
	
	private String displayName;
	
	private String companyName;
	
	private String categoryName;
	
	private String description;
	
	private Float sellingDiscount;
	
	private Float packageNetSellingPrice;
	
	public PartialProductBean(Product product) {
		super(product);
		setDisplayName(product.getDisplayName());
		setProductCode(product.getProductCode());
		setCompanyName(product.getCompany().getName());
		setCategoryName(product.getCategory().getName());
		setDescription(product.getDescription());
		setSellingDiscount(0.0f);
		setPackageNetSellingPrice(0.0f);
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getSellingDiscount() {
		return sellingDiscount;
	}

	public void setSellingDiscount(Float sellingDiscount) {
		this.sellingDiscount = sellingDiscount;
	}

	public Float getPackageNetSellingPrice() {
		return packageNetSellingPrice;
	}

	public void setPackageNetSellingPrice(Float packageNetSellingPrice) {
		this.packageNetSellingPrice = packageNetSellingPrice;
	}

	public String getFormattedPackageNetSellingPrice() {
		return CurrencyFormatter.pesoFormat(packageNetSellingPrice);
	}
}
