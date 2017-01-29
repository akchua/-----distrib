package com.chua.distributions.database.prototype;

import java.util.List;

import com.chua.distributions.database.entity.StockAdjust;
import com.chua.distributions.enums.Warehouse;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	26 Jan 2017
 */
public interface StockAdjustPrototype extends Prototype<StockAdjust, Long> {

	List<StockAdjust> findAllByProductAndWarehouse(Long productId, Warehouse warehouse);
}
