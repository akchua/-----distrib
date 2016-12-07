package com.chua.distributions.database.dao;

import java.util.List;

import org.hibernate.criterion.Order;

import com.chua.distributions.database.entity.Company;
import com.chua.distributions.database.prototype.CompanyPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 3, 2016
 */
public interface CompanyDAO extends DAO<Company, Long>, CompanyPrototype {

	ObjectList<Company> findAllWithPaging(int pageNumber, int resultsPerPage, String searchKey);
	
	ObjectList<Company> findAllWithPagingAndOrder(int pageNumber, int resultsPerPage, String searchKey, Order[] orders);
	
	List<Company> findAllWithOrder(Order[] orders);
}
