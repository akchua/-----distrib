package com.chua.distributions.rest.endpoint;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.StringWrapper;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.ClientOrderHandler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 12, 2016
 */
@Path("/clientorder")
public class ClientOrderEndpoint {

	@Autowired
	private ClientOrderHandler clientOrderHandler;
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public ClientOrder getClientOrder(@QueryParam("clientOrderId") Long clientOrderId) {
		return clientOrderHandler.getClientOrder(clientOrderId);
	}
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<ClientOrder> getClientOrderObjectList(@QueryParam("pageNumber") Integer pageNumber,
					@QueryParam("showPaid") Boolean showPaid) {
		return clientOrderHandler.getClientOrderObjectList(pageNumber, showPaid);
	}
	
	@GET
	@Path("/acceptedlist")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<ClientOrder> getAcceptedClientOrderObjectList(@QueryParam("pageNumber") Integer pageNumber,
			@QueryParam("warehouse") Warehouse warehouse) {
		return clientOrderHandler.getAcceptedClientOrderObjectList(pageNumber, warehouse);
	}
	
	@GET
	@Path("/receivedlist")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<ClientOrder> getReceivedClientOrderObjectList(@QueryParam("pageNumber") Integer pageNumber,
			@QueryParam("warehouse") Warehouse warehouse) {
		return clientOrderHandler.getReceivedClientOrderObjectList(pageNumber, warehouse);
	}
	
	@GET
	@Path("/paidlist")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<ClientOrder> getPaidClientOrderObjectList(@QueryParam("pageNumber") Integer pageNumber,
					@QueryParam("warehouse") Warehouse warehouse) {
		return clientOrderHandler.getPaidClientOrderObjectList(pageNumber, warehouse);
	}
	
	@GET
	@Path("/requestlist")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<ClientOrder> getSubmittedClientOrderObjectList(@QueryParam("pageNumber") Integer pageNumber,
					@QueryParam("showAccepted") Boolean showAccepted) {
		return clientOrderHandler.getClientOrderRequestObjectList(pageNumber, showAccepted);
	}
	
	@GET
	@Path("/formattedpayable")
	@Produces({ MediaType.APPLICATION_JSON })
	public StringWrapper getFormattedTotalPayable() {
		return clientOrderHandler.getFormattedTotalPayable();
	}
	
	@POST
	@Path("/add")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean addClientOrder() {
		return clientOrderHandler.addClientOrder();
	}
	
	@POST
	@Path("/submit")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean submitClientOrder(@FormParam("clientOrderId") Long clientOrderId) {
		return clientOrderHandler.submitClientOrder(clientOrderId);
	}
	
	@POST
	@Path("/testaccept")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean testAcceptClientOrder(@FormParam("clientOrderId") Long clientOrderId,
			@FormParam("warehouse") Warehouse warehouse) {
		return clientOrderHandler.testAcceptClientOrder(clientOrderId, warehouse);
	}
	
	@POST
	@Path("/accept")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean acceptClientOrder(@FormParam("clientOrderId") Long clientOrderId,
			@FormParam("warehouse") Warehouse warehouse) {
		return clientOrderHandler.acceptClientOrder(clientOrderId, warehouse);
	}
	
	@POST
	@Path("/adjustaccept")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean adjustAndAcceptClientOrder(@FormParam("clientOrderId") Long clientOrderId,
			@FormParam("warehouse") Warehouse warehouse) {
		return clientOrderHandler.adjustAndAcceptClientOrder(clientOrderId, warehouse);
	}
	
	@POST
	@Path("/pay")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean payClientOrder(@FormParam("clientOrderId") Long clientOrderId) {
		return clientOrderHandler.payClientOrder(clientOrderId);
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeClientOrder(@FormParam("clientOrderId") Long clientOrderId) {
		return clientOrderHandler.removeClientOrder(clientOrderId);
	}
	
	@GET
	@Path("/warehouse")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Warehouse> getWarehouseList() {
		return clientOrderHandler.getWarehouseList();
	}
}
