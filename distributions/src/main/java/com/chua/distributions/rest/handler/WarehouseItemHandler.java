package com.chua.distributions.rest.handler;

import com.chua.distributions.beans.StringWrapper;
import com.chua.distributions.database.entity.WarehouseItem;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	27 Jan 2017
 */
public interface WarehouseItemHandler {

	ObjectList<WarehouseItem> getWarehouseItemObjectList(Integer pageNumber, String searchKey, Warehouse warehouse);
	
	StringWrapper getFormattedPurchaseValue(Warehouse warehouse);
	
	boolean addToWarehouse(Long productId, Warehouse warehouse, Integer quantity);
	
	boolean removeFromWarehouse(Long productId, Warehouse warehouse, Integer quantity);
}
