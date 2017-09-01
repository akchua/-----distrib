package com.chua.distributions.rest.endpoint;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;

import com.chua.distributions.beans.PartialProductBean;
import com.chua.distributions.beans.PartialProductImageBean;
import com.chua.distributions.beans.ProductFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.database.entity.ProductImage;
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
	public Product getProduct(@QueryParam("productId") Long productId, @QueryParam("warehouseId") Long warehouseId) {
		return productHandler.getProduct(productId, warehouseId);
	}
	
	@GET
	@Path("/getimage/{fileName}")
	@Produces("image/*")
	public Response getProductImageByFileName(@PathParam("fileName") String fileName) throws IOException {
		File productImage = productHandler.findProductImageByFileName(fileName);
		if(productImage.exists())
			return Response.ok(productImage, new MimetypesFileTypeMap().getContentType(productImage))
				.build();
		else return null;
	}
	
	@GET
	@Path("/getpartial")
	@Produces({ MediaType.APPLICATION_JSON })
	public PartialProductBean getPartialProduct(@QueryParam("productId") Long productId) {
		return productHandler.getPartialProduct(productId);
	}
	
	@GET
	@Path("/getpartialfor")
	@Produces({ MediaType.APPLICATION_JSON })
	public PartialProductBean getPartialProductFor(@QueryParam("productId") Long productId, @QueryParam("clientId") Long clientId) {
		return productHandler.getPartialProductFor(productId, clientId);
	}
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<Product> getProductObjectList(@QueryParam("pageNumber") Integer pageNumber, 
				@QueryParam("searchKey") String searchKey,
				@QueryParam("companyId") Long companyId,
				@QueryParam("categoryId") Long categoryId,
				@QueryParam("warehouseId") Long warehouseId) {
		return productHandler.getProductObjectList(pageNumber, searchKey, companyId, categoryId, warehouseId);
	}
	
	@GET
	@Path("/listbyname")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Product> getProductListOrderByName() {
		return productHandler.getProductListOrderByName();
	}
	
	@GET
	@Path("/imagelist")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<ProductImage> getProductImageList(@QueryParam("productId") Long productId) {
		return productHandler.getProductImageList(productId);
	}
	
	@GET
	@Path("/partialimagelist")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<PartialProductImageBean> getPartialProductImageList(@QueryParam("productId") Long productId) {
		return productHandler.getPartialProductImageList(productId);
	}
	
	@GET
	@Path("/listpartial")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<PartialProductBean> getPartialProductObjectList(@QueryParam("pageNumber") Integer pageNumber, 
				@QueryParam("searchKey") String searchKey,
				@QueryParam("companyId") Long companyId,
				@QueryParam("categoryId") Long categoryId) {
		return productHandler.getPartialProductObjectList(pageNumber, searchKey, companyId, categoryId);
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
	@Path("/uploadimage")
	@Consumes(MediaType.MULTIPART_FORM_DATA)	
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean uploadProductImage(@FormDataParam("productId") Long productId,
			@FormDataParam("imageFile") InputStream in,
			@FormDataParam("imageFile") FormDataContentDisposition info) throws IOException {
		return productHandler.saveProductImage(productId, in, info);
	}
	
	@POST
	@Path("/setthumbnail")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean setProductImageAsThumbnail(@FormParam("productImageId") Long productImageId) {
		return productHandler.setProductImageAsThumbnail(productImageId);
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeProduct(@FormParam("productId") Long productId) {
		return productHandler.removeProduct(productId);
	}
	
	@POST
	@Path("/removeimage")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeProductImage(@FormParam("productImageId") Long productImageId) {
		return productHandler.removeProductImage(productImageId);
	}
	
	@POST
	@Path("/generatepricelist")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean generatePriceList(@FormParam("companyId") Long companyId,
					@FormParam("sendEmail") Boolean sendEmail) throws IOException {
		return productHandler.generatePriceList(companyId, sendEmail);
	}
}
