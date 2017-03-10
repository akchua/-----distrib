package com.chua.distributions.database.dao.impl;

import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Repository;

import com.chua.distributions.database.dao.ClientCompanyPriceDAO;
import com.chua.distributions.database.entity.ClientCompanyPrice;
import com.chua.distributions.objects.ObjectList;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Mar 2017
 */
@Repository
public class ClientCompanyPriceDAOImpl
		extends AbstractDAO<ClientCompanyPrice, Long>
		implements ClientCompanyPriceDAO {

	@Override
	public ClientCompanyPrice findByClientAndCompany(Long clientId, Long companyId) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("client.id", clientId));
		conjunction.add(Restrictions.eq("company.id", companyId));
		
		return findUniqueResult(null, null, null, conjunction);
	}
	
	@Override
	public ObjectList<ClientCompanyPrice> findAllWithPaging(int pageNumber, int resultsPerPage, Long clientId) {
		return findAllWithPagingAndOrder(pageNumber, resultsPerPage, clientId, null);
	}

	@Override
	public ObjectList<ClientCompanyPrice> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, Long clientId,
			Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(clientId != null) {
			conjunction.add(Restrictions.eq("client.id", clientId));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, new String[] { "company" }, new String[] { "comp" }, new JoinType[] { JoinType.INNER_JOIN }, orders, conjunction);
	}
}
