package com.chua.distributions.database.dao.impl;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.distributions.database.dao.PurchaseOrderDAO;
import com.chua.distributions.database.entity.PurchaseOrder;
import com.chua.distributions.enums.Area;
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
	public ObjectList<PurchaseOrder> findAllWithPaging(int pageNumber, int resultsPerPage, Long companyId, Area area) {
		return findAllWithPagingAndOrder(pageNumber, resultsPerPage, companyId, area, null);
	}

	@Override
	public ObjectList<PurchaseOrder> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, Long companyId,
			Area area, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(companyId != null) {
			conjunction.add(Restrictions.eq("company.id", companyId));
		}
		
		if(area != null) {
			conjunction.add(Restrictions.eq("area", area));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}
}
