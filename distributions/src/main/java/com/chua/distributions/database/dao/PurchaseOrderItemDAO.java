package com.chua.distributions.database.dao;

import org.hibernate.criterion.Order;

import com.chua.distributions.database.entity.PurchaseOrderItem;
import com.chua.distributions.database.prototype.PurchaseOrderItemPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 7, 2016
 */
public interface PurchaseOrderItemDAO extends DAO<PurchaseOrderItem, Long>, PurchaseOrderItemPrototype {

	ObjectList<PurchaseOrderItem> findAllWithPaging(int pageNumber, int resultsPerPage, Long purchaseOrderId);
	
	ObjectList<PurchaseOrderItem> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, Long purchaseOrderId, Order[] orders);
}
