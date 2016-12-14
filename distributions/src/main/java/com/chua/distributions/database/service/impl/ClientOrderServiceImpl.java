package com.chua.distributions.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.ClientOrderDAO;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.database.service.ClientOrderService;
import com.chua.distributions.enums.Status;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
@Service
public class ClientOrderServiceImpl
		extends AbstractService<ClientOrder, Long, ClientOrderDAO>
		implements ClientOrderService {

	@Autowired
	protected ClientOrderServiceImpl(ClientOrderDAO dao) {
		super(dao);
	}

	@Override
	public ObjectList<ClientOrder> findByClientWithPaging(int pageNumber, int resultsPerPage, Long clientId, boolean showPaid) {
		return dao.findByClientWithPaging(pageNumber, resultsPerPage, clientId, showPaid);
	}
	
	@Override
	public ObjectList<ClientOrder> findAllRequestWithPaging(int pageNumber, int resultsPerPage, boolean showAccepted) {
		if(showAccepted) return dao.findAllWithPagingAndStatus(pageNumber, resultsPerPage, new Status[] { Status.SUBMITTED, Status.ACCEPTED });
		else return	dao.findAllWithPagingAndStatus(pageNumber, resultsPerPage, new Status[] { Status.SUBMITTED });
	}
	
	@Override
	public ObjectList<ClientOrder> findAllPaidWithPagingOrderByLatest(int pageNumber, int resultsPerPage,
			Warehouse warehouse) {
		return dao.findByWarehouseWithPagingStatusAndOrder(pageNumber, resultsPerPage, warehouse, new Status[] { Status.PAID }, new Order[] { Order.desc("id") });
	}
	
	@Override
	public ObjectList<ClientOrder> findAllAcceptedWithPaging(int pageNumber, int resultsPerPage, Warehouse warehouse) {
		return dao.findByWarehouseWithPagingStatusAndOrder(pageNumber, resultsPerPage, warehouse, new Status[] { Status.ACCEPTED }, null);
	}
	
	@Override
	public ObjectList<ClientOrder> findAllReceivedWithPaging(int pageNumber, int resultsPerPage, Warehouse warehouse) {
		return dao.findByWarehouseWithPagingStatusAndOrder(pageNumber, resultsPerPage, warehouse, new Status[] { Status.RECEIVED }, null);
	}

	@Override
	public List<ClientOrder> findAllByClientWithStatusCreating(Long clientId) {
		return dao.findAllByClientAndStatus(clientId, new Status[] { Status.CREATING });
	}
}
