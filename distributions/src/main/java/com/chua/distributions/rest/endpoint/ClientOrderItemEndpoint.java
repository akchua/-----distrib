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
import com.chua.distributions.database.entity.ClientOrderItem;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.ClientOrderItemHandler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
@Path("/clientorderitem")
public class ClientOrderItemEndpoint {

	@Autowired
	private ClientOrderItemHandler clientOrderItemHandler;
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<ClientOrderItem> getClientOrderItemObjectList(@QueryParam("pageNumber") Integer pageNumber, @QueryParam("clientOrderId") Long clientOrderId) {
		return clientOrderItemHandler.getClientOrderItemObjectList(pageNumber, clientOrderId);
	}
	
	@GET
	@Path("/listbyproduct")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<ClientOrderItem> getClientOrderItemByProductObjectList(@QueryParam("pageNumber") Integer pageNumber,
					@QueryParam("productId") Long productId) {
		return clientOrderItemHandler.getClientOrderItemByProductObjectList(pageNumber, productId);
	}
	
	@POST
	@Path("/additem")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean addItem(@FormParam("productId") Long productId, @FormParam("clientOrderId") Long clientOrderId) {
		return clientOrderItemHandler.addItem(productId, clientOrderId);
	}
	
	@POST
	@Path("/removeitem")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeItem(@FormParam("clientOrderItemId") Long clientOrderItemId) {
		return clientOrderItemHandler.removeItem(clientOrderItemId);
	}
	
	@POST
	@Path("/changepiece")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean changePieceQuantity(@FormParam("clientOrderItemId") Long clientOrderItemId, @FormParam("pieceQuantity") Integer pieceQuantity) {
		return clientOrderItemHandler.changePieceQuantity(clientOrderItemId, pieceQuantity);
	}
	
	@POST
	@Path("/changepackage")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean changePackageQuantity(@FormParam("clientOrderItemId") Long clientOrderItemId, @FormParam("packageQuantity") Integer packageQuantity) {
		return clientOrderItemHandler.changePackageQuantity(clientOrderItemId, packageQuantity);
	}
	
	@POST
	@Path("/transferpiece")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean transferPiece(@FormParam("clientOrderItemId") Long clientOrderItemId, @FormParam("destinationOrderId") Long destinationOrderId) {
		return clientOrderItemHandler.transferPiece(clientOrderItemId, destinationOrderId);
	}
	
	@POST
	@Path("/transferpackage")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean transferPackage(@FormParam("clientOrderItemId") Long clientOrderItemId, @FormParam("destinationOrderId") Long destinationOrderId) {
		return clientOrderItemHandler.transferPackage(clientOrderItemId, destinationOrderId);
	}
	
	@POST
	@Path("/transferall")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean transferAll(@FormParam("clientOrderItemId") Long clientOrderItemId, @FormParam("destinationOrderId") Long destinationOrderId) {
		return clientOrderItemHandler.transferAll(clientOrderItemId, destinationOrderId);
	}
}
