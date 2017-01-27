define(['jquery'], function ($) {
	return {
		getStockAdjustByProductList: function(currentPage, productId, async) {
			return $.ajax({
				url: '/services/stockadjust/listbyproduct',
				async: async,
				data: {
					pageNumber: currentPage - 1,
					productId: productId
				}
			});
		},
		
		adjustStock: function(stockAdjustFormData) {
    		return $.ajax({
    			url: '/services/stockadjust/adjust',
    			method: 'POST',
    			data: {
    				stockAdjustFormData: stockAdjustFormData
    			} 
    		});
    	},
    	
    	getWarehouseList: function() {
    		return $.ajax({
    			url: '/services/stockadjust/warehouse'
    		});
    	}
	};
});