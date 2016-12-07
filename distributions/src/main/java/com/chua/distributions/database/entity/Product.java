package com.chua.distributions.database.entity;

import java.text.DecimalFormat;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.distributions.database.entity.base.BaseObject;
import com.chua.distributions.serializer.json.CategorySerializer;
import com.chua.distributions.serializer.json.CompanySerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 5, 2016
 */
@Entity(name = "Product")
@Table(name = Product.TABLE_NAME)
public class Product extends BaseObject {

	private static final long serialVersionUID = 8414389366639381125L;
	
	public static final String TABLE_NAME = "product";
	
	@JsonSerialize(using = CompanySerializer.class)
	private Company company;
	
	@JsonSerialize(using = CategorySerializer.class)
	private Category category;
	
	private String productCode;
	
	private String name;
	
	private String displayName;
	
	private String size;
	
	private Integer packaging;
	
	private String description;
	
	private Float grossPrice;
	
	private Float discount;
	
	private Float netPrice;
	
	private Float sellingPrice;
	
	private Float percentProfit;
	
	private Integer stockCount;
	
	@ManyToOne(targetEntity = Company.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	@ManyToOne(targetEntity = Category.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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
	@Column(name = "name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
	@Column(name = "size")
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
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
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Basic
	@Column(name = "gross_price")
	public Float getGrossPrice() {
		return grossPrice;
	}
	
	@Transient
	public Float getPackageGrossPrice() {
		return Math.round(grossPrice * packaging * 100.0f) / 100.0f;
	}
	
	@Transient
	public String getFormattedPackageGrossPrice() {
		DecimalFormat df = new DecimalFormat("#,##0.00");
		return "Php " + df.format(getPackageGrossPrice());
	}

	public void setGrossPrice(Float grossPrice) {
		this.grossPrice = grossPrice;
	}

	@Basic
	@Column(name = "discount")
	public Float getDiscount() {
		return discount;
	}
	
	@Transient
	public Float getDiscountAmount() {
		return Math.round(netPrice * discount) / 100.0f;
	}
	
	@Transient
	public Float getPackageDiscountAmount() {
		return Math.round(getPackageGrossPrice() * discount) / 100.0f;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	@Basic
	@Column(name = "net_price")
	public Float getNetPrice() {
		return netPrice;
	}
	
	@Transient
	public Float getPackageNetPrice() {
		return Math.round(netPrice * packaging * 100.0f) / 100.0f;
	}
	
	@Transient
	public String getFormattedPackageNetPrice() {
		DecimalFormat df = new DecimalFormat("#,##0.00");
		return "Php " + df.format(getPackageNetPrice());
	}

	public void setNetPrice(Float netPrice) {
		this.netPrice = netPrice;
	}

	@Basic
	@Column(name = "selling_price")
	public Float getSellingPrice() {
		return sellingPrice;
	}
	
	@Transient
	public Float getPackageSellingPrice() {
		return Math.round(sellingPrice * packaging * 100.0f) / 100.0f;
	}
	
	@Transient
	public String getFormattedPackageSellingPrice() {
		DecimalFormat df = new DecimalFormat("#,##0.00");
		return "Php " + df.format(getPackageSellingPrice());
	}

	public void setSellingPrice(Float sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	@Basic
	@Column(name = "percent_profit")
	public Float getPercentProfit() {
		return percentProfit;
	}
	
	@Transient
	public Float getProfitAmount() {
		return Math.round((netPrice - grossPrice) * 100.0f) / 100.0f;
	}
	
	@Transient
	public Float getPackageProfitAmount() {
		return Math.round((getPackageSellingPrice() - getPackageNetPrice()) * 100.0f) / 100.0f;
	}

	public void setPercentProfit(Float percentProfit) {
		this.percentProfit = percentProfit;
	}

	@Basic
	@Column(name = "stock_count")
	public Integer getStockCount() {
		return stockCount;
	}
	
	@Transient
	public String getFormattedStockCount() {
		String formattedStockCount = "";
		
		int wholeCount = stockCount / packaging;
		int innerCount = stockCount % packaging;
		if(wholeCount > 0) formattedStockCount += wholeCount + " ";
		if(innerCount > 0) formattedStockCount += innerCount + "/" + packaging;
		if(formattedStockCount.equals("")) formattedStockCount += "0";
		
		return formattedStockCount;
	}

	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
}
