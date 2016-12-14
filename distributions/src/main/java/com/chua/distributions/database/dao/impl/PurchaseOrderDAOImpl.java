package com.chua.distributions.database.dao.impl;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.distributions.database.dao.PurchaseOrderDAO;
import com.chua.distributions.database.entity.PurchaseOrder;
import com.chua.distributions.enums.Status;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 6, 2016
 */
@Repository
public class PurchaseOrderDAOImpl
		extends AbstractDAO<PurchaseOrder, Long>
		implements PurchaseOrderDAO {

	@Override
	public ObjectList<PurchaseOrder> findAllWithPaging(int pageNumber, int resultsPerPage, Long companyId, Warehouse warehouse, boolean showPaid) {
		return findAllWithPagingAndOrder(pageNumber, resultsPerPage, companyId, warehouse, showPaid, null);
	}

	@Override
	public ObjectList<PurchaseOrder> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, Long companyId,
			Warehouse warehouse, boolean showPaid, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(companyId != null) {
			conjunction.add(Restrictions.eq("company.id", companyId));
		}
		
		if(warehouse != null) {
			conjunction.add(Restrictions.eq("warehouse", warehouse));
		}
		
		if(!showPaid) {
			conjunction.add(Restrictions.ne("status", Status.PAID));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}

	@Override
	public ObjectList<PurchaseOrder> findAllPaidWithPagingAndOrder(int pageNumber, int resultsPerPage, Long companyId,
			Warehouse warehouse, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("status", Status.PAID));
		
		if(companyId != null) {
			conjunction.add(Restrictions.eq("company.id", companyId));
		}
		
		if(warehouse != null) {
			conjunction.add(Restrictions.eq("warehouse", warehouse));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}
}
