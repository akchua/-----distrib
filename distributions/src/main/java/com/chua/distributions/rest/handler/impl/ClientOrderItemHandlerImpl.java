package com.chua.distributions.rest.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.database.entity.ClientOrderItem;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.database.service.ClientOrderItemService;
import com.chua.distributions.database.service.ClientOrderService;
import com.chua.distributions.database.service.ProductService;
import com.chua.distributions.enums.Color;
import com.chua.distributions.enums.Status;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.ClientOrderItemHandler;
import com.chua.distributions.utility.Html;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
@Transactional
@Component
public class ClientOrderItemHandlerImpl implements ClientOrderItemHandler {

	@Autowired
	private ClientOrderItemService clientOrderItemService;
	
	@Autowired
	private ClientOrderService clientOrderService;
	
	@Autowired
	private ProductService productService;

	@Override
	public ObjectList<ClientOrderItem> getClientOrderItemObjectList(Integer pageNumber, Long clientOrderId) {
		return clientOrderItemService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), clientOrderId);
	}

	@Override
	public ResultBean addItem(Long productId, Long clientOrderId) {
		final ResultBean result;
		
		final Product product = productService.find(productId);
			
		if(product != null) {
			final ClientOrder clientOrder = clientOrderService.find(clientOrderId);
			if(clientOrder != null) {
				if(clientOrder.getStatus().equals(Status.CREATING) || clientOrder.getStatus().equals(Status.SUBMITTED)) {
					final ClientOrderItem clientOrderItem = clientOrderItemService.findByNameAndClientOrder(product.getDisplayName(), clientOrder.getId());
					
					if(clientOrderItem == null) {
						final ClientOrderItem clientOrderItemm = new ClientOrderItem();
						
						clientOrderItemm.setClientOrder(clientOrder);
						clientOrderItemm.setQuantity(product.getPackaging());
						setClientOrderItem(clientOrderItemm, product);
						
						result = new ResultBean();
						result.setSuccess(clientOrderItemService.insert(clientOrderItemm) != null);
						if(result.getSuccess()) {
							result.setMessage(Html.line("Added 1 package of " + Html.text(Color.BLUE, clientOrderItemm.getDisplayName()) + "."));
						} else {
							result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
						}
					} else {
						result = changeQuantity(clientOrderItem, clientOrderItem.getQuantity() + clientOrderItem.getPackaging());
					}
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
							Html.line(" You are not authorized to change order with status " + Html.text(Color.BLUE, clientOrder.getStatus().getDisplayName()) + "."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load order. Please refresh the page."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load product. Please refresh the page."));
		}
		
		return result;
	}

	@Override
	public ResultBean removeItem(Long clientOrderItemId) {
		final ResultBean result;
		final ClientOrderItem clientOrderItem = clientOrderItemService.find(clientOrderItemId);
		
		if(clientOrderItem != null) {
			if(clientOrderItem.getClientOrder().getStatus().equals(Status.CREATING) || clientOrderItem.getClientOrder().getStatus().equals(Status.SUBMITTED)) {
				result = removeItem(clientOrderItem);
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to change order with status " + Html.text(Color.BLUE, clientOrderItem.getClientOrder().getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load item. Please refresh the page."));
		}
		
		return result;
	}
	
	private ResultBean removeItem(ClientOrderItem clientOrderItem) {
		final ResultBean result;
		
		result = new ResultBean();
		result.setSuccess(clientOrderItemService.delete(clientOrderItem));
		if(result.getSuccess()) {
			result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed Item " + Html.text(Color.BLUE, clientOrderItem.getDisplayName()) + "."));
		} else {
			result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
		}
		
		return result;
	}

	@Override
	public ResultBean changePieceQuantity(Long clientOrderItemId, Integer pieceQuantity) {
		final ResultBean result;
		final ClientOrderItem clientOrderItem = clientOrderItemService.find(clientOrderItemId);
		
		if(clientOrderItem != null) {
			if(clientOrderItem.getClientOrder().getStatus().equals(Status.CREATING) || clientOrderItem.getClientOrder().getStatus().equals(Status.SUBMITTED)) {
				result = changeQuantity(clientOrderItem, (clientOrderItem.getPackageQuantity() * clientOrderItem.getPackaging()) + pieceQuantity);
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to change order with status " + Html.text(Color.BLUE, clientOrderItem.getClientOrder().getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load item. Please refresh the page."));
		}
		
		return result;
	}

	@Override
	public ResultBean changePackageQuantity(Long clientOrderItemId, Integer packageQuantity) {
		final ResultBean result;
		final ClientOrderItem clientOrderItem = clientOrderItemService.find(clientOrderItemId);
		
		if(clientOrderItem != null) {
			if(clientOrderItem.getClientOrder().getStatus().equals(Status.CREATING) || clientOrderItem.getClientOrder().getStatus().equals(Status.SUBMITTED)) {
				result = changeQuantity(clientOrderItem, (packageQuantity * clientOrderItem.getPackaging()) + clientOrderItem.getPieceQuantity());
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to change order with status " + Html.text(Color.BLUE, clientOrderItem.getClientOrder().getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load item. Please refresh the page."));
		}
		
		return result;
	}
	
	private ResultBean changeQuantity(ClientOrderItem clientOrderItem, Integer quantity) {
		final ResultBean result;
		
		if(quantity > 0) {
			if(quantity / clientOrderItem.getPackaging() < Integer.valueOf(1000) && quantity % clientOrderItem.getPackaging() < Integer.valueOf(1000)
					&& quantity / clientOrderItem.getPackaging() > Integer.valueOf(-1000) && quantity % clientOrderItem.getPackaging() > Integer.valueOf(-1000) ) {
				clientOrderItem.setQuantity(quantity);
				result = new ResultBean();
				result.setSuccess(clientOrderItemService.update(clientOrderItem));
				if(result.getSuccess()) {
					result.setMessage(Html.line("Quantity of " + Html.text(Color.BLUE, clientOrderItem.getDisplayName()) + " changed to " + Html.text(Color.GREEN, clientOrderItem.getFormattedQuantity()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line("Quantity " + Html.text(Color.RED, "cannot exceed ") + " the value of 999."));
			}
		} else {
			result = removeItem(clientOrderItem);
		}
		
		return result;
	}
	
	private void setClientOrderItem(ClientOrderItem clientOrderItem, Product product) {
		clientOrderItem.setProductId(product.getId());
		clientOrderItem.setProductCode(product.getProductCode());
		clientOrderItem.setDisplayName(product.getDisplayName());
		clientOrderItem.setPackaging(product.getPackaging());
		clientOrderItem.setUnitPrice((clientOrderItem.getClientOrder().getCreator().getMarkup() == 0.0f) 
						? product.getSellingPrice() 
						: product.getSellingPrice() * (100.0f + clientOrderItem.getClientOrder().getCreator().getMarkup()) / 100.0f);
		clientOrderItem.setDiscount(0.0f);		//PROMOTIONS UPDATE
	}
}
