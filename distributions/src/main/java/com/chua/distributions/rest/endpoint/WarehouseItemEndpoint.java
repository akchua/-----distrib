package com.chua.distributions.rest.endpoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.distributions.beans.StringWrapper;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.rest.handler.WarehouseItemHandler;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	1 Feb 2017
 */
@Path("/warehouseitem")
public class WarehouseItemEndpoint {

	@Autowired
	private WarehouseItemHandler warehouseItemHandler;
	
	@GET
	@Path("/formattedpurchasevalue")
	@Produces({ MediaType.APPLICATION_JSON })
	public StringWrapper getFormattedPurchaseValue(@QueryParam("warehouse") Warehouse warehouse) {
		return warehouseItemHandler.getFormattedPurchaseValue(warehouse);
	}
}
