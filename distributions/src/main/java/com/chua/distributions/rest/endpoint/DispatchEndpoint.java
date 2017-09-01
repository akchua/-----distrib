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

import com.chua.distributions.beans.DispatchFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.Dispatch;
import com.chua.distributions.database.entity.DispatchItem;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.DispatchHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 14, 2016
 */
@Path("/dispatch")
public class DispatchEndpoint {

	@Autowired
	private DispatchHandler dispatchHandler;
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public Dispatch getDispatch(@QueryParam("dispatchId") Long dispatchId) {
		return dispatchHandler.getDispatch(dispatchId);
	}
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<Dispatch> getDispatchObjectList(@QueryParam("pageNumber") Integer pageNumber,
			@QueryParam("showReceived") Boolean showReceived) {
		return dispatchHandler.getDispatchObjectList(pageNumber, showReceived);
	}
	
	@GET
	@Path("/itemlist")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<DispatchItem> getDispatchItemObjectList(@QueryParam("pageNumber") Integer pageNumber,
					@QueryParam("dispatchId") Long dispatchId) {
		return dispatchHandler.getDispatchItemObjectList(pageNumber, dispatchId);
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveDispatch(@FormParam("dispatchFormData") String dispatchFormData) throws IOException {
		final DispatchFormBean dispatchForm = new ObjectMapper().readValue(dispatchFormData, DispatchFormBean.class);
		return dispatchHandler.createDispatch(dispatchForm);
	}
	
	@POST
	@Path("/dispatch")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean dispatch(@FormParam("dispatchId") Long dispatchId) {
		return dispatchHandler.dispatch(dispatchId);
	}
	
	@POST
	@Path("/complete")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean completeDispatch(@FormParam("dispatchId") Long dispatchId) {
		return dispatchHandler.completeDispatch(dispatchId);
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeDispatch(@FormParam("dispatchId") Long dispatchId) {
		return dispatchHandler.removeDispatch(dispatchId);
	}
	
	@POST
	@Path("/additem")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean addItem(@FormParam("clientOrderId") Long clientOrderId, @FormParam("dispatchId") Long dispatchId) {
		return dispatchHandler.addItem(clientOrderId, dispatchId);
	}
	
	@POST
	@Path("/removeitem")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeItem(@FormParam("dispatchItemId") Long dispatchItemId) {
		return dispatchHandler.removeItem(dispatchItemId);
	}
}
