package com.chua.distributions.rest.handler.impl;

import java.util.Date;
import java.util.List;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.annotations.CheckAuthority;
import com.chua.distributions.beans.DispatchFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.constants.BusinessConstants;
import com.chua.distributions.constants.FileConstants;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.database.entity.ClientOrderItem;
import com.chua.distributions.database.entity.Dispatch;
import com.chua.distributions.database.entity.DispatchItem;
import com.chua.distributions.database.service.ClientOrderItemService;
import com.chua.distributions.database.service.ClientOrderService;
import com.chua.distributions.database.service.DispatchItemService;
import com.chua.distributions.database.service.DispatchService;
import com.chua.distributions.database.service.WarehouseService;
import com.chua.distributions.enums.Color;
import com.chua.distributions.enums.Status;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.DispatchHandler;
import com.chua.distributions.rest.handler.UserHandler;
import com.chua.distributions.rest.handler.WarehouseItemHandler;
import com.chua.distributions.utility.DateUtil;
import com.chua.distributions.utility.EmailUtil;
import com.chua.distributions.utility.Html;
import com.chua.distributions.utility.SimplePdfWriter;
import com.chua.distributions.utility.template.ClientOrderTemplate;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 14, 2016
 */
@Transactional
@Component
public class DispatchHandlerImpl implements DispatchHandler {

	@Autowired
	private DispatchService dispatchService;
	
	@Autowired
	private DispatchItemService dispatchItemService;
	
	@Autowired
	private ClientOrderService clientOrderService;
	
	@Autowired
	private ClientOrderItemService clientOrderItemService;
	
	@Autowired
	private WarehouseService warehouseService;

	@Autowired
	private WarehouseItemHandler warehouseItemHandler;
	
	@Autowired
	private UserHandler userHandler;
	
	@Autowired
	private EmailUtil emailUtil;
	
	@Autowired
	private FileConstants fileConstants;
	
