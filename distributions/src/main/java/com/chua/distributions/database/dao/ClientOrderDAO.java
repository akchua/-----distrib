package com.chua.distributions.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.database.prototype.ClientOrderPrototype;
import com.chua.distributions.enums.Status;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
public interface ClientOrderDAO extends DAO<ClientOrder, Long>, ClientOrderPrototype {

	ObjectList<ClientOrder> findByClientWithPaging(int pageNumber, int resultsPerPage, Long clientId, boolean showPaid);
	
	ObjectList<ClientOrder> findByClientWithPagingAndOrder(int pageNumber, int resultsPerPage, Long clientId, boolean showPaid, Order[] orders);
	
	ObjectList<ClientOrder> findAllWithPagingAndStatus(int pageNumber, int resultsPerPage, Status[] status);
	
	ObjectList<ClientOrder> findAllWithPagingStatusAndOrder(int pageNumber, int resultsPerPage, Status[] status, Order[] orders);
	
	ObjectList<ClientOrder> findByCreatorWithPagingStatusAndOrder(int pageNumber, int resultsPerPage, Status[] status, Long creatorId, Order[] orders);
	
	ObjectList<ClientOrder> findByWarehouseWithPagingStatusAndOrder(int pageNumber, int resultsPerPage, Warehouse warehouse, Status[] status, Order[] orders);

	List<ClientOrder> findAllByClientAndStatus(Long clientId, Status[] status);
	
	List<ClientOrder> findAllByStatus(Status[] status);
}
