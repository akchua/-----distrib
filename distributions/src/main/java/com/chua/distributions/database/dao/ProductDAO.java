package com.chua.distributions.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.distributions.database.entity.Product;
import com.chua.distributions.database.prototype.ProductPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 5, 2016
 */
public interface ProductDAO extends DAO<Product, Long>, ProductPrototype {
	
	ObjectList<Product> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey, Long companyId, Long categoryId);
	
	ObjectList<Product> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, String searchKey, Long companyId, Long categoryId, Order[] orders);
	
	List<Product> findAllWithOrder(Order[] orders);
	
	List<Product> findAllByCompanyWithOrder(Long companyId, Order[] orders);
	
	List<Product> findAllByCategoryWithOrder(Long categoryId, Order[] orders);
	
	Product findByProductCode(String productCode);
}
