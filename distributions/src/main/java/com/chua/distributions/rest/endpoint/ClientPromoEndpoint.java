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

import com.chua.distributions.beans.ClientPromoFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.ClientPromo;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.ClientPromoHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	3 Apr 2017
 */
@Path("clientpromo")
public class ClientPromoEndpoint {

	@Autowired
	private ClientPromoHandler clientPromoHandler;
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public ClientPromo getClientPromo(@QueryParam("clientPromoId") Long clientPromoId) {
		return clientPromoHandler.getClientPromo(clientPromoId);
	}
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<ClientPromo> getClientPromoObjectList(@QueryParam("pageNumber") Integer pageNumber, @QueryParam("clientId") Long clientId) {
		return clientPromoHandler.getClientPromoObjectList(pageNumber, clientId);
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveClientPromo(@FormParam("clientPromoFormData") String clientPromoFormData) throws IOException {
		final ResultBean result;

		final ClientPromoFormBean clientPromoForm = new ObjectMapper().readValue(clientPromoFormData, ClientPromoFormBean.class);
		if(clientPromoForm.getId() != null) {
			result = clientPromoHandler.updateClientPromo(clientPromoForm);
		} else {
			result = clientPromoHandler.createClientPromo(clientPromoForm);
		}
		
		return result;
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeClientPromo(@FormParam("clientPromoId") Long clientPromoId) {
		return clientPromoHandler.removeClientPromo(clientPromoId);
	}
}
