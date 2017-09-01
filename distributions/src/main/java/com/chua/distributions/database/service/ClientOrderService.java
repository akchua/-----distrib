package com.chua.distributions.database.service;

import java.util.List;

import com.chua.distributions.beans.SalesReportQueryBean;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.database.prototype.ClientOrderPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
public interface ClientOrderService extends Service<ClientOrder, Long>, ClientOrderPrototype {

	ObjectList<ClientOrder> findByClientWithPaging(int pageNumber, int resultsPerPage, Long clientId, boolean showPaid);
	
	ObjectList<ClientOrder> findAllRequestByCreatorWithPagingOrderByRequestedOn(int pageNumber, int resultsPerPage, Long creatorId);
	
	ObjectList<ClientOrder> findAllRequestWithPagingOrderByRequestedOn(int pageNumber, int resultsPerPage, boolean showAccepted);
	
	ObjectList<ClientOrder> findAllPaidWithPagingOrderByPaidOn(int pageNumber, int resultsPerPage, Long warehouseId);
	
	ObjectList<ClientOrder> findAllAcceptedWithPaging(int pageNumber, int resultsPerPage, Long warehouseId);
	
	ObjectList<ClientOrder> findAllReceivedWithPagingOrderByDeliveredOn(int pageNumber, int resultsPerPage, Long warehouseId, Long clientId);
	
	ObjectList<ClientOrder> findBySalesReportQueryWithPaging(int pageNumber, int resultsPerPage, SalesReportQueryBean salesReportQuery);
	
	List<ClientOrder> findAllCreatingOrSubmittedByClient(Long clientId);
	
	List<ClientOrder> findAllReceived();
	
	List<ClientOrder> findAllToFollowByClient(Long clientId);
}
