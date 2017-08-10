package com.chua.distributions.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.ProductDAO;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.database.service.ProductService;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 5, 2016
 */
@Service
public class ProductServiceImpl
		extends AbstractService<Product, Long, ProductDAO>
		implements ProductService {

	@Autowired
	protected ProductServiceImpl(ProductDAO dao) {
		super(dao);
	}

	@Override
	public ObjectList<Product> findAllWithPagingOrderByName(int pageNumber, int resultsPerPage, String searchKey, Long companyId, Long categoryId) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, companyId, categoryId, new Order[] { Order.asc("name") });
	}
	
	@Override
	public List<Product> findAllOrderByName() {
		return dao.findAllWithOrder(new Order[] { Order.asc("displayName") });
	}
	
	@Override
	public List<Product> findAllByCompanyOrderByCategory(Long companyId) {
		return dao.findAllByCompanyWithOrder(companyId, new Order[] { Order.asc("categoryy.name"), Order.asc("displayName"), Order.desc("packaging"), Order.asc("grossPrice") });
	}

	@Override
	public boolean isExistsByDisplayName(String displayName) {
		return dao.findByDisplayName(displayName) != null;
	}
	
	@Override
	public boolean isExistsByProductCode(String productCode) {
		return dao.findByProductCode(productCode) != null;
	}
	
	@Override
	public Product findByDisplayName(String displayName) {
		return dao.findByDisplayName(displayName);
	}
}
