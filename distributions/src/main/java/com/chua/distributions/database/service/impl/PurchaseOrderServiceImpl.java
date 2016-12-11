package com.chua.distributions.database.service.impl;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.PurchaseOrderDAO;
import com.chua.distributions.database.entity.PurchaseOrder;
import com.chua.distributions.database.service.PurchaseOrderService;
import com.chua.distributions.enums.Warehouse;
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
	public ObjectList<PurchaseOrder> findAllWithPagingOrderByStatus(int pageNumber, int resultsPerPage, Long companyId, Warehouse warehouse, boolean showPaid) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, companyId, warehouse, showPaid, new Order[] { Order.asc("status") });
	}
}
