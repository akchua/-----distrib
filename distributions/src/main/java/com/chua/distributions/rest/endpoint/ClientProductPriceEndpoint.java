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

import com.chua.distributions.beans.ClientProductPriceFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.ClientProductPrice;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.ClientProductPriceHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 18, 2017
 */
@Path("clientproductprice")
public class ClientProductPriceEndpoint {

	@Autowired
	private ClientProductPriceHandler clientProductPriceHandler;
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public ClientProductPrice getClientProductPrice(@QueryParam("clientProductPriceId") Long clientProductPriceId) {
		return clientProductPriceHandler.getClientProductPrice(clientProductPriceId);
	}
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<ClientProductPrice> getClientProductPriceObjectList(@QueryParam("pageNumber") Integer pageNumber, @QueryParam("clientId") Long clientId) {
		return clientProductPriceHandler.getClientProductPriceObjectList(pageNumber, clientId);
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveClientProductPrice(@FormParam("clientProductPriceFormData") String clientProductPriceFormData) throws IOException {
		final ResultBean result;

		final ClientProductPriceFormBean clientProductPriceForm = new ObjectMapper().readValue(clientProductPriceFormData, ClientProductPriceFormBean.class);
		if(clientProductPriceForm.getId() != null) {
			result = clientProductPriceHandler.updateClientProductPrice(clientProductPriceForm);
		} else {
			result = clientProductPriceHandler.createClientProductPrice(clientProductPriceForm);
		}
		
		return result;
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeClientProductPrice(@FormParam("clientProductPriceId") Long clientProductPriceId) {
		return clientProductPriceHandler.removeClientProductPrice(clientProductPriceId);
	}
}
