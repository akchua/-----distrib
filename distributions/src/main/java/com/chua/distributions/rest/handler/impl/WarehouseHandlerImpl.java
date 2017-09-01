package com.chua.distributions.rest.handler.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.annotations.CheckAuthority;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.WarehouseFormBean;
import com.chua.distributions.database.entity.Warehouse;
import com.chua.distributions.database.service.WarehouseService;
import com.chua.distributions.enums.Color;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.WarehouseHandler;
import com.chua.distributions.utility.Html;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   31 Aug 2017
 */
@Transactional
@Component
public class WarehouseHandlerImpl implements WarehouseHandler {

	@Autowired
	private WarehouseService warehouseService;

	@Override
	@CheckAuthority(minimumAuthority = 5)
	public Warehouse getWarehouse(Long warehouseId) {
		return warehouseService.find(warehouseId);
	}

	@Override
	@CheckAuthority(minimumAuthority = 5)
	public ObjectList<Warehouse> getWarehouseObjectList(Integer pageNumber, String searchKey) {
		return warehouseService.findAllWithPagingOrderByName(pageNumber, UserContextHolder.getItemsPerPage(), searchKey);
	}
	
	@Override
	public List<Warehouse> getWarehouseList() {
		return warehouseService.findAllOrderByName();
	}

	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean createWarehouse(WarehouseFormBean warehouseForm) {
		final ResultBean result;
		final ResultBean validateForm = validateWarehouseForm(warehouseForm);
		
		if(validateForm.getSuccess()) {
			if(warehouseService.isExistsByName(warehouseForm.getName().trim())) {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Warehouse name already exists.")));
			} else {
				final Warehouse warehouse = new Warehouse();
				
				setWarehouse(warehouse, warehouseForm);
				
				result = new ResultBean();
				result.setSuccess(warehouseService.insert(warehouse) != null);
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created Warehouse " + Html.text(Color.BLUE, warehouse.getName()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			}
		} else {
			result = validateForm;
		}
		
		return result;
	}

	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean updateWarehouse(WarehouseFormBean warehouseForm) {
		final ResultBean result;
		final Warehouse warehouse = warehouseService.find(warehouseForm.getId());
		
		if(warehouse != null) {
			final ResultBean validateForm = validateWarehouseForm(warehouseForm);
			if(validateForm.getSuccess()) {
				final Warehouse warehousee = warehouseService.findByName(warehouseForm.getName().trim());
				if(warehousee != null && warehouse.getId() != warehousee.getId()) {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Warehouse name already exists.")));
				} else {
					setWarehouse(warehouse, warehouseForm);
					
					result = new ResultBean();
					result.setSuccess(warehouseService.update(warehouse));
					if(result.getSuccess()) {
						result.setMessage(Html.line("Warehouse " + Html.text(Color.BLUE, warehouse.getName()) + " has been successfully " + Html.text(Color.GREEN, "updated") + "."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				}
			} else {
				result = validateForm;
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load warehouse. Please refresh the page."));
		}
		
		return result;
	}

	@Override
	@CheckAuthority(minimumAuthority = 2)
	public ResultBean removeWarehouse(Long warehouseId) {
		final ResultBean result;
		final Warehouse warehouse = warehouseService.find(warehouseId);
		
		if(warehouse != null) {
			result = new ResultBean();
			
			result.setSuccess(warehouseService.delete(warehouse));
			if(result.getSuccess()) {
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed Warehouse " + Html.text(Color.BLUE, warehouse.getName()) + "."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load warehouse. Please refresh the page."));
		}
		
		return result;
	}
	
	private void setWarehouse(Warehouse warehouse, WarehouseFormBean warehouseForm) {
		warehouse.setName(warehouseForm.getName());
		warehouse.setAddress(warehouseForm.getAddress());
	}
	
	private ResultBean validateWarehouseForm(WarehouseFormBean warehouseForm) {
		final ResultBean result;
		
		if(warehouseForm.getName() == null || warehouseForm.getName().trim().length() < 3 ||
				warehouseForm.getAddress() == null || warehouseForm.getName().trim().length() < 3) {
			result = new ResultBean(Boolean.FALSE, Html.line("All fields are " + Html.text(Color.RED, "required") + " and must contain at least 3 characters."));
		} else {
			result = new ResultBean(Boolean.TRUE, "");
		}
		
		return result;
	}
}
