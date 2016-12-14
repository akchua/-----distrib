package com.chua.distributions.database.dao;

import org.hibernate.criterion.Order;

import com.chua.distributions.database.entity.ClientOrderItem;
import com.chua.distributions.database.prototype.ClientOrderItemPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
public interface ClientOrderItemDAO extends DAO<ClientOrderItem, Long>, ClientOrderItemPrototype {

	ObjectList<ClientOrderItem> findAllWithPaging(int pageNumber, int resultsPerPage, Long clientOrderId);
	
	ObjectList<ClientOrderItem> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, Long clientOrderId, Order[] orders);
}
