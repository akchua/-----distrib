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

import com.chua.distributions.beans.CategoryFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.Category;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.CategoryHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 4, 2016
 */
@Path("/category")
public class CategoryEndpoint {

	@Autowired
	private CategoryHandler categoryHandler;
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public Category getCategory(@QueryParam("categoryId") Long categoryId) {
		return categoryHandler.getCategory(categoryId);
	}
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<Category> getCategoryObjectList(@QueryParam("pageNumber") Integer pageNumber, @QueryParam("searchKey") String searchKey) {
		return categoryHandler.getCategoryObjectList(pageNumber, searchKey);
	}
	
	@GET
	@Path("/listbyname")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Category> getCategoryList() {
		return categoryHandler.getCategoryList();
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveCategory(@FormParam("categoryFormData") String categoryFormData) throws IOException {
		final ResultBean result;

		final CategoryFormBean categoryForm = new ObjectMapper().readValue(categoryFormData, CategoryFormBean.class);
		if(categoryForm.getId() != null) {
			result = categoryHandler.updateCategory(categoryForm);
		} else {
			result = categoryHandler.createCategory(categoryForm);
		}
		
		return result;
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeCategory(@FormParam("categoryId") Long categoryId) {
		return categoryHandler.removeCategory(categoryId);
	}
}
