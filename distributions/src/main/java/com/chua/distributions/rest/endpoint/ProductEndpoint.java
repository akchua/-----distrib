package com.chua.distributions.rest.endpoint;

import java.io.IOException;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.distributions.beans.PartialProductBean;
import com.chua.distributions.beans.ProductFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.ProductHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 5, 2016
 */
@Path("/product")
public class ProductEndpoint {

	@Autowired
	private ProductHandler productHandler;
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public Product getProduct(@QueryParam("productId") Long productId, @QueryParam("warehouse") Warehouse warehouse) {
		return productHandler.getProduct(productId, warehouse);
	}
	
	@GET
	@Path("/getpartial")
	@Produces({ MediaType.APPLICATION_JSON })
	public PartialProductBean getPartialProduct(@QueryParam("productId") Long productId, @QueryParam("warehouse") Warehouse warehouse) {
		return productHandler.getPartialProduct(productId, warehouse);
	}
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<Product> getProductObjectList(@QueryParam("pageNumber") Integer pageNumber, 
				@QueryParam("searchKey") String searchKey,
				@QueryParam("companyId") Long companyId,
				@QueryParam("categoryId") Long categoryId,
				@QueryParam("warehouse") Warehouse warehouse) {
		return productHandler.getProductObjectList(pageNumber, searchKey, companyId, categoryId, warehouse);
	}
	
	@GET
	@Path("/listpartial")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<PartialProductBean> getPartialProductObjectList(@QueryParam("pageNumber") Integer pageNumber, 
				@QueryParam("searchKey") String searchKey,
				@QueryParam("companyId") Long companyId,
				@QueryParam("categoryId") Long categoryId,
				@QueryParam("warehouse") Warehouse warehouse) {
		return productHandler.getPartialProductObjectList(pageNumber, searchKey, companyId, categoryId, warehouse);
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveProduct(@FormParam("productFormData") String productFormData) throws IOException {
		final ResultBean result;

		final ProductFormBean productForm = new ObjectMapper().readValue(productFormData, ProductFormBean.class);
		if(productForm.getId() != null) {
			result = productHandler.updateProduct(productForm);
		} else {
			result = productHandler.createProduct(productForm);
		}
		
		return result;
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeProduct(@FormParam("productId") Long productId) {
		return productHandler.removeProduct(productId);
	}
}
