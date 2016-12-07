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
import com.chua.distributions.enums.Area;
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
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<PurchaseOrder> getPurchaseOrderObjectList(@QueryParam("pageNumber") Integer pageNumber,
					@QueryParam("companyId") Long companyId,
					@QueryParam("area") Area area) {
		return purchaseOrderHandler.getPurchaseOrderObjectList(pageNumber, companyId, area);
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
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removePurchaseOrder(@FormParam("purchaseOrderId") Long purchaseOrderId) {
		return purchaseOrderHandler.removePurchaseOrder(purchaseOrderId);
	}
	
	@GET
	@Path("/area")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Area> getAreaList() {
		return purchaseOrderHandler.getAreaList();
	}
}
