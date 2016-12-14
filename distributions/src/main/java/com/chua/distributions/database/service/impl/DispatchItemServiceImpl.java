package com.chua.distributions.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.DispatchItemDAO;
import com.chua.distributions.database.entity.DispatchItem;
import com.chua.distributions.database.service.DispatchItemService;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 14, 2016
 */
@Service
public class DispatchItemServiceImpl
		extends AbstractService<DispatchItem, Long, DispatchItemDAO>
		implements DispatchItemService{

	@Autowired
	protected DispatchItemServiceImpl(DispatchItemDAO dao) {
		super(dao);
	}

	@Override
	public List<DispatchItem> findAllByDispatch(Long dispatchId) {
		return dao.findAllByDispatch(dispatchId);
	}

	@Override
	public DispatchItem findByDispatchAndOrder(Long dispatchId, Long clientOrderId) {
		return dao.findByDispatchAndOrder(dispatchId, clientOrderId);
	}

	@Override
	public ObjectList<DispatchItem> findAllWithPagingOrderByLastUpdate(int pageNumber, int resultsPerPage,
			Long dispatchId) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, dispatchId, new Order[] { Order.desc("updatedOn") });
	}
}
