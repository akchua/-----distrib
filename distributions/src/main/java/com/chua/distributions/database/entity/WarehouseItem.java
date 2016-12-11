package com.chua.distributions.database.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.chua.distributions.database.entity.base.BaseObject;
import com.chua.distributions.enums.Warehouse;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 9, 2016
 */
@Entity(name = "WarehouseItem")
@Table(name = WarehouseItem.TABLE_NAME)
public class WarehouseItem extends BaseObject {

	private static final long serialVersionUID = -1143017366024508754L;

	public static final String TABLE_NAME = "warehouse_item";
	
	private Warehouse warehouse;
	
	private Product product;
	
	private Integer stockCount;

	@Enumerated(EnumType.STRING)
	@Column(name = "warehouse", length = 50)
	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	@ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Basic
	@Column(name = "stock_count")
	public Integer getStockCount() {
		return stockCount;
	}
	
	@Transient
	public String getFormattedStockCount() {
		String formattedStockCount = "";
		
		int packageCount = stockCount / product.getPackaging();
		int pieceCount = stockCount % product.getPackaging();
		if(packageCount > 0) formattedStockCount += packageCount + "";
		if(pieceCount > 0) formattedStockCount += " & " + pieceCount + "/" + product.getPackaging();
		if(formattedStockCount.equals("")) formattedStockCount += "0";
		
		return formattedStockCount;
	}

	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
}
