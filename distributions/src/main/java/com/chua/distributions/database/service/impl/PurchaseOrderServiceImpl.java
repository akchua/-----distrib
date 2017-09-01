package com.chua.distributions.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.beans.PurchaseReportQueryBean;
import com.chua.distributions.database.dao.PurchaseOrderDAO;
import com.chua.distributions.database.entity.PurchaseOrder;
import com.chua.distributions.database.service.PurchaseOrderService;
import com.chua.distributions.enums.Status;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 6, 2016
 */
@Service
public class PurchaseOrderServiceImpl
		extends AbstractService<PurchaseOrder, Long, PurchaseOrderDAO>
		implements PurchaseOrderService {

	@Autowired
	protected PurchaseOrderServiceImpl(PurchaseOrderDAO dao) {
		super(dao);
	}

	@Override
	public ObjectList<PurchaseOrder> findAllWithPagingOrderByStatus(int pageNumber, int resultsPerPage, Long companyId, Long warehouseId, boolean showPaid) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, companyId, warehouseId, showPaid, null);
	}

	@Override
	public ObjectList<PurchaseOrder> findAllPaidWithPagingOrderByLatest(int pageNumber, int resultsPerPage,
			Long companyId, Long warehouseId) {
		return dao.findAllPaidWithPagingAndOrder(pageNumber, resultsPerPage, companyId, warehouseId, new Order[] { Order.desc("id") });
	}

	@Override
	public List<PurchaseOrder> findAllToFollowByCompanyAndWarehouse(Long companyId, Long warehouseId) {
		return dao.findAllByCompanyWarehouseAndStatus(companyId, warehouseId, new Status[] { Status.TO_FOLLOW });
	}

	@Override
	public ObjectList<PurchaseOrder> findByPurchaseReportQueryWithPaging(int pageNumber, int resultsPerPage,
			PurchaseReportQueryBean purchaseReportQuery) {
		return dao.findByPurchaseReportQueryWithPaging(pageNumber, resultsPerPage, purchaseReportQuery);
	}
}
