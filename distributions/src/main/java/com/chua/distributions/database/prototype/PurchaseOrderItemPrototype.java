package com.chua.distributions.database.prototype;

import java.util.List;

import com.chua.distributions.database.entity.PurchaseOrderItem;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 7, 2016
 */
public interface PurchaseOrderItemPrototype extends Prototype<PurchaseOrderItem, Long> {

	PurchaseOrderItem findByNameAndPurchaseOrder(String displayName, Long purchaseOrderId);
	
	List<PurchaseOrderItem> findAllByPurchaseOrder(Long purchaseOrderId);
}
