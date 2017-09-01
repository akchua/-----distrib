package com.chua.distributions.rest.handler.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.annotations.CheckAuthority;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.database.entity.Warehouse;
import com.chua.distributions.database.entity.WarehouseItem;
import com.chua.distributions.database.service.ProductService;
import com.chua.distributions.database.service.WarehouseItemService;
import com.chua.distributions.database.service.WarehouseService;
import com.chua.distributions.objects.ObjectList;
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
	
	@Autowired
	private WarehouseService warehouseService;
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ObjectList<WarehouseItem> getWarehouseItemObjectList(Integer pageNumber, String searchKey, Long warehouseId) {
		return warehouseItemService.findAllWithPagingOrderByProductName(pageNumber, UserContextHolder.getItemsPerPage(), searchKey, warehouseId);
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public String getFormattedPurchaseValue(Long warehouseId) {
		final List<WarehouseItem> warehouseItems = warehouseItemService.findAllByWarehouse(warehouseId);
		
		float total = 0.0f;
		for(WarehouseItem item : warehouseItems) {
			total += item.getProduct().getNetPrice() * item.getStockCount();
		}
		
		return CurrencyFormatter.pesoFormat(total);
	}
	
	@Override
	public boolean addToWarehouse(Long productId, Long warehouseId, Integer quantity) {
		final WarehouseItem warehouseItem = warehouseItemService.findByProductAndWarehouse(productId, warehouseId);
		if(warehouseItem == null) {
			final Product product = productService.find(productId);
			final Warehouse warehouse = warehouseService.find(warehouseId);
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
	public boolean removeFromWarehouse(Long productId, Long warehouseId, Integer quantity) {
		return addToWarehouse(productId, warehouseId, 0 - quantity);
	}
}
