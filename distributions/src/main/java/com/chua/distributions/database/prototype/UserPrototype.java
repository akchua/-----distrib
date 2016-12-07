package com.chua.distributions.database.prototype;

import com.chua.distributions.database.entity.User;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 13, 2016
 */
public interface UserPrototype extends Prototype<User, Long> {
	
	User findByUsernameAndPassword(String username, String password);
	
	User findByUsername(String username);
	
	User findByUsernameOrEmail(String username, String emailAddress);
};
