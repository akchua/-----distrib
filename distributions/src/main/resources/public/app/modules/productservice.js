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
		
		getPartialProduct: function(productId, warehouse) {
			return $.ajax({
				url: '/services/product/getpartial',
				data: {
					productId: productId,
					warehouse: warehouse
				}
			});
		},
		
		getPartialProductFor: function(productId, clientId) {
			return $.ajax({
				url: '/services/product/getpartialfor',
				data: {
					productId: productId,
					clientId: clientId
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
		
		getProductListByName: function() {
			return $.ajax({
				url: '/services/product/listbyname'
			});
		},
		
		getPartialProductList: function(currentPage, searchKey, companyId, categoryId, warehouse, async) {
			return $.ajax({
				url: '/services/product/listpartial',
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