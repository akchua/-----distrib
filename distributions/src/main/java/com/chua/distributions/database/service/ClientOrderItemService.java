package com.chua.distributions.database.service;

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
}
