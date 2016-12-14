package com.chua.distributions.database.dao.impl;

import java.util.List;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.distributions.database.dao.ClientOrderItemDAO;
import com.chua.distributions.database.entity.ClientOrderItem;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
@Repository
public class ClientOrderItemDAOImpl
		extends AbstractDAO<ClientOrderItem, Long> 
		implements ClientOrderItemDAO {

	@Override
	public ObjectList<ClientOrderItem> findAllWithPaging(int pageNumber, int resultsPerPage, Long clientOrderId) {
		return findAllWithPagingAndOrder(pageNumber, resultsPerPage, clientOrderId, null);
	}

	@Override
	public ObjectList<ClientOrderItem> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, Long clientOrderId,
			Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("clientOrder.id", clientOrderId));
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}

	@Override
	public ClientOrderItem findByNameAndClientOrder(String displayName, Long clientOrderId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("displayName", displayName));
		conjunction.add(Restrictions.eq("clientOrder.id", clientOrderId));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public List<ClientOrderItem> findAllByClientOrder(Long clientOrderId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("clientOrder.id", clientOrderId));
		
		return findAllByCriterionList(null, null, null, null, conjunction);
	}
}
