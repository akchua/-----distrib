package com.chua.distributions.rest.handler.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.beans.PurchaseOrderFormBean;
import com.chua.distributions.beans.PurchaseReportQueryBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.constants.FileConstants;
import com.chua.distributions.constants.MailConstants;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.database.entity.PurchaseOrder;
import com.chua.distributions.database.entity.PurchaseOrderItem;
import com.chua.distributions.database.entity.WarehouseItem;
import com.chua.distributions.database.service.CompanyService;
import com.chua.distributions.database.service.ProductService;
import com.chua.distributions.database.service.PurchaseOrderItemService;
import com.chua.distributions.database.service.PurchaseOrderService;
import com.chua.distributions.database.service.WarehouseItemService;
import com.chua.distributions.enums.Color;
import com.chua.distributions.enums.Status;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.PurchaseOrderHandler;
import com.chua.distributions.utility.EmailUtil;
import com.chua.distributions.utility.Html;
import com.chua.distributions.utility.SimplePdfWriter;
import com.chua.distributions.utility.format.PurchaseOrderFormatter;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 6, 2016
 */
@Transactional
@Component
public class PurchaseOrderHandlerImpl implements PurchaseOrderHandler {

	@Autowired
	private WarehouseItemService warehouseItemService;
	
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@Autowired
	private PurchaseOrderItemService purchaseOrderItemService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private PurchaseOrderFormatter purchaseOrderFormatter;

	@Override
	public PurchaseOrder getPurchaseOrder(Long purchaseOrderId) {
		refreshPurchaseOrder(purchaseOrderId);
		return purchaseOrderService.find(purchaseOrderId);
	}
	
	@Override
	public PurchaseOrder getTransferInstance(Long sourceId) {
		final PurchaseOrder sourceOrder = purchaseOrderService.find(sourceId);
		PurchaseOrder transferInstance = null;
		
		if(!sourceOrder.getNetTotal().equals(0.0f)) {
			final List<PurchaseOrder> toFollowOrders = purchaseOrderService.findAllToFollowByCompanyAndWarehouse(sourceOrder.getCompany().getId(), sourceOrder.getWarehouse());
			
			for(PurchaseOrder purchaseOrder : toFollowOrders) {
				if(purchaseOrder.getNetTotal().equals(0.0f) && !purchaseOrder.getId().equals(sourceId)) {
					transferInstance = purchaseOrder;
					break;
				}
			}
			
			if(transferInstance == null) {
				final PurchaseOrder newPurchaseOrder = new PurchaseOrder();
				
				newPurchaseOrder.setCreator(UserContextHolder.getUser().getUserEntity());
				newPurchaseOrder.setStatus(Status.TO_FOLLOW);
				newPurchaseOrder.setWarehouse(sourceOrder.getWarehouse());
				newPurchaseOrder.setCompany(sourceOrder.getCompany());
				newPurchaseOrder.setGrossTotal(0.0f);
				newPurchaseOrder.setDiscountTotal(0.0f);
				
				purchaseOrderService.insert(newPurchaseOrder);
				transferInstance = newPurchaseOrder;
			}
		} else {
			transferInstance = sourceOrder;
		}
		
		return transferInstance;
	}

	@Override
	public ObjectList<PurchaseOrder> getPurchaseOrderObjectList(Integer pageNumber, Long companyId, Warehouse warehouse, Boolean showPaid) {
		return purchaseOrderService.findAllWithPagingOrderByStatus(pageNumber, UserContextHolder.getItemsPerPage(), companyId, warehouse, showPaid);
	}
	
	@Override
	public ObjectList<PurchaseOrder> getPaidPurchaseOrderObjectList(Integer pageNumber, Long companyId,
			Warehouse warehouse) {
		return purchaseOrderService.findAllPaidWithPagingOrderByLatest(pageNumber, UserContextHolder.getItemsPerPage(), companyId, warehouse);
	}
	
