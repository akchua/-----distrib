package com.chua.distributions.database.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Repository;

import com.chua.distributions.database.dao.ProductDAO;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 5, 2016
 */
@Repository
public class ProductDAOImpl
		extends AbstractDAO<Product, Long>
		implements ProductDAO {

	@Override
	public ObjectList<Product> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey, Long companyId, Long categoryId) {
		return findAllWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, companyId, categoryId, null);
	}

	@Override
	public ObjectList<Product> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, String searchKey, Long companyId, Long categoryId,
			Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(StringUtils.isNotBlank(searchKey))
		{
			conjunction.add(Restrictions.disjunction()
					.add(Restrictions.ilike("displayName", searchKey, MatchMode.ANYWHERE))
					.add(Restrictions.ilike("productCode", searchKey, MatchMode.ANYWHERE)));
		}
		
		if(companyId != null) {
			conjunction.add(Restrictions.eq("company.id", companyId));
		}
		
		if(categoryId != null) {
			conjunction.add(Restrictions.eq("category.id", categoryId));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}

	@Override
	public Product findByDisplayName(String displayName) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("displayName", displayName));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public Product findByProductCode(String productCode) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("productCode", productCode));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public List<Product> findAllWithOrder(Order[] orders) {
		return findAllByCriterionList(null, null, null, orders, Restrictions.eq("isValid", Boolean.TRUE));
	}
	
	@Override
	public List<Product> findAllByCompanyWithOrder(Long companyId, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("company.id", companyId));
		
		String[] associatedPaths = { "category" };
		String[] aliasNames = { "categoryy" };
		JoinType[] joinTypes = { JoinType.INNER_JOIN };
		
		return findAllByCriterionList(associatedPaths, aliasNames, joinTypes, orders, conjunction);
	}
}
