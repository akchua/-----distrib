package com.chua.distributions.database.service;

import com.chua.distributions.database.entity.DispatchItem;
import com.chua.distributions.database.prototype.DispatchItemPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 14, 2016
 */
public interface DispatchItemService extends Service<DispatchItem, Long>, DispatchItemPrototype {

	ObjectList<DispatchItem> findAllWithPagingOrderByLastUpdate(int pageNumber, int resultsPerPage, Long dispatchId);
}
