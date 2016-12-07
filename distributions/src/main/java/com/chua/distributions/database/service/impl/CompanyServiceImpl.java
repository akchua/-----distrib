package com.chua.distributions.database.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chua.distributions.database.dao.CompanyDAO;
import com.chua.distributions.database.entity.Company;
import com.chua.distributions.database.service.CompanyService;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 3, 2016
 */
@Service
public class CompanyServiceImpl
		extends AbstractService<Company, Long, CompanyDAO> 
		implements CompanyService {

	@Autowired
	protected CompanyServiceImpl(CompanyDAO dao) {
		super(dao);
	}

	@Override
	public ObjectList<Company> findAllWithPagingOrderByName(int pageNumber, int resultsPerPage, String searchKey) {
		return dao.findAllWithPagingAndOrder(pageNumber, resultsPerPage, searchKey, new Order[] { Order.asc("name") });
	}
	
	@Override
	public List<Company> findAllOrderByName() {
		return dao.findAllWithOrder(new Order[] { Order.asc("name") });
	}

	@Override
	public boolean isExistsByName(String name) {
		return dao.findByName(name) != null;
	}

	@Override
	public Company findByName(String name) {
		return dao.findByName(name);
	}
}
