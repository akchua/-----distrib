package com.chua.distributions.rest.handler;

import java.util.List;

import com.chua.distributions.beans.CategoryFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.Category;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 4, 2016
 */
public interface CategoryHandler {

	Category getCategory(Long categoryId);

	ObjectList<Category> getCategoryObjectList(Integer pageNumber, String searchKey);
	
	List<Category> getCategoryList();
	
	ResultBean createCategory(CategoryFormBean categoryForm);
	
	ResultBean updateCategory(CategoryFormBean categoryForm);
	
	ResultBean removeCategory(Long categoryId);
}
