package com.chua.distributions.rest.handler;

import com.chua.distributions.enums.Warehouse;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	27 Jan 2017
 */
public interface WarehouseItemHandler {

	boolean addToWarehouse(Long productId, Warehouse warehouse, Integer quantity);
	
	boolean removeFromWarehouse(Long productId, Warehouse warehouse, Integer quantity);
}
