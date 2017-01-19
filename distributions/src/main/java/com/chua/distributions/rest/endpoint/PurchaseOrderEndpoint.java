package com.chua.distributions.rest.endpoint;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.distributions.beans.PurchaseOrderFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.PurchaseOrder;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.PurchaseOrderHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 6, 2016
 */
@Path("/purchaseorder")
public class PurchaseOrderEndpoint {

	@Autowired
	private PurchaseOrderHandler purchaseOrderHandler;
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public PurchaseOrder getPurchaseOrder(@QueryParam("purchaseOrderId") Long purchaseOrderId) {
		return purchaseOrderHandler.getPurchaseOrder(purchaseOrderId);
	}
	
	@GET
	@Path("/gettransfer")
	@Produces({ MediaType.APPLICATION_JSON })
	public PurchaseOrder getTransferInstance(@QueryParam("sourceId") Long sourceId) {
		return purchaseOrderHandler.getTransferInstance(sourceId);
	}
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<PurchaseOrder> getPurchaseOrderObjectList(@QueryParam("pageNumber") Integer pageNumber,
					@QueryParam("companyId") Long companyId,
					@QueryParam("warehouse") Warehouse warehouse,
					@QueryParam("showPaid") Boolean showPaid) {
		return purchaseOrderHandler.getPurchaseOrderObjectList(pageNumber, companyId, warehouse, showPaid);
	}
	
	@GET
	@Path("/paidlist")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<PurchaseOrder> getPaidPurchaseOrderObjectList(@QueryParam("pageNumber") Integer pageNumber,
					@QueryParam("companyId") Long companyId,
					@QueryParam("warehouse") Warehouse warehouse) {
		return purchaseOrderHandler.getPaidPurchaseOrderObjectList(pageNumber, companyId, warehouse);
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean savePurchaseOrder(@FormParam("purchaseOrderFormData") String purchaseOrderFormData) throws IOException {
		final ResultBean result;

		final PurchaseOrderFormBean purchaseOrderForm = new ObjectMapper().readValue(purchaseOrderFormData, PurchaseOrderFormBean.class);
		if(purchaseOrderForm.getId() != null) {
			result = purchaseOrderHandler.updatePurchaseOrder(purchaseOrderForm);
		} else {
			result = purchaseOrderHandler.createPurchaseOrder(purchaseOrderForm);
		}
		
		return result;
	}
	
	@POST
	@Path("/submit")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean submitPurchaseOrder(@FormParam("purchaseOrderId") Long purchaseOrderId) {
		return purchaseOrderHandler.submitPurchaseOrder(purchaseOrderId);
	}
	
	@POST
	@Path("/send")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean sendPurchaseOrder(@FormParam("purchaseOrderId") Long purchaseOrderId) {
		return purchaseOrderHandler.sendPurchaseOrder(purchaseOrderId);
	}
	
	@POST
	@Path("/receive")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean receivePurchaseOrder(@FormParam("purchaseOrderId") Long purchaseOrderId) {
		return purchaseOrderHandler.receivePurchaseOrder(purchaseOrderId);
	}
	
	@POST
	@Path("/pay")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean payPurchaseOrder(@FormParam("purchaseOrderId") Long purchaseOrderId) {
		return purchaseOrderHandler.payPurchaseOrder(purchaseOrderId);
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removePurchaseOrder(@FormParam("purchaseOrderId") Long purchaseOrderId) {
		return purchaseOrderHandler.removePurchaseOrder(purchaseOrderId);
	}
	
	@GET
	@Path("/warehouse")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Warehouse> getWarehouseList() {
		return purchaseOrderHandler.getWarehouseList();
	}
}
