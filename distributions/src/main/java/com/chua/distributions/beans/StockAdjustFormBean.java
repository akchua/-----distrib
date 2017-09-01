package com.chua.distributions.beans;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	26 Jan 2017
 */
public class StockAdjustFormBean {

	private Long warehouseId;
	
	private String description;
	
	private Integer pieceQuantity;
	
	private Integer packageQuantity;
	
	private Long productId;

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPieceQuantity() {
		return pieceQuantity;
	}

	public void setPieceQuantity(Integer pieceQuantity) {
		this.pieceQuantity = pieceQuantity;
	}

	public Integer getPackageQuantity() {
		return packageQuantity;
	}

	public void setPackageQuantity(Integer packageQuantity) {
		this.packageQuantity = packageQuantity;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
}
