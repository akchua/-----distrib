package com.chua.distributions.rest.handler.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.StockAdjustFormBean;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.database.entity.StockAdjust;
import com.chua.distributions.database.service.ProductService;
import com.chua.distributions.database.service.StockAdjustService;
import com.chua.distributions.enums.Color;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.StockAdjustHandler;
import com.chua.distributions.rest.handler.WarehouseItemHandler;
import com.chua.distributions.utility.Html;
import com.chua.distributions.utility.format.QuantityFormatter;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	26 Jan 2017
 */
@Component
@Transactional
public class StockAdjustHandlerImpl implements StockAdjustHandler {

	@Autowired
	private StockAdjustService stockAdjustService;
	
	@Autowired
	private WarehouseItemHandler warehouseItemHandler;
	
	@Autowired
	private ProductService productService;
	
	@Override
	public ObjectList<StockAdjust> getStockAdjustByProductObjectList(Integer pageNumber, Long productId) {
		return stockAdjustService.findByProductWithPagingOrderByLastUpdate(pageNumber, UserContextHolder.getItemsPerPage(), productId);
	}
	
	@Override
	public ResultBean adjustStock(StockAdjustFormBean stockAdjustForm) {
		final ResultBean result;
		final ResultBean validateForm = validateStockAdjustForm(stockAdjustForm);
		
		if(validateForm.getSuccess()) {
			final Product product = productService.find(stockAdjustForm.getProductId());
			
			if(product != null) {
				if(stockAdjustForm.getPackageQuantity() == null) stockAdjustForm.setPackageQuantity(0);
				if(stockAdjustForm.getPieceQuantity() == null) stockAdjustForm.setPieceQuantity(0);
				Integer quantity = stockAdjustForm.getPackageQuantity() * product.getPackaging() + stockAdjustForm.getPieceQuantity();
				if(quantity != 0) {
					result = new ResultBean();
					
					final StockAdjust stockAdjust = new StockAdjust();
					setStockAdjust(stockAdjust, stockAdjustForm, product, quantity);
					stockAdjust.setCreator(UserContextHolder.getUser().getUserEntity());
					
					result.setSuccess(stockAdjustService.insert(stockAdjust) != null &&
								warehouseItemHandler.addToWarehouse(stockAdjustForm.getProductId(), stockAdjustForm.getWarehouse(), quantity));
					if(result.getSuccess()) {
						if(quantity > 0) {
							result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " added " + QuantityFormatter.format(quantity, product.getPackaging()) 
										+ " packages of " + Html.text(Color.BLUE, product.getDisplayName()) + " at " + Html.text(Color.BLUE, stockAdjustForm.getWarehouse().getDisplayName()) + "."));
						} else {
							result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed " + QuantityFormatter.format(-quantity, product.getPackaging()) 
							+ " packages of " + Html.text(Color.BLUE, product.getDisplayName()) + " at " + Html.text(Color.BLUE, stockAdjustForm.getWarehouse().getDisplayName()) + "."));
						}
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line("Please input non-zero quantity."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load product. Please refresh the page."));
			}
		} else {
			result = validateForm;
		}
		
		return result;
	}
	
	@Override
	public ResultBean removeAdjustment(Long stockAdjustId) {
		final ResultBean result;
		final StockAdjust stockAdjust = stockAdjustService.find(stockAdjustId);
		
		if(stockAdjust != null) {
			final Product product = productService.find(stockAdjust.getProductId());
			
			if(product != null) {
				result = new ResultBean();
				
				result.setSuccess(warehouseItemHandler.removeFromWarehouse(stockAdjust.getProductId(), stockAdjust.getWarehouse(), stockAdjust.getQuantity())
						&& stockAdjustService.delete(stockAdjust));
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " cancelled adjustment of " + stockAdjust.getFormattedQuantity() 
								+ " packages of " + Html.text(Color.BLUE, product.getDisplayName()) + " at " + Html.text(Color.BLUE, stockAdjust.getWarehouse().getDisplayName()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load product. Please refresh the page."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load adjustment. Please refresh the page."));
		}
		
		
		return result;
	}
	
	private ResultBean validateStockAdjustForm(StockAdjustFormBean stockAdjustForm) {
		final ResultBean result;
		
		if(stockAdjustForm.getWarehouse() == null || stockAdjustForm.getDescription() == null 
				|| (stockAdjustForm.getPieceQuantity() == null && stockAdjustForm.getPackageQuantity() == null)) {
			result = new ResultBean(Boolean.FALSE, Html.line("All fields are " + Html.text(Color.RED, "required") + "."));
		} else {
			result = new ResultBean(Boolean.TRUE, "");
		}
		
		return result;
	}
	
	private void setStockAdjust(StockAdjust stockAdjust, StockAdjustFormBean stockAdjustForm, Product product, Integer quantity) {
		stockAdjust.setProductId(product.getId());
		stockAdjust.setProductCode(product.getProductCode());
		stockAdjust.setDisplayName(product.getDisplayName());
		stockAdjust.setPackaging(product.getPackaging());
		stockAdjust.setWarehouse(stockAdjustForm.getWarehouse());
		stockAdjust.setQuantity(quantity);
		stockAdjust.setDescription(stockAdjustForm.getDescription());
	}

	@Override
	public List<Warehouse> getWarehouseList() {
		return Stream.of(Warehouse.values())
				.collect(Collectors.toList());
	}
}
