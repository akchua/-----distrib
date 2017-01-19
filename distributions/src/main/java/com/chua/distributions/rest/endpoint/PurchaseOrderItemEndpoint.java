package com.chua.distributions.rest.endpoint;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.PurchaseOrderItem;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.PurchaseOrderItemHandler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 7, 2016
 */
@Path("/purchaseorderitem")
public class PurchaseOrderItemEndpoint {

	@Autowired
	private PurchaseOrderItemHandler purchaseOrderItemHandler;
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<PurchaseOrderItem> getPurchaseOrderItemObjectList(@QueryParam("pageNumber") Integer pageNumber, @QueryParam("purchaseOrderId") Long purchaseOrderId) {
		return purchaseOrderItemHandler.getPurchaseOrderItemObjectList(pageNumber, purchaseOrderId);
	}
	
	@GET
	@Path("/listbyproduct")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<PurchaseOrderItem> getPurchaseOrderItemByProductObjectList(@QueryParam("pageNumber") Integer pageNumber,
					@QueryParam("productId") Long productId) {
		return purchaseOrderItemHandler.getPurchaseOrderItemByProductObjectList(pageNumber, productId);
	}
	
	@POST
	@Path("/additem")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean addItem(@FormParam("productId") Long productId, @FormParam("purchaseOrderId") Long purchaseOrderId) {
		return purchaseOrderItemHandler.addItem(productId, purchaseOrderId);
	}
	
	@POST
	@Path("/removeitem")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeItem(@FormParam("purchaseOrderItemId") Long purchaseOrderItemId) {
		return purchaseOrderItemHandler.removeItem(purchaseOrderItemId);
	}
	
	@POST
	@Path("/changepiece")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean changePieceQuantity(@FormParam("purchaseOrderItemId") Long purchaseOrderItemId, @FormParam("pieceQuantity") Integer pieceQuantity) {
		return purchaseOrderItemHandler.changePieceQuantity(purchaseOrderItemId, pieceQuantity);
	}
	
	@POST
	@Path("/changepackage")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean changePackageQuantity(@FormParam("purchaseOrderItemId") Long purchaseOrderItemId, @FormParam("packageQuantity") Integer packageQuantity) {
		return purchaseOrderItemHandler.changePackageQuantity(purchaseOrderItemId, packageQuantity);
	}
	
	@POST
	@Path("/transferpiece")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean transferPiece(@FormParam("purchaseOrderItemId") Long purchaseOrderItemId, @FormParam("destinationOrderId") Long destinationOrderId) {
		return purchaseOrderItemHandler.transferPiece(purchaseOrderItemId, destinationOrderId);
	}
	
	@POST
	@Path("/transferpackage")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean transferPackage(@FormParam("purchaseOrderItemId") Long purchaseOrderItemId, @FormParam("destinationOrderId") Long destinationOrderId) {
		return purchaseOrderItemHandler.transferPackage(purchaseOrderItemId, destinationOrderId);
	}
	
	@POST
	@Path("/transferall")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean transferAll(@FormParam("purchaseOrderItemId") Long purchaseOrderItemId, @FormParam("destinationOrderId") Long destinationOrderId) {
		return purchaseOrderItemHandler.transferAll(purchaseOrderItemId, destinationOrderId);
	}
}
