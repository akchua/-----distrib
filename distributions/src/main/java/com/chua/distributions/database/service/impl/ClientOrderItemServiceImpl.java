package com.chua.distributions.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.ClientOrderItemDAO;
import com.chua.distributions.database.entity.ClientOrderItem;
import com.chua.distributions.database.service.ClientOrderItemService;
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
	public ObjectList<ClientOrderItem> findAllWithPagingOrderByLastUpdate(int pageNumber, int resultsPerPage,
			Long clientOrderId) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, clientOrderId, new Order[] { Order.desc("updatedOn") });
	}

	@Override
	public ClientOrderItem findByNameAndClientOrder(String displayName, Long clientOrderId) {
		return dao.findByNameAndClientOrder(displayName, clientOrderId);
	}

	@Override
	public List<ClientOrderItem> findAllByClientOrder(Long clientOrderId) {
		return dao.findAllByClientOrder(clientOrderId);
	}
}
