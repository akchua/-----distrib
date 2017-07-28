package com.chua.distributions.rest.handler.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.annotations.CheckAuthority;
import com.chua.distributions.beans.PartialProductBean;
import com.chua.distributions.beans.ProductFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.constants.FileConstants;
import com.chua.distributions.constants.ImageConstants;
import com.chua.distributions.database.entity.ClientCompanyPrice;
import com.chua.distributions.database.entity.ClientProductPrice;
import com.chua.distributions.database.entity.ClientPromo;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.database.entity.ProductImage;
import com.chua.distributions.database.entity.User;
import com.chua.distributions.database.entity.WarehouseItem;
import com.chua.distributions.database.service.CategoryService;
import com.chua.distributions.database.service.ClientCompanyPriceService;
import com.chua.distributions.database.service.ClientProductPriceService;
import com.chua.distributions.database.service.ClientPromoService;
import com.chua.distributions.database.service.CompanyService;
import com.chua.distributions.database.service.ProductImageService;
import com.chua.distributions.database.service.ProductService;
import com.chua.distributions.database.service.UserService;
import com.chua.distributions.database.service.WarehouseItemService;
import com.chua.distributions.enums.Color;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.ProductHandler;
import com.chua.distributions.utility.Html;
import com.chua.distributions.utility.StringHelper;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 5, 2016
 */
@Transactional
@Component
public class ProductHandlerImpl implements ProductHandler {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductImageService productImageService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private WarehouseItemService warehouseItemService;
	
	@Autowired
	private ClientCompanyPriceService clientCompanyPriceService;
	
	@Autowired
	private ClientProductPriceService clientProductPriceService;
	
	@Autowired
	private ClientPromoService clientPromoService;
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public Product getProduct(Long productId, Warehouse warehouse) {
		final Product product = productService.find(productId);
		if(product != null) setProductStock(product, warehouse);
		return product;
	}
	
	@Override
	public File findProductImageByFileName(String fileName) {
		return new File(FileConstants.PRODUCT_IMAGE_HOME + fileName);
	}
	
