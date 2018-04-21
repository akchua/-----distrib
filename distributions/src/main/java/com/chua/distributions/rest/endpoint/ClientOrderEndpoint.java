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

import com.chua.distributions.beans.PartialClientOrderBean;
import com.chua.distributions.beans.ClientRankQueryBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.SalesReportQueryBean;
import com.chua.distributions.database.entity.ClientOrder;
import com.chua.distributions.enums.Area;
import com.chua.distributions.enums.ClientRankType;
import com.chua.distributions.enums.ClientSalesReportType;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.ClientOrderHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	@Path("/getpartial")
	@Produces({ MediaType.APPLICATION_JSON })
	public PartialClientOrderBean getPartialClientOrder(@QueryParam("clientOrderId") Long clientOrderId) {
		return clientOrderHandler.getPartialClientOrder(clientOrderId);
	}
	
	@GET
	@Path("/gettransfer")
	@Produces({ MediaType.APPLICATION_JSON })
	public ClientOrder getTransferInstance(@QueryParam("sourceId") Long sourceId) {
		return clientOrderHandler.getTransferInstance(sourceId);
	}
	
	@GET
	@Path("/listpartial")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<PartialClientOrderBean> getPartialClientOrderObjectList(@QueryParam("pageNumber") Integer pageNumber,
					@QueryParam("showPaid") Boolean showPaid) {
		return clientOrderHandler.getPartialClientOrderObjectList(pageNumber, showPaid);
	}
	
	@GET
	@Path("/requestlist")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<ClientOrder> getSubmittedClientOrderObjectList(@QueryParam("pageNumber") Integer pageNumber,
					@QueryParam("showAccepted") Boolean showAccepted) {
		return clientOrderHandler.getClientOrderRequestObjectList(pageNumber, showAccepted);
	}
	
	@GET
	@Path("/requestlistbycurrentuser")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<ClientOrder> getClientOrderRequestObjectListCreatedByCurrentUser(@QueryParam("pageNumber") Integer pageNumber) {
		return clientOrderHandler.getClientOrderRequestObjectListCreatedByCurrentUser(pageNumber);
	}
	
	@GET
	@Path("/acceptedlist")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<ClientOrder> getAcceptedClientOrderObjectList(@QueryParam("pageNumber") Integer pageNumber,
			@QueryParam("warehouseId") Long warehouseId) {
		return clientOrderHandler.getAcceptedClientOrderObjectList(pageNumber, warehouseId);
	}
	
	@GET
	@Path("/receivedlist")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<ClientOrder> getReceivedClientOrderObjectList(@QueryParam("pageNumber") Integer pageNumber,
			@QueryParam("warehouseId") Long warehouseId, @QueryParam("clientId") Long clientId) {
		return clientOrderHandler.getReceivedClientOrderObjectList(pageNumber, warehouseId, clientId);
	}
	
	@GET
	@Path("/paidlist")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<ClientOrder> getPaidClientOrderObjectList(@QueryParam("pageNumber") Integer pageNumber,
					@QueryParam("warehouseId") Long warehouseId) {
		return clientOrderHandler.getPaidClientOrderObjectList(pageNumber, warehouseId);
	}
	
	@GET
	@Path("/listbyreportquery")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<ClientOrder> getClientOrderObjectListBySalesReportQuery(@QueryParam("pageNumber") Integer pageNumber,
					@QueryParam("salesReportQueryData") String salesReportQueryData) throws IOException {
		return clientOrderHandler.getClientOrderObjectListBySalesReportQuery(pageNumber, new ObjectMapper().readValue(salesReportQueryData, SalesReportQueryBean.class));
	}
	
	@GET
	@Path("/formattedpayable")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getFormattedTotalPayable() {
		return clientOrderHandler.getFormattedTotalPayable();
	}
	
	@POST
	@Path("/add")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean addClientOrderFor(@FormParam("companyId") Long companyId, @FormParam("clientId") Long clientId) {
		final ResultBean result;
		
		if(clientId != null) {
			result = clientOrderHandler.addClientOrder(companyId, clientId);
		} else {
			result = clientOrderHandler.addClientOrder(companyId);
		}
		
		return result;
	}
	
	@POST
	@Path("/submit")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean submitClientOrder(@FormParam("clientOrderId") Long clientOrderId) {
		return clientOrderHandler.submitClientOrder(clientOrderId);
	}
	
	/*@POST
	@Path("/testaccept")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean testAcceptClientOrder(@FormParam("clientOrderId") Long clientOrderId,
			@FormParam("warehouse") Warehouse warehouse) {
		return clientOrderHandler.testAcceptClientOrder(clientOrderId, warehouse);
	}*/
	
	@POST
	@Path("/accept")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean acceptClientOrder(@FormParam("clientOrderId") Long clientOrderId) {
		return clientOrderHandler.acceptClientOrder(clientOrderId);
	}
	
	/*@POST
	@Path("/adjustaccept")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean adjustAndAcceptClientOrder(@FormParam("clientOrderId") Long clientOrderId,
			@FormParam("warehouse") Warehouse warehouse) {
		return clientOrderHandler.adjustAndAcceptClientOrder(clientOrderId, warehouse);
	}*/
	
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
	
	@POST
	@Path("/generatereport")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean generateReport(@FormParam("salesReportQueryData") String salesReportQueryData) throws IOException {
		final SalesReportQueryBean salesReportQuery = new ObjectMapper().readValue(salesReportQueryData, SalesReportQueryBean.class);
		return clientOrderHandler.generateReport(salesReportQuery);
	}
	
	@POST
	@Path("/generateclientranking")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean generateClientRanking(@FormParam("clientRankQueryData") String clientRankQueryData) throws IOException {
		final ClientRankQueryBean clientRankQuery = new ObjectMapper().readValue(clientRankQueryData, ClientRankQueryBean.class);
		return clientOrderHandler.generateClientRanking(clientRankQuery);
	}
	
	@GET
	@Path("/clientreporttypes")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<ClientSalesReportType> getClientSalesReportTypes() {
		return clientOrderHandler.getClientSalesReportTypes();
	}
	
	@GET
	@Path("/clientranktypes")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<ClientRankType> getClientRankTypes() {
		return clientOrderHandler.getClientRankTypes();
	}
	
	@GET
	@Path("/area")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Area> getAreaList() {
		return clientOrderHandler.getAreaList();
	}
}
