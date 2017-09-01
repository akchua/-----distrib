package com.chua.distributions.rest.handler;

import com.chua.distributions.database.entity.WarehouseItem;
import com.chua.distributions.objects.ObjectList;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	27 Jan 2017
 */
public interface WarehouseItemHandler {

	ObjectList<WarehouseItem> getWarehouseItemObjectList(Integer pageNumber, String searchKey, Long warehouseId);
	
	String getFormattedPurchaseValue(Long warehouseId);
	
	boolean addToWarehouse(Long productId, Long warehouseId, Integer quantity);
	
	boolean removeFromWarehouse(Long productId, Long warehouseId, Integer quantity);
}
