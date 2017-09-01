package com.chua.distributions.rest.handler;

import java.util.List;

import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.WarehouseFormBean;
import com.chua.distributions.database.entity.Warehouse;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   31 Aug 2017
 */
public interface WarehouseHandler {

	Warehouse getWarehouse(Long warehouseId);

	ObjectList<Warehouse> getWarehouseObjectList(Integer pageNumber, String searchKey);
	
	List<Warehouse> getWarehouseList();
	
	ResultBean createWarehouse(WarehouseFormBean warehouseForm);
	
	ResultBean updateWarehouse(WarehouseFormBean warehouseForm);
	
	ResultBean removeWarehouse(Long warehouseId);
}
