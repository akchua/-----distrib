package com.chua.distributions.database.service;

import java.util.List;

import com.chua.distributions.database.entity.User;
import com.chua.distributions.database.prototype.UserPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 13, 2016
 */
public interface UserService extends Service<User, Long>, UserPrototype {

	boolean isExistByUsername(String username);
	
	ObjectList<User> findAllWithPagingOrderByNameAndUserType(int pageNumber, int resultsPerPage, String searchKey);
	
	ObjectList<User> findAllClientsWithPagingOrderByBusinessName(int pageNumber, int resultsPerPage, String searchKey);
	
	List<User> findAllClients();
	
	List<User> findAllClientsOrderByBusinessName();
	
	List<User> findAllAdministrators();
}
