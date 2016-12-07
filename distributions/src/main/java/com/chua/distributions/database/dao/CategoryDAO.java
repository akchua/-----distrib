package com.chua.distributions.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.distributions.database.entity.Category;
import com.chua.distributions.database.prototype.CategoryPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 4, 2016
 */
public interface CategoryDAO extends DAO<Category, Long>, CategoryPrototype {

	ObjectList<Category> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey);
	
	ObjectList<Category> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, String searchKey, Order[] orders);
	
	List<Category> findAllWithOrder(Order[] orders);
}
