package com.chua.distributions.rest.handler;

import com.chua.distributions.beans.PartialProductBean;
import com.chua.distributions.beans.ProductFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 5, 2016
 */
public interface ProductHandler {

	Product getProduct(Long productId, Warehouse warehouse);
	
	PartialProductBean getPartialProduct(Long productId, Warehouse warehouse);

	ObjectList<Product> getProductObjectList(Integer pageNumber, String searchKey, Long companyId, Long categoryId, Warehouse warehouse);
	
	ObjectList<PartialProductBean> getPartialProductObjectList(Integer pageNumber, String searchKey, Long companyId, Long categoryId, Warehouse warehouse);
	
	ResultBean createProduct(ProductFormBean productForm);
	
	ResultBean updateProduct(ProductFormBean productForm);
	
	ResultBean removeProduct(Long productId);
}
