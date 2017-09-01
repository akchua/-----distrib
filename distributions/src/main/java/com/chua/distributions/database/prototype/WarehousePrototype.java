package com.chua.distributions.database.prototype;

import com.chua.distributions.database.entity.Warehouse;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   31 Aug 2017
 */
public interface WarehousePrototype extends Prototype<Warehouse, Long> {

	Warehouse findByName(String name);
}
