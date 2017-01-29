package com.chua.distributions.database.service;

import com.chua.distributions.database.entity.StockAdjust;
import com.chua.distributions.database.prototype.StockAdjustPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	26 Jan 2017
 */
public interface StockAdjustService extends Service<StockAdjust, Long>, StockAdjustPrototype {

	ObjectList<StockAdjust> findByProductWithPagingOrderByLastUpdate(int pageNumber, int resultsPerPage, Long productId);
}
