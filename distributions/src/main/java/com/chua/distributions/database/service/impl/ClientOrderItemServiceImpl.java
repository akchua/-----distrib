package com.chua.distributions.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.ClientOrderItemDAO;
import com.chua.distributions.database.entity.ClientOrderItem;
import com.chua.distributions.database.service.ClientOrderItemService;
import com.chua.distributions.enums.Status;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
@Service
public class ClientOrderItemServiceImpl
		extends AbstractService<ClientOrderItem, Long, ClientOrderItemDAO>
		implements ClientOrderItemService {

	@Autowired
	protected ClientOrderItemServiceImpl(ClientOrderItemDAO dao) {
		super(dao);
	}

	@Override
	public ObjectList<ClientOrderItem> findAllWithPaging(int pageNumber, int resultsPerPage,
			Long clientOrderId) {
		return dao.findAllWithPaging(pageNumber, resultsPerPage, clientOrderId);
	}
	
	@Override
	public ObjectList<ClientOrderItem> findByProductWithPagingOrderByLastUpdate(int pageNumber, int resultsPerPage,
			Long productId) {
		return dao.findByProductWithPagingAndOrder(pageNumber, resultsPerPage, productId, new Order[] { Order.desc("order.deliveredOn") });
	}

	@Override
	public ClientOrderItem findByProductAndClientOrder(Long productId, Long clientOrderId) {
		return dao.findByProductAndClientOrder(productId, clientOrderId);
	}

	@Override
	public List<ClientOrderItem> findAllByClientOrder(Long clientOrderId) {
		return dao.findAllByClientOrder(clientOrderId);
	}
	
	@Override
	public List<ClientOrderItem> findAllUnstockedByProductAndWarehouse(Long productId, Warehouse warehouse) {
		return dao.findAllByProductWarehouseAndStatus(productId, warehouse, new Status[] { Status.PAID, Status.RECEIVED });
	}
}
