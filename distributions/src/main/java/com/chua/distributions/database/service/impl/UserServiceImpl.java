package com.chua.distributions.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.UserDAO;
import com.chua.distributions.database.entity.User;
import com.chua.distributions.database.service.UserService;
import com.chua.distributions.enums.UserType;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 13, 2016
 */
@Service
public class UserServiceImpl
		extends AbstractService<User, Long, UserDAO> 
		implements UserService {

	@Autowired
	protected UserServiceImpl(UserDAO dao) {
		super(dao);
	}

	@Override
	public User findByUsernameAndPassword(String username, String password) {
		return dao.findByUsernameAndPassword(username, password);
	}

	@Override
	public boolean isExistByUsername(String username) {
		return dao.findByUsername(username) != null;
	}

	@Override
	public User findByUsername(String username) {
		return dao.findByUsername(username);
	}
	
	@Override
	public User findByUsernameOrEmail(String username, String emailAddress) {
		return dao.findByUsernameOrEmail(username, emailAddress);
	}

	@Override
	public ObjectList<User> findAllWithPagingOrderByNameAndUserType(int pageNumber, int resultsPerPage, String searchKey) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, new Order[] { Order.asc("userType"), Order.asc("lastName"), Order.asc("firstName") });
	}

	@Override
	public ObjectList<User> findAllClientsWithPagingOrderByBusinessName(int pageNumber, int resultsPerPage, String searchKey) {
		return dao.findAllClientsWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, new Order[] { Order.asc("businessName") });
	}
	
	@Override
	public List<User> findAllClients() {
		return dao.findAllByUserType(UserType.CLIENT);
	}
	
	@Override
	public List<User> findAllClientsOrderByBusinessName() {
		return dao.findAllByUserTypeWithOrder(UserType.CLIENT, new Order[] { Order.asc("businessName") });
	}

	@Override
	public List<User> findAllAdministrators() {
		return dao.findAllByUserType(UserType.ADMINISTRATOR);
	}

	@Override
	public List<User> findAllManagers() {
		return dao.findAllByUserType(UserType.MANAGER);
	}
}
