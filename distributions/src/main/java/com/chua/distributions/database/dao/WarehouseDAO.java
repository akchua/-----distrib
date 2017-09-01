package com.chua.distributions.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.distributions.database.entity.Warehouse;
import com.chua.distributions.database.prototype.WarehousePrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   31 Aug 2017
 */
public interface WarehouseDAO extends DAO<Warehouse, Long>, WarehousePrototype {

	ObjectList<Warehouse> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey);
	
	ObjectList<Warehouse> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, String searchKey, Order[] orders);
	
	List<Warehouse> findAllWithOrder(Order[] orders);
}
