package com.chua.distributions.database.dao.impl;

import java.util.List;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.distributions.database.dao.ClientOrderDAO;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.enums.Status;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
@Repository
public class ClientOrderDAOImpl
		extends AbstractDAO<ClientOrder, Long>
		implements ClientOrderDAO {

	@Override
	public ObjectList<ClientOrder> findByClientWithPaging(int pageNumber, int resultsPerPage, Long clientId, boolean showPaid) {
		return findByClientWithPagingAndOrder(pageNumber, resultsPerPage, clientId, showPaid, null);
	}

	@Override
	public ObjectList<ClientOrder> findByClientWithPagingAndOrder(int pageNumber, int resultsPerPage, Long clientId, boolean showPaid,
			Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("creator.id", clientId));
		
		if(!showPaid) {
			conjunction.add(Restrictions.ne("status", Status.PAID));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}
	
	@Override
	public ObjectList<ClientOrder> findAllWithPagingAndStatus(int pageNumber, int resultsPerPage, Status[] status) {
		return findAllWithPagingStatusAndOrder(pageNumber, resultsPerPage, status, null);
	}
	
	@Override
	public ObjectList<ClientOrder> findAllWithPagingStatusAndOrder(int pageNumber, int resultsPerPage, Status[] status,
			Order[] orders) {
		return findByWarehouseWithPagingStatusAndOrder(pageNumber, resultsPerPage, null, status, orders);
	}
	
	@Override
	public ObjectList<ClientOrder> findByWarehouseWithPagingStatusAndOrder(int pageNumber, int resultsPerPage,
			Warehouse warehouse, Status[] status, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(warehouse != null) {
			conjunction.add(Restrictions.eq("warehouse", warehouse));
		}
		
		if(status != null && status.length > 0) {
			final Junction disjunction = Restrictions.disjunction();
			for(Status stat : status) {
				disjunction.add(Restrictions.eq("status", stat));
			}
			conjunction.add(disjunction);
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}

	@Override
	public List<ClientOrder> findAllByClientAndStatus(Long clientId, Status[] status) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("creator.id", clientId));
		
		if(status != null && status.length > 0) {
			final Junction disjunction = Restrictions.disjunction();
			for(Status stat : status) {
				disjunction.add(Restrictions.eq("status", stat));
			}
			conjunction.add(disjunction);
		}
		
		return findAllByCriterionList(null, null, null, null, conjunction);
	}

	@Override
	public List<ClientOrder> findAllByStatus(Status[] status) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(status != null && status.length > 0) {
			final Junction disjunction = Restrictions.disjunction();
			for(Status stat : status) {
				disjunction.add(Restrictions.eq("status", stat));
			}
			conjunction.add(disjunction);
		}
		
		return findAllByCriterionList(null, null, null, null, conjunction);
	}
}
