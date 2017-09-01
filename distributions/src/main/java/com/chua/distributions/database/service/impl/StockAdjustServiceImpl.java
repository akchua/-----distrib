package com.chua.distributions.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.StockAdjustDAO;
import com.chua.distributions.database.entity.StockAdjust;
import com.chua.distributions.database.service.StockAdjustService;
import com.chua.distributions.objects.ObjectList;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	26 Jan 2017
 */
@Service
public class StockAdjustServiceImpl
		extends AbstractService<StockAdjust, Long, StockAdjustDAO>
		implements StockAdjustService {

	@Autowired
	protected StockAdjustServiceImpl(StockAdjustDAO dao) {
		super(dao);
	}

	@Override
	public ObjectList<StockAdjust> findByProductWithPagingOrderByLastUpdate(int pageNumber, int resultsPerPage,
			Long productId) {
		return dao.findByProductWithPagingAndOrder(pageNumber, resultsPerPage, productId, new Order[] { Order.desc("updatedOn") });
	}

	@Override
	public List<StockAdjust> findAllByProductAndWarehouse(Long productId, Long warehouseId) {
		return dao.findAllByProductAndWarehouse(productId, warehouseId);
	}
}
