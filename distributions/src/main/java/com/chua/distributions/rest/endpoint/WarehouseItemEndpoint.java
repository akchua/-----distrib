package com.chua.distributions.rest.endpoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.distributions.database.entity.WarehouseItem;
import com.chua.distributions.objects.ObjectList;
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
	@Path("/warehouseitemlist")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<WarehouseItem> getWarehouseItemObjectList(@QueryParam("pageNumber") Integer pageNumber, 
				@QueryParam("searchKey") String searchKey,
				@QueryParam("warehouseId") Long warehouseId) {
		return warehouseItemHandler.getWarehouseItemObjectList(pageNumber, searchKey, warehouseId);
	}
	
	@GET
	@Path("/formattedpurchasevalue")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getFormattedPurchaseValue(@QueryParam("warehouseId") Long warehouseId) {
		return warehouseItemHandler.getFormattedPurchaseValue(warehouseId);
	}
}
