package com.chua.distributions.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.WarehouseDAO;
import com.chua.distributions.database.entity.Warehouse;
import com.chua.distributions.database.service.WarehouseService;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   31 Aug 2017
 */
@Service
public class WarehouseServiceImpl
		extends AbstractService<Warehouse, Long, WarehouseDAO>
		implements WarehouseService {

	@Autowired
	protected WarehouseServiceImpl(WarehouseDAO dao) {
		super(dao);
	}
	
	@Override
	public ObjectList<Warehouse> findAllWithPagingOrderByName(int pageNumber, int resultsPerPage, String searchKey) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, new Order[] { Order.asc("name") });
	}
	
	@Override
	public List<Warehouse> findAllOrderByName() {
		return dao.findAllWithOrder(new Order[] { Order.asc("name") });
	}

	@Override
	public boolean isExistsByName(String name) {
		return dao.findByName(name) != null;
	}

	@Override
	public Warehouse findByName(String name) {
		return dao.findByName(name);
	}
}
