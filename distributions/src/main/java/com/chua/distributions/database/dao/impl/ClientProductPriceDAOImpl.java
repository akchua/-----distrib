package com.chua.distributions.database.dao.impl;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Repository;

import com.chua.distributions.database.dao.ClientProductPriceDAO;
import com.chua.distributions.database.entity.ClientProductPrice;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 18, 2017
 */
@Repository
public class ClientProductPriceDAOImpl
		extends AbstractDAO<ClientProductPrice, Long>
		implements ClientProductPriceDAO {

	@Override
	public ClientProductPrice findByClientAndProduct(Long clientId, Long productId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("client.id", clientId));
		conjunction.add(Restrictions.eq("product.id", productId));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public ObjectList<ClientProductPrice> findAllWithPaging(int pageNumber, int resultsPerPage, Long clientId) {
		return findAllWithPagingAndOrder(pageNumber, resultsPerPage, clientId, null);
	}

	@Override
	public ObjectList<ClientProductPrice> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, Long clientId,
			Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(clientId != null) {
			conjunction.add(Restrictions.eq("client.id", clientId));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, new String[] { "product" }, new String[] { "prod" }, new JoinType[] { JoinType.INNER_JOIN }, orders, conjunction);
	}
}
