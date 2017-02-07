package com.chua.distributions.rest.endpoint;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.distributions.beans.CompanyFormBean;
import com.chua.distributions.beans.PartialCompanyBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.Company;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.CompanyHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 3, 2016
 */
@Path("/company")
public class CompanyEndpoint {

	@Autowired
	private CompanyHandler companyHandler;
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public Company getCompany(@QueryParam("companyId") Long companyId) {
		return companyHandler.getCompany(companyId);
	}
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<Company> getCompanyObjectList(@QueryParam("pageNumber") Integer pageNumber, @QueryParam("searchKey") String searchKey) {
		return companyHandler.getCompanyObjectList(pageNumber, searchKey);
	}
	
	@GET
	@Path("listbyname")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Company> getCompanyList() {
		return companyHandler.getCompanyList();
	}
	
	@GET
	@Path("listpartialbyname")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<PartialCompanyBean> getPartialCompanyList() {
		return companyHandler.getPartialCompanyList();
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveCompany(@FormParam("companyFormData") String companyFormData) throws IOException {
		final ResultBean result;

		final CompanyFormBean companyForm = new ObjectMapper().readValue(companyFormData, CompanyFormBean.class);
		if(companyForm.getId() != null) {
			result = companyHandler.updateCompany(companyForm);
		} else {
			result = companyHandler.createCompany(companyForm);
		}
		
		return result;
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeCompany(@FormParam("companyId") Long companyId) {
		return companyHandler.removeCompany(companyId);
	}
}
