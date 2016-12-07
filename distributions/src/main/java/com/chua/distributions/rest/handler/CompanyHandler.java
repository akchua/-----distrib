package com.chua.distributions.rest.handler;

import java.util.List;

import com.chua.distributions.beans.CompanyFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.Company;
import com.chua.distributions.objects.ObjectList;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 3, 2016
 */
public interface CompanyHandler {
	
	Company getCompany(Long companyId);

	ObjectList<Company> getCompanyObjectList(Integer pageNumber, String searchKey);
	
	List<Company> getCompanyList();
	
	ResultBean createCompany(CompanyFormBean companyForm);
	
	ResultBean updateCompany(CompanyFormBean companyForm);
	
	ResultBean removeCompany(Long companyId);
}
