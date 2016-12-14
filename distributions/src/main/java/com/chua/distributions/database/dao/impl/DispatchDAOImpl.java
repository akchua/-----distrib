package com.chua.distributions.database.dao.impl;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.distributions.database.dao.DispatchDAO;
import com.chua.distributions.database.entity.Dispatch;
import com.chua.distributions.enums.Status;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 14, 2016
 */
@Repository
public class DispatchDAOImpl
		extends AbstractDAO<Dispatch, Long>
		implements DispatchDAO {

	@Override
	public ObjectList<Dispatch> findAllWithPaging(int pageNumber, int resultsPerPage, boolean showReceived) {
		return findAllWithPagingAndOrder(pageNumber, resultsPerPage, showReceived, null);
	}

	@Override
	public ObjectList<Dispatch> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, boolean showReceived
			, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(!showReceived) {
			conjunction.add(Restrictions.ne("status", Status.RECEIVED));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}
}
