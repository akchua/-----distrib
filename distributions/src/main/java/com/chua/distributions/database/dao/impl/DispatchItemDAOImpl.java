package com.chua.distributions.database.dao.impl;

import java.util.List;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.distributions.database.dao.DispatchItemDAO;
import com.chua.distributions.database.entity.DispatchItem;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 14, 2016
 */
@Repository
public class DispatchItemDAOImpl
		extends AbstractDAO<DispatchItem, Long>
		implements DispatchItemDAO {

	@Override
	public List<DispatchItem> findAllByDispatch(Long dispatchId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("dispatch.id", dispatchId));
		
		return findAllByCriterionList(null, null, null, null, conjunction);
	}

	@Override
	public DispatchItem findByDispatchAndOrder(Long dispatchId, Long clientOrderId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("dispatch.id", dispatchId));
		conjunction.add(Restrictions.eq("clientOrder.id", clientOrderId));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public ObjectList<DispatchItem> findAllWithPaging(int pageNumber, int resultsPerPage, Long dispatchId) {
		return findAllWithPagingAndOrder(pageNumber, resultsPerPage, dispatchId, null);
	}

	@Override
	public ObjectList<DispatchItem> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, Long dispatchId,
			Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("dispatch.id", dispatchId));
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}
}
