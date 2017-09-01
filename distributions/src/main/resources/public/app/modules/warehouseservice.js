define(['jquery'], function ($) {
	return {
		getWarehouse: function(warehouseId) {
			return $.ajax({
				url: '/services/warehouse/get',
				data: {
					warehouseId: warehouseId
				}
			});
		},
		
		getWarehouseList: function(currentPage, searchKey) {
			return $.ajax({
				url: '/services/warehouse/list',
				data: {
					pageNumber: currentPage - 1,
					searchKey: searchKey
				}
			});
		},
		
		getWarehouseListByName: function() {
			return $.ajax({
				url: '/services/warehouse/listbyname'
			});
		},
		
		saveWarehouse: function(warehouseFormData) {
    		return $.ajax({
    			url: '/services/warehouse/save',
    			method: 'POST',
    			data: {
    				warehouseFormData: warehouseFormData
    			} 
    		});
    	},
    	
    	removeWarehouse: function(warehouseId) {
    		return $.ajax({
    			url: '/services/warehouse/remove',
    			method: 'POST',
    			data: {
    				warehouseId: warehouseId
    			}
    		});
    	}
	};
});