	@Override
	public ObjectList<PurchaseOrder> getPurchaseOrderObjectListByPurchaseReportQuery(Integer pageNumber,
			PurchaseReportQueryBean purchaseReportQuery) {
		return purchaseOrderService.findByPurchaseReportQueryWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), purchaseReportQuery);
	}

	@Override
	public ResultBean createPurchaseOrder(PurchaseOrderFormBean purchaseOrderForm) {
		final ResultBean result;
		final ResultBean validateForm = validatePurchaseOrderForm(purchaseOrderForm);
		
		if(validateForm.getSuccess()) {
			final PurchaseOrder purchaseOrder = new PurchaseOrder();
			
			purchaseOrder.setCreator(UserContextHolder.getUser().getUserEntity());
			purchaseOrder.setStatus(Status.CREATING);
			setPurchaseOrder(purchaseOrder, purchaseOrderForm);
			
			result = new ResultBean();
			result.setSuccess(purchaseOrderService.insert(purchaseOrder) != null);
			if(result.getSuccess()) {
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created Purchase Order of " + Html.text(Color.BLUE, "ID #" + purchaseOrder.getId()) + "."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = validateForm;
		}
		
		return result;
	}

	@Override
	public ResultBean updatePurchaseOrder(PurchaseOrderFormBean purchaseOrderForm) {
		final ResultBean result;
		final PurchaseOrder purchaseOrder = purchaseOrderService.find(purchaseOrderForm.getId());
		
		if(purchaseOrder != null) {
			if(purchaseOrder.getStatus().equals(Status.CREATING)) {
				final ResultBean validateForm = validatePurchaseOrderForm(purchaseOrderForm);
				if(validateForm.getSuccess()) {
					setPurchaseOrder(purchaseOrder, purchaseOrderForm);
					
					result = new ResultBean();
					result.setSuccess(purchaseOrderService.update(purchaseOrder));
					if(result.getSuccess()) {
						result.setMessage(Html.line("Purchase Order of " + Html.text(Color.BLUE, "ID #" + purchaseOrder.getId()) + " has been successfully " + Html.text(Color.GREEN, "updated") + "."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				} else {
					result = validateForm;
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to change purchase order with status " + Html.text(Color.BLUE, purchaseOrder.getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load purchase order. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean submitPurchaseOrder(Long purchaseOrderId) {
		final ResultBean result;
		final PurchaseOrder purchaseOrder = purchaseOrderService.find(purchaseOrderId);
		
		if(purchaseOrder != null) {
			if(purchaseOrder.getStatus().equals(Status.CREATING)) {
				if(!purchaseOrder.getNetTotal().equals(Float.valueOf(0.0f))) {
					purchaseOrder.setStatus(Status.SUBMITTED);
					
					result = new ResultBean();
					result.setSuccess(purchaseOrderService.update(purchaseOrder));
					if(result.getSuccess()) {
						result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " submitted Purchase Order of " + Html.text(Color.BLUE, "ID #" + purchaseOrder.getId()) + "."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Submit Failed.") + " Please submit a non-empty form."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to submit purchase order with status " + Html.text(Color.BLUE, purchaseOrder.getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load purchase order. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean sendPurchaseOrder(Long purchaseOrderId) {
		final ResultBean result;
		final PurchaseOrder purchaseOrder = purchaseOrderService.find(purchaseOrderId);
		
		if(purchaseOrder != null) {
			if(purchaseOrder.getStatus().equals(Status.SUBMITTED)) {
				if(!purchaseOrder.getNetTotal().equals(0.0f)) {
					final String filePath = FileConstants.FILE_HOME + "files/purchase_order/PurchaseOrder_#" + purchaseOrder.getId() + ".pdf";
					SimplePdfWriter.write(purchaseOrderFormatter.format(purchaseOrder, purchaseOrderItemService.findAllByPurchaseOrder(purchaseOrder.getId())), filePath, true);
					boolean flag = EmailUtil.send(purchaseOrder.getCompany().getEmailAddress(), 
							null,
							MailConstants.DEFAULT_EMAIL,
							"Purchase Order",
							"Purchase Order #" + purchaseOrder.getId(),
							new String[] { filePath });
					
					purchaseOrder.setStatus(Status.ACCEPTED);
					
					result = new ResultBean();
					result.setSuccess(flag && purchaseOrderService.update(purchaseOrder));
					if(result.getSuccess()) {
						result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " sent email of Purchase Order with " + Html.text(Color.BLUE, "ID #" + purchaseOrder.getId()) + " to " + Html.text(Color.TURQUOISE, purchaseOrder.getCompany().getEmailAddress()) + "."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Send Failed.") + " Please send a non-empty form."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to send purchase order with status " + Html.text(Color.BLUE, purchaseOrder.getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load purchase order. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean receivePurchaseOrder(Long purchaseOrderId) {
		final ResultBean result;
		final PurchaseOrder purchaseOrder = purchaseOrderService.find(purchaseOrderId);
		
		if(purchaseOrder != null) {
			if(purchaseOrder.getStatus().equals(Status.ACCEPTED) || purchaseOrder.getStatus().equals(Status.TO_FOLLOW)) {
				if(addToWarehouse(purchaseOrder)) {
					purchaseOrder.setStatus(Status.RECEIVED);
					
					result = new ResultBean();
					result.setSuccess(purchaseOrderService.update(purchaseOrder));
					if(result.getSuccess()) {
						result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " added Purchase Order of " + Html.text(Color.BLUE, "ID #" + purchaseOrder.getId()) + " to stock."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to receive purchase order with status " + Html.text(Color.BLUE, purchaseOrder.getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load purchase order. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	public ResultBean payPurchaseOrder(Long purchaseOrderId) {
		final ResultBean result;
		final PurchaseOrder purchaseOrder = purchaseOrderService.find(purchaseOrderId);
		
		if(purchaseOrder != null) {
			if(purchaseOrder.getStatus().equals(Status.RECEIVED)) {
				purchaseOrder.setStatus(Status.PAID);
				
				result = new ResultBean();
				result.setSuccess(purchaseOrderService.update(purchaseOrder));
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " marked Purchase Order of " + Html.text(Color.BLUE, "ID #" + purchaseOrder.getId()) + " as paid."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to pay purchase order with status " + Html.text(Color.BLUE, purchaseOrder.getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load purchase order. Please refresh the page."));
		}
		
		return result;
	}

	@Override
	public ResultBean removePurchaseOrder(Long purchaseOrderId) {
		final ResultBean result;
		final PurchaseOrder purchaseOrder = purchaseOrderService.find(purchaseOrderId);
		
		if(purchaseOrder != null) {
			if(purchaseOrder.getStatus().equals(Status.CREATING) || purchaseOrder.getStatus().equals(Status.SUBMITTED)
					|| (UserContextHolder.getUser().getUserType().getAuthority() <= Integer.valueOf(2))
						&& !purchaseOrder.getStatus().equals(Status.RECEIVED)
						&& !purchaseOrder.getStatus().equals(Status.PAID)
						&& !purchaseOrder.getStatus().equals(Status.CANCELLED)) {
				result = new ResultBean();
				
				purchaseOrder.setStatus(Status.CANCELLED);
				
				result.setSuccess(purchaseOrderService.delete(purchaseOrder));
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed Purchase Order of " + Html.text(Color.BLUE, "ID #" + purchaseOrder.getId()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to remove purchase order with status " + Html.text(Color.BLUE, purchaseOrder.getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load purchase order. Please refresh the page."));
		}
		
		return result;
	}

	@Override
	public List<Warehouse> getWarehouseList() {
		return Stream.of(Warehouse.values())
				.collect(Collectors.toList());
	}
	
	private void refreshPurchaseOrder(Long purchaseOrderId) {
		final PurchaseOrder purchaseOrder = purchaseOrderService.find(purchaseOrderId);
		
		if(purchaseOrder != null) {
			Float grossTotal = 0.0f;
			Float discountTotal = 0.0f;
			List<PurchaseOrderItem> purchaseOrderItems = purchaseOrderItemService.findAllByPurchaseOrder(purchaseOrderId);
			
			for(PurchaseOrderItem purchaseOrderItem : purchaseOrderItems) {
				grossTotal += purchaseOrderItem.getGrossPrice();
				discountTotal += purchaseOrderItem.getDiscountAmount();
			}
			
			purchaseOrder.setGrossTotal(grossTotal);
			purchaseOrder.setDiscountTotal(discountTotal);
			purchaseOrderService.update(purchaseOrder);
		}
	}
	
	private boolean addToWarehouse(PurchaseOrder purchaseOrder) {
		List<PurchaseOrderItem> purchaseOrderItems = purchaseOrderItemService.findAllByPurchaseOrder(purchaseOrder.getId());
		for(PurchaseOrderItem purchaseOrderItem : purchaseOrderItems) {
			final WarehouseItem warehouseItem = warehouseItemService.findByProductAndWarehouse(purchaseOrderItem.getProductId(), purchaseOrder.getWarehouse());
			if(warehouseItem == null) {
				final Product product = productService.find(purchaseOrderItem.getProductId());
				final WarehouseItem warehouzeItem = new WarehouseItem();
				
				warehouzeItem.setProduct(product);
				warehouzeItem.setWarehouse(purchaseOrder.getWarehouse());
				warehouzeItem.setStockCount(purchaseOrderItem.getQuantity());
				
				if(warehouseItemService.insert(warehouzeItem) == null) return false;
			} else {
				warehouseItem.setStockCount(warehouseItem.getStockCount() + purchaseOrderItem.getQuantity());
				
				if(!warehouseItemService.update(warehouseItem)) return false;;
			}
		}
		
		return true;
	}
	
	private void setPurchaseOrder(PurchaseOrder purchaseOrder, PurchaseOrderFormBean purchaseOrderForm) {
		purchaseOrder.setWarehouse(purchaseOrderForm.getWarehouse());
		purchaseOrder.setCompany(companyService.find(purchaseOrderForm.getCompanyId()));
		if(purchaseOrder.getGrossTotal() == null) purchaseOrder.setGrossTotal(0.0f);
		if(purchaseOrder.getDiscountTotal() == null) purchaseOrder.setDiscountTotal(0.0f);
	}
	
	private ResultBean validatePurchaseOrderForm(PurchaseOrderFormBean purchaseOrderForm) {
		final ResultBean result;
		
		if(purchaseOrderForm.getCompanyId() == null || purchaseOrderForm.getWarehouse() == null) {
			result = new ResultBean(Boolean.FALSE, Html.line("All fields are " + Html.text(Color.RED, "required") + "."));
		} else {
			result = new ResultBean(Boolean.TRUE, "");
		}
		
		return result;
	}
}
