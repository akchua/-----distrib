package com.chua.distributions.rest.handler.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.beans.StringWrapper;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.database.entity.WarehouseItem;
import com.chua.distributions.database.service.ProductService;
import com.chua.distributions.database.service.WarehouseItemService;
import com.chua.distributions.enums.Warehouse;
import com.chua.distributions.rest.handler.WarehouseItemHandler;
import com.chua.distributions.utility.format.CurrencyFormatter;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	27 Jan 2017
 */
@Component
@Transactional
public class WarehouseItemHandlerImpl implements WarehouseItemHandler {

	@Autowired
	private WarehouseItemService warehouseItemService;
	
	@Autowired
	private ProductService productService;
	
	@Override
	public StringWrapper getFormattedPurchaseValue(Warehouse warehouse) {
		final StringWrapper sw = new StringWrapper();
		final List<WarehouseItem> warehouseItems = warehouseItemService.findAllByWarehouse(warehouse);
		
		float total = 0.0f;
		for(WarehouseItem item : warehouseItems) {
			total += item.getProduct().getNetPrice() * item.getStockCount();
		}
		sw.setContent(CurrencyFormatter.pesoFormat(total));
		
		return sw;
	}
	
	@Override
	public boolean addToWarehouse(Long productId, Warehouse warehouse, Integer quantity) {
		final WarehouseItem warehouseItem = warehouseItemService.findByProductAndWarehouse(productId, warehouse);
		if(warehouseItem == null) {
			final Product product = productService.find(productId);
			final WarehouseItem warehouzeItem = new WarehouseItem();
			
			warehouzeItem.setProduct(product);
			warehouzeItem.setWarehouse(warehouse);
			warehouzeItem.setStockCount(quantity);
			
			if(warehouseItemService.insert(warehouzeItem) == null) return false;
		} else {
			warehouseItem.setStockCount(warehouseItem.getStockCount() + quantity);
			
			if(!warehouseItemService.update(warehouseItem)) return false;;
		}
		
		return true;
	}

	@Override
	public boolean removeFromWarehouse(Long productId, Warehouse warehouse, Integer quantity) {
		return addToWarehouse(productId, warehouse, 0 - quantity);
	}
}
