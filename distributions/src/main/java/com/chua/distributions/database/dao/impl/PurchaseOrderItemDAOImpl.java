package com.chua.distributions.database.dao.impl;

import java.util.List;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Repository;

import com.chua.distributions.database.dao.PurchaseOrderItemDAO;
import com.chua.distributions.database.entity.PurchaseOrderItem;
import com.chua.distributions.enums.Status;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 7, 2016
 */
@Repository
public class PurchaseOrderItemDAOImpl
		extends AbstractDAO<PurchaseOrderItem, Long>
		implements PurchaseOrderItemDAO {

	@Override
	public ObjectList<PurchaseOrderItem> findAllWithPaging(int pageNumber, int resultsPerPage, Long purchaseOrderId) {
		return findAllWithPagingAndOrder(pageNumber, resultsPerPage, purchaseOrderId, null);
	}

	@Override
	public ObjectList<PurchaseOrderItem> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage,
			Long purchaseOrderId, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("purchaseOrder.id", purchaseOrderId));
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}
	
	@Override
	public ObjectList<PurchaseOrderItem> findByProductWithPagingAndOrder(int pageNumber, int resultsPerPage,
			Long productId, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("productId", productId));
		conjunction.add(Restrictions.eq("purchase.status", Status.PAID));
		
		String[] associatedPaths = { "purchaseOrder" };
		String[] aliasNames = { "purchase" };
		JoinType[] joinTypes = { JoinType.INNER_JOIN };
		
		return findAllByCriterion(pageNumber, resultsPerPage, associatedPaths, aliasNames, joinTypes, orders, conjunction);
	}

	@Override
	public PurchaseOrderItem findByNameAndPurchaseOrder(String displayName, Long purchaseOrderId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("displayName", displayName));
		conjunction.add(Restrictions.eq("purchaseOrder.id", purchaseOrderId));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public List<PurchaseOrderItem> findAllByPurchaseOrder(Long purchaseOrderId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("purchaseOrder.id", purchaseOrderId));
		
		return findAllByCriterionList(null, null, null, null, conjunction);
	}
}