	@Autowired
	private BusinessConstants businessConstants;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public Dispatch getDispatch(Long dispatchId) {
		refreshDispatch(dispatchId);
		return dispatchService.find(dispatchId);
	}

	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ObjectList<Dispatch> getDispatchObjectList(Integer pageNumber, Boolean showReceived) {
		return dispatchService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), showReceived);
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ObjectList<DispatchItem> getDispatchItemObjectList(Integer pageNumber, Long dispatchId) {
		return dispatchItemService.findAllWithPagingOrderByLastUpdate(pageNumber, UserContextHolder.getItemsPerPage(), dispatchId);
	}

	@Override
	@CheckAuthority(minimumAuthority = 4)
	public ResultBean createDispatch(DispatchFormBean dispatchForm) {
		final ResultBean result;
		final ResultBean validateForm = validateDispatchForm(dispatchForm);
		
		if(validateForm.getSuccess()) {
			final Dispatch dispatch = new Dispatch();
			setDispatch(dispatch, dispatchForm);
			dispatch.setStatus(Status.CREATING);
			dispatch.setDeliveredOn(DateUtil.getDefaultDate());
			
			result = new ResultBean();
			result.setSuccess(dispatchService.insert(dispatch) != null);
			if(result.getSuccess()) {
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created dispatch."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = validateForm;
		}
		
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 4)
	public ResultBean dispatch(Long dispatchId) {
		final ResultBean result;
		final Dispatch dispatch = dispatchService.find(dispatchId);
		
		if(dispatch != null) {
			if(dispatch.getStatus().equals(Status.CREATING)) {
				final List<DispatchItem> dispatchItems = dispatchItemService.findAllByDispatch(dispatchId);
				
				if(dispatchItems != null && dispatchItems.size() > 0) {
					boolean flag = true;
					String printableDispatch = "";
					
					for(DispatchItem dispatchItem : dispatchItems) {
						final ClientOrder clientOrder = dispatchItem.getClientOrder();
						clientOrder.setStatus(Status.DISPATCHED);
						
						// GENERATING PRINTABLE CLIENT ORDER
						final List<ClientOrderItem> clientOrderItems = clientOrderItemService.findAllByClientOrder(clientOrder.getId());
						printableDispatch += new ClientOrderTemplate(clientOrder, clientOrderItems, businessConstants)
												.merge(velocityEngine) + "\n";
						//
						
						if(!clientOrderService.update(clientOrder)) {
							flag = false;
							break;
						}
					}
					
					if(flag) {
						// CREATING PRINTABLE DISPATCH FILE
						final String filePath = fileConstants.getDispatchHome() + "Dispatch_#" + dispatch.getId() + ".pdf";
						SimplePdfWriter.write(printableDispatch, businessConstants.getBusinessShortName(), filePath, true);
						//
						
						// SENDING PRINTABLE DISPATCH TO THIS USER
						flag = emailUtil.send(UserContextHolder.getUser().getEmailAddress(), 
								null,
								userHandler.getEmailOfAllAdminAndManagers(),
								"Dispatch",
								"Dispatch #" + dispatch.getId(),
								new String[] { filePath });
						//
					}
					
					dispatch.setStatus(Status.DISPATCHED);
					result = new ResultBean();
					result.setSuccess(flag && dispatchService.update(dispatch));
					if(result.getSuccess()) {
						result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " dispatched " + Html.text(Color.BLUE, "ID #" + dispatch.getId()) + ".")
								+ Html.line("A printable copy of this dispatch has been sent to your email " + Html.text(Color.BLUE, UserContextHolder.getUser().getEmailAddress()) + "."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Dispatch Failed.") + " Please submit a non-empty dispatch form."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to send dispatch with status " + Html.text(Color.BLUE, dispatch.getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load dispatch. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 4)
	public ResultBean completeDispatch(Long dispatchId) {
		final ResultBean result;
		final Dispatch dispatch = dispatchService.find(dispatchId);
		
		if(dispatch != null) {
			if(dispatch.getStatus().equals(Status.DISPATCHED)) {
				final List<DispatchItem> dispatchItems = dispatchItemService.findAllByDispatch(dispatchId);
				
				boolean flag = true;
				
				for(DispatchItem dispatchItem : dispatchItems) {
					final ClientOrder clientOrder = dispatchItem.getClientOrder();
					flag = removeFromWareHouse(clientOrder);
					clientOrder.setStatus(Status.RECEIVED);
					clientOrder.setDispatcher(UserContextHolder.getUser().getUserEntity());
					clientOrder.setDeliveredOn(new Date());
					
					if(!clientOrderService.update(clientOrder)) {
						flag = false;
						break;
					}
				}
				
				dispatch.setStatus(Status.RECEIVED);
				dispatch.setDeliveredOn(new Date());
				
				result = new ResultBean();
				result.setSuccess(flag && dispatchService.update(dispatch));
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " finalized dispatch of " + Html.text(Color.BLUE, "ID #" + dispatch.getId()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to complete dispatch with status " + Html.text(Color.BLUE, dispatch.getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load dispatch. Please refresh the page."));
		}		
		
		return result;
	}

	@Override
	@CheckAuthority(minimumAuthority = 4)
	public ResultBean removeDispatch(Long dispatchId) {
		final ResultBean result;
		final Dispatch dispatch = dispatchService.find(dispatchId);
		
		if(dispatch != null) {
			if(dispatch.getStatus().equals(Status.CREATING)
					|| (UserContextHolder.getUser().getUserType().getAuthority() <= Integer.valueOf(2)
						&& !dispatch.getStatus().equals(Status.RECEIVED)
						&& !dispatch.getStatus().equals(Status.CANCELLED))) {
				// REVERT STATUS OF DISPATCHED ORDERS
				final List<DispatchItem> dispatchItems = dispatchItemService.findAllByDispatch(dispatchId);
				for(DispatchItem dispatchItem : dispatchItems) {
					final ClientOrder clientOrder = dispatchItem.getClientOrder();
					clientOrder.setWarehouse(null);
					clientOrder.setStatus(Status.ACCEPTED);
					clientOrderService.update(clientOrder);
				}
				//
				
				dispatch.setStatus(Status.CANCELLED);
				result = new ResultBean();
				result.setSuccess(dispatchService.delete(dispatch));
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed Dispatch of " + Html.text(Color.BLUE, "ID #" + dispatch.getId()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to remove dispatch with status " + Html.text(Color.BLUE, dispatch.getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load dispatch. Please refresh the page."));
		}
		
		return result;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 4)
	public ResultBean addItem(Long clientOrderId, Long dispatchId) {
		final ResultBean result;
		final ClientOrder clientOrder = clientOrderService.find(clientOrderId);
		
		if(clientOrder != null) {
			if(clientOrder.getStatus().equals(Status.ACCEPTED) || clientOrder.getStatus().equals(Status.TO_FOLLOW)) {
				if(!clientOrder.getNetTotal().equals(0.0f)) {
					final Dispatch dispatch = dispatchService.find(dispatchId);
					
					if(dispatch != null) {
						if(dispatch.getStatus().equals(Status.CREATING) || 
								(UserContextHolder.getUser().getUserType().getAuthority() <= 2 &&
										!dispatch.getStatus().equals(Status.RECEIVED) &&
										!dispatch.getStatus().equals(Status.CANCELLED))) {
							final DispatchItem dispatchItem = dispatchItemService.findByDispatchAndOrder(dispatchId, clientOrderId);
							
							if(dispatchItem == null) {
								final DispatchItem dispatchItemm = new DispatchItem();
								dispatchItemm.setClientOrder(clientOrder);
								dispatchItemm.setDispatch(dispatch);
								clientOrder.setWarehouse(dispatch.getWarehouse());
								clientOrder.setStatus(Status.DISPATCHED);
								
								result = new ResultBean();
								result.setSuccess(dispatchItemService.insert(dispatchItemm) != null &&
										clientOrderService.update(clientOrder));
								if(result.getSuccess()) {
									result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " added Order " + Html.text(Color.BLUE, "ID #" + clientOrder.getId()) + "."));
								} else {
									result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
								}
							} else {
								result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Client Order already in this dispatch.") + " Please refresh the page."));
							}
						} else {
							result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
									Html.line(" You are not authorized to add to dispatch with status " + Html.text(Color.BLUE, dispatch.getStatus().getDisplayName()) + "."));
						}
					} else {
						result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load dispatch. Please refresh the page."));
					}
				} else {
					result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Selected Order is empty."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to add client order with status " + Html.text(Color.BLUE, clientOrder.getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load client order. Please refresh the page."));
		}
		
		return result;
	}

	@Override
	@CheckAuthority(minimumAuthority = 4)
	public ResultBean removeItem(Long dispatchItemId) {
		final ResultBean result;
		final DispatchItem dispatchItem = dispatchItemService.find(dispatchItemId);
		
		if(dispatchItem != null) {
			if(dispatchItem.getDispatch().getStatus().equals(Status.CREATING)
					|| (UserContextHolder.getUser().getUserType().getAuthority() <= Integer.valueOf(2)
						&& !dispatchItem.getDispatch().getStatus().equals(Status.RECEIVED)
						&& !dispatchItem.getDispatch().getStatus().equals(Status.CANCELLED))) {
				final ClientOrder clientOrder = dispatchItem.getClientOrder();
				clientOrder.setWarehouse(null);
				clientOrder.setStatus(Status.ACCEPTED);
				
				result = new ResultBean();
				result.setSuccess(clientOrderService.update(clientOrder) && dispatchItemService.delete(dispatchItem));
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed Order of " + Html.text(Color.BLUE, "ID #" + clientOrder.getId()) + " from dispatch of " + Html.text(Color.BLUE, "ID #" + dispatchItem.getDispatch().getId()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			} else {
				result = new ResultBean(Boolean.FALSE, Html.line(Color.RED, "Request Denied!") +
						Html.line(" You are not authorized to remove order from dispatch with status " + Html.text(Color.BLUE, dispatchItem.getDispatch().getStatus().getDisplayName()) + "."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load dispatch item. Please refresh the page."));
		}
		
		return result;
	}
	
	private boolean removeFromWareHouse(ClientOrder clientOrder) {
		List<ClientOrderItem> clientOrderItems = clientOrderItemService.findAllByClientOrder(clientOrder.getId());
		
		for(ClientOrderItem clientOrderItem : clientOrderItems) {
			if(!warehouseItemHandler.removeFromWarehouse(clientOrderItem.getProductId(), clientOrder.getWarehouse().getId(), clientOrderItem.getQuantity())) 
				return false;
		}
		
		return true;
	}
	
	private void refreshDispatch(Long dispatchId) {
		final Dispatch dispatch = dispatchService.find(dispatchId);
		
		if(dispatch != null) {
			Float totalAmount = 0.0f;
			final List<DispatchItem> dispatchItems = dispatchItemService.findAllByDispatch(dispatchId);
			
			for(DispatchItem dispatchItem : dispatchItems) {
				totalAmount += dispatchItem.getClientOrder().getNetTotal();
			}
			
			dispatch.setOrderCount(dispatchItems.size());
			dispatch.setTotalAmount(totalAmount);
			dispatchService.update(dispatch);
		}
	}
	
	private void setDispatch(Dispatch dispatch, DispatchFormBean dispatchForm) {
		dispatch.setWarehouse(warehouseService.find(dispatchForm.getWarehouseId()));
		if(dispatch.getOrderCount() == null) dispatch.setOrderCount(Integer.valueOf(0));
		if(dispatch.getTotalAmount() == null) dispatch.setTotalAmount(0.0f);
	}
	
	private ResultBean validateDispatchForm(DispatchFormBean dispatchForm) {
		final ResultBean result;
		
		if(dispatchForm.getWarehouseId() == null) {
			result = new ResultBean(Boolean.FALSE, Html.line("All fields are " + Html.text(Color.RED, "required") + "."));
		} else {
			result = new ResultBean(Boolean.TRUE, "");
		}
		
		return result;
	}
}
