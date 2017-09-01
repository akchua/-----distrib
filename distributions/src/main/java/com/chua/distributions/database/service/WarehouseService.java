package com.chua.distributions.database.service;

import java.util.List;

import com.chua.distributions.database.entity.Warehouse;
import com.chua.distributions.database.prototype.WarehousePrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   31 Aug 2017
 */
public interface WarehouseService extends Service<Warehouse, Long>, WarehousePrototype {

	ObjectList<Warehouse> findAllWithPagingOrderByName(int pageNumber, int resultsPerPage, String searchKey);
	
	List<Warehouse> findAllOrderByName();
	
	boolean isExistsByName(String name);
}
