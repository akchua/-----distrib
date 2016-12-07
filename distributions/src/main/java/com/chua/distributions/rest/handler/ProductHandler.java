package com.chua.distributions.rest.handler;

import com.chua.distributions.beans.ProductFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 5, 2016
 */
public interface ProductHandler {

	Product getProduct(Long productId);

	ObjectList<Product> getProductObjectList(Integer pageNumber, String searchKey, Long companyId, Long categoryId);
	
	ResultBean createProduct(ProductFormBean productForm);
	
	ResultBean updateProduct(ProductFormBean productForm);
	
	ResultBean removeProduct(Long productId);
}
