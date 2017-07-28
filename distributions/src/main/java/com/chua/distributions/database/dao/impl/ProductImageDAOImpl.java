package com.chua.distributions.database.dao.impl;

import java.util.List;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.distributions.database.dao.ProductImageDAO;
import com.chua.distributions.database.entity.ProductImage;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   27 Jul 2017
 */
@Repository
public class ProductImageDAOImpl
		extends AbstractDAO<ProductImage, Long>
		implements ProductImageDAO {

	@Override
	public List<ProductImage> findAllByProductId(Long productId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("product.id", productId));
		
		return findAllByCriterionList(null, null, null, null, conjunction);
	}
}
