package com.chua.distributions.rest.handler.impl;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.StringWrapper;
import com.chua.distributions.constants.MailConstants;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.database.entity.ClientOrderItem;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.database.entity.WarehouseItem;
import com.chua.distributions.database.service.ClientOrderItemService;
import com.chua.distributions.database.service.ClientOrderService;
import com.chua.distributions.database.service.ProductService;
import com.chua.distributions.database.service.WarehouseItemService;
import com.chua.distributions.enums.Color;
import com.chua.distributions.enums.Status;
import com.chua.distributions.enums.UserType;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.ClientOrderHandler;
import com.chua.distributions.utility.EmailUtil;
import com.chua.distributions.utility.Html;
import com.chua.distributions.utility.format.QuantityFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
@Transactional
@Component
public class ClientOrderHandlerImpl implements ClientOrderHandler {

	@Autowired
	private ClientOrderService clientOrderService;
	
	@Autowired
	private ClientOrderItemService clientOrderItemService;
	
	@Autowired
	private WarehouseItemService warehouseItemService;
	
	@Autowired
	private ProductService productService;

	@Override
	public ClientOrder getClientOrder(Long clientOrderId) {
		refreshClientOrder(clientOrderId);
		return clientOrderService.find(clientOrderId);
	}

	@Override
	public ObjectList<ClientOrder> getClientOrderObjectList(Integer pageNumber, Boolean showPaid) {
		return clientOrderService.findByClientWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), UserContextHolder.getUser().getId(), showPaid);
	}
	
	@Override
	public ObjectList<ClientOrder> getClientOrderRequestObjectList(Integer pageNumber, Boolean showAccepted) {
		return clientOrderService.findAllRequestWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), showAccepted);
	}
	
	@Override
	public ObjectList<ClientOrder> getAcceptedClientOrderObjectList(Integer pageNumber, Warehouse warehouse) {
		return clientOrderService.findAllAcceptedWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), warehouse);
	}
	
	@Override
	public ObjectList<ClientOrder> getReceivedClientOrderObjectList(Integer pageNumber, Warehouse warehouse) {
		return clientOrderService.findAllReceivedWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), warehouse);
	}
	
	@Override
	public ObjectList<ClientOrder> getPaidClientOrderObjectList(Integer pageNumber, Warehouse warehouse) {
		return clientOrderService.findAllPaidWithPagingOrderByLatest(pageNumber, UserContextHolder.getItemsPerPage(), warehouse);
	}
	
	@Override
	public StringWrapper getFormattedTotalPayable() {
		final StringWrapper sw = new StringWrapper();
		List<ClientOrder> clientOrders = clientOrderService.findAllWithStatusReceived();
		
		float total = 0.0f;
		for(ClientOrder clientOrder : clientOrders) {
			total += clientOrder.getNetTotal();
		}
		
		DecimalFormat df = new DecimalFormat("#,##0.00");
		sw.setContent("Php " + df.format(total));
		
		return sw;
	}

	@Override
	public ResultBean addClientOrder() {
		final ResultBean result;
		
		if(UserContextHolder.getUser().getUserType().equals(UserType.CLIENT)) {
			final List<ClientOrder> clientOrders = clientOrderService.findAllByClientWithStatusCreating(UserContextHolder.getUser().getId());
			
			if(clientOrders != null && clientOrders.size() < 5) {
				final ClientOrder clientOrder = new ClientOrder();
				
				clientOrder.setCreator(UserContextHolder.getUser().getUserEntity());
				clientOrder.setGrossTotal(0.0f);
				clientOrder.setDiscountTotal(0.0f);
				clientOrder.setStatus(Status.CREATING);
				clientOrder.setWarehouse(null);
				clientOrder.setAdditionalDiscount(UserContextHolder.getUser().getDiscount());
				clientOrder.setLessVat(UserContextHolder.getUser().getVatType().getLessVat());
				clientOrder.setDeliveredOn(new DateTime(2000, 1, 1, 0, 0, 0));
				
				result = new ResultBean();
				result.setSuccess(clientOrderService.insert(clientOrder) != null);
				if(result.getSuccess()) {
					result.setMessage(clientOrder.getId() + "");
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line("You are " + Html.text(Color.RED, "NOT ALLOWED") + " to create more than 5 orders with status " + Html.text(Color.BLUE, "CREATING") + " at the same time."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line("You are " + Html.text(Color.RED, "NOT ALLOWED") + " to create an order. Only " + Html.text(Color.BLUE, "Clients") + " are allowed."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean submitClientOrder(Long clientOrderId) {
		final ResultBean result;
		final ClientOrder clientOrder = clientOrderService.find(clientOrderId);
		
		if(clientOrder != null) {
			if(clientOrder.getStatus().equals(Status.CREATING)) {
				if(!clientOrder.getNetTotal().equals(Float.valueOf(0.0f))) {
					clientOrder.setStatus(Status.SUBMITTED);
					
					result = new ResultBean();
					result.setSuccess(clientOrderService.update(clientOrder));
					if(result.getSuccess()) {
						result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " submitted Order of " + Html.text(Color.BLUE, "ID #" + clientOrder.getId()) + "."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Submit Failed.") + " Please submit a non-empty form."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to submit order with status " + Html.text(Color.BLUE, clientOrder.getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load order. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean testAcceptClientOrder(Long clientOrderId, Warehouse warehouse) {
		final ResultBean result;
		final ClientOrder clientOrder = clientOrderService.find(clientOrderId);
		
		if(clientOrder != null && warehouse != null && (clientOrder.getWarehouse() == null || !clientOrder.getWarehouse().equals(warehouse))) {
			List<ClientOrderItem> clientOrderItems = clientOrderItemService.findAllByClientOrder(clientOrderId);
			int itemCount = clientOrderItems.size();
			
			result = new ResultBean();
			result.setSuccess(Boolean.TRUE);
			result.setMessage(Html.line(Color.RED, "Missing Items: "));
			for(ClientOrderItem orderItem : clientOrderItems) {
				WarehouseItem warehouseItem = warehouseItemService.findByProductAndWarehouse(orderItem.getProductId(), warehouse);
				if(warehouseItem == null || warehouseItem.getStockCount() < orderItem.getQuantity()) {
					result.setSuccess(Boolean.FALSE);
					if(warehouseItem == null) result.setMessage(result.getMessage() + Html.line(Color.BLUE, QuantityFormatter.format((orderItem.getQuantity()), orderItem.getPackaging()) + " " + orderItem.getDisplayName()));
					else result.setMessage(result.getMessage() + Html.line(Color.BLUE, QuantityFormatter.format((orderItem.getQuantity() - warehouseItem.getStockCount()), orderItem.getPackaging()) + " " + orderItem.getDisplayName()));
					itemCount--;
				}
			}
			
			if(itemCount == 0) {
				result.setMessage(Html.line(Color.RED, "All items are missing. Adjusting will result to the cancellation of the order."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, "");
		}
		
		return result;
	}

	@Override
	public ResultBean acceptClientOrder(Long clientOrderId, Warehouse warehouse) {
		final ResultBean result;
		final ClientOrder clientOrder = clientOrderService.find(clientOrderId);
		
		if(clientOrder != null) {
			if(clientOrder.getStatus().equals(Status.SUBMITTED) || clientOrder.getStatus().equals(Status.ACCEPTED)) {
				boolean flag = true;
				if(clientOrder.getWarehouse() != null) {
					flag = addToWarehouse(clientOrder, clientOrder.getWarehouse());
				}
				if(flag) {
					result = acceptClientOrder(clientOrder, warehouse);
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load order. Please refresh the page."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to accept order with status " + Html.text(Color.BLUE, clientOrder.getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load order. Please refresh the page."));
		}
		
		return result;
	}

	@Override
	public ResultBean adjustAndAcceptClientOrder(Long clientOrderId, Warehouse warehouse) {
		final ResultBean result;
		final ClientOrder clientOrder = clientOrderService.find(clientOrderId);
		
		if(clientOrder != null) {
			if(clientOrder.getStatus().equals(Status.SUBMITTED) || clientOrder.getStatus().equals(Status.ACCEPTED)) {
				boolean flag = true;
				if(clientOrder.getWarehouse() != null) {
					flag = addToWarehouse(clientOrder, clientOrder.getWarehouse());
				}
				if(flag) {
					adjustClientOrder(clientOrder, warehouse);
					if(clientOrder.getNetTotal() != 0.0f) {
						result = acceptClientOrder(clientOrder, warehouse);
					} else {
						result = removeClientOrder(clientOrder.getId());
					}
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load order. Please refresh the page."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to accept order with status " + Html.text(Color.BLUE, clientOrder.getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load order. Please refresh the page."));
		}
		
		return result;
	}
	
	private ResultBean acceptClientOrder(ClientOrder clientOrder, Warehouse warehouse) {
		final ResultBean result;
		
		if(removeFromWareHouse(clientOrder, warehouse)) {
			clientOrder.setStatus(Status.ACCEPTED);
			clientOrder.setWarehouse(warehouse);
			
			result = new ResultBean();
			result.setSuccess(clientOrderService.update(clientOrder) &&
					EmailUtil.send(clientOrder.getCreator().getEmailAddress(), 
							null,
							MailConstants.DEFAULT_EMAIL,
							"Order Accepted",
							"Thank you " + clientOrder.getCreator().getFormattedName() + "(" + clientOrder.getCreator().getBusinessName() + ") for ordering at Prime Pad."
							+ "This email is to inform you that your order has just been accepted and will be delivered to you as soon as possible.",
							null));
			if(result.getSuccess()) {
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " accepted and removed Order of " + Html.text(Color.BLUE, "ID #" + clientOrder.getId()) + " from stock."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean payClientOrder(Long clientOrderId) {
		final ResultBean result;
		final ClientOrder clientOrder = clientOrderService.find(clientOrderId);
		
		if(clientOrder != null) {
			if(clientOrder.getStatus().equals(Status.RECEIVED)) {
				clientOrder.setStatus(Status.PAID);
				
				result = new ResultBean();
				result.setSuccess(clientOrderService.update(clientOrder));
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " finalized and received payment of Order " + Html.text(Color.BLUE, "ID #" + clientOrder.getId()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to accept payment of order with status " + Html.text(Color.BLUE, clientOrder.getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load order. Please refresh the page."));
		}
		
		return result;
	}

	@Override
	public ResultBean removeClientOrder(Long clientOrderId) {
		final ResultBean result;
		final ClientOrder clientOrder = clientOrderService.find(clientOrderId);
		
		if(clientOrder != null) {
			if(clientOrder.getStatus().equals(Status.CREATING) || clientOrder.getStatus().equals(Status.SUBMITTED) 
					|| (UserContextHolder.getUser().getUserType().getAuthority() <= Integer.valueOf(2))
						&& clientOrder.getStatus().equals(Status.ACCEPTED)) {
				result = new ResultBean();
				
				if(clientOrder.getStatus().equals(Status.ACCEPTED)) {
					addToWarehouse(clientOrder, clientOrder.getWarehouse());
				}
				clientOrder.setStatus(Status.CANCELLED);
				
				result.setSuccess(clientOrderService.delete(clientOrder));
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed Order of " + Html.text(Color.BLUE, "ID #" + clientOrder.getId()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to remove order with status " + Html.text(Color.BLUE, clientOrder.getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load order. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	public List<Warehouse> getWarehouseList() {
		return Stream.of(Warehouse.values())
				.collect(Collectors.toList());
	}
	
	private void adjustClientOrder(ClientOrder clientOrder, Warehouse warehouse) {
		List<ClientOrderItem> clientOrderItems = clientOrderItemService.findAllByClientOrder(clientOrder.getId());
		for(ClientOrderItem clientOrderItem : clientOrderItems) {
			final WarehouseItem warehouseItem = warehouseItemService.findByProductAndWarehouse(clientOrderItem.getProductId(), warehouse);
			if(warehouseItem == null || warehouseItem.getStockCount() <= 0) {
				clientOrderItemService.delete(clientOrderItem);
			} else if(warehouseItem.getStockCount() < clientOrderItem.getQuantity()) {
				clientOrderItem.setQuantity(warehouseItem.getStockCount());
				clientOrderItemService.update(clientOrderItem);
			}
		}
		refreshClientOrder(clientOrder.getId());
	}
	
	private boolean removeFromWareHouse(ClientOrder clientOrder, Warehouse warehouse) {
		List<ClientOrderItem> clientOrderItems = clientOrderItemService.findAllByClientOrder(clientOrder.getId());
		for(ClientOrderItem clientOrderItem : clientOrderItems) {
			final WarehouseItem warehouseItem = warehouseItemService.findByProductAndWarehouse(clientOrderItem.getProductId(), warehouse);
			if(warehouseItem == null) {
				final Product product = productService.find(clientOrderItem.getProductId());
				final WarehouseItem warehouzeItem = new WarehouseItem();
				
				warehouzeItem.setProduct(product);
				warehouzeItem.setWarehouse(warehouse);
				warehouzeItem.setStockCount(0 - clientOrderItem.getQuantity());
				
				if(warehouseItemService.insert(warehouzeItem) == null) return false;
			} else {
				warehouseItem.setStockCount(warehouseItem.getStockCount() - clientOrderItem.getQuantity());
				
				if(!warehouseItemService.update(warehouseItem)) return false;;
			}
		}
		
		return true;
	}
	
	private boolean addToWarehouse(ClientOrder clientOrder, Warehouse warehouse) {
		List<ClientOrderItem> clientOrderItems = clientOrderItemService.findAllByClientOrder(clientOrder.getId());
		for(ClientOrderItem clientOrderItem : clientOrderItems) {
			final WarehouseItem warehouseItem = warehouseItemService.findByProductAndWarehouse(clientOrderItem.getProductId(), warehouse);
			if(warehouseItem == null) {
				final Product product = productService.find(clientOrderItem.getProductId());
				final WarehouseItem warehouzeItem = new WarehouseItem();
				
				warehouzeItem.setProduct(product);
				warehouzeItem.setWarehouse(warehouse);
				warehouzeItem.setStockCount(clientOrderItem.getQuantity());
				
				if(warehouseItemService.insert(warehouzeItem) == null) return false;
			} else {
				warehouseItem.setStockCount(warehouseItem.getStockCount() + clientOrderItem.getQuantity());
				
				if(!warehouseItemService.update(warehouseItem)) return false;;
			}
		}
		
		return true;
	}
	
	private void refreshClientOrder(Long clientOrderId) {
		final ClientOrder clientOrder = clientOrderService.find(clientOrderId);
		
		if(clientOrder != null) {
			Float grossTotal = 0.0f;
			Float discountTotal = 0.0f;
			List<ClientOrderItem> clientOrderItems = clientOrderItemService.findAllByClientOrder(clientOrderId);
			
			for(ClientOrderItem clientOrderItem : clientOrderItems) {
				grossTotal += clientOrderItem.getGrossPrice();
				discountTotal += clientOrderItem.getDiscountAmount();
			}
			
			clientOrder.setGrossTotal(grossTotal);
			clientOrder.setDiscountTotal(discountTotal);
			clientOrderService.update(clientOrder);
		}
	}
}
