package com.chua.distributions.database.service;

import com.chua.distributions.database.entity.WarehouseItem;
import com.chua.distributions.database.prototype.WarehouseItemPrototype;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 9, 2016
 */
public interface WarehouseItemService extends Service<WarehouseItem, Long>, WarehouseItemPrototype {

	ObjectList<WarehouseItem> findAllWithPagingOrderByProductName(int pageNumber, int resultsPerPage, String searchKey, Warehouse warehouse);
}
