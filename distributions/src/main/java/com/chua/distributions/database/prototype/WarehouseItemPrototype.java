package com.chua.distributions.database.prototype;

import java.util.List;

import com.chua.distributions.database.entity.WarehouseItem;
import com.chua.distributions.enums.Warehouse;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 9, 2016
 */
public interface WarehouseItemPrototype extends Prototype<WarehouseItem, Long> {

	List<WarehouseItem> findAllByProduct(Long productId);
	
	WarehouseItem findByProductAndWarehouse(Long productId, Warehouse warehouse);
}
