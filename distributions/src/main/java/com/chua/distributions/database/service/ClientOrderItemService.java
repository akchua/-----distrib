package com.chua.distributions.database.service;

import java.util.List;

import com.chua.distributions.database.entity.ClientOrderItem;
import com.chua.distributions.database.prototype.ClientOrderItemPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
public interface ClientOrderItemService extends Service<ClientOrderItem, Long>, ClientOrderItemPrototype {

	ObjectList<ClientOrderItem> findAllWithPaging(int pageNumber, int resultsPerPage, Long clientOrderId);
	
	ObjectList<ClientOrderItem> findByProductWithPagingOrderByLastUpdate(int pageNumber, int resultsPerPage, Long productId);
	
	/**
	 * This is a heavy process used to locate group of unstocked client order items.
	 * @param productId The id of the product to locate. Cannot be null.
	 * @param warehouse The warehouse where the client order was unstocked. Any warehouse if null.
	 * @return The list of unstocked client order items belonging to the group.
	 */
	List<ClientOrderItem> findAllUnstockedByProductAndWarehouse(Long productId, Long warehouseId);
}