	@Override
	public PartialProductBean getPartialProduct(Long productId, Warehouse warehouse) {
		return getPartialProductFor(productService.find(productId), UserContextHolder.getUser().getUserEntity());
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public PartialProductBean getPartialProductFor(Long productId, Long userId) {
		return getPartialProductFor(productService.find(productId), userService.find(userId));
	}
	
	public PartialProductBean getPartialProductFor(Product product, User user) {
		final PartialProductBean partialProduct;
		if(product != null && user != null) {
			partialProduct = new PartialProductBean(product);
			partialProduct.setSellingDiscount(getFinalSellingDiscount(product, user));
			partialProduct.setPackageNetSellingPrice(getFinalBasePackageSellingPrice(product, user));
		}
		else partialProduct = null;
		return partialProduct;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ObjectList<Product> getProductObjectList(Integer pageNumber, String searchKey, Long companyId, Long categoryId, Warehouse warehouse) {
		ObjectList<Product> objProducts = productService.findAllWithPagingOrderByName(pageNumber, UserContextHolder.getItemsPerPage(), searchKey, companyId, categoryId);
		for(Product product : objProducts.getList()) setProductStock(product, warehouse);
		return objProducts;
	}
	
	@Override
	public ObjectList<PartialProductBean> getPartialProductObjectList(Integer pageNumber, String searchKey, Long companyId, Long categoryId, Warehouse warehouse) {
		ObjectList<PartialProductBean> objPartialProducts = new ObjectList<PartialProductBean>();
		ObjectList<Product> objProducts = productService.findAllWithPagingOrderByName(pageNumber, UserContextHolder.getItemsPerPage(), searchKey, companyId, categoryId);
		if(objProducts != null) {
			objPartialProducts.setTotal(objProducts.getTotal());
			final List<PartialProductBean> partialProducts = new ArrayList<PartialProductBean>();
			for(Product product : objProducts.getList()) {
				partialProducts.add(getPartialProductFor(product, UserContextHolder.getUser().getUserEntity()));
			}
			objPartialProducts.setList(partialProducts);
		}
		return objPartialProducts;
	}
	
	@Override
	public List<Product> getProductListOrderByName() {
		return productService.findAllOrderByName();
	}
	
	@Override
	public List<ProductImage> getProductImageList(Long productId) {
		return productImageService.findAllByProductId(productId);
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean createProduct(ProductFormBean productForm) {
		final ResultBean result;
		final ResultBean validateForm = validateProductForm(productForm);
		
		if(validateForm.getSuccess()) {
			final String displayName = getDisplayName(productForm);
			if(productService.isExistsByDisplayName(displayName)) {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Product name already exists.")));
			} else if(productService.isExistsByProductCode(productForm.getProductCode())) {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Product code already exists.")));
			} else {
				final Product product = new Product();
				product.setDisplayName(displayName);
				product.setImage(ImageConstants.DEFAULT_IMAGE);
				setProduct(product, productForm);
				
				result = new ResultBean();
				result.setSuccess(productService.insert(product) != null);
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created Product " + Html.text(Color.BLUE, product.getDisplayName()) + "."));
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
	@CheckAuthority(minimumAuthority = 5)
	public ResultBean saveProductImage(Long productId, InputStream in, FormDataContentDisposition info) throws IOException {
		final ResultBean result;
		final String fileName = UUID.randomUUID().toString() + "." + StringHelper.getFileExtension(info.getFileName());
		File imageFile = new File(FileConstants.PRODUCT_IMAGE_HOME + fileName);
		
		if(!imageFile.exists()) {
			Files.copy(in, imageFile.toPath());
			final Product product = productService.find(productId);
			if(product != null) {
				result = new ResultBean();
				
				final ProductImage productImage = new ProductImage();
				productImage.setProduct(product);
				productImage.setFileName(fileName);
				
				result.setSuccess(productImageService.insert(productImage) != null);
				if(result.getSuccess()) {
					result.setMessage(Html.line(Color.GREEN, "Upload Successful."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load product. Please refresh the page."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, "Error please try uploading again.");
		}
		
		return result;
	}

	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean updateProduct(ProductFormBean productForm) {
		final ResultBean result;
		final Product product = productService.find(productForm.getId());
		
		if(product != null) {
			final ResultBean validateForm = validateProductForm(productForm);
			if(validateForm.getSuccess()) {
				final String displayName = getDisplayName(productForm);
				final Product productt = productService.findByDisplayName(displayName);
				if(productt != null && product.getId() != productt.getId()) {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Product name already exists.")));
				} else {
					product.setDisplayName(displayName);
					setProduct(product, productForm);
					
					result = new ResultBean();
					result.setSuccess(productService.update(product));
					if(result.getSuccess()) {
						result.setMessage(Html.line("Product " + Html.text(Color.BLUE, product.getDisplayName()) + " has been successfully " + Html.text(Color.GREEN, "updated") + "."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				}
			} else {
				result = validateForm;
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load product. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ResultBean setProductImageAsThumbnail(Long productImageId) {
		final ResultBean result;
		final ProductImage productImage = productImageService.find(productImageId);
		
		if(productImage != null) {
			result = new ResultBean();
			final Product product = productImage.getProduct();
			
			product.setImage(productImage.getFileName());
			result.setSuccess(productService.update(product));
			if(result.getSuccess()) {
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " set Product Image as thumbnail for " + Html.text(Color.BLUE, product.getName()) + "."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load product image. Please refresh the page."));
		}
		
		return result;
	}

	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean removeProduct(Long productId) {
		final ResultBean result;
		final Product product = productService.find(productId);
		
		if(product != null) {
			result = new ResultBean();
			
			result.setSuccess(productService.delete(product));
			if(result.getSuccess()) {
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed Product " + Html.text(Color.BLUE, product.getName()) + "."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load product. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ResultBean removeProductImage(Long productImageId) {
		final ResultBean result;
		final ProductImage productImage = productImageService.find(productImageId);
		
		if(productImage != null) {
			result = new ResultBean();
			final Product product = productImage.getProduct();
			
			// REMOVE AS THUMBNAIL IF DELETED
			if(product.getImage().equals(productImage.getFileName())) {
				product.setImage(ImageConstants.DEFAULT_IMAGE);
				productService.update(product);
			}
			
			result.setSuccess(productImageService.delete(productImage));
			if(result.getSuccess()) {
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed Product Image."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load product image. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	public Float getFinalBaseUnitSellingPrice(Product product, User user) {
		final Float finalBaseUnitPrice;
		final ClientProductPrice clientProductPrice = clientProductPriceService.findByClientAndProduct(user.getId(), product.getId());
		
		if(clientProductPrice != null) {
			finalBaseUnitPrice = clientProductPrice.getSellingPrice();
		} else {
			final ClientCompanyPrice clientCompanyPrice = clientCompanyPriceService.findByClientAndCompany(user.getId(), product.getCompany().getId());
			
			if(clientCompanyPrice != null) {
				finalBaseUnitPrice = product.getSellingPrice() * (100.0f + clientCompanyPrice.getMarkup()) / 100.0f;
			} else {
				finalBaseUnitPrice = product.getSellingPrice();
			}
		}
		
		return finalBaseUnitPrice;
	}
	
	@Override
	public Float getFinalBasePackageSellingPrice(Product product, User user) {
		return Math.round(getFinalBaseUnitSellingPrice(product, user) * product.getPackaging() * 100.0f) / 100.0f;
	}
	
	@Override
	public Float getFinalSellingDiscount(Product product, User user) {
		final Float finalSellingDiscount;
		final ClientPromo clientPromo = clientPromoService.findByClientAndProduct(user.getId(), product.getId());
		
		if(clientPromo != null) {
			finalSellingDiscount = clientPromo.getDiscount();
		} else {
			finalSellingDiscount = 0.0f;
		}
		
		return finalSellingDiscount;
	}
	
	private void setProduct(Product product, ProductFormBean productForm) {
		product.setCompany(companyService.find(productForm.getCompanyId()));
		product.setCategory(categoryService.find(productForm.getCategoryId()));
		product.setProductCode(productForm.getProductCode().trim());
		product.setName(productForm.getName().trim());
		product.setSize(productForm.getSize().trim());
		product.setPackaging(productForm.getPackaging());
		product.setDescription(productForm.getDescription().trim());
		product.setGrossPrice(productForm.getPackageGrossPrice() / productForm.getPackaging());
		product.setDiscount(productForm.getDiscount());
		product.setSellingPrice(productForm.getPackageSellingPrice() / productForm.getPackaging());
		product.setPercentProfit(productForm.getPercentProfit());
	}
	
	private void setProductStock(Product product, Warehouse warehouse) {
		final List<WarehouseItem> warehouseItems = warehouseItemService.findAllByProduct(product.getId());
		Integer stockCountAll = 0;
		Integer stockCountCurrent = 0;
		for(WarehouseItem warehouseItem : warehouseItems) {
			stockCountAll += warehouseItem.getStockCount();
			if(warehouse != null && warehouseItem.getWarehouse().equals(warehouse)) {
				stockCountCurrent = warehouseItem.getStockCount();
			}
		}
		product.setStockCountCurrent(stockCountCurrent);
		product.setStockCountAll(stockCountAll);
	}
	
	private String getDisplayName(ProductFormBean productForm) {
		String displayName = productForm.getName() + " ";
		
		if(productForm.getPackaging() > 1) displayName += productForm.getPackaging() + "x";
		displayName += productForm.getSize() + "";
		
		return displayName;
	}
	
	private ResultBean validateProductForm(ProductFormBean productForm) {
		final ResultBean result;
		
		if(productForm.getCompanyId() == null || productForm.getCategoryId() == null ||
				productForm.getProductCode() == null || productForm.getProductCode().trim().length() < 3 ||
				productForm.getName() == null || productForm.getName().trim().length() < 3 ||
				productForm.getSize() == null ||
				productForm.getPackaging() == null || productForm.getPackaging() <= 0 ||
				productForm.getDescription() == null || productForm.getDescription().trim().length() < 3 ||
				productForm.getPackageGrossPrice() == null || productForm.getPackageGrossPrice() <= 0 ||
				productForm.getDiscount() == null || productForm.getDiscount() <= 0 ||
				productForm.getPackageNetPrice() == null || productForm.getPackageNetPrice() <= 0 ||
				productForm.getPackageSellingPrice() == null || productForm.getPackageSellingPrice() <= 0||
				productForm.getPercentProfit() == null || productForm.getPercentProfit() <= 0) {
			result = new ResultBean(Boolean.FALSE, Html.line("All fields are " + Html.text(Color.RED, "required") + " and must contain at least 3 characters or must have a value greater than 0."));
		} else if(productForm.getPackaging() > 999) {
			result = new ResultBean(Boolean.FALSE, Html.line("Packaging " + Html.text(Color.RED, "cannot exceed") + " the value of 999."));
		} else if(productForm.getName().length() > 40) {
			result = new ResultBean(Boolean.FALSE, Html.line("Name of product exceeds " + Html.text(Color.RED, "maximum (40) characters") + "."));
		} else {
			result = new ResultBean(Boolean.TRUE, "");
		}
		
		return result;
	}
}
