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
import com.chua.distributions.beans.StockAdjustFormBean;
import com.chua.distributions.database.entity.StockAdjust;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.StockAdjustHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	26 Jan 2017
 */
@Path("stockadjust")
public class StockAdjustEndpoint {

	@Autowired
	private StockAdjustHandler stockAdjustHandler;

	@GET
	@Path("/listbyproduct")
	@Produces({ MediaType.APPLICATION_JSON })
	public ObjectList<StockAdjust> getStockAdjustByProductObjectList(@QueryParam("pageNumber") Integer pageNumber,
					@QueryParam("productId") Long productId) {
		return stockAdjustHandler.getStockAdjustByProductObjectList(pageNumber, productId);
	}
	
	@POST
	@Path("/adjust")
	@Produces({ MediaType.APPLICATION_JSON })
	public ResultBean adjustStock(@FormParam("stockAdjustFormData") String stockAdjustFormData) throws IOException {
		return stockAdjustHandler.adjustStock(new ObjectMapper().readValue(stockAdjustFormData, StockAdjustFormBean.class));
	}
	
	@GET
	@Path("/warehouse")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Warehouse> getWarehouseList() {
		return stockAdjustHandler.getWarehouseList();
	}
}
