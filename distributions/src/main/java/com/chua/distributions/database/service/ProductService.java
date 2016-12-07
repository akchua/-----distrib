package com.chua.distributions.database.service;

import com.chua.distributions.database.entity.Product;
import com.chua.distributions.database.prototype.ProductPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 5, 2016
 */
public interface ProductService extends Service<Product, Long>, ProductPrototype {

	ObjectList<Product> findAllWithPagingOrderByName(int pageNumber, int resultsPerPage, String searchKey, Long companyId, Long categoryId);
	
	boolean isExistsByDisplayName(String name);
}
