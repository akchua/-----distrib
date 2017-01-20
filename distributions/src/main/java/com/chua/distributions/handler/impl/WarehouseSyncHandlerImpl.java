package com.chua.distributions.handler.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.ClientOrderItem;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.database.entity.PurchaseOrderItem;
import com.chua.distributions.database.entity.User;
import com.chua.distributions.database.entity.WarehouseItem;
import com.chua.distributions.database.service.ClientOrderItemService;
import com.chua.distributions.database.service.PurchaseOrderItemService;
import com.chua.distributions.database.service.UserService;
import com.chua.distributions.database.service.WarehouseItemService;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.handler.WarehouseSyncHandler;
import com.chua.distributions.utility.EmailUtil;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	19 Jan 2017
 */
@Component
@Transactional
public class WarehouseSyncHandlerImpl implements WarehouseSyncHandler {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private WarehouseItemService warehouseItemService;
	
	@Autowired
	private ClientOrderItemService clientOrderItemService;
	
	@Autowired
	private PurchaseOrderItemService purchaseOrderItemService;
	
	@Autowired
	private UserService userService;
	
	@Override
	public ResultBean syncExisting(Warehouse warehouse) {
		final ResultBean result = new ResultBean();
		result.setSuccess(Boolean.TRUE);
		final List<WarehouseItem> warehouseItems = warehouseItemService.findAllByWarehouse(warehouse);
		
		List<String> errorList = new ArrayList<String>();
		
		LOG.info("Starting sync of warehouse " + warehouse.getDisplayName() + ".");
		
		for(WarehouseItem warehouseItem : warehouseItems) {
			Product product = warehouseItem.getProduct();
			List<ClientOrderItem> clientOrderItems = clientOrderItemService.findAllUnstockedByProductAndWarehouse(product.getId(), warehouse);
			List<PurchaseOrderItem> purchaseOrderItems = purchaseOrderItemService.findAllStockedByProductAndWarehouse(product.getId(), warehouse);
			if(clientOrderItems != null && purchaseOrderItems != null) {
				Integer stockCount = warehouseItem.getStockCount();
				Integer actualCount = 0;
				for(ClientOrderItem clientOrderItem : clientOrderItems) {
					actualCount -= clientOrderItem.getQuantity();
				}
				for(PurchaseOrderItem purchaseOrderItem : purchaseOrderItems) {
					actualCount += purchaseOrderItem.getQuantity();
				}
				if(!stockCount.equals(actualCount)) {
					String error = "Adjust " + product.getDisplayName() + " at " + warehouse.getDisplayName() + " from " + stockCount + " to " + actualCount;
					errorList.add(error);
					LOG.info(error);
				}
			} else {
				result.setSuccess(Boolean.FALSE);
				break;
			}
		}
		
		if(result.getSuccess()) {
			if(errorList.size() == 0) {
				result.setMessage("Sync for " + warehouse.getDisplayName() + " complete. No errors found.");
			} else {
				// NOTIFY ADMINS
					String recipient = "";
					final List<User> administrators = userService.findAllAdministrators();
					
					for(User admin : administrators) {
						recipient += admin.getEmailAddress() + ", ";
					}
					
					String message = "Sync for " + warehouse.getDisplayName() + " complete. " + errorList.size() + " error/s found." + "\n\n";
					
					for(String s : errorList) {
						message += s + "\n";
					}
					
					EmailUtil.send(recipient, "Warehouse Sync", message);
				//
				
				result.setMessage("Sync for " + warehouse.getDisplayName() + " complete. " + errorList.size() + " error/s found.");
			}
		} else {
			result.setMessage("Sync for " + warehouse.getDisplayName() + " failed.");
		}
		
		return result;
	}
}
