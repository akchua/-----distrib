package com.chua.distributions.rest.handler.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.beans.PurchaseOrderFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.PurchaseOrder;
import com.chua.distributions.database.service.CompanyService;
import com.chua.distributions.database.service.PurchaseOrderService;
import com.chua.distributions.enums.Area;
import com.chua.distributions.enums.Color;
import com.chua.distributions.enums.Status;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.PurchaseOrderHandler;
import com.chua.distributions.utility.Html;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 6, 2016
 */
@Transactional
@Component
public class PurchaseOrderHandlerImpl implements PurchaseOrderHandler {

	@Autowired
	private PurchaseOrderService purchaseOrderService;
	
	@Autowired
	private CompanyService companyService;

	@Override
	public PurchaseOrder getPurchaseOrder(Long purchaseOrderId) {
		return purchaseOrderService.find(purchaseOrderId);
	}

	@Override
	public ObjectList<PurchaseOrder> getPurchaseOrderObjectList(Integer pageNumber, Long companyId, Area area) {
		return purchaseOrderService.findAllWithPaging(pageNumber, UserContextHolder.getItemsPerPage(), companyId, area);
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
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created PurchaseOrder of " + Html.text(Color.BLUE, "ID #" + purchaseOrder.getId()) + "."));
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
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load purchase order. Please refresh the page."));
		}
		
		return result;
	}

	@Override
	public ResultBean removePurchaseOrder(Long purchaseOrderId) {
		final ResultBean result;
		final PurchaseOrder purchaseOrder = purchaseOrderService.find(purchaseOrderId);
		
		if(purchaseOrder != null) {
			result = new ResultBean();
			
			result.setSuccess(purchaseOrderService.delete(purchaseOrder));
			if(result.getSuccess()) {
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed Purchase Order of " + Html.text(Color.BLUE, "ID #" + purchaseOrder.getId()) + "."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load purchase order. Please refresh the page."));
		}
		
		return result;
	}

	@Override
	public List<Area> getAreaList() {
		return Stream.of(Area.values())
				.collect(Collectors.toList());
	}
	
	private void setPurchaseOrder(PurchaseOrder purchaseOrder, PurchaseOrderFormBean purchaseOrderForm) {
		purchaseOrder.setArea(purchaseOrderForm.getArea());
		purchaseOrder.setCompany(companyService.find(purchaseOrderForm.getCompanyId()));
		if(purchaseOrder.getGrossTotal() == null) purchaseOrder.setGrossTotal(0.0f);
		if(purchaseOrder.getDiscountTotal() == null) purchaseOrder.setDiscountTotal(0.0f);
		if(purchaseOrder.getNetTotal() == null) purchaseOrder.setNetTotal(0.0f);
	}
	
	private ResultBean validatePurchaseOrderForm(PurchaseOrderFormBean purchaseOrderForm) {
		final ResultBean result;
		
		if(purchaseOrderForm.getCompanyId() == null || purchaseOrderForm.getArea() == null) {
			result = new ResultBean(Boolean.FALSE, Html.line("All fields are " + Html.text(Color.RED, "required") + "."));
		} else {
			result = new ResultBean(Boolean.TRUE, "");
		}
		
		return result;
	}
}
