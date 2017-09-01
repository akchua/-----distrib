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

import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.WarehouseFormBean;
import com.chua.distributions.database.entity.Warehouse;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.WarehouseHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   31 Aug 2017
 */
@Path("/warehouse")
public class WarehouseEndpoint {

	@Autowired
	private WarehouseHandler warehouseHandler;
	
	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	public Warehouse getWarehouse(@QueryParam("warehouseId") Long warehouseId) {
		return warehouseHandler.getWarehouse(warehouseId);
	}
	
	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<Warehouse> getWarehouseObjectList(@QueryParam("pageNumber") Integer pageNumber, @QueryParam("searchKey") String searchKey) {
		return warehouseHandler.getWarehouseObjectList(pageNumber, searchKey);
	}
	
	@GET
	@Path("/listbyname")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Warehouse> getWarehouseList() {
		return warehouseHandler.getWarehouseList();
	}
	
	@POST
	@Path("/save")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean saveWarehouse(@FormParam("warehouseFormData") String warehouseFormData) throws IOException {
		final ResultBean result;

		final WarehouseFormBean warehouseForm = new ObjectMapper().readValue(warehouseFormData, WarehouseFormBean.class);
		if(warehouseForm.getId() != null) {
			result = warehouseHandler.updateWarehouse(warehouseForm);
		} else {
			result = warehouseHandler.createWarehouse(warehouseForm);
		}
		
		return result;
	}
	
	@POST
	@Path("/remove")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean removeWarehouse(@FormParam("warehouseId") Long warehouseId) {
		return warehouseHandler.removeWarehouse(warehouseId);
	}
}
