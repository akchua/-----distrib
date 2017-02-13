package com.chua.distributions.database.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.chua.distributions.database.dao.UserDAO;
import com.chua.distributions.database.entity.User;
import com.chua.distributions.enums.UserType;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 13, 2016
 */
@Repository
public class UserDAOImpl
		extends AbstractDAO<User, Long> 
		implements UserDAO {
	
	@Override
	public ObjectList<User> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey) {
		return findAllWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, null);
	}
	
	@Override
	public ObjectList<User> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, String searchKey,
			Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		
		if(StringUtils.isNotBlank(searchKey))
		{
			conjunction.add(Restrictions.disjunction()
					.add(Restrictions.ilike("firstName", searchKey, MatchMode.ANYWHERE))
					.add(Restrictions.ilike("lastName", searchKey, MatchMode.ANYWHERE)));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}
	
	@Override
	public ObjectList<User> findAllClientsWithPagingAndOrder(int pageNumber, int resultsPerPage, String searchKey,
			Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("userType", UserType.CLIENT));
		
		if(StringUtils.isNotBlank(searchKey))
		{
			conjunction.add(Restrictions.disjunction()
					.add(Restrictions.ilike("firstName", searchKey, MatchMode.ANYWHERE))
					.add(Restrictions.ilike("lastName", searchKey, MatchMode.ANYWHERE)));
		}
		
		return findAllByCriterion(pageNumber, resultsPerPage, null, null, null, orders, conjunction);
	}
	
	@Override
	public List<User> findAllByUserType(UserType userType) {
		return findAllByUserTypeWithOrder(userType, null);
	}
	
	@Override
	public List<User> findAllByUserTypeWithOrder(UserType userType, Order[] orders) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("userType", userType));
		
		return findAllByCriterionList(null, null, null, orders, conjunction);
	}

	@Override
	public User findByUsernameAndPassword(String username, String password) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("username", username));
		conjunction.add(Restrictions.eq("password", password));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public User findByUsername(String username) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.eq("username", username));
		
		return findUniqueResult(null, null, null, conjunction);
	}

	@Override
	public User findByUsernameOrEmail(String username, String emailAddress) {
		final Junction conjunction = Restrictions.conjunction();
		conjunction.add(Restrictions.eq("isValid", Boolean.TRUE));
		conjunction.add(Restrictions.disjunction()
				.add(Restrictions.eq("username", username))
				.add(Restrictions.eq("emailAddress", emailAddress)));
		
		return findUniqueResult(null, null, null, conjunction);
	}
}
