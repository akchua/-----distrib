package com.chua.distributions.rest.endpoint;

import java.io.IOException;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.distributions.beans.ClientCompanyPriceFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.ClientCompanyPrice;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.ClientCompanyPriceHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	7 Mar 2017
 */
@Path("/clientcompanyprice")
public class ClientCompanyPriceEndpoint {

	@Autowired
	private ClientCompanyPriceHandler clientCompanyPriceHandler;
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public ClientCompanyPrice getClientCompanyPrice(@QueryParam("clientCompanyPriceId") Long clientCompanyPriceId) {
		return clientCompanyPriceHandler.getClientCompanyPrice(clientCompanyPriceId);
	}
	
	@GET
	@Path("/getbyclientandproduct")
	@Produces({ MediaType.APPLICATION_JSON })
	public ClientCompanyPrice getClientCompanyPriceByClientAndProduct(@QueryParam("clientId") Long clientId, @QueryParam("productId") Long productId) {
		return clientCompanyPriceHandler.getClientCompanyPriceByClientAndProduct(clientId, productId);
	}
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<ClientCompanyPrice> getClientCompanyPriceObjectList(@QueryParam("pageNumber") Integer pageNumber, @QueryParam("clientId") Long clientId) {
		return clientCompanyPriceHandler.getClientCompanyPriceObjectList(pageNumber, clientId);
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveClientCompanyPrice(@FormParam("clientCompanyPriceFormData") String clientCompanyPriceFormData) throws IOException {
		final ResultBean result;

		final ClientCompanyPriceFormBean clientCompanyPriceForm = new ObjectMapper().readValue(clientCompanyPriceFormData, ClientCompanyPriceFormBean.class);
		if(clientCompanyPriceForm.getId() != null) {
			result = clientCompanyPriceHandler.updateClientCompanyPrice(clientCompanyPriceForm);
		} else {
			result = clientCompanyPriceHandler.createClientCompanyPrice(clientCompanyPriceForm);
		}
		
		return result;
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeClientCompanyPrice(@FormParam("clientCompanyPriceId") Long clientCompanyPriceId) {
		return clientCompanyPriceHandler.removeClientCompanyPrice(clientCompanyPriceId);
	}
}
