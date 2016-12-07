package com.chua.distributions.database.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.PurchaseOrderDAO;
import com.chua.distributions.database.entity.PurchaseOrder;
import com.chua.distributions.database.service.PurchaseOrderService;
import com.chua.distributions.enums.Area;
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
	public ObjectList<PurchaseOrder> findAllWithPaging(int pageNumber, int resultsPerPage, Long companyId, Area area) {
		return dao.findAllWithPaging(pageNumber, resultsPerPage, companyId, area);
	}
}
