package com.chua.distributions.database.dao;

import org.hibernate.criterion.Order;

import com.chua.distributions.database.entity.WarehouseItem;
import com.chua.distributions.database.prototype.WarehouseItemPrototype;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 9, 2016
 */
public interface WarehouseItemDAO extends DAO<WarehouseItem, Long>, WarehouseItemPrototype {

	ObjectList<WarehouseItem> findAllWithPaging(int pageNumber, int resultsPerPage, Warehouse warehouse);
	
	ObjectList<WarehouseItem> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, Warehouse warehouse, Order[] orders);
}
