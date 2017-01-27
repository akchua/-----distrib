package com.chua.distributions.database.dao;

import org.hibernate.criterion.Order;

import com.chua.distributions.database.entity.StockAdjust;
import com.chua.distributions.database.prototype.StockAdjustPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	26 Jan 2017
 */
public interface StockAdjustDAO extends DAO<StockAdjust, Long>, StockAdjustPrototype {
	
	ObjectList<StockAdjust> findByProductWithPagingAndOrder(int pageNumber, int resultsPerPage, Long productId, Order[] orders);
}
