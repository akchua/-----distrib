package com.chua.distributions.rest.handler.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotAuthorizedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.annotations.CheckAuthority;
import com.chua.distributions.beans.PartialClientOrderItemBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.database.entity.ClientOrderItem;
import com.chua.distributions.database.entity.ClientPromo;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.database.service.ClientOrderItemService;
import com.chua.distributions.database.service.ClientOrderService;
import com.chua.distributions.database.service.ClientPromoService;
import com.chua.distributions.database.service.ProductService;
import com.chua.distributions.enums.Color;
import com.chua.distributions.enums.Status;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.ClientOrderItemHandler;
import com.chua.distributions.rest.handler.ProductHandler;
import com.chua.distributions.utility.Html;
import com.chua.distributions.utility.format.QuantityFormatter;

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
	
	@Autowired
	private ClientPromoService clientPromoService;
	
	@Autowired
	private ProductHandler productHandler;

	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ObjectList<ClientOrderItem> getClientOrderItemObjectList(Integer pageNumber, Long clientOrderId) {
		return clientOrderItemService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), clientOrderId);
	}
	
	@Override
	public ObjectList<PartialClientOrderItemBean> getPartialClientOrderItemObjectList(Integer pageNumber,
			Long clientOrderId) {
		final ObjectList<PartialClientOrderItemBean> objPartialClientOrderItems = new ObjectList<PartialClientOrderItemBean>();
		final ObjectList<ClientOrderItem> objClientOrderItems = clientOrderItemService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), clientOrderId);
		
		if(objClientOrderItems != null) {
			objPartialClientOrderItems.setTotal(objClientOrderItems.getTotal());
			final List<PartialClientOrderItemBean> partialClientOrderItems = new ArrayList<PartialClientOrderItemBean>();
			for(ClientOrderItem clientOrderItem : objClientOrderItems.getList()) {
				partialClientOrderItems.add(new PartialClientOrderItemBean(clientOrderItem));
			}
			objPartialClientOrderItems.setList(partialClientOrderItems);
		}
		
		return objPartialClientOrderItems;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ObjectList<ClientOrderItem> getClientOrderItemByProductObjectList(Integer pageNumber, Long productId) {
		return clientOrderItemService.findByProductWithPagingOrderByLastUpdate(pageNumber, UserContextHolder.getItemsPerPage(), productId);
	}

	@Override
	public ResultBean addItem(Long productId, Long clientOrderId) {
		final ResultBean result;
		
		final Product product = productService.find(productId);
			
		if(product != null) {
			final ClientOrder clientOrder = clientOrderService.find(clientOrderId);
			if(clientOrder != null) {
				if(UserContextHolder.getUser().getId().equals(clientOrder.getClient().getId()) || 
						UserContextHolder.getUser().getId().equals(clientOrder.getCreator().getId())) {
					if(clientOrder.getStatus().equals(Status.CREATING) || clientOrder.getStatus().equals(Status.SUBMITTED)) {
						result = addItem(product, clientOrder, product.getPackaging());
					} else {
						result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
								Html.line(" You are not authorized to change order with status " + Html.text(Color.BLUE, clientOrder.getStatus().getDisplayName()) + "."));
					}
				} else {
					throw new NotAuthorizedException("User is not authenticated.");
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load order. Please refresh the page."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load product. Please refresh the page."));
		}
		
		return result;
	}
	
	private ResultBean addItem(Product product, ClientOrder clientOrder, Integer quantity) {
		final ResultBean result;
		
		final ClientOrderItem clientOrderItem = clientOrderItemService.findByProductAndClientOrder(product.getId(), clientOrder.getId());
		
		if(clientOrderItem == null) {
			if(quantity > 0) {
				final ClientOrderItem clientOrderItemm = new ClientOrderItem();
				
				clientOrderItemm.setClientOrder(clientOrder);
				clientOrderItemm.setQuantity(quantity);
				setClientOrderItem(clientOrderItemm, product);
				applyPromo(clientOrderItemm, product);
				
				result = new ResultBean();
				result.setSuccess(clientOrderItemService.insert(clientOrderItemm) != null);
				if(result.getSuccess()) {
					result.setMessage(Html.line("Added " + QuantityFormatter.format(quantity, product.getPackaging()) + " package of " + Html.text(Color.BLUE, clientOrderItemm.getDisplayName()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Item quantity must be greater than 0."));
			}
		} else {
			result = changeQuantity(clientOrderItem, clientOrderItem.getQuantity() + quantity);
		}
		
		return result;
	}

	@Override
	public ResultBean removeItem(Long clientOrderItemId) {
		final ResultBean result;
		final ClientOrderItem clientOrderItem = clientOrderItemService.find(clientOrderItemId);
		
		if(clientOrderItem != null) {
			if(UserContextHolder.getUser().getId().equals(clientOrderItem.getClientOrder().getClient().getId()) ||
					UserContextHolder.getUser().getId().equals(clientOrderItem.getClientOrder().getCreator().getId())) {
				if(clientOrderItem.getClientOrder().getStatus().equals(Status.CREATING) || clientOrderItem.getClientOrder().getStatus().equals(Status.SUBMITTED)) {
					result = removeItem(clientOrderItem);
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
							Html.line(" You are not authorized to change order with status " + Html.text(Color.BLUE, clientOrderItem.getClientOrder().getStatus().getDisplayName()) + "."));
				}
			} else {
				throw new NotAuthorizedException("User is not authenticated.");
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
			if(UserContextHolder.getUser().getId().equals(clientOrderItem.getClientOrder().getClient().getId()) ||
					UserContextHolder.getUser().getId().equals(clientOrderItem.getClientOrder().getCreator().getId())) {
				if(clientOrderItem.getClientOrder().getStatus().equals(Status.CREATING) || clientOrderItem.getClientOrder().getStatus().equals(Status.SUBMITTED)) {
					result = changeQuantity(clientOrderItem, (clientOrderItem.getPackageQuantity() * clientOrderItem.getPackaging()) + pieceQuantity);
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
							Html.line(" You are not authorized to change order with status " + Html.text(Color.BLUE, clientOrderItem.getClientOrder().getStatus().getDisplayName()) + "."));
				}
			} else {
				throw new NotAuthorizedException("User is not authenticated.");
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
			if(UserContextHolder.getUser().getId().equals(clientOrderItem.getClientOrder().getClient().getId()) || 
					UserContextHolder.getUser().getId().equals(clientOrderItem.getClientOrder().getCreator().getId())) {
				if(clientOrderItem.getClientOrder().getStatus().equals(Status.CREATING) || clientOrderItem.getClientOrder().getStatus().equals(Status.SUBMITTED)) {
					result = changeQuantity(clientOrderItem, (packageQuantity * clientOrderItem.getPackaging()) + clientOrderItem.getPieceQuantity());
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
							Html.line(" You are not authorized to change order with status " + Html.text(Color.BLUE, clientOrderItem.getClientOrder().getStatus().getDisplayName()) + "."));
				}
			} else {
				throw new NotAuthorizedException("User is not authenticated.");
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
	
	@Override
	public ResultBean transferPiece(Long clientOrderItemId, Long destinationOrderId) {
		final ResultBean result;
		final ClientOrderItem clientOrderItem = clientOrderItemService.find(clientOrderItemId);
		final ClientOrder destinationOrder = clientOrderService.find(destinationOrderId);
		final ResultBean validateTransfer = validateTransfer(clientOrderItem, destinationOrder);
		
		if(validateTransfer.getSuccess()) {
			result = transferItem(clientOrderItem, destinationOrder, 1);
		} else {
			result = validateTransfer;
		}
		
		return result;
	}

	@Override
	public ResultBean transferPackage(Long clientOrderItemId, Long destinationOrderId) {
		final ResultBean result;
		final ClientOrderItem clientOrderItem = clientOrderItemService.find(clientOrderItemId);
		final ClientOrder destinationOrder = clientOrderService.find(destinationOrderId);
		final ResultBean validateTransfer = validateTransfer(clientOrderItem, destinationOrder);
		
		if(validateTransfer.getSuccess()) {
			result = transferItem(clientOrderItem, destinationOrder, clientOrderItem.getPackaging());
		} else {
			result = validateTransfer;
		}
		
		return result;
	}

	@Override
	public ResultBean transferAll(Long clientOrderItemId, Long destinationOrderId) {
		final ResultBean result;
		final ClientOrderItem clientOrderItem = clientOrderItemService.find(clientOrderItemId);
		final ClientOrder destinationOrder = clientOrderService.find(destinationOrderId);
		final ResultBean validateTransfer = validateTransfer(clientOrderItem, destinationOrder);
		
		if(validateTransfer.getSuccess()) {
			result = transferItem(clientOrderItem, destinationOrder, clientOrderItem.getQuantity());
		} else {
			result = validateTransfer;
		}
		
		return result;
	}
	
	private ResultBean transferItem(ClientOrderItem clientOrderItem, ClientOrder destinationOrder, Integer quantity) {
		final ResultBean result;
		
		// SET TO [MAX] IF EXCEEDS QUANTITY
		if(quantity > clientOrderItem.getQuantity()) {
			quantity = clientOrderItem.getQuantity();
		}
		//
		
		result = new ResultBean();
		
		Product product = productService.find(clientOrderItem.getProductId());
		
		result.setSuccess(changeQuantity(clientOrderItem, clientOrderItem.getQuantity() - quantity).getSuccess()
							&& addItem(product, destinationOrder, quantity).getSuccess());
		if(result.getSuccess()) {
			result.setMessage(Html.line("Transferred "
								+ Html.text(Color.GREEN, QuantityFormatter.format(quantity, product.getPackaging()))
								+ " " + product.getDisplayName() + " from "
								+ Html.text(Color.BLUE, "Order of " + Html.text(Color.BLUE, "ID #" + clientOrderItem.getClientOrder().getId()))
								+ " to "
								+ Html.text(Color.BLUE, "Order of " + Html.text(Color.BLUE, "ID #" + destinationOrder.getId())) + "."));
		} else {
			result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
		}
		
		return result;
	}
	
	private ResultBean validateTransfer(ClientOrderItem clientOrderItem, ClientOrder destinationOrder) {
		final ResultBean result;
		
		if(clientOrderItem != null) {
			if(destinationOrder != null) {
				Status sourceStatus = clientOrderItem.getClientOrder().getStatus();
				
				if(sourceStatus.equals(Status.ACCEPTED) || sourceStatus.equals(Status.TO_FOLLOW)
						|| (UserContextHolder.getUser().getUserType().getAuthority() <= Integer.valueOf(2)
							&& !sourceStatus.equals(Status.RECEIVED)
							&& !sourceStatus.equals(Status.CANCELLED))) {
					Status destinationStatus = destinationOrder.getStatus();
					if(destinationStatus.equals(Status.ACCEPTED) || destinationStatus.equals(Status.TO_FOLLOW)
							|| (UserContextHolder.getUser().getUserType().getAuthority() <= Integer.valueOf(2)
								&& !destinationStatus.equals(Status.RECEIVED)
								&& !destinationStatus.equals(Status.CANCELLED))) {
						result = new ResultBean(Boolean.TRUE, "");
					} else {
						result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
								Html.line(" You are not authorized to change order with status " + Html.text(Color.BLUE, destinationStatus.getDisplayName()) + "."));
					}
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
							Html.line(" You are not authorized to change order with status " + Html.text(Color.BLUE, sourceStatus.getDisplayName()) + "."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load order. Please refresh the page."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load item. Please refresh the page."));
		}
		
		return result;
	}
	
	private void setClientOrderItem(ClientOrderItem clientOrderItem, Product product) {
		clientOrderItem.setProductId(product.getId());
		clientOrderItem.setProductCode(product.getProductCode());
		clientOrderItem.setDisplayName(product.getDisplayName());
		clientOrderItem.setPackaging(product.getPackaging());
		clientOrderItem.setUnitPrice(productHandler.getFinalBaseUnitPrice(product, clientOrderItem.getClientOrder().getClient()));
		clientOrderItem.setDiscount(0.0f);
	}
	
	private void applyPromo(ClientOrderItem clientOrderItem, Product product) {
		final ClientPromo clientPromo = clientPromoService.findByClientAndProduct(clientOrderItem.getClientOrder().getClient().getId(), product.getId());
		
		if(clientPromo != null) {
			clientOrderItem.setDiscount(clientPromo.getDiscount());
		}
	}
}
