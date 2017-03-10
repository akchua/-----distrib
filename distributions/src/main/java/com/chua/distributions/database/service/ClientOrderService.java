package com.chua.distributions.database.service;

import java.util.List;

import com.chua.distributions.beans.SalesReportQueryBean;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.database.prototype.ClientOrderPrototype;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
public interface ClientOrderService extends Service<ClientOrder, Long>, ClientOrderPrototype {

	ObjectList<ClientOrder> findByClientWithPaging(int pageNumber, int resultsPerPage, Long clientId, boolean showPaid);
	
	ObjectList<ClientOrder> findAllRequestByCreatorWithPagingOrderByLatest(int pageNumber, int resultsPerPage, Long creatorId);
	
	ObjectList<ClientOrder> findAllRequestWithPagingOrderByLatest(int pageNumber, int resultsPerPage, boolean showAccepted);
	
	ObjectList<ClientOrder> findAllPaidWithPagingOrderByLatest(int pageNumber, int resultsPerPage, Warehouse warehouse);
	
	ObjectList<ClientOrder> findAllAcceptedWithPaging(int pageNumber, int resultsPerPage, Warehouse warehouse);
	
	ObjectList<ClientOrder> findAllReceivedWithPaging(int pageNumber, int resultsPerPage, Warehouse warehouse);
	
	ObjectList<ClientOrder> findBySalesReportQueryWithPaging(int pageNumber, int resultsPerPage, SalesReportQueryBean salesReportQuery);
	
	List<ClientOrder> findAllCreatingOrSubmittedByClient(Long clientId);
	
	List<ClientOrder> findAllReceived();
	
	List<ClientOrder> findAllToFollowByClient(Long clientId);
}
