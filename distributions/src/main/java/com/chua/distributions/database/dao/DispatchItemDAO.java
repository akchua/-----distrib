package com.chua.distributions.database.dao;

import org.hibernate.criterion.Order;

import com.chua.distributions.database.entity.DispatchItem;
import com.chua.distributions.database.prototype.DispatchItemPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 14, 2016
 */
public interface DispatchItemDAO extends DAO<DispatchItem, Long>, DispatchItemPrototype {

	ObjectList<DispatchItem> findAllWithPaging(int pageNumber, int resultsPerPage, Long dispatchId);
	
	ObjectList<DispatchItem> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, Long dispatchId, Order[] orders);
}
