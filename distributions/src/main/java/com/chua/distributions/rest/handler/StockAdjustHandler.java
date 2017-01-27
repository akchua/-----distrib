package com.chua.distributions.rest.handler;

import java.util.List;

import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.StockAdjustFormBean;
import com.chua.distributions.database.entity.StockAdjust;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	26 Jan 2017
 */
public interface StockAdjustHandler {

	ObjectList<StockAdjust> getStockAdjustByProductObjectList(Integer pageNumber, Long productId);
	
	ResultBean adjustStock(StockAdjustFormBean stockAdjustForm);

	List<Warehouse> getWarehouseList();
}
