package com.chua.distributions.rest.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.database.entity.PurchaseOrder;
import com.chua.distributions.database.entity.PurchaseOrderItem;
import com.chua.distributions.database.service.ProductService;
import com.chua.distributions.database.service.PurchaseOrderItemService;
import com.chua.distributions.database.service.PurchaseOrderService;
import com.chua.distributions.enums.Color;
import com.chua.distributions.enums.Status;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.PurchaseOrderItemHandler;
import com.chua.distributions.utility.Html;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 7, 2016
 */
@Transactional
@Component
public class PurchaseOrderItemHandlerImpl implements PurchaseOrderItemHandler {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@Autowired
	private PurchaseOrderItemService purchaseOrderItemService;

	@Override
	public ObjectList<PurchaseOrderItem> getPurchaseOrderItemObjectList(Integer pageNumber, Long purchaseOrderId) {
		return purchaseOrderItemService.findAllWithPagingOrderByLastUpdate(pageNumber, UserContextHolder.getItemsPerPage(), purchaseOrderId);
	}

	@Override
	public ResultBean addItem(Long productId, Long purchaseOrderId) {
		final ResultBean result;
		
		final Product product = productService.find(productId);
			
		if(product != null) {
			final PurchaseOrder purchaseOrder = purchaseOrderService.find(purchaseOrderId);
			if(purchaseOrder != null) {
				if(purchaseOrder.getStatus().equals(Status.CREATING) || UserContextHolder.getUser().getUserType().getAuthority() <= Integer.valueOf(2)
						&& !purchaseOrder.getStatus().equals(Status.PAID)) {
					final PurchaseOrderItem purchaseOrderItem = purchaseOrderItemService.findByNameAndPurchaseOrder(product.getDisplayName(), purchaseOrder.getId());
					
					if(purchaseOrderItem == null) {
						final PurchaseOrderItem purchaseOrderItemm = new PurchaseOrderItem();
						
						purchaseOrderItemm.setPurchaseOrder(purchaseOrder);
						purchaseOrderItemm.setQuantity(product.getPackaging());
						setPurchaseOrderItem(purchaseOrderItemm, product);
						
						result = new ResultBean();
						result.setSuccess(purchaseOrderItemService.insert(purchaseOrderItemm) != null);
						if(result.getSuccess()) {
							result.setMessage(Html.line("Added 1 package of " + Html.text(Color.BLUE, purchaseOrderItemm.getDisplayName()) + "."));
						} else {
							result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
						}
					} else {
						result = changeQuantity(purchaseOrderItem, purchaseOrderItem.getQuantity() + purchaseOrderItem.getPackaging());
					}
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
							Html.line(" You are not authorized to change purchase order with status " + Html.text(Color.BLUE, purchaseOrder.getStatus().getDisplayName()) + "."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load purchase order. Please refresh the page."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load product. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean removeItem(Long purchaseOrderItemId) {
		final ResultBean result;
		final PurchaseOrderItem purchaseOrderItem = purchaseOrderItemService.find(purchaseOrderItemId);
		
		if(purchaseOrderItem != null) {
			if(purchaseOrderItem.getPurchaseOrder().getStatus().equals(Status.CREATING) || UserContextHolder.getUser().getUserType().getAuthority() <= Integer.valueOf(2)
					&& !purchaseOrderItem.getPurchaseOrder().getStatus().equals(Status.PAID)) {
				result = removeItem(purchaseOrderItem);
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to change purchase order with status " + Html.text(Color.BLUE, purchaseOrderItem.getPurchaseOrder().getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load item. Please refresh the page."));
		}
		
		return result;
	}
	
	private ResultBean removeItem(PurchaseOrderItem purchaseOrderItem) {
		final ResultBean result;
		
		result = new ResultBean();
		result.setSuccess(purchaseOrderItemService.delete(purchaseOrderItem));
		if(result.getSuccess()) {
			result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed Item " + Html.text(Color.BLUE, purchaseOrderItem.getDisplayName()) + "."));
		} else {
			result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean changePieceQuantity(Long purchaseOrderItemId, Integer pieceQuantity) {
		final ResultBean result;
		final PurchaseOrderItem purchaseOrderItem = purchaseOrderItemService.find(purchaseOrderItemId);
		
		if(purchaseOrderItem != null) {
			if(purchaseOrderItem.getPurchaseOrder().getStatus().equals(Status.CREATING) || UserContextHolder.getUser().getUserType().getAuthority() <= Integer.valueOf(2)
					&& !purchaseOrderItem.getPurchaseOrder().getStatus().equals(Status.PAID)) {
				result = changeQuantity(purchaseOrderItem, (purchaseOrderItem.getPackageQuantity() * purchaseOrderItem.getPackaging()) + pieceQuantity);
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to change purchase order with status " + Html.text(Color.BLUE, purchaseOrderItem.getPurchaseOrder().getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load item. Please refresh the page."));
		}
		
		return result;
	}

	@Override
	public ResultBean changePackageQuantity(Long purchaseOrderItemId, Integer packageQuantity) {
		final ResultBean result;
		final PurchaseOrderItem purchaseOrderItem = purchaseOrderItemService.find(purchaseOrderItemId);
		
		if(purchaseOrderItem != null) {
			if(purchaseOrderItem.getPurchaseOrder().getStatus().equals(Status.CREATING) || UserContextHolder.getUser().getUserType().getAuthority() <= Integer.valueOf(2)
					&& !purchaseOrderItem.getPurchaseOrder().getStatus().equals(Status.PAID)) {
				result = changeQuantity(purchaseOrderItem, (packageQuantity * purchaseOrderItem.getPackaging()) + purchaseOrderItem.getPieceQuantity());
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to change purchase order with status " + Html.text(Color.BLUE, purchaseOrderItem.getPurchaseOrder().getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load item. Please refresh the page."));
		}
		
		return result;
	}
	
	private ResultBean changeQuantity(PurchaseOrderItem purchaseOrderItem, Integer quantity) {
		final ResultBean result;
		
		if(!quantity.equals(0)) {
			if(quantity / purchaseOrderItem.getPackaging() < Integer.valueOf(1000) && quantity % purchaseOrderItem.getPackaging() < Integer.valueOf(1000)
					&& quantity / purchaseOrderItem.getPackaging() > Integer.valueOf(-1000) && quantity % purchaseOrderItem.getPackaging() > Integer.valueOf(-1000) ) {
				purchaseOrderItem.setQuantity(quantity);
				result = new ResultBean();
				result.setSuccess(purchaseOrderItemService.update(purchaseOrderItem));
				if(result.getSuccess()) {
					result.setMessage(Html.line("Quantity of " + Html.text(Color.BLUE, purchaseOrderItem.getDisplayName()) + " changed to " + Html.text(Color.GREEN, purchaseOrderItem.getFormattedQuantity()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line("Quantity " + Html.text(Color.RED, "cannot exceed ") + " the value of 999."));
			}
		} else {
			result = removeItem(purchaseOrderItem);
		}
		
		return result;
	}
	
	private void setPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem, Product product) {
		purchaseOrderItem.setProductId(product.getId());
		purchaseOrderItem.setProductCode(product.getProductCode());
		purchaseOrderItem.setDisplayName(product.getDisplayName());
		purchaseOrderItem.setPackaging(product.getPackaging());
		purchaseOrderItem.setUnitPrice(product.getGrossPrice());
		purchaseOrderItem.setDiscount(product.getDiscount());
	}
}
