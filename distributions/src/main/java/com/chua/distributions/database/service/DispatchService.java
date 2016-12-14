package com.chua.distributions.database.service;

import com.chua.distributions.database.entity.Dispatch;
import com.chua.distributions.database.prototype.DispatchPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 14, 2016
 */
public interface DispatchService extends Service<Dispatch, Long>, DispatchPrototype {

	ObjectList<Dispatch> findAllWithPagingOrderByStatus(int pageNumber, int resultsPerPage, boolean showReceived);
}
