package com.chua.distributions.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.PurchaseOrderItemDAO;
import com.chua.distributions.database.entity.PurchaseOrderItem;
import com.chua.distributions.database.service.PurchaseOrderItemService;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 7, 2016
 */
@Service
public class PurchaseOrderItemServiceImpl
		extends AbstractService<PurchaseOrderItem, Long, PurchaseOrderItemDAO>
		implements PurchaseOrderItemService {

	@Autowired
	protected PurchaseOrderItemServiceImpl(PurchaseOrderItemDAO dao) {
		super(dao);
	}

	@Override
	public ObjectList<PurchaseOrderItem> findAllWithPaging(int pageNumber, int resultsPerPage,
			Long purchaseOrderId) {
		return dao.findAllWithPaging(pageNumber, resultsPerPage, purchaseOrderId);
	}
	
	@Override
	public ObjectList<PurchaseOrderItem> findByProductWithPagingOrderByLastUpdate(int pageNumber, int resultsPerPage,
			Long productId) {
		return dao.findByProductWithPagingAndOrder(pageNumber, resultsPerPage, productId, new Order[] { Order.desc("updatedOn") });
	}

	@Override
	public PurchaseOrderItem findByNameAndPurchaseOrder(String displayName, Long purchaseOrderId) {
		return dao.findByNameAndPurchaseOrder(displayName, purchaseOrderId);
	}

	@Override
	public List<PurchaseOrderItem> findAllByPurchaseOrder(Long purchaseOrderId) {
		return dao.findAllByPurchaseOrder(purchaseOrderId);
	}
}
