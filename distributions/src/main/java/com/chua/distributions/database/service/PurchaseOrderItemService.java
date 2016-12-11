package com.chua.distributions.database.service;

import com.chua.distributions.database.entity.PurchaseOrderItem;
import com.chua.distributions.database.prototype.PurchaseOrderItemPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 7, 2016
 */
public interface PurchaseOrderItemService extends Service<PurchaseOrderItem, Long>, PurchaseOrderItemPrototype {

	ObjectList<PurchaseOrderItem> findAllWithPagingOrderByLastUpdate(int pageNumber, int resultsPerPage, Long purchaseOrderId);
}
