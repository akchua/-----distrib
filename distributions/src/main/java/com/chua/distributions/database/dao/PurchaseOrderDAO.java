package com.chua.distributions.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.distributions.database.entity.PurchaseOrder;
import com.chua.distributions.database.prototype.PurchaseOrderPrototype;
import com.chua.distributions.enums.Status;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 6, 2016
 */
public interface PurchaseOrderDAO extends DAO<PurchaseOrder, Long>, PurchaseOrderPrototype {

	ObjectList<PurchaseOrder> findAllWithPaging(int pageNumber, int resultsPerPage, Long companyId, Long warehouseId, boolean showPaid);
	
	ObjectList<PurchaseOrder> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, Long companyId, Long warehouseId, boolean showPaid, Order[] orders);
	
	ObjectList<PurchaseOrder> findAllPaidWithPagingAndOrder(int pageNumber, int resultsPerPage, Long companyId, Long warehouseId, Order[] orders);
	
	List<PurchaseOrder> findAllByCompanyWarehouseAndStatus(Long companyId, Long warehouseId, Status[] status);
}
