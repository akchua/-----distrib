package com.chua.distributions.database.service.impl;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.DispatchDAO;
import com.chua.distributions.database.entity.Dispatch;
import com.chua.distributions.database.service.DispatchService;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 14, 2016
 */
@Service
public class DispatchServiceImpl
		extends AbstractService<Dispatch, Long, DispatchDAO>
		implements DispatchService {

	@Autowired
	protected DispatchServiceImpl(DispatchDAO dao) {
		super(dao);
	}

	@Override
	public ObjectList<Dispatch> findAllWithPagingOrderByStatus(int pageNumber, int resultsPerPage, boolean showReceived) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, showReceived, new Order[] { Order.asc("status") });
	}
}
