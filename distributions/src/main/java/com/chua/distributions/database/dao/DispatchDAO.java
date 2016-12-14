package com.chua.distributions.database.dao;

import org.hibernate.criterion.Order;

import com.chua.distributions.database.entity.Dispatch;
import com.chua.distributions.database.prototype.DispatchPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 14, 2016
 */
public interface DispatchDAO extends DAO<Dispatch, Long>, DispatchPrototype {

	ObjectList<Dispatch> findAllWithPaging(int pageNumber, int resultsPerPage, boolean showReceived);
	
	ObjectList<Dispatch> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, boolean showReceived, Order[] orders);
	
}
