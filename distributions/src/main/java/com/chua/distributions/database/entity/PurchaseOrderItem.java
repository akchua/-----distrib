package com.chua.distributions.database.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 7, 2016
 */
@Entity(name = "PurchaseOrderItem")
@Table(name = PurchaseOrderItem.TABLE_NAME)
public class PurchaseOrderItem extends OrderItem {

	private static final long serialVersionUID = 8945895393917955211L;

	public static final String TABLE_NAME = "purchase_order_item";
	
	private PurchaseOrder purchaseOrder;

	@ManyToOne(targetEntity = PurchaseOrder.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "purchase_order_id")
	@Where(clause = "valid = 1")
	@NotFound(action = NotFoundAction.IGNORE)
	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
}
