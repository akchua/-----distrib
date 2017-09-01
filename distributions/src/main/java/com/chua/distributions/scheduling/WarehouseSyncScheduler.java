package com.chua.distributions.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.Warehouse;
import com.chua.distributions.database.service.WarehouseService;
import com.chua.distributions.rest.handler.WarehouseSyncHandler;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	19 Jan 2017
 */
@Component
public class WarehouseSyncScheduler {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private WarehouseService warehouseService;
	
	@Autowired
	private WarehouseSyncHandler warehouseSyncHandler;
	
	/**
	 * Weekly Warehouse Sync Report
	 * fires at 2:00AM every Saturday
	 * includes: all warehouses
	 */
	@Scheduled(cron = "0 0 2 * * SAT")
	public void weeklyWarehouseSync() {
		LOG.info("Starting weekly warehouse sync.");
		
		for(Warehouse warehouse : warehouseService.findAllList()) {
			ResultBean result = warehouseSyncHandler.syncExisting(warehouse);
			if(result.getSuccess()) {
				LOG.info(result.getMessage());
			} else {
				LOG.error(result.getMessage());
			}
		}
		
		LOG.info("Weekly warehouse sync complete...");
	}
}
