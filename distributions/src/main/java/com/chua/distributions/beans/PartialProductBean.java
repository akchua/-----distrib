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
	
	private String image;
	
	private String companyName;
	
	private String categoryName;
	
	private String description;
	
	private Float packageNetSellingPrice;
	
	private Float sellingDiscount;
	
	public PartialProductBean(Product product) {
		super(product);
		setDisplayName(product.getDisplayName());
		setProductCode(product.getProductCode());
		setImage(product.getImage());
		setCompanyName(product.getCompany().getName());
		setCategoryName(product.getCategory().getName());
		setDescription(product.getDescription());
		setPackageNetSellingPrice(0.0f);
		setSellingDiscount(0.0f);
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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

	public Float getPackageNetSellingPrice() {
		return packageNetSellingPrice;
	}
	
	public String getFormattedPackageNetSellingPrice() {
		return CurrencyFormatter.pesoFormat(packageNetSellingPrice);
	}

	public void setPackageNetSellingPrice(Float packageNetSellingPrice) {
		this.packageNetSellingPrice = packageNetSellingPrice;
	}
	
	public Float getSellingDiscount() {
		return sellingDiscount;
	}
	
	public Float getDiscountAmount() {
		return (getPackageNetSellingPrice() * sellingDiscount) / 100.0f;
	}
	
	public String getFormattedPackageSellingDiscount() {
		return CurrencyFormatter.pesoFormat(getDiscountAmount()) + " (" + sellingDiscount + "%)";
	}

	public void setSellingDiscount(Float sellingDiscount) {
		this.sellingDiscount = sellingDiscount;
	}
}
