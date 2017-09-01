package com.chua.distributions.database.dao.impl;

import java.util.List;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.distributions.database.dao.StockAdjustDAO;
import com.chua.distributions.database.entity.StockAdjust;
import com.chua.distributions.objects.ObjectList;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	26 Jan 2017
 */
@Repository
public class StockAdjustDAOImpl
		extends AbstractDAO<StockAdjust, Long>
		implements StockAdjustDAO {

	@Override
	public ObjectList<StockAdjust> findByProductWithPagingAndOrder(int pageNumber, int resultsPerPage,
			Long productId, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("productId", productId));
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}

	@Override
	public List<StockAdjust> findAllByProductAndWarehouse(Long productId, Long warehouseId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("productId", productId));
		
		if(warehouseId != null) {
			conjunction.add(Restrictions.eq("warehouse.id", warehouseId));
		}
		
		return findAllByCriterionList(null, null, null, null, conjunction);
	}
}
