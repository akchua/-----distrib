package com.chua.distributions.database.entity;

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
import com.chua.distributions.serializer.json.ProductSerializer;
import com.chua.distributions.serializer.json.WarehouseSerializer;
import com.chua.distributions.utility.format.QuantityFormatter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
	
	@JsonSerialize(using = WarehouseSerializer.class)
	private Warehouse warehouse;
	
	@JsonSerialize(using = ProductSerializer.class)
	private Product product;
	
	private Integer stockCount;

	@ManyToOne(targetEntity = Warehouse.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "warehouse_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
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
		return QuantityFormatter.format(stockCount, product.getPackaging());
	}

	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
}
