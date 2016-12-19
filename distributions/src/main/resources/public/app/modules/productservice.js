define(['jquery'], function ($) {
	return {
		getProduct: function(productId, warehouse) {
			return $.ajax({
				url: '/services/product/get',
				data: {
					productId: productId,
					warehouse: warehouse
				}
			});
		},
		
		getProductList: function(currentPage, searchKey, companyId, categoryId, warehouse, async) {
			return $.ajax({
				url: '/services/product/list',
				async: async,
				data: {
					pageNumber: currentPage - 1,
					searchKey: searchKey,
					companyId: companyId,
					categoryId: categoryId,
					warehouse, warehouse
				}
			});
		},
		
		getWarehouseItemList: function(currentPage, searchKey, warehouse, async) {
			return $.ajax({
				url: '/services/product/warehouseitemlist',
				async: async,
				data: {
					pageNumber: currentPage - 1,
					searchKey: searchKey,
					warehouse, warehouse
				}
			});
		},
		
		saveProduct: function(productFormData) {
    		return $.ajax({
    			url: '/services/product/save',
    			method: 'POST',
    			data: {
    				productFormData: productFormData
    			} 
    		});
    	},
    	
    	removeProduct: function(productId) {
    		return $.ajax({
    			url: '/services/product/remove',
    			method: 'POST',
    			data: {
    				productId: productId
    			}
    		});
    	}
	};
});