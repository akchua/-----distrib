package com.chua.distributions.database.dao.impl;

import java.util.List;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.distributions.database.dao.WarehouseItemDAO;
import com.chua.distributions.database.entity.WarehouseItem;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 9, 2016
 */
@Repository
public class WarehouseItemDAOImpl
		extends AbstractDAO<WarehouseItem, Long>
		implements WarehouseItemDAO {

	@Override
	public ObjectList<WarehouseItem> findAllWithPaging(int pageNumber, int resultsPerPage, Warehouse warehouse) {
		return findAllWithPagingAndOrder(pageNumber, resultsPerPage, warehouse, null);
	}

	@Override
	public ObjectList<WarehouseItem> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, Warehouse warehouse,
			Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.ne("stockCount", Integer.valueOf(0)));
		
		if(warehouse != null) {
			conjunction.add(Restrictions.eq("warehouse", warehouse));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, new String[] { "product" }, new String[] { "prod" }, new int[] { 0 }, orders, conjunction);
	}
	
	@Override
	public List<WarehouseItem> findAllByProduct(Long productId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("product.id", productId));
		
		return findAllByCriterionList(null, null, null, null, conjunction);
	}
	
	@Override
	public WarehouseItem findByProductAndWarehouse(Long productId, Warehouse warehouse) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("product.id", productId));
		conjunction.add(Restrictions.eq("warehouse", warehouse));
		
		return findUniqueResult(null, null, null, conjunction);
	}
}
