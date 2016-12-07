define(['jquery'], function ($) {
	return {
		getProduct: function(productId) {
			return $.ajax({
				url: '/services/product/get',
				data: {
					productId: productId
				}
			});
		},
		
		getProductList: function(currentPage, searchKey, companyId, categoryId) {
			return $.ajax({
				url: '/services/product/list',
				data: {
					pageNumber: currentPage - 1,
					searchKey: searchKey,
					companyId: companyId,
					categoryId: categoryId
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