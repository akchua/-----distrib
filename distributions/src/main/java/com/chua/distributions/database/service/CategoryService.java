package com.chua.distributions.database.service;

import java.util.List;

import com.chua.distributions.database.entity.Category;
import com.chua.distributions.database.prototype.CategoryPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 4, 2016
 */
public interface CategoryService extends Service<Category, Long>, CategoryPrototype {

	ObjectList<Category> findAllWithPagingOrderByName(int pageNumber, int resultsPerPage, String searchKey);
	
	List<Category> findAllOrderByName();
	
	boolean isExistsByName(String name);
}
