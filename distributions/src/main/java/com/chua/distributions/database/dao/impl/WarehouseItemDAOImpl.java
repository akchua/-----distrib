package com.chua.distributions.database.dao.impl;

import java.util.List;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.distributions.database.dao.WarehouseItemDAO;
import com.chua.distributions.database.entity.WarehouseItem;
import com.chua.distributions.enums.Warehouse;

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
