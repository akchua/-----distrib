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
import com.chua.distributions.serializer.json.UserSerializer;
import com.chua.distributions.serializer.json.WarehouseSerializer;
import com.chua.distributions.utility.format.QuantityFormatter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	26 Jan 2017
 */
@Entity(name = "StockAdjust")
@Table(name = StockAdjust.TABLE_NAME)
public class StockAdjust extends BaseObject {

	private static final long serialVersionUID = -5600283328299778959L;
	
	public static final String TABLE_NAME = "stock_adjust";
	
	private Long productId;
	
	private String productCode;
	
	private String displayName;
	
	private Integer packaging;
	
	@JsonSerialize(using = WarehouseSerializer.class)
	private Warehouse warehouse;
	
	private Integer quantity;
	
	private String description;

	@JsonSerialize(using = UserSerializer.class)
	private User creator;
	
	@Basic
	@Column(name = "product_id")
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
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

	@Basic
	@Column(name = "quantity")
	public Integer getQuantity() {
		return quantity;
	}
	
	@Transient
	public String getFormattedQuantity() {
		return QuantityFormatter.format(quantity, packaging);
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Basic
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}
}
