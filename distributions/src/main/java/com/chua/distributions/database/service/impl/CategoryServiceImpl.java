package com.chua.distributions.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.CategoryDAO;
import com.chua.distributions.database.entity.Category;
import com.chua.distributions.database.service.CategoryService;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 4, 2016
 */
@Service
public class CategoryServiceImpl
		extends AbstractService<Category, Long, CategoryDAO>
		implements CategoryService{

	@Autowired
	protected CategoryServiceImpl(CategoryDAO dao) {
		super(dao);
	}

	@Override
	public ObjectList<Category> findAllWithPagingOrderByName(int pageNumber, int resultsPerPage, String searchKey) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, new Order[] { Order.asc("name") });
	}
	
	@Override
	public List<Category> findAllOrderByName() {
		return dao.findAllWithOrder(new Order[] { Order.asc("name") });
	}

	@Override
	public boolean isExistsByName(String name) {
		return dao.findByName(name) != null;
	}
	
	@Override
	public Category findByName(String name) {
		return dao.findByName(name);
	}
}
