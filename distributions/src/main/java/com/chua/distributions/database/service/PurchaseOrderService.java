package com.chua.distributions.database.service;

import java.util.List;

import com.chua.distributions.database.entity.PurchaseOrder;
import com.chua.distributions.database.prototype.PurchaseOrderPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 6, 2016
 */
public interface PurchaseOrderService extends Service<PurchaseOrder, Long>, PurchaseOrderPrototype {

	ObjectList<PurchaseOrder> findAllWithPagingOrderByStatus(int pageNumber, int resultsPerPage, Long companyId, Long warehouseId, boolean showPaid);
	
	ObjectList<PurchaseOrder> findAllPaidWithPagingOrderByLatest(int pageNumber, int resultsPerPage, Long companyId, Long warehouseId);

	List<PurchaseOrder> findAllToFollowByCompanyAndWarehouse(Long companyId, Long warehouseId);
}
