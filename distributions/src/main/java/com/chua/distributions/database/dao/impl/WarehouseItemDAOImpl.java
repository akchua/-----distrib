package com.chua.distributions.database.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
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
	public ObjectList<WarehouseItem> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey, Warehouse warehouse) {
		return findAllWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, warehouse, null);
	}

	@Override
	public ObjectList<WarehouseItem> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, String searchKey,
			Warehouse warehouse, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.ne("stockCount", Integer.valueOf(0)));
		
		if(StringUtils.isNotBlank(searchKey))
		{
			conjunction.add(Restrictions.disjunction()
					.add(Restrictions.ilike("prod.displayName", searchKey, MatchMode.ANYWHERE)));
		}
		
		if(warehouse != null) {
			conjunction.add(Restrictions.eq("warehouse", warehouse));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, new String[] { "product" }, new String[] { "prod" }, new JoinType[] { JoinType.INNER_JOIN }, orders, conjunction);
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
