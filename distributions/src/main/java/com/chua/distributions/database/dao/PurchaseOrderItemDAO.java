package com.chua.distributions.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.distributions.database.entity.PurchaseOrderItem;
import com.chua.distributions.database.prototype.PurchaseOrderItemPrototype;
import com.chua.distributions.enums.Status;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 7, 2016
 */
public interface PurchaseOrderItemDAO extends DAO<PurchaseOrderItem, Long>, PurchaseOrderItemPrototype {

	ObjectList<PurchaseOrderItem> findAllWithPaging(int pageNumber, int resultsPerPage, Long purchaseOrderId);
	
	ObjectList<PurchaseOrderItem> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, Long purchaseOrderId, Order[] orders);
	
	ObjectList<PurchaseOrderItem> findByProductWithPagingAndOrder(int pageNumber, int resultsPerPage, Long productId, Order[] orders);
	
	/**
	 * This is a heavy process used to locate specific group of purchase order items.
	 * @param productId The id of the product to locate. Cannot be null.
	 * @param warehouse The warehouse where the purchase order item was stocked. Any warehouse if null.
	 * @param status The status of the purchase order it belongs to. Any Status if null.
	 * @return A list of purchase order items belonging to the group.
	 */
	List<PurchaseOrderItem> findAllByProductWarehouseAndStatus(Long productId, Warehouse warehouse, Status[] status);
}
