package com.chua.distributions.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.beans.SalesReportQueryBean;
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
	public ObjectList<ClientOrder> findAllRequestByCreatorWithPagingOrderByRequestedOn(int pageNumber, int resultsPerPage, Long creatorId) {
		return dao.findByCreatorWithPagingStatusAndOrder(pageNumber, resultsPerPage, new Status[] { Status.CREATING, Status.SUBMITTED }, creatorId, new Order[] { Order.asc("requestedOn") });
	}
	
	@Override
	public ObjectList<ClientOrder> findAllRequestWithPagingOrderByRequestedOn(int pageNumber, int resultsPerPage, boolean showAccepted) {
		final Status[] status;
		if(showAccepted) status = new Status[] { Status.SUBMITTED, Status.ACCEPTED, Status.TO_FOLLOW };
		else status = new Status[] { Status.SUBMITTED };
		
		return dao.findAllWithPagingStatusAndOrder(pageNumber, resultsPerPage, status, new Order[] { Order.asc("requestedOn") });
	}
	
	@Override
	public ObjectList<ClientOrder> findAllPaidWithPagingOrderByPaidOn(int pageNumber, int resultsPerPage,
			Warehouse warehouse) {
		return dao.findByWarehouseWithPagingStatusAndOrder(pageNumber, resultsPerPage, warehouse, new Status[] { Status.PAID }, new Order[] { Order.desc("paidOn") });
	}
	
	@Override
	public ObjectList<ClientOrder> findAllAcceptedWithPaging(int pageNumber, int resultsPerPage, Warehouse warehouse) {
		return dao.findByWarehouseWithPagingStatusAndOrder(pageNumber, resultsPerPage, warehouse, new Status[] { Status.ACCEPTED, Status.TO_FOLLOW }, null);
	}
	
	@Override
	public ObjectList<ClientOrder> findAllReceivedWithPagingOrderByDeliveredOn(int pageNumber, int resultsPerPage, Warehouse warehouse) {
		return dao.findByWarehouseWithPagingStatusAndOrder(pageNumber, resultsPerPage, warehouse, new Status[] { Status.RECEIVED }, new Order[] { Order.asc("deliveredOn") });
	}

	@Override
	public List<ClientOrder> findAllCreatingOrSubmittedByClient(Long clientId) {
		return dao.findAllByClientAndStatus(clientId, new Status[] { Status.CREATING, Status.SUBMITTED });
	}

	@Override
	public List<ClientOrder> findAllReceived() {
		return dao.findAllByStatus(new Status[] { Status.RECEIVED });
	}
	
	@Override
	public List<ClientOrder> findAllToFollowByClient(Long clientId) {
		return dao.findAllByClientAndStatus(clientId, new Status[] { Status.TO_FOLLOW });
	}

	@Override
	public ObjectList<ClientOrder> findBySalesReportQueryWithPaging(int pageNumber, int resultsPerPage,
			SalesReportQueryBean salesReportQuery) {
		return dao.findBySalesReportQueryWithPagingAndOrder(pageNumber, resultsPerPage, salesReportQuery, new Order[] { Order.asc("createdOn") });
	}
	
	@Override
	public List<ClientOrder> findAllBySalesReportQuery(SalesReportQueryBean salesReportQuery) {
		return dao.findAllBySalesReportQuery(salesReportQuery);
	}
}
