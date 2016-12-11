package com.chua.distributions.database.dao;

import org.hibernate.criterion.Order;

import com.chua.distributions.database.entity.User;
import com.chua.distributions.database.prototype.UserPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 13, 2016
 */
public interface UserDAO extends DAO<User, Long>, UserPrototype {

	ObjectList<User> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey);
	
	ObjectList<User> findAllClientsWithPagingAndOrder(int pageNumber, int resultsPerPage, String searchKey, Order[] orders);
	
	ObjectList<User> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, String searchKey, Order[] orders);
}
