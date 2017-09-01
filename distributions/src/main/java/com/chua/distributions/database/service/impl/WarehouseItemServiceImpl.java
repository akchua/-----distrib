package com.chua.distributions.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.WarehouseItemDAO;
import com.chua.distributions.database.entity.WarehouseItem;
import com.chua.distributions.database.service.WarehouseItemService;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 9, 2016
 */
@Service
public class WarehouseItemServiceImpl
		extends AbstractService<WarehouseItem, Long, WarehouseItemDAO>
		implements WarehouseItemService {

	@Autowired
	protected WarehouseItemServiceImpl(WarehouseItemDAO dao) {
		super(dao);
	}
	
	@Override
	public ObjectList<WarehouseItem> findAllWithPagingOrderByProductName(int pageNumber, int resultsPerPage,
			String searchKey, Long warehouseId) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, warehouseId, new Order[] { Order.asc("prod.displayName") });
	}
	
	@Override
	public List<WarehouseItem> findAllByProduct(Long productId) {
		return dao.findAllByProduct(productId);
	}
	
	@Override
	public List<WarehouseItem> findAllByWarehouse(Long warehouseId) {
		return dao.findAllByWarehouse(warehouseId);
	}

	@Override
	public WarehouseItem findByProductAndWarehouse(Long productId, Long warehouseId) {
		return dao.findByProductAndWarehouse(productId, warehouseId);
	}
}
