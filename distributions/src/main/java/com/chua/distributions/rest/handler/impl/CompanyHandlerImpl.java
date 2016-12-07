package com.chua.distributions.rest.handler.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.beans.CompanyFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.Company;
import com.chua.distributions.database.service.CompanyService;
import com.chua.distributions.enums.Color;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.CompanyHandler;
import com.chua.distributions.utility.EmailUtil;
import com.chua.distributions.utility.Html;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 3, 2016
 */
@Transactional
@Component
public class CompanyHandlerImpl implements CompanyHandler {

	@Autowired
	private CompanyService companyService;
	
	@Override
	public Company getCompany(Long companyId) {
		return companyService.find(companyId);
	}

	@Override
	public ObjectList<Company> getCompanyObjectList(Integer pageNumber, String searchKey) {
		return companyService.findAllWithPagingOrderByName(pageNumber, UserContextHolder.getItemsPerPage(), searchKey);
	}
	
	@Override
	public List<Company> getCompanyList() {
		return companyService.findAllOrderByName();
	}
	
	@Override
	public ResultBean createCompany(CompanyFormBean companyForm) {
		final ResultBean result;
		final ResultBean validateForm = validateCompanyForm(companyForm);
		
		if(validateForm.getSuccess()) {
			if(companyService.isExistsByName(companyForm.getName().trim())) {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Company name already exists.")));
			} else {
				final Company company = new Company();
				
				setCompany(company, companyForm);
				
				result = new ResultBean();
				result.setSuccess(companyService.insert(company) != null);
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created Company " + Html.text(Color.BLUE, company.getName()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			}
		} else {
			result = validateForm;
		}
		
		return result;
	}
	
	@Override
	public ResultBean updateCompany(CompanyFormBean companyForm) {
		final ResultBean result;
		final Company company = companyService.find(companyForm.getId());
		
		if(company != null) {
			final ResultBean validateForm = validateCompanyForm(companyForm);
			if(validateForm.getSuccess()) {
				final Company companyy = companyService.findByName(companyForm.getName().trim());
				if(companyy != null && company.getId() != companyy.getId()) {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Company name already exists.")));
				} else {
					setCompany(company, companyForm);
					
					result = new ResultBean();
					result.setSuccess(companyService.update(company));
					if(result.getSuccess()) {
						result.setMessage(Html.line("Company " + Html.text(Color.BLUE, company.getName()) + " has been successfully " + Html.text(Color.GREEN, "updated") + "."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				}
			} else {
				result = validateForm;
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load company. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean removeCompany(Long companyId) {
		final ResultBean result;
		final Company company = companyService.find(companyId);
		
		if(company != null) {
			result = new ResultBean();
			
			result.setSuccess(companyService.delete(company));
			if(result.getSuccess()) {
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed Company " + Html.text(Color.BLUE, company.getName()) + "."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load company. Please refresh the page."));
		}
		
		return result;
	}
	
	private void setCompany(Company company, CompanyFormBean companyForm) {
		company.setName(companyForm.getName().trim());
		company.setContactPerson(companyForm.getContactPerson().trim());
		company.setContactNumber(companyForm.getContactNumber().trim());
		company.setEmailAddress(companyForm.getEmailAddress().trim());
	}
	
	private ResultBean validateCompanyForm(CompanyFormBean companyForm) {
		final ResultBean result;
		
		if(companyForm.getName() == null || companyForm.getName().trim().length() < 3 ||
				companyForm.getContactPerson() == null || companyForm.getContactPerson().trim().length() < 3 ||
				companyForm.getContactNumber() == null || companyForm.getContactNumber().trim().length() < 3 ||
				companyForm.getEmailAddress() == null || companyForm.getEmailAddress().trim().length() < 3) {
			result = new ResultBean(Boolean.FALSE, Html.line("All fields are " + Html.text(Color.RED, "required") + " and must contain at least 3 characters."));
		} else if(!EmailUtil.validateEmail(companyForm.getEmailAddress().trim())) {
			result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Invalid Email Address!"));
		} else {
			result = new ResultBean(Boolean.TRUE, "");
		}
		
		return result;
	}
}
