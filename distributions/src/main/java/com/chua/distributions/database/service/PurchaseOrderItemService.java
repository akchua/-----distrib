package com.chua.distributions.database.service;

import java.util.List;

import com.chua.distributions.database.entity.PurchaseOrderItem;
import com.chua.distributions.database.prototype.PurchaseOrderItemPrototype;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 7, 2016
 */
public interface PurchaseOrderItemService extends Service<PurchaseOrderItem, Long>, PurchaseOrderItemPrototype {

	ObjectList<PurchaseOrderItem> findAllWithPaging(int pageNumber, int resultsPerPage, Long purchaseOrderId);
	
	ObjectList<PurchaseOrderItem> findByProductWithPagingOrderByLastUpdate(int pageNumber, int resultsPerPage, Long productId);

	/**
	 * This is a heavy process used to locate group of stocked purchase order items.
	 * @param productId The id of the product to locate. Cannot be null.
	 * @param warehouse The warehouse where the purchase order was stocked. Any warehouse if null.
	 * @return The list of stocked purchase order items belonging to the group.
	 */
	List<PurchaseOrderItem> findAllStockedByProductAndWarehouse(Long productId, Warehouse warehouse);
}
