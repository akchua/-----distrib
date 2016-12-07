package com.chua.distributions.database.service;

import java.util.List;

import com.chua.distributions.database.entity.Company;
import com.chua.distributions.database.prototype.CompanyPrototype;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 3, 2016
 */
public interface CompanyService extends Service<Company, Long>, CompanyPrototype {

	ObjectList<Company> findAllWithPagingOrderByName(int pageNumber, int resultsPerPage, String searchKey);
	
	List<Company> findAllOrderByName();
	
	boolean isExistsByName(String name);
}
