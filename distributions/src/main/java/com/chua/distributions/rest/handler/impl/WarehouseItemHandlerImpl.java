package com.chua.distributions.rest.handler.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.annotations.CheckAuthority;
import com.chua.distributions.beans.StringWrapper;
import com.chua.distributions.beans.WarehouseValueBean;
import com.chua.distributions.database.entity.Product;
import com.chua.distributions.database.entity.WarehouseItem;
import com.chua.distributions.database.service.ProductService;
import com.chua.distributions.database.service.WarehouseItemService;
import com.chua.distributions.enums.Warehouse;
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
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ObjectList<WarehouseItem> getWarehouseItemObjectList(Integer pageNumber, String searchKey, Warehouse warehouse) {
		return warehouseItemService.findAllWithPagingOrderByProductName(pageNumber, UserContextHolder.getItemsPerPage(), searchKey, warehouse);
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public StringWrapper getFormattedPurchaseValue(Warehouse warehouse) {
		final StringWrapper sw = new StringWrapper();
		final List<WarehouseItem> warehouseItems = warehouseItemService.findAllByWarehouse(warehouse);
		
		sw.setContent(getFormattedPurchaseValue(warehouseItems));
		
		return sw;
	}
	
	@Override
	@CheckAuthority(minimumAuthority = 5)
	public List<WarehouseValueBean> getWarehouseValueList() {
		final List<WarehouseValueBean> warehouseValues = new ArrayList<WarehouseValueBean>();
		for(Warehouse warehouse : Warehouse.values()) {
			final List<WarehouseItem> warehouseItems = warehouseItemService.findAllByWarehouse(warehouse);
			final WarehouseValueBean warehouseValue = new WarehouseValueBean();
			warehouseValue.setWarehouse(warehouse);
			warehouseValue.setFormattedPurchaseValue(getFormattedPurchaseValue(warehouseItems));
			warehouseValues.add(warehouseValue);
		}
		return warehouseValues;
	}
	
	private String getFormattedPurchaseValue(List<WarehouseItem> warehouseItems) {
		float total = 0.0f;
		for(WarehouseItem item : warehouseItems) {
			total += item.getProduct().getNetPrice() * item.getStockCount();
		}
		return CurrencyFormatter.pesoFormat(total);
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
